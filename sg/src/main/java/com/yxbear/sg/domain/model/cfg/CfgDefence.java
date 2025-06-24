package com.yxbear.sg.domain.model.cfg;

import com.yxbear.core.bean.EntityBean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CfgDefence implements EntityBean<Integer> {

    /** 唯一主键 */
    private Integer id;

    /** 城防类型 6：陷阱 7：拒马 8：箭塔 9：滚木 10：雷石 */
    private Integer defenceType;

    /** 名称 */
    private String name;

    /** 描述 */
    private String description;

    /** 生命 */
    private Integer hp;

    /** 攻击 */
    private Integer ap;

    /** 防御 */
    private Integer dp;

    /** 攻击范围 */
    private Integer apRange;

    /** 移动速度 */
    private Integer speed;

    /** 载重 */
    private Integer carry;

    /** 招募耗时 */
    private Integer timeNeed;

    /** 招募耗木材 */
    private Integer woodNeed;

    /** 招募耗石头 */
    private Integer rockNeed;

    /** 招募耗铁锭 */
    private Integer ironNeed;

    /** 招募耗粮食 */
    private Integer foodNeed;

    /** 招募耗金币 */
    private Integer goldNeed;

    /** 招募耗城墙空间 */
    private Integer areaNeed;

}