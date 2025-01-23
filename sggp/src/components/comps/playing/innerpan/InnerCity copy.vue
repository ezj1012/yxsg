<script setup lang="ts">

import { CityBuildState, type CityBuild } from '@/app/api';
import { DATA_KEY_CITY_BUILD_INNER } from '@/app/constant';
import type { SanGuo } from '@/app/sg';
import type Img from '@/app/utils/Img';
import { ref, inject, type Ref, watch, onMounted, computed } from 'vue'

const { sg } = inject('sg') as { sg: SanGuo }

class Build {
    x: number
    y: number
    lvx?: number
    lvy?: number
    w: number
    h: number
    z: number = 15
    emptyImg: Img.ImgDef
    img: Img.ImgDef
    cb?: CityBuild

    constructor(x: number, y: number, w: number, h: number, emptyUrl: Img.ImgDef, url: Img.ImgDef) {
        this.x = x
        this.y = y
        this.w = w
        this.h = h
        this.emptyImg = emptyUrl
        this.img = url
    }

    static of(x: number, y: number, emptyUrl: Img.ImgDef, url: Img.ImgDef) {
        const w = emptyUrl.width
        const h = emptyUrl.height
        return new Build(x, y, w, h, emptyUrl, url)
    }

    getX() {
        if (this.cb && this.cb.id !== 0 && this.cb.id !== 20 && this.cb.id !== 6 && this.cb.lv > 0) {
            return this.x + 3;
        } else {
            return this.x;
        }
    }

    getY() {
        if (this.cb && this.cb.id !== 0 && this.cb.id !== 20 && this.cb.id !== 6 && this.cb.lv > 0) {
            return this.y - 30;
        } else {
            return this.y;
        }
    }

    isChange() {
        return this.cb && this.cb.state != CityBuildState.Normal
    }

    getW() {
        return this.getImg().width
    }

    getH() {
        return this.getImg().height
    }

    getUrl() {
        return `url(${this.getImg().getDataUrl()})`
    }

    getImg() {
        return this.cb && this.cb.lv > 0 ? this.img : this.emptyImg
    }

    getLvX() {
        return this.lvx || this.getW() / 2 - 12
    }

    getLvY() {
        return this.lvy || this.getH() / 2
    }

    setLvXy(x: number, y: number) {
        this.lvx = x
        this.lvy = y
        return this
    }

    isShowLvImg() {
        return sg.dataMgr.playMgr.showLv()
    }

    getLvImg() {
        let lv = this.cb ? this.cb.lv : 0
        return sg.res.getGlobarCfgMgr().getLvImg(lv)
    }

    getMsg() {
        let msg = '空地'
        if (this.cb && this.cb.id !== 0) {
            // console.log(this.cb, GCfg.getBuilding(this.cb.id), GCfg.getBuildings())
            msg = sg.res.getGlobarCfgMgr().getBuilding(this.cb.id)!.name
            if (this.cb.lv == 0) {
                if (this.cb.id == 20) {
                    msg = '无' + msg
                }
                return msg
            } else {
                return `${msg} ${this.cb.lv}级`
            }
        }
        return '空地'
    }

    update(s: CityBuild) {
        this.cb = s
        if (this.cb.lv === 0) {
            this.img = this.emptyImg
        } else {
            this.img = buildImgs.value.get(this.cb.id)!
            if (!this.img) {
                console.log('error ! ', this.cb)
            }
        }
    }
}

const builds = ref<Build[]>([])
const buildImgs = ref(new Map<number, Img.ImgDef>)

