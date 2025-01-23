<script setup lang="ts">
// import Ctrl from '@/app/ctrl';
// import { CityBuildState, type PlayerCityBuild } from '@/app/model/comm';
// import { GCfg } from '@/app/res/GloabalCfgMgr';
// import ActionMgr from '@/app/stage/ActionMgr';
// import { CompType } from '@/app/stage/CompMappingCache';
// import type { CompInfo } from '@/app/stage/CompMgr';
// import type { BgVcfg, OutCityVcfg, ProvincesMapVcfg } from '@/app/stage/CompModel';
// import DataMgr from '@/app/stage/DataMgr';
// import type Img from '@/app/utils/Img';
// import Msg from '@/app/utils/Msg';
// import Res from '@/app/utils/Res';
// import { Content } from 'view-ui-plus';
// import { ref, inject, type Ref, watch, onMounted, shallowRef } from 'vue'

// class Build {
//     x: number
//     y: number
//     lvx?: number
//     lvy?: number
//     w: number
//     h: number
//     z: number = 15
//     emptyImg: Img.ImgDef
//     img: Img.ImgDef
//     cb?: PlayerCityBuild
//     showLv?: number

//     constructor(x: number, y: number, w: number, h: number, emptyUrl: Img.ImgDef, url: Img.ImgDef) {
//         this.x = x
//         this.y = y
//         this.w = w
//         this.h = h
//         this.emptyImg = emptyUrl
//         this.img = url
//     }

//     static of(x: number, y: number, emptyUrl: Img.ImgDef, url: Img.ImgDef) {
//         const w = emptyUrl.width
//         const h = emptyUrl.height
//         return new Build(x, y, w, h, emptyUrl, url)
//     }

//     getX() {
//         if (this.cb && this.cb.id !== 0 && this.cb.id !== 20 && this.cb.id !== 6 && this.cb.lv > 0) {
//             return this.x + 3;
//         } else {
//             return this.x;
//         }
//     }

//     getY() {
//         if (this.cb && this.cb.id !== 0 && this.cb.id !== 20 && this.cb.id !== 6 && this.cb.lv > 0) {
//             return this.y - 30;
//         } else {
//             return this.y;
//         }
//     }

//     isChange() {
//         return this.cb && this.cb.state != CityBuildState.Normal
//     }

//     getW() {
//         return this.getImg().width
//     }

//     getH() {
//         return this.getImg().height
//     }

//     getUrl() {
//         return this.getImg().getDataUrl()
//     }

//     getImg() {
//         return this.cb && this.cb.lv > 0 ? this.img : this.emptyImg
//     }

//     getLvX() {
//         return this.lvx || this.getW() / 2 - 12
//     }

//     getLvY() {
//         return this.lvy || this.getH() / 2
//     }

//     setLvXy(x: number, y: number) {
//         this.lvx = x
//         this.lvy = y
//         return this
//     }

//     isShowLvImg() {
//         return DataMgr.get('playing#btn_top_lv_sct') && this.cb && this.cb.id != 0
//     }

//     getLvImg() {
//         let lv = this.cb ? this.cb.lv : 0
//         return GCfg.getLvImg(lv)
//     }

//     getMsg() {
//         let msg = '空地'
//         if (this.cb && this.cb.id !== 0) {
//             // console.log(this.cb, GCfg.getBuilding(this.cb.id), GCfg.getBuildings())
//             msg = GCfg.getBuilding(this.cb.id)!.name
//             if (this.cb.lv == 0) {
//                 if (this.cb.id == 20) {
//                     msg = '无' + msg
//                 }
//                 return msg
//             } else {
//                 return `${msg} ${this.cb.lv}级`
//             }
//         }
//         return '空地'
//     }

//     update(s: PlayerCityBuild) {
//         this.cb = s
//         if (this.cb.lv === 0) {
//             this.img = this.emptyImg
//         } else {
//             this.img = buildImgs.value.get(this.cb.id)!
//             if (!this.img) {
//                 console.log('error ! ', this.cb)
//             }
//         }
//     }



// }

// function of(x: number, y: number, idx: number) {
//     const b = Build.of(x, y, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!)
//     b.showLv = idx
//     return b
// }

