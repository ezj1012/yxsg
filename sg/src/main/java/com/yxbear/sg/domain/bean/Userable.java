package com.yxbear.sg.domain.bean;

import java.security.Principal;

public interface Userable extends Principal {

    Integer getId();

    String getName();

    default Integer getVip() {
        return 0;
    }

}
