package com.yxbear.sg.domain.model.gi;

import com.yxbear.core.bean.EntityBean;

import lombok.Data;

@Data
public class GiPlayer implements EntityBean<Integer> {

    /** 唯一主键 */
    private Integer id;

    /** 名字 */
    private String name;

    /** 玩家状态 0: 正常,1 新手,2 免战玩家,4: 被打完主城的玩家 */
    private Integer state;

    /** 玩家组 0:正常玩家 1:GM 2: NPC */
    private Integer playerGroup;

    /** 性别 1:男 2:女 */
    private Integer gender;

    /** 君主将最后所在城池 */
    private Integer lastCity;

    /** 脸ID */
    private String face;

    /** 旗帜名称 */
    private String flagChar;

    /** 排名 */
    private Integer playerRank;

    /** 声望 */
    private Long prestige;

    /** 战场获得声望 */
    private Long warPrestige;

    /** 战争进攻获得声望 */
    private Long warAttackPrestige;

    /** 战争防御获得声望 */
    private Long warDefencePrestige;

    /** 爵位等级 */
    private Integer nobility;

    /** 官职 */
    private Integer officepos;

    /** 联盟ID */
    private Integer unionId;

    /** 联盟职位 */
    private Integer unionPos;

    /** 元宝 */
    private Long money;

    /** 用户的装备栏数量 */
    private Integer armorColumn;

    /** vip等级 */
    private Integer vipLevel;



}