<script setup lang="ts">
// import { CityBuildState, type PlayerCityBuild } from '@/app/model/comm';
// import { GCfg } from '@/app/res/GloabalCfgMgr';
// import type { CompInfo } from '@/app/stage/CompMgr';
// import type { BgVcfg, InnerCityVcfg, ProvincesMapVcfg } from '@/app/stage/CompModel';
// import DataMgr from '@/app/stage/DataMgr';
// import type Img from '@/app/utils/Img';
// import Msg from '@/app/utils/Msg';
// import Res from '@/app/utils/Res';
// import { Content } from 'view-ui-plus';
// import { ref, inject, type Ref, watch, onMounted, computed } from 'vue'

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

// function initArrays(builds: Ref<Build[]>) {
//     builds.value.push(
//         // 0: 官服,1: 城墙
//         Build.of(370, 180, Res.imgCfgMgr.getGroup('playing#innercity#goverment')!.getDef()!, Res.imgCfgMgr.getGroup('playing#innercity#goverment')!.getDef()!)
//             .setLvXy(155, 35)
//         ,
//         Build.of(0, 25, Res.imgCfgMgr.getGroup('playing#innercity#townwall')!.getDef()!, Res.imgCfgMgr.getGroup('playing#innercity#townwall')!.getDef()!)
//             .setLvXy(565, 140)
//         ,
//         // 
//         Build.of(297, 124, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!),
//         Build.of(352, 152, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!),
//         Build.of(517, 236, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!),
//         Build.of(572, 264, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!),

//         Build.of(242, 152, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!),
//         Build.of(297, 180, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!),
//         Build.of(462, 264, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!),
//         Build.of(517, 292, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!),

//         Build.of(187, 180, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!),
//         Build.of(242, 208, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!),
//         Build.of(297, 236, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!),
//         Build.of(352, 264, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!),
//         Build.of(407, 292, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!),
//         Build.of(462, 320, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!),

//         Build.of(132, 208, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!),
//         Build.of(187, 236, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!),
//         Build.of(242, 264, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!),
//         Build.of(297, 292, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!),
//         Build.of(352, 320, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!),
//         Build.of(407, 348, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!),


//         Build.of(77, 236, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!),
//         Build.of(132, 264, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!),
//         Build.of(187, 292, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!),
//         Build.of(242, 320, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!),
//         Build.of(297, 348, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!),
//         Build.of(352, 376, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!),


//         Build.of(22, 264, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!),
//         Build.of(77, 292, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!),
//         Build.of(132, 320, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!),
//         Build.of(187, 348, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!),
//         Build.of(242, 376, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!),
//         Build.of(297, 404, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!, Res.imgCfgMgr.getGroup('playing#buildbg')!.getDef()!),
//     )
//     builds.value[0].z = 1
//     builds.value[1].z = 1

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

// }

// function refreshState(builds: Ref<Build[]>) {
//     const datas = DataMgr.get('playing#player_city_innerBuilds') as PlayerCityBuild[]
//     if (datas) {
//         for (let i = 0; i < builds.value.length; i++) {
//             const build = builds.value[i];
//             build.update(datas[i])
//         }
//     }
// }

// const { compInfo } = defineProps({ compInfo: { required: true } }) as { compInfo: CompInfo }

// const builds = ref<Build[]>([])
// const buildImgs = ref(new Map<number, Img.ImgDef>)
// const msgg = ref(new Msg.CompactSimpleMsg(''))
// const msgV = ref({ msg: msgg, isHover: isHoverItem })
// const cfgV = ref<InnerCityVcfg>(compInfo.getCfg() as InnerCityVcfg)
// const hoverbuildIdx = ref(-1)

// const show = ref(DataMgr.get('playing#btn_top_innercity_sct') ? true : false)


// initArrays(builds)
// DataMgr.subscribe('playing#player_city_innerBuilds', undefined, (key: string, newValue: any, oldValue: any) => refreshState(builds));
// DataMgr.subscribe('playing#btn_top_innercity_sct', undefined, (key: string, newValue: any, oldValue: any) => { show.value = newValue ? true : false });

// refreshState(builds)

// onMounted(() => { })

// function isHoverItem(e: MouseEvent) {
//     for (let i = builds.value.length - 1; i >= 0; i--) {
//         const build = builds.value[i]
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


</script>

<template>
    <!--  <div v-show="show" class="inner-city" v-msg="msgV"
        :style="{ backgroundImage: `url(${cfgV.bgImg?.url})`, left: `${cfgV.x}px`, top: `${cfgV.y}px`, zIndex: cfgV.z }">

       建筑-->
    <!--    <div class="build" v-for="build, idx in builds" :style="{
            backgroundImage: `url(${build.getUrl()})`, left: `${build.getX()}px`, top: `${build.getY()}px`, width:
                `${build.getW()}px`, height: `${build.getH()}px`, zIndex: build.z
        }" :class="{
            'br': hoverbuildIdx == idx,
            'gr': build.isChange()
        }">
            <div class="lv-img" v-show="build.isShowLvImg()" :style="{
            backgroundImage: `url(${build.getLvImg().getDataUrl()})`, left: `${build.getLvX()}px`, top: `${build.getLvY()}px`
        }"></div>
           {{ `${idx}=${build.x},${build.y}` }}
        </div>


    </div> -->
    <div>世界地图</div>
</template>
<style lang="less" scoped>
.inner-city {
    position: absolute;
    width: 731px;
    height: 550px;
    user-select: none;

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
    }

    .gr {
        filter: grayscale(1);
    }
}
</style>
