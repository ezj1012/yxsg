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
import type { CfgStr } from '@/app/cfg';
import { formatSeconds } from '@/app/constant';


import type { CityBuilding } from '@/app/api/apiModel';
const { sg } = inject('sg') as { sg: SanGuo }
const { cfg } = defineProps({ cfg: { required: true } }) as { cfg: CfgStr }
const panId = ref(1)
watch(() => cfg, () => { sg.ctx.dataMgr.subscribeValue("group#cityRes", panId, 1) }, { immediate: true })

const show = ref(false)
const builds = ref<CityBuilding[]>([])
const time = ref(new Date().getTime())
const timerId = shallowRef()
onMounted(() => {
    sg.ctx.dataMgr.subscribe('player#time', undefined, updateBuildState)
    updateBuildState()
    sg.ctx.dataMgr.subscribeValue('playing#topbutton_build_sct', show)
    timerId.value = setInterval(() => {
        time.value = new Date().getTime()
    }, 1000);
})

const updateBuildState = async (key?: string, newValue?: any, oldValue?: any) => {
    const bs: CityBuilding[] = []
    if (sg.ctx.playMgr.play && sg.ctx.playMgr.play.city) {
        sg.ctx.play?.city.buildings.forEach(b => {
            if (b.status !== 0) {
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
</script>
<template>
    <div v-show="show" v-size="cfg" class="main-city-builds">
        <div class="b" v-for="b in builds">
            <span style="width: 30px;"></span>
            <span style="width: 60px;"> {{ `${sg.ctx.gCfgMgr.cfg?.buildingsMap[b.bid].name}${b.status === 1 ? '↑' :
                '↓'}` }}</span>
            <span style="width: 60px;" :style="{ color: b.status === 1 ? '#0f0' : '#f00' }">{{ `${b.lv} → ${b.goalLv}`
            }}</span>
            <span :style="{ color: b.status === 1 ? '#0f0' : '#f00' }">{{ ` ${formatSeconds((b.endTime - time) / 1000)}`
            }}</span>
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

        span {
            display: inline-block;
        }
    }

}
</style>