function initArrays(builds: Ref<Build[]>) {
    builds.value.push(
        //         // 0: 官服,1: 城墙
        Build.of(370, 180, sg.res.getImgGroup('playing#innercity#goverment')!.hasDef()!, sg.res.getImgGroup('playing#innercity#goverment')!.hasDef()!)
            .setLvXy(155, 35)
        ,
        Build.of(0, 25, sg.res.getImgGroup('playing#innercity#townwall')!.hasDef()!, sg.res.getImgGroup('playing#innercity#townwall')!.hasDef()!)
            .setLvXy(565, 140)
        ,
        // 
        Build.of(297, 124, sg.res.getImgGroup('playing#buildbg')!.hasDef()!, sg.res.getImgGroup('playing#buildbg')!.hasDef()!),
        Build.of(352, 152, sg.res.getImgGroup('playing#buildbg')!.hasDef()!, sg.res.getImgGroup('playing#buildbg')!.hasDef()!),
        Build.of(517, 236, sg.res.getImgGroup('playing#buildbg')!.hasDef()!, sg.res.getImgGroup('playing#buildbg')!.hasDef()!),
        Build.of(572, 264, sg.res.getImgGroup('playing#buildbg')!.hasDef()!, sg.res.getImgGroup('playing#buildbg')!.hasDef()!),

        Build.of(242, 152, sg.res.getImgGroup('playing#buildbg')!.hasDef()!, sg.res.getImgGroup('playing#buildbg')!.hasDef()!),
        Build.of(297, 180, sg.res.getImgGroup('playing#buildbg')!.hasDef()!, sg.res.getImgGroup('playing#buildbg')!.hasDef()!),
        Build.of(462, 264, sg.res.getImgGroup('playing#buildbg')!.hasDef()!, sg.res.getImgGroup('playing#buildbg')!.hasDef()!),
        Build.of(517, 292, sg.res.getImgGroup('playing#buildbg')!.hasDef()!, sg.res.getImgGroup('playing#buildbg')!.hasDef()!),

        Build.of(187, 180, sg.res.getImgGroup('playing#buildbg')!.hasDef()!, sg.res.getImgGroup('playing#buildbg')!.hasDef()!),
        Build.of(242, 208, sg.res.getImgGroup('playing#buildbg')!.hasDef()!, sg.res.getImgGroup('playing#buildbg')!.hasDef()!),
        Build.of(297, 236, sg.res.getImgGroup('playing#buildbg')!.hasDef()!, sg.res.getImgGroup('playing#buildbg')!.hasDef()!),
        Build.of(352, 264, sg.res.getImgGroup('playing#buildbg')!.hasDef()!, sg.res.getImgGroup('playing#buildbg')!.hasDef()!),
        Build.of(407, 292, sg.res.getImgGroup('playing#buildbg')!.hasDef()!, sg.res.getImgGroup('playing#buildbg')!.hasDef()!),
        Build.of(462, 320, sg.res.getImgGroup('playing#buildbg')!.hasDef()!, sg.res.getImgGroup('playing#buildbg')!.hasDef()!),

        Build.of(132, 208, sg.res.getImgGroup('playing#buildbg')!.hasDef()!, sg.res.getImgGroup('playing#buildbg')!.hasDef()!),
        Build.of(187, 236, sg.res.getImgGroup('playing#buildbg')!.hasDef()!, sg.res.getImgGroup('playing#buildbg')!.hasDef()!),
        Build.of(242, 264, sg.res.getImgGroup('playing#buildbg')!.hasDef()!, sg.res.getImgGroup('playing#buildbg')!.hasDef()!),
        Build.of(297, 292, sg.res.getImgGroup('playing#buildbg')!.hasDef()!, sg.res.getImgGroup('playing#buildbg')!.hasDef()!),
        Build.of(352, 320, sg.res.getImgGroup('playing#buildbg')!.hasDef()!, sg.res.getImgGroup('playing#buildbg')!.hasDef()!),
        Build.of(407, 348, sg.res.getImgGroup('playing#buildbg')!.hasDef()!, sg.res.getImgGroup('playing#buildbg')!.hasDef()!),


        Build.of(77, 236, sg.res.getImgGroup('playing#buildbg')!.hasDef()!, sg.res.getImgGroup('playing#buildbg')!.hasDef()!),
        Build.of(132, 264, sg.res.getImgGroup('playing#buildbg')!.hasDef()!, sg.res.getImgGroup('playing#buildbg')!.hasDef()!),
        Build.of(187, 292, sg.res.getImgGroup('playing#buildbg')!.hasDef()!, sg.res.getImgGroup('playing#buildbg')!.hasDef()!),
        Build.of(242, 320, sg.res.getImgGroup('playing#buildbg')!.hasDef()!, sg.res.getImgGroup('playing#buildbg')!.hasDef()!),
        Build.of(297, 348, sg.res.getImgGroup('playing#buildbg')!.hasDef()!, sg.res.getImgGroup('playing#buildbg')!.hasDef()!),
        Build.of(352, 376, sg.res.getImgGroup('playing#buildbg')!.hasDef()!, sg.res.getImgGroup('playing#buildbg')!.hasDef()!),


        Build.of(22, 264, sg.res.getImgGroup('playing#buildbg')!.hasDef()!, sg.res.getImgGroup('playing#buildbg')!.hasDef()!),
        Build.of(77, 292, sg.res.getImgGroup('playing#buildbg')!.hasDef()!, sg.res.getImgGroup('playing#buildbg')!.hasDef()!),
        Build.of(132, 320, sg.res.getImgGroup('playing#buildbg')!.hasDef()!, sg.res.getImgGroup('playing#buildbg')!.hasDef()!),
        Build.of(187, 348, sg.res.getImgGroup('playing#buildbg')!.hasDef()!, sg.res.getImgGroup('playing#buildbg')!.hasDef()!),
        Build.of(242, 376, sg.res.getImgGroup('playing#buildbg')!.hasDef()!, sg.res.getImgGroup('playing#buildbg')!.hasDef()!),
        Build.of(297, 404, sg.res.getImgGroup('playing#buildbg')!.hasDef()!, sg.res.getImgGroup('playing#buildbg')!.hasDef()!),
    )
    builds.value[0].z = 1
    builds.value[1].z = 1

    //     // 
    buildImgs.value.clear()
    sg.res.globarCfgMgr.getBuildings().forEach(b => {
        if (b.place == 1) {
            // console.log(`key: playing#innercity#${b.typeName}`)
            try {
                buildImgs.value.set(b.id, sg.res.getImgGroup(`playing#innercity#${b.typeName}`)!.hasDef()!)
            } catch (error) {
            }
        }
    })
}