// function initArrays(builds: Ref<Build[]>) {
//     builds.value.push(
//         of(279, 58, 0), // 0
//         of(335, 87, 0), // 1
//         of(615, 232, 0), // 6
//         of(727, 290, 0), // 8
//         of(783, 319, 0), // 9
//         of(223, 87, 0), // 10
//         of(279, 116, 0), // 11
//         of(559, 261, 0), // 16
//         of(615, 290, 0), // 17
//         of(727, 348, 0), // 19
//         of(167, 116, 0), // 20
//         of(223, 145, 0), // 21
//         of(503, 290, 0), // 26
//         of(559, 319, 0), // 27
//         of(615, 348, 0), // 28
//         of(111, 145, 0), // 30
//         of(167, 174, 0), // 31
//         of(223, 203, 0), // 32
//         of(279, 232, 0), // 33
//         of(335, 261, 0), // 34
//         of(391, 290, 0), // 35
//         of(447, 319, 0), // 36
//         of(503, 348, 0), // 37
//         of(559, 377, 0), // 38

//         of(167, 232, 2), // 43
//         of(223, 261, 2), // 44
//         of(279, 290, 2), // 45

//         of(111, 203, 3), // 42
//         of(335, 319, 3), // 46
//         of(391, 348, 3), // 47

//         of(615, 406, 4), // 39
//         of(447, 377, 4), // 48
//         of(503, 406, 4), // 49

//         of(111, 261, 5), // 52
//         of(167, 290, 5), // 53
//         of(223, 319, 5), // 54

//         of(279, 348, 6), // 55
//         of(335, 377, 6), // 56
//         of(391, 406, 6), // 57


//         of(111, 319, 7), // 63
//         of(167, 348, 7), // 64
//         of(223, 377, 7), // 65

//         of(55, 174, 8), // 41
//         of(55, 232, 8), // 51
//         of(55, 290, 8), // 62

//         of(447, 435, 9), // 58
//         of(279, 406, 9), // 66
//         of(335, 435, 9), // 67

//         of(55, 348, 10), // 73
//         of(111, 377, 10), // 74
//         of(167, 406, 10), // 75


//         of(223, 435, 11), // 76
//         of(279, 464, 11), // 77
//         of(391, 464, 11), // 68

//         of(55, 406, 12), // 84
//         of(111, 435, 12), // 85
//         of(167, 464, 12), // 86

//         of(-1, 203, 13), // 50
//         of(-1, 261, 13), // 61
//         of(-1, 319, 13), // 72

//         of(-1, 377, 14), // 83
//         of(-1, 435, 14), // 94
//         of(55, 464, 14), // 95

//         of(335, 493, 15), // 78
//         of(223, 493, 15), // 87
//         of(111, 493, 15), // 96
//     )
//     console.log(`build size ${builds.value.length}`)

//     // 
//     buildImgs.value.clear()
//     GCfg.getBuildings().forEach(b => {
//         if (b.place == 1) {
//             // console.log(`key: playing#innercity#${b.typeName}`)
//             try {
//                 buildImgs.value.set(b.id, Res.imgCfgMgr.getGroup(`playing#innercity#${b.typeName}`)!.getDef()!)
//             } catch (error) {

//             }
//         }
//     })
//     refreshShowBuilds()
// }

// function refreshState(builds: Ref<Build[]>) {
//     const datas = DataMgr.get('playing#player_city_innerBuilds') as PlayerCityBuild[]
//     if (datas) {
//         for (let i = 0; i < showBuilds.value.length; i++) {
//             const build = showBuilds.value[i];
//             // build.update(datas[i])
//         }
//     }
// }
// const { compInfo } = defineProps({ compInfo: { required: true } }) as { compInfo: CompInfo }



// // 官府等级
// const gLv = ref(1)
// // 城池显示样式
// const cityImg = ref(Res.imgCfgMgr.getGroup('playing#outcity#building_outertown')!.getDef()!)
// // 所有的建筑
// const builds = shallowRef<Build[]>([])
// // 要显示的建筑
// const showBuilds = ref<Build[]>([])

// watch(gLv, refreshShowBuilds, { immediate: true })

// function refreshShowBuilds() {
//     const show = [];
//     let tSize = gLv.value >= 15 ? 15 : gLv.value < 0 ? 1 : gLv.value
//     tSize = 15
//     const maxSize = (tSize * 3) + 21
//     for (let i = 0; i < maxSize && i < builds.value.length; i++) {
//         show.push(builds.value[i])
//     }
//     showBuilds.value = show
// }

// const buildImgs = ref(new Map<number, Img.ImgDef>)
// const msgg = ref(new Msg.CompactSimpleMsg(''))
// const msgV = ref({ msg: msgg, isHover: isHoverItem })
// const cfgV = ref<OutCityVcfg>(compInfo.getCfg() as OutCityVcfg)
// const hoverbuildIdx = ref(-1)


// const show = ref(DataMgr.get('playing#btn_top_outcity_sct') ? true : false)
// DataMgr.subscribe('playing#btn_top_outcity_sct', undefined, (key: string, newValue: any, oldValue: any) => { show.value = newValue ? true : false });

