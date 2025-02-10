package com.yxbear.sg.domain.model.gi;

import com.yxbear.core.bean.EntityBean;

import lombok.Data;

@Data
public class GiCityHero implements EntityBean<Integer> {

    /** 唯一主键 */
    private Integer id;

    /** 归属哪个玩家，如果是NPC的话，为0 */
    private Integer playId;

    /** 所属npcId,如果是玩家,为0 */
    private Integer npcId;

    /**  */
    private Integer cityId;

    /**  */
    private String name;

    /** 类型0:玩家城池1:名城2:郡城3:州城4:都城5:玩家主城 */
    private Integer cityType;

    /** 状态：0，空闲；1,城守；2,出征，3,战斗,4，驻守,5,俘虏 */
    private Integer state;

    /** 1男:2女 */
    private Integer gender;

    /**  */
    private Integer face;

    /** 将领类型,0普通武将，1黄巾活动武将,2聚贤包开出武将,3-9圣诞武将，1001-1005 */
    private Integer heroType;

    /** 状态：0，健康；1，重伤；2，修养 */
    private Integer heroHealth;

    /** 级别 */
    private Integer level;

    /** 经验值 */
    private Long exp;

    /** 领将的统率 */
    private Integer commandBase;

    /** 率统加成 */
    private Integer commandAddOn;

    /** 内政 */
    private Integer affairsBase;

    /** 勇武 */
    private Integer braveryBase;

    /** 智谋 */
    private Integer wisdomBase;

    /** 内政加点 */
    private Integer affairsAdd;

    /** 勇武加点 */
    private Integer braveryAdd;

    /** 智谋加点 */
    private Integer wisdomAdd;

    /** 政内加成 */
    private Long affairsAddOn;

    /** 勇武加成 */
    private Long braveryAddOn;

    /** 智谋加成 */
    private Long wisdomAddOn;

    /** 体力加成 */
    private Integer forceMaxAddOn;

    /** 精力加成 */
    private Integer energyMaxAddOn;

    /** 速度加成 */
    private Integer speedAddOn;

    /** 攻击加成 */
    private Long attackAddOn;

    /** 防御加成 */
    private Long defenceAddOn;

    /** 忠诚 */
    private Integer loyalty;

    /** 创建时间 */
    private Long createTime;

    /** 最后一次修改时间 */
    private Long modifyTime;

}