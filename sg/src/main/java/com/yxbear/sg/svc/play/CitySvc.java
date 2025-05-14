package com.yxbear.sg.svc.play;

import com.yxbear.sg.domain.bean.Userable;
import com.yxbear.sg.engine.model.ProvinceLand;

public interface CitySvc {

    void createFirstCity(ProvinceLand land, Userable user);

    void deleteAll(int playId);

}