function refreshState(builds: Ref<Build[]>) {
    const datas = sg.dataMgr.get('playing#player_city_innerBuilds') as CityBuild[]
    if (datas) {
        for (let i = 0; i < builds.value.length; i++) {
            const build = builds.value[i];
            build.update(datas[i])
        }
    }
}

// const { compInfo } = defineProps({ compInfo: { required: true } }) as { compInfo: CompInfo }

// const builds = ref<Build[]>([])
// const buildImgs = ref(new Map<number, Img.ImgDef>)
// const msgg = ref(new Msg.CompactSimpleMsg(''))
// const msgV = ref({ msg: msgg, isHover: isHoverItem })
// const cfgV = ref<InnerCityVcfg>(compInfo.getCfg() as InnerCityVcfg)
const hoverbuildIdx = ref(-1)

// const show = ref(DataMgr.get('playing#btn_top_innercity_sct') ? true : false)


initArrays(builds)
// DataMgr.subscribe('playing#player_city_innerBuilds', undefined, (key: string, newValue: any, oldValue: any) => refreshState(builds));
// DataMgr.subscribe('playing#btn_top_innercity_sct', undefined, (key: string, newValue: any, oldValue: any) => { show.value = newValue ? true : false });

refreshState(builds)

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


const msgInfo = ref('')
const msgV = ref({ msg: msgInfo, isHover: isHoverItem })


const highBg = `url(${sg.res.getImgGroup('playing#innercity#map_high').hasDef()?.getDataUrl()})`
const lowBg = `url(${sg.res.getImgGroup('playing#innercity#map_low').hasDef()?.getDataUrl()})`
const styles = computed(() => {
    const s = {} as any
    s.backgroundImage = builds.value[1].cb && builds.value[1].cb.lv > 0 ? highBg : lowBg
    return s
})
function isHoverItem(e: MouseEvent) {
    for (let i = builds.value.length - 1; i >= 0; i--) {
        const build = builds.value[i]
        if (build.getImg().isHover(e.offsetX - build.getX(), e.offsetY - build.getY())) {
            msgInfo.value = build.getMsg()
            if (hoverbuildIdx.value !== i) {
                hoverbuildIdx.value = i
            }
            return true
        }
    }
    if (hoverbuildIdx.value !== -1) {
        hoverbuildIdx.value = -1
    }
    return false
}

async function clickBuild(e: MouseEvent) {
    // console.log(isHoverItem(e))
    if (hoverbuildIdx.value === -1) {
        console.log('啥也没电')
        return
    } else if (hoverbuildIdx.value === 0) {
        console.log(hoverbuildIdx.value, '官府 ')
    } else if (hoverbuildIdx.value === 1) {
        console.log(hoverbuildIdx.value, '城墙 ')
    } else {
        const build = builds.value[hoverbuildIdx.value]
        console.log(build)
        if (build.cb && build.cb.state != 0) {

        } else {
            await openBuildList(build)
        }
    }

}

async function openBuildList(build: Build) {
    sg.funPanMgr.setComp('fun_pan#city_build_inner_list')
    sg.dataMgr.setByKey(DATA_KEY_CITY_BUILD_INNER, build)
}
</script>

<template>
    <div class="inner-city" :style="styles" v-msg="msgV" @click="clickBuild">
        <!-- 建筑 -->
        <!--建筑-->
        <div class=" build" v-for="build, idx in builds"
            :style="{ backgroundImage: build.getUrl(), left: `${build.getX()}px`, top: `${build.getY()}px`, width: `${build.getW()}px`, height: `${build.getH()}px`, zIndex: build.z }"
            :class="{
                'br': hoverbuildIdx == idx,
                'gr': build.isChange()
            }">
            <div class="lv-img" v-show="build.isShowLvImg()" :style="{
                backgroundImage: `url(${build.getLvImg().getDataUrl()})`, left: `${build.getLvX()}px`, top: `${build.getLvY()}px`
            }"></div>
            {{ `${idx}=${build.x},${build.y}` }}
        </div>
    </div>
</template>
<style lang="less" scoped>
.inner-city {
    position: absolute;
    width: 731px;
    height: 550px;
    user-select: none;
    background-repeat: no-repeat;
    background-position-y: 1px;

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
