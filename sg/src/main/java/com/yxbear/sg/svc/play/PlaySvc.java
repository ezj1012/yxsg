package com.yxbear.sg.svc.play;

import com.yxbear.sg.svc.play.bean.PlayInfo;
import com.yxbear.sg.svc.play.bean.QPlayState;

public interface PlaySvc {

    PlayInfo getPlayInfo(QPlayState state);

}
