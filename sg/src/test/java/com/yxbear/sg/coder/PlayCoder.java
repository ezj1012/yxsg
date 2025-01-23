package com.yxbear.sg.coder;

import com.yxbear.core.coder.CodeBuilder;

public class PlayCoder {

    public static void main(String[] args) {
        CodeBuilder coder = new CodeBuilder("sg", "com.yxbear.sg");
        coder.pkg("gi")
                // 用户管理
                .modelIntNoAudit("player") //
                .attrStr("name", "名字") //
                .attrInt4("state", "玩家状态 0: 正常,1 新手,2 免战玩家,4: 被打完主城的玩家") //
                .attrInt4("playerGroup", "玩家组 0:正常玩家 1:GM 2: NPC") //
                .attrInt4("gender", "性别 1:男 2:女") //
                .attrInt("lastCity", "君主将最后所在城池") //
                .attrStr100("face", "脸ID") //
                .attrStr40("flagChar", "旗帜名称") //
                .attrInt4("playerRank", "排名") //
                .attrLong("prestige", "声望") //
                .attrLong("warPrestige", "战场获得声望") //
                .attrLong("warAttackPrestige", "战争进攻获得声望") //
                .attrLong("warDefencePrestige", "战争防御获得声望") //
                .attrInt4("nobility", "爵位等级") //
                .attrInt4("officepos", "官职") //
                .attrInt("unionId", "联盟ID") //
                .attrInt4("unionPos", "联盟职位") //
                .attrLong("money", "元宝") //
                .attrInt4("armorColumn", "用户的装备栏数量") //
                .attrInt4("vipLevel", "vip等级") //
                ._____endCheck____()//
                //
                .buildPackageCode();
    }

}
