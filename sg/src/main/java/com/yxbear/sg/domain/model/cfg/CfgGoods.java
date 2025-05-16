package com.yxbear.sg.domain.model.cfg;

import com.yxbear.core.bean.EntityBean;

import lombok.Data;

@Data
public class CfgGoods implements EntityBean<Integer> {

    /** 唯一主键 */
    private Integer id;

    /** 名字 */
    private String name;

    /** 0: 禁用 1:使用中 */
    private Integer inuse;

    /** 战场获得概率 */
    private Integer battle;

    /** 青铜箱获得概率 */
    private Integer copperBox;

    /** 白银箱获得概率 */
    private Integer silverBox;

    /** 黄金箱获得概率 */
    private Integer goldBox;

    /** 钻石箱获得概率 */
    private Integer diamondBox;

    /** 价值 */
    private Integer goodsValue;

    /** 0: 宝物类,1:加速类,2:生产类,3:宝箱类,4:珠宝类,5:任务类 */
    private Integer groupType;

    /** 物品图片 */
    private String image;

    /** 描述 */
    private String description;

    /** 使用指南 */
    private String useGuide;

    /** 0: 无法主动使用,1:可以页面使用 */
    private Integer autoUse;

    /** 1: 盔类, 2:甲类, 3:鞍类, 4:绳类, 5:脚类 */
    private Integer zuoqiType;

    /** 等级 */
    private Integer level;

    /** 属性加成 */
    private String attrs;

    /** 1: 将领头像image = x对应hero_boy_x.jpg */
    private Integer imageType;

    /** 0表示不能批量使用，1表示可以批量使用 */
    private Integer batch;

    /** 0表示不可以合成，1表示可以合成 */
    private Integer compose;

}