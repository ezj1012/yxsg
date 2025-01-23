package com.yxbear.sg.svc.sys;

import java.util.Collections;
import java.util.List;

import com.yxbear.sg.domain.SGConstant;
import com.yxbear.sg.domain.bean.LoginUser;
import com.yxbear.sg.domain.model.sys.SysUser;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;

public interface UserSvc extends StpInterface {

    SysUser checkUserByLoginCodeAndPwd(String username, String password);

    SysUser updateLoginStatus(Integer id, String token, String string);

    @Override
    default List<String> getPermissionList(Object loginId, String loginType) {
        return Collections.emptyList();
    }

    @Override
    default List<String> getRoleList(Object loginId, String loginType) {
        LoginUser user = StpUtil.getSession().getModel(SGConstant.USER_SESSION_KEY, LoginUser.class);
        return user.getRoles();
    }

}
