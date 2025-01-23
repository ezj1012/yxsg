<script setup lang="ts">

import { CityBuildState, type CityBuild } from '@/app/api';
import { DATA_KEY_CITY_BUILD_INNER } from '@/app/constant';
import { DataMgr } from '@/app/dataMgr';
import type { SanGuo } from '@/app/sg';
import type Img from '@/app/utils/Img';
import { ref, inject, type Ref, watch, onMounted, computed } from 'vue'

const { sg } = inject('sg') as { sg: SanGuo }

const hoverbuildIdx = ref(-1)

onMounted(() => { })

// const msgInfo = ref('')
const msgV = ref({ content: '', isHover: isHoverItem })
const buildMgr = sg.dataMgr.playMgr.cityMgr.innerBuildMgr
const builds = ref(buildMgr.builds)
const isShowLvImg = ref(true)
const highBg = `url(${sg.res.getImgGroup('playing#innercity#map_high').hasDef()?.getDataUrl()})`
const lowBg = `url(${sg.res.getImgGroup('playing#innercity#map_low').hasDef()?.getDataUrl()})`
const styles = computed(() => {
    const s = {} as any
    s.backgroundImage = builds.value[1].getLv() && builds.value[1].getLv() > 0 ? highBg : lowBg
    return s
})

function isHoverItem(e: MouseEvent) {
    for (let i = builds.value.length - 1; i >= 0; i--) {
        const build = builds.value[i]
        if (build.getImg().isHover(e.offsetX - build.getX(), e.offsetY - build.getY())) {
            msgV.value.content = build.getMsg()
            // sg.dataMgr.setHoverMsg(build.getMsg())
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

async function openBuildList(build: any) {
    sg.funPanMgr.setComp('fun_pan#city_build_inner_list')
    // sg.dataMgr.setByKey(DATA_KEY_CITY_BUILD_INNER, build)
}


</script>

<template>
    <div v-msg="msgV" class="inner-city" :style="styles" @click="clickBuild">
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
