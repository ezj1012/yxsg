<script lang="ts" setup>
// import type { SanGuo } from '@/app/sg';
// import { computed, inject, onMounted, ref, watch } from 'vue';
// import Scroll from '../../Scroll.vue';
// import PBtn from '../../PBtn.vue';
// import CityBuildItem from './CityBuildItem.vue';
// import { CfgStr } from '@/app/cfg';
// import Text from '../../Text.vue';
// import type { CityBuildingCfg } from '@/app/api/apiModel';
// import { reg } from '@/app/action';
// const datas = ref<any[]>([])
// const inner = ref(false)

import type { SanGuo } from '@/app/sg';
import { inject, onMounted, onUnmounted, ref, shallowRef, watch } from 'vue';
import { CfgStr } from '@/app/cfg';
import { formatSeconds } from '@/app/constant';


import type { CityBuilding } from '@/app/api/apiModel';
import PBtn from '../../PBtn.vue';
import { reg } from '@/app/action';
const { sg } = inject('sg') as { sg: SanGuo }
const { cfg } = defineProps({ cfg: { required: true } }) as { cfg: CfgStr }
const panId = ref(1)
watch(() => cfg, () => { sg.ctx.dataMgr.subscribeValue("group#cityRes", panId, 1) }, { immediate: true })

const show = ref(false)
const builds = ref<CityBuilding[]>([])
const time = ref(new Date().getTime())
const timerId = shallowRef()
const accerBtns = ref([
    cfgAccer(0),
    cfgAccer(1),
    cfgAccer(2),
    cfgAccer(3),
    cfgAccer(4),
    cfgAccer(5),
    cfgAccer(6),
])
const cancelBtns = ref([
    cfgCancel(0),
    cfgCancel(1),
    cfgCancel(2),
    cfgCancel(3),
    cfgCancel(4),
    cfgCancel(5),
    cfgCancel(6),
])

function cfgAccer(idx: number) {
    const cfg = `K:playing#build_accer_${idx};T:I_BTN;S:2,4,11,11,25;RFI:common#btn_accer;TXT:T:,F:12,C:#fff,;;SMSG:加速;ACT:buildAccer;`
    return new CfgStr(cfg)
}
function cfgCancel(idx: number) {
    const cfg = `K:playing#build_cancel_${idx};T:I_BTN;S:16,4,11,11,25;RFI:common#btn_cancel;TXT:T:,F:12,C:#fff,;;SMSG:取消;ACT:buildCancel`
    return new CfgStr(cfg)
}

onMounted(() => {
    sg.ctx.dataMgr.subscribeValue('playing#topbutton_build_sct', show)
    timerId.value = setInterval(() => {
        updateBuildState()

    }, 1000);
})

const updateBuildState = async (key?: string, newValue?: any, oldValue?: any) => {
    const bs: CityBuilding[] = []
    if (sg.ctx.playMgr.play && sg.ctx.playMgr.play.city) {
        time.value = sg.ctx.playMgr.play.time
        sg.ctx.play?.city.buildings.forEach(b => {
            if (b.status !== 0) {
                b.endTimeFormat = formatSeconds((b.endTime - time.value) / 1000)
                bs.push(b)
            }
        })
    }
    builds.value = bs
}

onUnmounted(() => {
    sg.ctx.dataMgr.unsubscribe('player#time', updateBuildState)
    clearInterval(timerId.value)
})

function findBuilds(key: string) {
    const idx = Number(key.substring(key.lastIndexOf("_") + 1))
    debugger
    return builds.value[idx];
}

reg('buildAccer', async ({ cfg }) => {
    const build = findBuilds(cfg!.key())
    console.log('buildAccer', build)
})

reg('buildCancel', async ({ cfg }) => {
    const build = findBuilds(cfg!.key())
    console.log(`buildCancel`, build)
})
</script>
<template>
    <div v-show="show" v-size="cfg" class="main-city-builds">
        <div class="b" v-for="b, idx in builds" :key="b.id">
            <span style="width: 30px;">
                <PBtn :cfg="accerBtns[idx]" />
                <PBtn :cfg="cancelBtns[idx]" />
            </span>
            <span style="width: 60px;"> {{ `${sg.ctx.gCfgMgr.cfg?.buildingsMap[b.bid].name}${b.status === 1 ? '↑' :
                '↓'}` }}</span>
            <span style="width: 60px;" :style="{ color: b.status === 1 ? '#0f0' : '#f00' }">{{ `${b.goalLv === b.lv ?
                b.lv - 1 : b.lv} → ${b.goalLv}`
                }}</span>
            <span>{{ b.endTimeFormat }}</span>
        </div>
    </div>
</template>
<style lang="less" scoped>
.main-city-builds {

    background-color: #00000088;
    // background-color: red;
    position: absolute;
    width: 100%;
    height: 100%;
    padding: 2px 0;
    box-sizing: border-box;

    .b {
        font-size: 12px;
        color: #fff;
        user-select: none;
        position: relative;

        span {
            display: inline-block;
        }
    }

}
</style>