// watch(cfgV, () => { }, { immediate: true })


// initArrays(builds)
// // 建筑资源
// DataMgr.subscribe('playing#player_city_outBuilds', undefined, (key: string, newValue: any, oldValue: any) => refreshState(builds));
// // 刷新城池图片和官服等级
// DataMgr.subscribe('playing#player_city_innerBuilds', undefined, (key: string, newValue: any, oldValue: any) => refreshCityImg());

// refreshState(builds)
// refreshCityImg()

// function refreshCityImg() {
//     const datas = DataMgr.get('playing#player_city_innerBuilds') as PlayerCityBuild[]

//     let key = 'playing#outcity#building_outertown'
//     if (datas && datas.length > 1) {
//         if (datas[1].lv > 0) {
//             key = 'playing#outcity#building_outercity'
//         }
//         gLv.value = datas[0].lv
//     }
//     cityImg.value = Res.imgCfgMgr.getGroup(key)!.getDef()!
// }

// onMounted(() => { })

// function isHoverItem(e: MouseEvent) {

//     if (cityImg.value.isHover(e.offsetX - 335, e.offsetY - 90)) {
//         msgg.value.msg = '城池'
//         if (hoverbuildIdx.value !== -2) {
//             hoverbuildIdx.value = -2
//         }
//         return true
//     }

//     for (let i = showBuilds.value.length - 1; i >= 0; i--) {
//         const build = showBuilds.value[i]
//         if (build.getImg().isHover(e.offsetX - build.getX(), e.offsetY - build.getY())) {
//             msgg.value.msg = build.getMsg()
//             if (hoverbuildIdx.value !== i) {
//                 hoverbuildIdx.value = i
//             }
//             return true
//         }
//     }
//     if (hoverbuildIdx.value !== -1) {
//         hoverbuildIdx.value = -1
//     }
//     return false
// }

// async function clickFun(e: MouseEvent) {
//     if (hoverbuildIdx.value === -2) {
//         goInCity(e)
//         return
//     } else if (hoverbuildIdx.value == -1) {
//         return
//     }
//     const build = showBuilds.value[hoverbuildIdx.value]
//     if (!build) { return }
//     if (build.cb) {
//         openCityBuild(build)
//         return
//     } else {
//         Ctrl.showFunPanl(CompType.cityBuildList)
//     }
// }

// async function openCityBuild(cb: Build) {

// }

// async function goInCity(e: MouseEvent) {
//     console.log('go in city', hoverbuildIdx.value)
//     await ActionMgr.execBtn('playing#mian_scene_innercity')
//     DataMgr.set('playing#btn_top_innercity_sct', true)
//     e.stopPropagation()
//     e.preventDefault()
// }


</script>

<template>
   <!--   <div v-show="show" class="out-city" v-msg="msgV"
        :style="{ backgroundImage: `url(${cfgV.bgImg?.url})`, left: `${cfgV.x}px`, top: `${cfgV.y}px`, zIndex: cfgV.z }"
        @click="clickFun">

        <div class="inner-city" :style="{ backgroundImage: `url(${cityImg.getDataUrl()})` }"
            :class="{ 'br': hoverbuildIdx == -2 }"></div>

       建筑
        <div class="build" v-for="build, idx in showBuilds" :style="{
            backgroundImage: `url(${build.getUrl()})`, left: `${build.getX()}px`, top: `${build.getY()}px`, width:
                `${build.getW()}px`, height: `${build.getH()}px`, zIndex: build.z
        }" :class="{
            'br': hoverbuildIdx == idx,
            'gr': build.isChange()
        }">
            {{ `${build.showLv}:${build.getX()},${build.getY()}` }}
            <div class="lv-img" v-show="build.isShowLvImg()" :style="{
            backgroundImage: `url(${build.getLvImg().getDataUrl()})`, left: `${build.getLvX()}px`, top: `${build.getLvY()}px`
        }"> </div>
        </div>
    </div>-->
    <div>城池</div>
</template>
<style lang="less" scoped>
.out-city {
    position: absolute;
    width: 731px;
    height: 550px;
    user-select: none;

    .inner-city {
        position: absolute;
        background-repeat: no-repeat;
        pointer-events: none;
        left: 335px;
        top: 90px;
        width: 356px;
        height: 222px;
    }

    .build {
        position: absolute;
        pointer-events: none;
        background-repeat: no-repeat;
        text-align: center;
        line-height: 55px;
        color: red;

        .lv-img {
            position: absolute;
            background-repeat: no-repeat;
            width: 20px;
            height: 15px;
        }
    }

    .br {
        filter: brightness(1.3);
        cursor: pointer;
    }

    .gr {
        filter: grayscale(1);
    }
}
</style>
