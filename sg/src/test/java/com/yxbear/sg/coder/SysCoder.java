package com.yxbear.sg.coder;

import com.yxbear.core.coder.CodeBuilder;

public class SysCoder {
    public static void main(String[] args) {
        CodeBuilder coder = new CodeBuilder("sg", "com.yxbear.sg");
        coder.pkg("sys")
                // id生成器
                // .modelLong("primaryKey").attrStr100("name", "表名字").attrLong("value", "当前值")
                // .attrLong("version", "当前获取次数")._____endCheck____()
                // 用户管理
                .modelIntNoAudit("user") //
                .attrStr("loginCode", "登录名称") //
                .attrStr("password", "密码") //
                .attrStr("qqNo", "qq号") //
                .attrInt("adminFlag", "1为管理员,0为普通用户") //
                .attrInt("status", "1为正常,2未激活,3锁定,0删除") //
                .attrLong("lastLoginTime", "最后登录时间!")//
                .attrStr("lastToken", "最后登录Token!")//
                .attrStr("lastIp", "最后登录Ip!")._____endCheck____() //
                // 登录日志
                .modelIntNoAudit("userLog")//
                .attrInt("action", "1登录,2登出")//
                .attrStr500("remark", "内容")//
                .attrStr("token", "最后登录Token!")//
                .attrStr("ip", "最后登录Ip!")//
                ._____endCheck____()//
                //
                .buildPackageCode();
    }

}
