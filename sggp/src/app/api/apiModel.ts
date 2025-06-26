export class User {
    token: string
    name: string
    id: number
    constructor(token: string, name: string, id: number) {
        this.token = token
        this.name = name
        this.id = id
    }
}

export interface FrameStageCfg {
    key: string
    name: string
    comps: string[]
}
export interface CfgGoods {
    id: number
    name: string
    inuse: number
    battle: number
    copperBox: number
    silverBox: number
    goldBox: number
    diamondBox: number
    goodsValue: number
    groupType: number
    image: string
    description: string
    useGuide: string
    autoUse: number
    zuoqiType: number
    level: number
    attrs: string
    imageType: number
    batch: number
    compose: number
}
export interface FrameCfg {
    rsmHttpRoot: string
    imgs: string[]
    stages: FrameStageCfg[]
}

export interface GlobalCfg {
    playerIcons: PlayerIcon[]
    buildingsMap: Record<number, CityBuildingCfg>
    soldiersMap: Record<number, SoldierCfg>
    defencesMap: Record<number, DefenceCfg>
}

export interface CityBuildingCfg {
    id: number
    place: number
    name: string
    typeName: string
    description: string
    levels: Record<number, CfgBuildingLevel>;
    // lv > type
    preCdts: Record<number, Record<0 | 1 | 2, Record<number, number>>>;
}



export interface CfgBuildingLevel {
    id: number
    buildId: number
    level: number
    upgradeWood: number
    upgradeRock: number
    upgradeIron: number
    upgradeFood: number
    upgradeGold: number
    upgradePeople: number
    upgradeTime: number
    usingPeople: number
    description: string
}

export interface CfgSoldier {
    id: number
    soldierType: number
    fromCity: number
    name: string
    description: string
    hp: number
    ap: number
    dp: number
    apRange: number
    speed: number
    carry: number
    timeNeed: number
    woodNeed: number
    rockNeed: number
    ironNeed: number
    foodNeed: number
    goldNeed: number
    peopleNeed: number
    foodUse: number
}


export interface SoldierCfg extends CfgSoldier {
    preCdts: Record<0 | 1 | 2, Record<number, number>>;
}
export interface CfgDefence {
    id: number
    defenceType: number
    name: string
    description: string
    hp: number
    ap: number
    dp: number
    apRange: number
    speed: number
    carry: number
    timeNeed: number
    woodNeed: number
    rockNeed: number
    ironNeed: number
    foodNeed: number
    goldNeed: number
    areaNeed: number
}
export interface DefenceCfg extends CfgDefence {
    preCdts: Record<0 | 1 | 2, Record<number, number>>;
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
    unionName: string
    money: number
    armorColumn: number
    vipLevel: number
    cities: CityIntro[]
    city: PlayCityInfo
    foods: Record<number, number>
    usedFoods: Record<number, any>
    time: number
}

export interface PlayGoods {
    id: number
    goodsId: number
    playId: number
    goodsType: number
    count: number
    cfg?: CfgGoods
    img?: string
}

export interface PlayCityInfo {
    id: number
    playId: number
    name: string
    cityType: number
    state: number
    chiefhId: number
    counsellorId: number
    generalId: number
    collegelv: number
    provinceId: number
    conscript: number
    discardTime: number
    res: GiCityResource
    // resAdd: GiCityResourceAdd
    buildings: CityBuilding[]
    // defences: GiCityDefence[]
    soldiers: GiCitySoldier[]
    defences: GiCityDefence[]
    heros: CityHero[]
    maxOuterBuild: number
}
export interface GiCityHero {
    id: number
    playId: number
    npcId: number
    cityId: number
    name: string
    cityType: number
    state: number
    gender: number
    face: number
    heroType: number
    heroHealth: number
    level: number
    exp: number
    commandBase: number
    commandAddOn: number
    affairsBase: number
    braveryBase: number
    wisdomBase: number
    affairsAdd: number
    braveryAdd: number
    wisdomAdd: number
    affairsAddOn: number
    braveryAddOn: number
    wisdomAddOn: number
    forceMaxAddOn: number
    energyMaxAddOn: number
    speedAddOn: number
    attackAddOn: number
    defenceAddOn: number
    loyalty: number
}

export interface CityHero extends GiCityHero {

}

export interface GiCitySoldier {
    id: number
    cityId: number
    soldierId: number
    count: number
    curLevel: number
}

export interface GiCityDefence {
    id: number
    cityId: number
    defenceId: number
    count: number
}

export interface CityBuilding {
    id: number
    cityId: number
    bid: number
    pos: number
    lv: number
    status: number
    goalLv: number
    endTime: number
    endTimeFormat?: string //后端计算
    nextLv: number
}

export interface GiCityResource {
    id: number
    morale: number
    moraleStable: number
    complaint: number
    tax: number
    wood: number
    woodAdd: number
    woodMax: number
    rock: number
    rockAdd: number
    rockMax: number
    iron: number
    ironAdd: number
    ironMax: number
    food: number
    foodAdd: number
    foodArmyUse: number
    foodMax: number
    gold: number
    heroFee: number
    goldMax: number
    goldRate: number
    people: number
    peopleStable: number
    peopleWorking: number
    peopleBuilding: number
    peopleMax: number
    chiefLoyalty: number
    changing: number
    lastUpdate: number
    vacation: number
    forbidden: number
    autoBuilding: number
    autoTechnic: number
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
    idStr?: string;
}

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

export interface CityTechnic {
    id: number
    name: string
    typeName: string
    description: string
}


export interface CityIntro {
    id: number
    name: string
    people: number
}
