package com.yxbear.sg.domain.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.yxbear.sg.domain.model.sys.SysUser;

import lombok.Data;

@Data
public class LoginUser implements Userable {

    private Integer id;

    private String name;

    private String token;

    private Long loginTime;

    private Long lastAccessTime;

    private List<String> roles = new ArrayList<>();

    public LoginUser(Integer id, String name, Long loginTime, boolean isAdmin) {
        super();
        this.id = id;
        this.name = name;
        this.loginTime = loginTime;
        if (isAdmin) {
            this.setAdminFlag();
        }
    }

    public static LoginUser of(SysUser user) {
        return new LoginUser(user.getId(), user.getLoginCode(), user.getLastLoginTime(), Objects.equals(user.getAdminFlag(), 1));
    }

    public void setAdminFlag() {
        if (!roles.contains("admin")) {
            roles.add("admin");
        }
    }

    public boolean isAdmin() {
        return roles.contains("admin");
    }

    public List<String> getRoles() {
        return roles;
    }

}
