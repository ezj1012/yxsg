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
public class CCfgTechnicCondition implements Condition {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer[] ids;

    private Integer startId;

    private Integer endId;

    private Integer technicId;

    private Integer[] technicIds;

    private Integer startTechnicId;

    private Integer endTechnicId;

    private Integer levelId;

    private Integer[] levelIds;

    private Integer startLevelId;

    private Integer endLevelId;

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