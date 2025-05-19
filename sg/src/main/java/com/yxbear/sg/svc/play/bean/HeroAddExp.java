package com.yxbear.sg.svc.play.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HeroAddExp {
    int heroId;
    long exp;
    int fromType; // 0: 建筑,1:

    public static HeroAddExp fromBuild(int heroId, long exp) {
        return new HeroAddExp(heroId, exp, 0);
    }
}
