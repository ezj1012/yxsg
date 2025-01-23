package com.yxbear.sg.coder;

import com.yxbear.core.coder.CodeBuilder;

public class WorldCoder {

    public static void main(String[] args) {
        CodeBuilder coder = new CodeBuilder("sg", "com.yxbear.sg");
        coder.pkg("mem")//
                .modelIntNoAudit("worldTile") //
                .attrInt2("tileType", "格子地型：0:城池;1:平地;2:沙漠;3:森林;4:草地;5:高山;6:湖泊;7:沼泽;") //
                .attrInt2("province", "州") //
                .attrInt2("jun", "郡") //
                .attrInt4("curLevel", "当前等级") //
                .attrInt4("maxLevel", "最大等级") //
                .attrLong("ownerCityId", "所属城池") //
                .attrInt2("state", "当前状态") //
                ._____endCheck____() //
                //
                .buildPackageCode();
    }

}
