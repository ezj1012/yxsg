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
public class CCfgBuilding implements Condition {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer[] ids;

    private Integer startId;

    private Integer endId;

    private String name;

    private String[] names;

    private String nameEqual;

    private String typeName;

    private String[] typeNames;

    private String typeNameEqual;

    private String description;

    private String[] descriptions;

    private String descriptionEqual;

    private Integer place;

    private Integer[] places;

    private Integer startPlace;

    private Integer endPlace;

}