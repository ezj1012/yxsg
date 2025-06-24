package com.yxbear.sg.domain.model.cfg;

import com.yxbear.core.bean.EntityBean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CfgSoldier implements EntityBean<Integer> {

    /** 唯一主键 */
    private Integer id;

    /** 兵种类型 1：民夫 2：义兵 3：斥侯 4：长枪兵 5：刀盾兵 6：弓箭兵 7：轻骑兵 8：铁骑兵 9：辎重兵 10：床弩 11：冲车 12：投石车 */
    private Integer soldierType;

    /** 是否为城内可招兵种 */
    private Integer fromCity;

    /** 名称 */
    private String name;

    /** 描述 */
    private String description;

    /** 生命 */
    private Long hp;

    /** 攻击 */
    private Long ap;

    /** 防御 */
    private Long dp;

    /** 攻击范围 */
    private Long apRange;

    /** 移动速度 */
    private Long speed;

    /** 载重 */
    private Long carry;

    /** 招募耗时 */
    private Long timeNeed;

    /** 招募耗木材 */
    private Long woodNeed;

    /** 招募耗石头 */
    private Long rockNeed;

    /** 招募耗铁锭 */
    private Long ironNeed;

    /** 招募耗粮食 */
    private Long foodNeed;

    /** 招募耗金币 */
    private Long goldNeed;

    /** 招募耗人口 */
    private Long peopleNeed;

    /** 每小时耗粮食 */
    private Long foodUse;

}