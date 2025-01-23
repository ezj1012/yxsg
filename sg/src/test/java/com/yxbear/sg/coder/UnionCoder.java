package com.yxbear.sg.coder;

import com.yxbear.core.coder.CodeBuilder;

public class UnionCoder {

    public static void main(String[] args) {
        CodeBuilder coder = new CodeBuilder("sg", "com.yxbear.sg");
        coder.pkg("gi")
                // 用户管理
                .modelIntNoAudit("union") //
                .attrStr("name", "名字") //
                ._____endCheck____()//
                //
                .buildPackageCode();
    }

}
