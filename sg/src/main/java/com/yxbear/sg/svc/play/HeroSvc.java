package com.yxbear.sg.svc.play;

import com.yxbear.sg.domain.bean.Userable;
import com.yxbear.sg.engine.model.ProvinceLand;
import com.yxbear.sg.svc.play.bean.PlayInfo;

public interface HeroSvc {

    void createFirstHero(ProvinceLand land, Userable user);


}
