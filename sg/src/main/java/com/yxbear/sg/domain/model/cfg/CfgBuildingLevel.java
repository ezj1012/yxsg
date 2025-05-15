package com.yxbear.sg.domain.model.cfg;

import com.yxbear.core.bean.EntityBean;

import lombok.Data;

@Data
public class CfgBuildingLevel implements EntityBean<Integer> {

    /** 唯一主键 */
    private Integer id;

    /** 建筑ID */
    private Integer buildId;

    /** 建筑等级 */
    private Integer level;

    /** 消耗木材 */
    private Long upgradeWood;

    /** 消耗石头 */
    private Long upgradeRock;

    /** 消耗铁定 */
    private Long upgradeIron;

    /** 消耗粮食 */
    private Long upgradeFood;

    /** 升级消耗金币 */
    private Long upgradeGold;

    /** 升级依赖人口 */
    private Integer upgradePeople;

    /** 升级耗时 */
    private Long upgradeTime;

    /** 占用人口 */
    private Long usingPeople;

    /** 描述 */
    private String description;

    /** 创建时间 */
    private Long createTime;

    /** 最后一次修改时间 */
    private Long modifyTime;

}