package com.yxbear.sg.svc.sys.impl;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.yxbear.core.CommUtils;
import com.yxbear.core.exception.ServiceException;
import com.yxbear.sg.domain.SystemUtils;
import com.yxbear.sg.domain.mapper.sys.SysUserLogMapper;
import com.yxbear.sg.domain.mapper.sys.SysUserMapper;
import com.yxbear.sg.domain.model.sys.CSysUser;
import com.yxbear.sg.domain.model.sys.SysUser;
import com.yxbear.sg.domain.model.sys.SysUserLog;
import com.yxbear.sg.svc.sys.UserSvc;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.crypto.symmetric.AES;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserSvcImpl implements UserSvc {

    static final byte[] keys = "xiongjianrexuesg".getBytes();

    SysUserMapper userMapper;

    SysUserLogMapper logMapper;

    @Override
    public SysUser checkUserByLoginCodeAndPwd(String loginCode, String pageEncodePasswd) {
        SystemUtils.checkEmpty(loginCode, "登录名");
        SystemUtils.checkEmpty(pageEncodePasswd, "密码");
        String pwd = encode(loginCode, pageDecode(pageEncodePasswd));

        CSysUser cdt = new CSysUser();
        cdt.setLoginCodeEqual(loginCode);
        List<SysUser> userList = userMapper.queryPageList(1, 2, cdt, "ID");
        if (userList.isEmpty()) { throw new ServiceException(ServiceException.USER_NOT_FOUND, "用户不存在!"); }
        if (!Objects.equals(userList.get(0).getPassword(), pwd)) { throw new ServiceException(ServiceException.USER_PWD_ERR, "用户名或密码错误!"); }

        if (userList.size() > 2) { throw new ServiceException("发现同名用户[" + loginCode + "],通知管理员处理!"); }
        return userList.isEmpty() ? null : userList.get(0);
    }

    @Override
    public SysUser updateLoginStatus(Integer userId, String token, String ip) {
        SysUser upt = new SysUser();
        upt.setLastToken(token);
        upt.setLastLoginTime(CommUtils.curDateTime());
        upt.setLastIp(ip);
        SysUserLog log = new SysUserLog();
        log.setAction(1);
        log.setToken(token);
        log.setIp(ip);
        log.setRemark("");
        logMapper.save(log);
        userMapper.updateById(userId, upt);

        return userMapper.selectById(userId);
    }

    /**
     * 用来解密前端页面传入的加密密码
     * 
     * @param passwd
     * @return
     */
    public String pageDecode(String passwd) {
        AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, keys);
        aes.setIv(keys);
        return aes.decryptStr(passwd);
    }

    /**
     * 不可逆加密.用于加密用户密码
     * 
     * @param loginCode
     * @param passwd
     * @return
     */
    private String encode(String loginCode, String passwd) {
        Digester digester = new Digester(DigestAlgorithm.SHA256);
        digester.setSalt(salt(loginCode));
        return digester.digestHex(passwd);
    }

    private byte[] salt(String loginCode) {
        byte[] bytes = loginCode.getBytes(StandardCharsets.UTF_8);
        byte[] salt = new byte[16];
        for (int i = 0; i < salt.length; i++) {
            salt[i] = i >= bytes.length ? (byte) i : bytes[i];
        }
        return salt;
    }

}
