export interface PlayerIcon {
    orderNo: number
    /**
     * 0 未定义<br/>
     * 男性 {@linkplain SGConstant#GENDER_MALE }<br/>
     * 女性 {@linkplain SGConstant#GENDER_FEMALE }
     */
    genderType: number
    //
    icon: string
}

export interface Province {
    id: number
    name: string
    minority: boolean
    canMoveCity: boolean
    playerMaxCount: number
    playerCount: number
    cityCount: number
}

export interface GlobalCfg {
    playerIcons: PlayerIcon[]
}
export interface CityIntro {
    id: number
    name: string
}

export interface PlayInfo {
    id: number
    name: string
    state: number
    playerGroup: number
    gender: number
    lastCity: number
    face: string
    flagChar: string
    playerRank: number
    prestige: number
    warPrestige: number
    warAttackPrestige: number
    warDefencePrestige: number
    nobility: number
    officepos: number
    unionId: number
    unionPos: number
    money: number
    armorColumn: number
    vipLevel: number
    cities: CityIntro[]
    // city: PlayCityInfo
}


export interface MemMapTile {
    id: number
    tileType: number
    province: number
    jun: number
    curLevel: number
    maxLevel: number
    ownerCityId: number
    state: number
}
