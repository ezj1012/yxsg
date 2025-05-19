package com.yxbear.sg.domain;

import java.util.Set;

public interface SGConstant {

    static final String USER_SESSION_KEY = "user_info";

    static final String PLAYER_SESSION_KEY = "player_info";

    /** 性别男 */
    static final int GENDER_MALE = 1;

    /** 性别女 */
    static final int GENDER_FEMALE = 2;

    /**
     * 特殊建筑ID集合：这些ID代表游戏中具有唯一功能的建筑类型。
     *
     * <pre>
     * 编号对应建筑如下：
     * 6  - 官府
     * 7  - 书院
     * 13 - 市场
     * 14 - 铁匠铺
     * 16 - 马厩
     * 15 - 工匠作坊
     * 10 - 客栈
     * 17 - 仓库
     * 8  - 校场
     * 11 - 招贤馆
     * 12 - 鸿胪寺
     * 18 - 驿站
     * 19 - 烽火台
     * 20 - 城墙
     * </pre>
     */
    public static final Set<Integer> UNIQUE_BUILD_IDS = Set.of(6, 7, 13, 14, 16, 15, 10, 17, 8, 11, 12, 18, 19, 20);

}
