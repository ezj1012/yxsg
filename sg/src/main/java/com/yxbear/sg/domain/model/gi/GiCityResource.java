package com.yxbear.sg.domain.model.gi;

import com.yxbear.core.bean.EntityBean;

import lombok.Data;

@Data
public class GiCityResource implements EntityBean<Integer> {

    /** 唯一主键 */
    private Integer id;

    /** 当前民心 */
    private Integer morale;

    /** 民心稳定值 */
    private Integer moraleStable;

    /** 民怨 */
    private Integer complaint;

    /** 税率 */
    private Integer tax;

    /** 库存木材 */
    private Long wood;

    /** 木材产量 */
    private Long woodAdd;

    /** 木材上限 */
    private Long woodMax;

    /** 库存石材 */
    private Long rock;

    /** 石材产量 */
    private Long rockAdd;

    /** 石材上限 */
    private Long rockMax;

    /** 库存铁锭 */
    private Long iron;

    /** 铁锭产量 */
    private Long ironAdd;

    /** 铁锭上限 */
    private Long ironMax;

    /** 库存粮食 */
    private Long food;

    /** 粮食产量 */
    private Long foodAdd;

    /** 军队耗粮食 */
    private Long foodArmyUse;

    /** 粮食上限 */
    private Long foodMax;

    /** 库存黄金 */
    private Long gold;

    /** 将领薪水 */
    private Long heroFee;

    /** 黄金上限 */
    private Long goldMax;

    /** 黄金产量分倍率 */
    private Integer goldRate;

    /** 当前总人口 */
    private Long people;

    /** 人口稳定值 */
    private Long peopleStable;

    /** 工作耗人口 */
    private Long peopleWorking;

    /** 建筑人口 */
    private Long peopleBuilding;

    /** 人口最大值 */
    private Long peopleMax;

    /** 首领忠诚?? */
    private Integer chiefLoyalty;

    /**  */
    private Long changing;

    /**  */
    private Long lastUpdate;

    /** ０表示正常，１表示休假 */
    private Integer vacation;

    /** ０表示正常，１表示封禁 */
    private Integer forbidden;

    /** 自动建筑 */
    private Integer autoBuilding;

    /** 自动科技 */
    private Integer autoTechnic;

}