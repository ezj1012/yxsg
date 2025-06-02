<script setup lang="ts">
import type { CityBuilding } from '@/app/api/apiModel';
import { BuildMgr } from '@/app/buildMgr';
import { BuildItem, useInnerBuilds } from '@/app/cityMgr';
import type { HoverMsgDef } from '@/app/commModel';
import { CompactSimpleMsg } from '@/app/msg/hoverMsgMgr';
import type { SgRes } from '@/app/res';
import type { SanGuo, SgCtx } from '@/app/sg';
import { computed, inject, onMounted, onUnmounted, ref, watch } from 'vue';
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
    sub()
})
onUnmounted(() => {
    ctx.dataMgr.unsubscribe("playing#player_time", sub)
})

function sub() {
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
        sg.ctx.dataMgr.setByKey('fun_pan#city_builds_params', { inner: true, build: b.cb })
        sg.ctx.funPanMgr.setComp('fun_pan#build_house')
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
