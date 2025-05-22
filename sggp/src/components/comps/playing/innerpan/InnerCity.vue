<script setup lang="ts">
import type { CityBuilding } from '@/app/api/apiModel';
import { BuildMgr } from '@/app/buildMgr';
import { BuildItem, useInnerBuilds } from '@/app/cityMgr';
import type { HoverMsgDef } from '@/app/commModel';
import { CompactSimpleMsg } from '@/app/msg/hoverMsgMgr';
import type { SgRes } from '@/app/res';
import type { SanGuo, SgCtx } from '@/app/sg';
import { computed, inject, onMounted, onUnmounted, ref, watch } from 'vue';


// import { CityBuildState, type CityBuild } from '@/app/api';
// import { DATA_KEY_CITY_BUILD_INNER } from '@/app/constant';
// import { DataMgr } from '@/app/dataMgr';
// import type { SanGuo } from '@/app/sg';
// import type Img from '@/app/utils/Img';
// import { ref, inject, type Ref, watch, onMounted, computed } from 'vue'

// 

// const hoverbuildIdx = ref(-1)

// onMounted(() => { })

// // const msgInfo = ref('')
// const msgV = ref({ content: '', isHover: isHoverItem })
// const buildMgr = sg.ctx.dataMgr.playMgr.cityMgr.innerBuildMgr
// const builds = ref(buildMgr.builds)
// const isShowLvImg = ref(true)
// const highBg = `url(${sg.ctx.res.getImgGroup('playing#innercity#map_high').hasDef()?.getDataUrl()})`
// const lowBg = `url(${sg.ctx.res.getImgGroup('playing#innercity#map_low').hasDef()?.getDataUrl()})`
// const styles = computed(() => {
//     const s = {} as any
//     s.backgroundImage = builds.value[1].getLv() && builds.value[1].getLv() > 0 ? highBg : lowBg
//     return s
// })

// function isHoverItem(e: MouseEvent) {
//     for (let i = builds.value.length - 1; i >= 0; i--) {
//         const build = builds.value[i]
//         if (build.getImg().isHover(e.offsetX - build.getX(), e.offsetY - build.getY())) {
//             msgV.value.content = build.getMsg()
//             // sg.ctx.dataMgr.setHoverMsg(build.getMsg())
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

// async function clickBuild(e: MouseEvent) {
//     if (hoverbuildIdx.value === -1) {
//         console.log('啥也没电')
//         return
//     } else if (hoverbuildIdx.value === 0) {
//         console.log(hoverbuildIdx.value, '官府 ')
//     } else if (hoverbuildIdx.value === 1) {
//         console.log(hoverbuildIdx.value, '城墙 ')
//     } else {
//         const build = builds.value[hoverbuildIdx.value]
//         console.log(build)
//         if (build.cb && build.cb.state != 0) {

//         } else {
//             await openBuildList(build)
//         }
//     }

// }

// async function openBuildList(build: any) {
//     // sg.funPanMgr.setComp('fun_pan#city_build_inner_list')
//     // sg.ctx.dataMgr.setByKey(DATA_KEY_CITY_BUILD_INNER, build)
// }



const { sg, ctx, res } = inject('sg') as { sg: SanGuo, ctx: SgCtx, res: SgRes }
const bgLow = res.img("playing#innercity#map_low")
const bgHigh = res.img("playing#innercity#map_high")
const bg = computed(() => ctx.play!.city.buildings[1].lv == 0 ? bgLow : bgHigh)

BuildItem.bg = res.getImg("playing#innercity#buildbg")!
BuildItem.ctx = ctx
const builds = ref<BuildItem[]>(useInnerBuilds())
const showLv = ref(false)
onMounted(() => {
    ctx.dataMgr.subscribe("playing#player_time", undefined, sub)
    ctx.dataMgr.subscribeValue('playing#topbutton_level_sct', showLv)
    sub('')
})
onUnmounted(() => {
    ctx.dataMgr.unsubscribe("playing#player_time", sub)
    // ctx.dataMgr.unsubscribe()
})

function sub(cfg: any) {
    const bmap = new Map<Number, CityBuilding>()
    ctx.play?.city.buildings.forEach(b => {
        bmap.set(b.pos, b)
    })
    for (let pos = 0; pos < builds.value.length; pos++) {
        builds.value[pos].setBuild(bmap.get(pos))
    }
}
const msgg = ref(new CompactSimpleMsg(''))
const msgV = ref<HoverMsgDef>({ content: msgg, isHover: isHoverItem })
const hoverbuildIdx = ref(-1)

function isHoverItem(e: MouseEvent) {
    for (let i = builds.value.length - 1; i >= 0; i--) {
        const build = builds.value[i]
        if (build.bgImg.isHover(e.offsetX - build.x, e.offsetY - build.y)) {
            msgg.value.msg = build.getMsg()
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
    if (hoverbuildIdx.value < 0) { return }
    const b = builds.value[hoverbuildIdx.value]
    if (b.cb) {
        console.log(hoverbuildIdx.value, b.cb)
    } else {
        sg.ctx.dataMgr.setByKey('fun_pan#city_builds_params', { inner: true, bid: 0, pos: hoverbuildIdx.value })
        sg.ctx.funPanMgr.setComp('fun_pan#city_builds')
    }
}

</script>

<template>
    <div v-msg="msgV" class="inner-city" :style="{ backgroundImage: bg }" @click.prevent="clickBuild">
        <div v-for="b, idx in builds" class="build" :class="{
            'br': hoverbuildIdx == idx,
            'gr': b.upd
        }" :style="{ backgroundImage: b.bgUrl, left: b.xp, top: b.yp, width: b.wp, height: b.hp, zIndex: b.z }">
            <div class="lv-img" v-show="b.lv > 0 && showLv" :style="{
                backgroundImage: b.lvImg, left: b.lxp, top: b.lyp
            }"></div>
        </div>
    </div>
    <!-- <div class="inner-city" :style="styles" @click="clickBuild">
        <div v-for="build, idx in builds" class="build" :class="{
            'br': hoverbuildIdx == idx,
            'gr': build.isChange()
        }" :style="{
            backgroundImage: build.getUrl(), left: `${build.getX()}px`, top: `${build.getY()}px`, width:
                `${build.getW()}px`, height: `${build.getH()}px`, zIndex: build.z
        }">
            <div class="lv-img" v-show="build.getLv() > 0 && isShowLvImg" :style="{
                backgroundImage: build.getLvImgUrl(sg), left: `${build.getLvX()}px`, top: `${build.getLvY()}px`
            }"></div>
        </div>
    </div> -->
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
