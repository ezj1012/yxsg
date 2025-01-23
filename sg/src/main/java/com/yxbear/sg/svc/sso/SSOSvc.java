package com.yxbear.sg.svc.sso;

import com.yxbear.sg.domain.bean.LoginUser;

public interface SSOSvc {

    LoginUser playLogin(String username, String password);

    Integer logout();

    Integer logout(Integer id);

}
