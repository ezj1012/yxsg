package com.yxbear.sg.test;

import com.yxbear.sg.SgAppMain;

public class SgMainTest {

    public static void main(String[] args) {
        args = new String[] {"--spring.profiles.active=code"};
        SgAppMain.main(args);
    }

}
