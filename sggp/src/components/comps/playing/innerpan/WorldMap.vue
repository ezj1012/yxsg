<script setup lang="ts">
import { cid2xy } from '@/app/constant';
import type { SanGuo } from '@/app/sg';
import { CanvasWindow } from '@/app/worldMap';
import WorldMapCtrl from './WorldMapCtrl.vue';
import { inject, onMounted, provide, ref, shallowRef } from 'vue';


const { sg } = inject('sg') as { sg: SanGuo }

const ws = 10
const hs = 16
const w = 731
const h = 550
const canvas = shallowRef<HTMLCanvasElement>()
const window = shallowRef<CanvasWindow>()
const x = ref(1)
const y = ref(1)
provide('worldMap', { window, x, y })

onMounted(async () => {
    if (canvas.value) {
        window.value = new CanvasWindow(sg, w, h, ws, hs, canvas.value)
        const cityId = sg.dataMgr.getByKey('playing#player_lastCity')
        const xy = cid2xy(cityId)
        window.value.moveTo(xy.x, xy.y)
        x.value = xy.x
        y.value = xy.y
        window.value.mapChange = (nx: number, ny: number) => {
            x.value = nx
            y.value = ny
        }
        await window.value.draw()
    }
})

async function click(e: MouseEvent) {
    console.log(window.value?.map.lastHoverTiles)
}

</script>

<template>
    <canvas id="world-map" ref="canvas" @click="click">世界地图</canvas>
    <!-- <WorldMapCtrl /> -->
</template>
<style lang="less" scoped>
#world-map {
    position: absolute;
    width: 731px;
    height: 550px;
    user-select: none;
}
</style>
