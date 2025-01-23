package com.yxbear.sg.domain;

import org.springframework.beans.BeanUtils;

import com.yxbear.core.exception.CoreException;
import com.yxbear.sg.domain.bean.LoginUser;

import cn.dev33.satoken.stp.StpUtil;

public class SystemUtils {

    public static LoginUser getCurUser() {
        try {
            return StpUtil.getSession().getModel(SGConstant.USER_SESSION_KEY, LoginUser.class);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str.trim());
    }

    public static void checkEmpty(Object obj, String str) {
        if (obj == null || obj instanceof String && "".equals(((String) obj).trim())) { throw new CoreException("参数" + str + "不可为空!"); }
    }

    public static <T> T copy(Object source, Class<? extends T> clazz) {
        T t = BeanUtils.instantiateClass(clazz);
        BeanUtils.copyProperties(source, t);
        return t;
    }

    /**
     * 
     * @param x
     *            1-500
     * @param y
     *            1-500
     * @return
     */
    public static int wid(int x, int y) {
        return ((Math.floorDiv(y, 10)) * 10000 + (Math.floorDiv(x, 10)) * 100 + (y % 10) * 10 + (x % 10));
    }

    public static int cid(int x, int y) {
        return y * 1000 + x;
    }

    public static int[] cid2xy(int cid) {
        int y = Math.floorDiv(cid, 1000);
        int x = (cid % 1000);
        return new int[] {x, y};
    }

    public static int wid2cid(int wid) {
        int y = (Math.floorDiv(wid, 10000) * 10 + Math.floorDiv((wid % 100), 10));
        int x = (Math.floorDiv((wid % 10000), 100) * 10 + (wid % 10));
        return y * 1000 + x;
    }

    public static int[] wid2xy(int wid) {
        int y = (Math.floorDiv(wid, 10000) * 10 + Math.floorDiv((wid % 100), 10));
        int x = (Math.floorDiv((wid % 10000), 100) * 10 + (wid % 10));
        return new int[] {x, y};
    }

    public static int cid2wid(int cid) {
        int y = Math.floorDiv(cid, 1000);
        int x = (cid % 1000);
        return wid(x, y);
    }

}
