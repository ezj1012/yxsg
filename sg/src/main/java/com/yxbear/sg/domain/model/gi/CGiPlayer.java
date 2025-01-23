package com.yxbear.sg.domain.model.gi;

import com.yxbear.core.bean.Condition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CGiPlayer implements Condition {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer[] ids;

    private Integer startId;

    private Integer endId;

    private String name;

    private String[] names;

    private String nameEqual;

    private Integer state;

    private Integer[] states;

    private Integer startState;

    private Integer endState;

    private Integer playerGroup;

    private Integer[] playerGroups;

    private Integer startPlayerGroup;

    private Integer endPlayerGroup;

    private Integer gender;

    private Integer[] genders;

    private Integer startGender;

    private Integer endGender;

    private Integer lastCity;

    private Integer[] lastCitys;

    private Integer startLastCity;

    private Integer endLastCity;

    private String face;

    private String[] faces;

    private String faceEqual;

    private String flagChar;

    private String[] flagChars;

    private String flagCharEqual;

    private Integer playerRank;

    private Integer[] playerRanks;

    private Integer startPlayerRank;

    private Integer endPlayerRank;

    private Long prestige;

    private Long[] prestiges;

    private Long startPrestige;

    private Long endPrestige;

    private Long warPrestige;

    private Long[] warPrestiges;

    private Long startWarPrestige;

    private Long endWarPrestige;

    private Long warAttackPrestige;

    private Long[] warAttackPrestiges;

    private Long startWarAttackPrestige;

    private Long endWarAttackPrestige;

    private Long warDefencePrestige;

    private Long[] warDefencePrestiges;

    private Long startWarDefencePrestige;

    private Long endWarDefencePrestige;

    private Integer nobility;

    private Integer[] nobilitys;

    private Integer startNobility;

    private Integer endNobility;

    private Integer officepos;

    private Integer[] officeposs;

    private Integer startOfficepos;

    private Integer endOfficepos;

    private Integer unionId;

    private Integer[] unionIds;

    private Integer startUnionId;

    private Integer endUnionId;

    private Integer unionPos;

    private Integer[] unionPoss;

    private Integer startUnionPos;

    private Integer endUnionPos;

    private Long money;

    private Long[] moneys;

    private Long startMoney;

    private Long endMoney;

    private Integer armorColumn;

    private Integer[] armorColumns;

    private Integer startArmorColumn;

    private Integer endArmorColumn;

    private Integer vipLevel;

    private Integer[] vipLevels;

    private Integer startVipLevel;

    private Integer endVipLevel;

}