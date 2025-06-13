package com.yxbear.sg.svc.cfg.bean;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.lang.NonNull;

import com.yxbear.core.exception.ServiceException;
import com.yxbear.sg.domain.model.cfg.CfgNobility;
import com.yxbear.sg.domain.model.cfg.CfgOffice;

import lombok.Data;

@Data
public class GlobalCfg {

    private String name;

    private List<PlayerIcon> playerIcons;

    private Map<Integer, Building> buildingsMap;

    private Map<Integer, Technic> technicsMap;

    private LinkedHashMap<Integer, CfgOffice> officeMap;

    private LinkedHashMap<Integer, CfgNobility> nobilityMap;

    public @NonNull
    Building getAndCheckBuilding(int bid) {
        if (this.buildingsMap == null || !this.buildingsMap.containsKey(bid)) { throw new ServiceException("建筑[" + bid + "]不存在!"); }
        return this.buildingsMap.get(bid);
    }

    public @NonNull
    Technic getAndCheckTechnic(int tid) {
        if (this.technicsMap == null || !this.technicsMap.containsKey(tid)) { throw new ServiceException("科技[" + tid + "]不存在!"); }
        return this.technicsMap.get(tid);
    }

}
