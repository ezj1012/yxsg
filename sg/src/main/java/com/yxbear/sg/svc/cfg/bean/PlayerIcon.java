package com.yxbear.sg.svc.cfg.bean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.yxbear.sg.domain.SGConstant;

public record PlayerIcon(
        //
        int orderNo, //
        /**
         * 0 未定义<br/>
         * 男性 {@linkplain SGConstant#GENDER_MALE }<br/>
         * 女性 {@linkplain SGConstant#GENDER_FEMALE }
         */
        int genderType,
        //
        String icon) {

    public static final String DIR_PREFIX = "/rsm/player/";

    private static final List<PlayerIcon> playerIcons = new ArrayList<>(32);

    public static void refresh(final File iconDir) {
        File[] listFiles = iconDir.listFiles(File::isFile);
        if (listFiles == null || listFiles.length == 0) {
            playerIcons.clear();
            return;
        }
        List<PlayerIcon> newPlayerIcons = new ArrayList<>(32);
        final OrderCount count = new OrderCount();

        Stream.of(listFiles).map(File::getName).sorted().forEach(fName -> {
            PlayerIcon icon = PlayerIcon.ofIcon(fName, count);
            if (icon.genderType() != 0) {
                newPlayerIcons.add(icon);
            }
        });
        playerIcons.clear();
        playerIcons.addAll(newPlayerIcons);
    }

    public static List<PlayerIcon> getPlayerIcons() {
        return playerIcons;
    }

    private static PlayerIcon ofIcon(final String fileName, final OrderCount count) {
        int genderType = 0;
        try {
            genderType = Integer.parseInt(fileName.substring(0, 1));
            genderType = genderType == SGConstant.GENDER_MALE || genderType == SGConstant.GENDER_FEMALE ? genderType : 0;
        } catch (Exception e) {
            // nothing
        }
        return new PlayerIcon(count.getOrderByGenderType(genderType), genderType, DIR_PREFIX + fileName);
    }

    private static class OrderCount {

        int maleOrder = 0;

        int femaleOrder = 0;

        int unknowOrder = 0;

        public int getOrderByGenderType(int genderType) {
            if (genderType == SGConstant.GENDER_MALE) {
                return 10000 + maleOrder++;
            } else if (genderType == SGConstant.GENDER_FEMALE) {
                return 20000 + femaleOrder++;
            } else {
                return unknowOrder++;
            }
        }

    }

}