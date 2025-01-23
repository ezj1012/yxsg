package com.yxbear.sg.engine.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WorldTile {

    /** 唯一主键 */
    Integer id;

    /** 格子地型：0:城池;1:平地;2:沙漠;3:森林;4:草地;5:高山;6:湖泊;7:沼泽; */
    Integer tileType;

    /** 州 */
    Integer province;

    /** 郡 */
    Integer jun;

    /** 当前等级 */
    Integer curLevel;

    /** 最大等级 */
    Integer maxLevel;

    /** 所属城池 */
    Long ownerCityId;

    /** 当前状态 */
    Integer state;

    // 超出范围的.
    public WorldTile(Integer id) {
        super();
        this.id = id;
        this.state = -1;
    }

}
