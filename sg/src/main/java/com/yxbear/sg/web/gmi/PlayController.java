package com.yxbear.sg.web.gmi;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yxbear.core.bean.R;
import com.yxbear.sg.domain.bean.LoginUser;
import com.yxbear.sg.svc.play.PlayRegSvc;
import com.yxbear.sg.svc.play.PlaySvc;
import com.yxbear.sg.svc.play.bean.PlayInfo;
import com.yxbear.sg.svc.play.bean.QPlayState;
import com.yxbear.sg.svc.play.bean.RegPlay;
import com.yxbear.sg.svc.sso.SSOSvc;
import com.yxbear.sg.web.gmi.bean.LoginPlay;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/play")
@AllArgsConstructor
public class PlayController {

    SSOSvc ssoSvc;

    PlaySvc palySvc;

    PlayRegSvc playRegSvc;

    @PostMapping("/login")
    public R<LoginUser> login(@RequestBody LoginPlay user) {
        return R.of(ssoSvc.playLogin(user.getUsername(), user.getPassword()));
    }

    @PostMapping("/reg")
    public R<PlayInfo> playReg(@RequestBody RegPlay user) {
        playRegSvc.regPlay(user);
        return R.of(palySvc.getPlayInfo(null));
    }

    @PostMapping("/info")
    public R<PlayInfo> getPlayInfo(@RequestBody(required = false) QPlayState state) {
        return R.of(palySvc.getPlayInfo(state));
    }

}
