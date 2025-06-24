package com.yxbear.sg.domain.model.cfg;

import com.yxbear.core.bean.Condition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CCfgSoldierCondition implements Condition {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer[] ids;

    private Integer startId;

    private Integer endId;

    private Integer soldierId;

    private Integer[] soldierIds;

    private Integer startSoldierId;

    private Integer endSoldierId;

    private Integer preType;

    private Integer[] preTypes;

    private Integer startPreType;

    private Integer endPreType;

    private Integer preId;

    private Integer[] preIds;

    private Integer startPreId;

    private Integer endPreId;

    private Integer preLevel;

    private Integer[] preLevels;

    private Integer startPreLevel;

    private Integer endPreLevel;

}