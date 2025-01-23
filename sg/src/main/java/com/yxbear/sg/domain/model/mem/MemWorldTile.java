package com.yxbear.sg.domain.model.mem;

import com.yxbear.core.bean.EntityBean;

import lombok.Data;

@Data
public class MemWorldTile implements EntityBean<Integer> {

    /** 唯一主键 */
    private Integer id;

    /** 格子地型：0:城池;1:平地;2:沙漠;3:森林;4:草地;5:高山;6:湖泊;7:沼泽; */
    private Integer tileType;

    /** 州 */
    private Integer province;

    /** 郡 */
    private Integer jun;

    /** 当前等级 */
    private Integer curLevel;

    /** 最大等级 */
    private Integer maxLevel;

    /** 所属城池 */
    private Long ownerCityId;

    /** 当前状态 */
    private Integer state;



}