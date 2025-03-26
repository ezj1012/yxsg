<script setup lang="ts">
import { wid, wid2cid, wid2xy, wid2xyStr } from '@/app/constant';
import { Img } from '@/app/img';
import type { MemMapTile } from '@/app/modelData';
import type { SanGuo } from '@/app/sg';
import { inject, onMounted, onUnmounted, provide, reactive, ref, shallowRef, watch } from 'vue';
import WorldMapCtrl from './WorldMapCtrl.vue';
import { bgRes, MapCanvas, MapTile, type MapCtx } from '@/app/worldMap';



const { sg } = inject('sg') as { sg: SanGuo }
const canvas = shallowRef<MapCanvas>()
const mapCtx = ref<MapCtx>({
    x: 15,
    y: 15,
    showLv: false,
    getMapTiles: async (params: { x: number, y: number, xw: number, yw: number }) => await sg.api.envApi.getMapTiles(params),
    updateMapPosition: (nx: number, ny: number) => { mapCtx.value.x = nx; mapCtx.value.y = ny; },
    updateHoverTile: (tile: MapTile | undefined) => { updateHover(tile) },
    moveTo: async (mapX: number, mapY: number) => { await canvas.value?.moveTo(mapX, mapY) },
})

provide('worldMap', { mapCtx })

onMounted(async () => {
    await Img.loadImages(bgRes, () => { })
    canvas.value = new MapCanvas(document.getElementById('map')! as HTMLCanvasElement, mapCtx.value)
    await canvas.value.moveTo(mapCtx.value.x, mapCtx.value.y)
})

onUnmounted(() => { canvas.value?.dispose() })


function doClick(e: MouseEvent) {
    const tile = canvas.value!.isHover(e.offsetX, e.offsetY)
    console.log('click  ele: ', tile)
}

function updateHover(tile: MapTile | undefined) {
    console.log('hover ele', tile?.data)
}

</script>
<template>
    <canvas id="map" class="sg-playing" @click="doClick">
    </canvas>
    <WorldMapCtrl />
</template>
<style lang="less" scoped>
.sg-playing {
    position: absolute;
    width: 731px;
    height: 550px;
    user-select: none;
    left: 120px;
    top: 20px;
}
</style>