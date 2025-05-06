package com.yxbear.sg.svc.play;

import com.yxbear.sg.domain.bean.Userable;
import com.yxbear.sg.engine.model.ProvinceLand;
import com.yxbear.sg.svc.play.bean.PlayInfo;

public interface CitySvc {

    void createFirstCity(ProvinceLand land, Userable user);

    void fillCity(PlayInfo info);

    void deleteAll(int playId);

}
