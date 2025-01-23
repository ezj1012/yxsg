package com.yxbear.sg.svc.sso.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.yxbear.sg.domain.SGConstant;
import com.yxbear.sg.domain.SystemUtils;
import com.yxbear.sg.domain.bean.LoginUser;
import com.yxbear.sg.domain.model.sys.SysUser;
import com.yxbear.sg.svc.sso.SSOSvc;
import com.yxbear.sg.svc.sys.UserSvc;

import cn.dev33.satoken.stp.StpUtil;

@Service
public class SSOSvcImpl implements SSOSvc {

    final UserSvc userSvc;

    final List<LoginUser> users = new ArrayList<>();

    public SSOSvcImpl(UserSvc userSvc) {
        super();
        this.userSvc = userSvc;
    }

    @Override
    public LoginUser playLogin(String username, String password) {
        LoginUser curLoginUser = SystemUtils.getCurUser();
        if (curLoginUser != null) {
            cacheLogout(curLoginUser.getId());
        }
        SysUser curUser = userSvc.checkUserByLoginCodeAndPwd(username, password);
        curLoginUser = LoginUser.of(curUser);

        return cacheLogin(curLoginUser);
    }

    @Override
    public Integer logout() {
        LoginUser curLoginUser = SystemUtils.getCurUser();
        if (curLoginUser != null) {
            cacheLogout(curLoginUser.getId());
            return 1;
        }
        return 0;
    }

    @Override
    public Integer logout(Integer id) {
        cacheLogout(id);
        return 1;
    }

    private LoginUser cacheLogin(LoginUser curLoginUser) {
        cacheLogout(curLoginUser.getId());

        // 标记登录
        StpUtil.login(curLoginUser.getId());
        StpUtil.getSession().set(SGConstant.USER_SESSION_KEY, curLoginUser);

        try {
            // 保存登录状态
            curLoginUser.setToken(StpUtil.getTokenValue());
            curLoginUser.setLastAccessTime(System.currentTimeMillis());

            SysUser sysUser = userSvc.updateLoginStatus(curLoginUser.getId(), curLoginUser.getToken(), "");
            curLoginUser.setLoginTime(sysUser.getLastLoginTime());
            //
            synchronized (users) {
                users.add(curLoginUser);
            }
        } catch (Exception e) {
            StpUtil.logout();
            throw e;
        }

        return curLoginUser;
    }

    private void cacheLogout(Integer id) {
        StpUtil.logout(id);
        synchronized (users) {
            Iterator<LoginUser> userIter = users.iterator();
            while (userIter.hasNext()) {
                LoginUser user = userIter.next();
                if (user == null || Objects.equals(user.getId(), id)) {
                    userIter.remove();
                }
            }
        }
    }

}
