package com.yxbear.sg.domain.model.cfg;

import com.yxbear.core.bean.EntityBean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CfgTechnicLevel implements EntityBean<Integer> {

    /** 唯一主键 */
    private Integer id;

    /** 科技ID */
    private Integer technicId;

    /** 科技等级 */
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

    /** 描述 */
    private String description;

}