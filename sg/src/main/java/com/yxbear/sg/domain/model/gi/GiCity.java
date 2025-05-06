package com.yxbear.sg.domain.model.gi;

import com.yxbear.core.bean.EntityBean;

import lombok.Data;

@Data
public class GiCity implements EntityBean<Integer> {

    /** 唯一主键 */
    private Integer id;

    /**  */
    private Integer playId;

    /**  */
    private String name;

    /** 类型0:玩家城池1:名城2:郡城3:州城4:都城5:玩家主城 */
    private Integer cityType;

    /** 城池状态0：正常;1：新手保护;2：免战;3：休假 */
    private Integer state;

    /**  */
    private Integer chiefhId;

    /**  */
    private Integer counsellorId;

    /**  */
    private Integer provinceId;

    /** 否是可以募兵，0表示不可以，1表示可以，大于2，最后招募时间 */
    private Integer conscript;

    /** 废弃时间 */
    private Long discardTime;

}