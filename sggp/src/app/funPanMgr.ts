
// import FunPanlInventory from "@/components/comps/playing/funpan/FunPanlInventory.vue";
// import FunPanlArmor from "@/components/comps/playing/funpan/FunPanlArmor.vue";
// import FunPanlKing from "@/components/comps/playing/funpan/FunPanlKing.vue";
// import FunPanInnverBuild from "@/components/comps/playing/funpan/builds/FunPanInnverBuild.vue";
// import HeroFun from "@/components/test/funpanl/HeroFun.vue";
// import CityBuildList from "@/components/comps/playing/funpanl/CityBuildList.vue";

import HeroFun from "@/components/test/funpanl/HeroFun.vue";
import CityBuildList from "@/components/comps/playing/funpanl/CityBuildList.vue";
import BuildHouse from "@/components/comps/playing/building/House.vue";
export const gFunPanComps = [
    // { key: 'fun_pan#playerinfo_inventory', comp: FunPanlInventory, content: '宝物', size: 13, color: '--gold' },
    // { key: 'fun_pan#playerinfo_armor', comp: FunPanlArmor, content: '装备', size: 13, color: '--gold' },
    // { key: 'fun_pan#playerinfo_king', comp: FunPanlKing, content: '君主', size: 13, color: '--gold' },
    // { key: 'fun_pan#city_build_inner_list', comp: FunPanInnverBuild, content: '建造建筑', size: 13, color: '--gold' },
    { key: 'fun_pan#city_builds', comp: CityBuildList, content: '建筑', size: 13, color: '--gold' },
    { key: 'fun_pan#topbutton_hero', comp: HeroFun, content: '英雄', size: 13, color: '--gold' },
    
    
    { key: 'fun_pan#build_house', comp: BuildHouse, content: '建筑_房子', size: 13, color: '--gold' },
]
