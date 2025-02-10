package com.yxbear.sg.domain.model.gi;

import com.yxbear.core.bean.EntityBean;

import lombok.Data;

@Data
public class GiCityResourceAdd implements EntityBean<Integer> {

    /** 唯一主键 */
    private Integer id;

    /** 粮食人口生产比例 */
    private Integer foodRate;

    /** 木头人口生产比例 */
    private Integer woodRate;

    /** 石材人口生产比例 */
    private Integer rockRate;

    /** 铁锭人口生产比例 */
    private Integer ironRate;

    /** 仓库存放粮食数量 */
    private Long foodStore;

    /** 仓库存木头食数量 */
    private Long woodStore;

    /** 仓库存石材食数量 */
    private Long rockStore;

    /** 仓库存铁锭食数量 */
    private Long ironStore;

    /** 野地对粮食加成 */
    private Long foodFieldAdd;

    /** 野地对木头加成 */
    private Long woodFieldAdd;

    /** 野地对石材加成 */
    private Long rockFieldAdd;

    /** 野地对铁锭加成 */
    private Long ironFieldAdd;

    /** 物品对粮食加成 */
    private Long foodGoodsAdd;

    /** 物品对木头加成 */
    private Long woodGoodsAdd;

    /** 物品对石材加成 */
    private Long rockGoodsAdd;

    /** 物品对铁锭加成 */
    private Long ironGoodsAdd;

    /** 将领加成 */
    private Long chiefAdd;

    /** 科技对粮食加成 */
    private Long foodSkillAdd;

    /** 科技对木头加成 */
    private Long woodSkillAdd;

    /** 科技对石材加成 */
    private Long rockSkillAdd;

    /** 科技对铁锭加成 */
    private Long ironSkillAdd;

    /** 资源是否发生过变化 */
    private Integer resourceChange;

}