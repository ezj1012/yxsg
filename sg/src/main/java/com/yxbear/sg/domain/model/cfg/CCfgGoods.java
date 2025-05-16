package com.yxbear.sg.domain.model.cfg;

import com.yxbear.core.bean.Condition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CCfgGoods implements Condition {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer[] ids;

    private Integer startId;

    private Integer endId;

    private String name;

    private String[] names;

    private String nameEqual;

    private Integer inuse;

    private Integer[] inuses;

    private Integer startInuse;

    private Integer endInuse;

    private Integer battle;

    private Integer[] battles;

    private Integer startBattle;

    private Integer endBattle;

    private Integer copperBox;

    private Integer[] copperBoxs;

    private Integer startCopperBox;

    private Integer endCopperBox;

    private Integer silverBox;

    private Integer[] silverBoxs;

    private Integer startSilverBox;

    private Integer endSilverBox;

    private Integer goldBox;

    private Integer[] goldBoxs;

    private Integer startGoldBox;

    private Integer endGoldBox;

    private Integer diamondBox;

    private Integer[] diamondBoxs;

    private Integer startDiamondBox;

    private Integer endDiamondBox;

    private Integer goodsValue;

    private Integer[] goodsValues;

    private Integer startGoodsValue;

    private Integer endGoodsValue;

    private Integer groupType;

    private Integer[] groupTypes;

    private Integer startGroupType;

    private Integer endGroupType;

    private String image;

    private String[] images;

    private String imageEqual;

    private String description;

    private String[] descriptions;

    private String descriptionEqual;

    private String useGuide;

    private String[] useGuides;

    private String useGuideEqual;

    private Integer autoUse;

    private Integer[] autoUses;

    private Integer startAutoUse;

    private Integer endAutoUse;

    private Integer zuoqiType;

    private Integer[] zuoqiTypes;

    private Integer startZuoqiType;

    private Integer endZuoqiType;

    private Integer level;

    private Integer[] levels;

    private Integer startLevel;

    private Integer endLevel;

    private String attrs;

    private String[] attrss;

    private String attrsEqual;

    private Integer imageType;

    private Integer[] imageTypes;

    private Integer startImageType;

    private Integer endImageType;

    private Integer batch;

    private Integer[] batchs;

    private Integer startBatch;

    private Integer endBatch;

    private Integer compose;

    private Integer[] composes;

    private Integer startCompose;

    private Integer endCompose;

}