<script setup lang="ts">
import { Img } from '@/app/utils/img';
import type { SanGuo } from '@/app/sg';
import { inject, onMounted, onUnmounted, provide, reactive, ref, shallowRef, watch } from 'vue';
import WorldMapCtrl from './WorldMapCtrl.vue';
import { bgRes, MapCanvas, MapTile, type MapCtx } from '@/app/worldMap';
import { cid2xy, wid2xyStr } from '@/app/constant';



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
    getSg: () => sg,
})

provide('worldMap', { mapCtx })

onMounted(async () => {
    await Img.loadImages(bgRes, () => { })
    canvas.value = new MapCanvas(document.getElementById('world-map')! as HTMLCanvasElement, mapCtx.value)

    const cityId = sg.dataMgr.getByKey('playing#player_lastCity')
    const xy = cid2xy(cityId)
    await mapCtx.value.moveTo(xy.x, xy.y)


    sg.dataMgr.subscribe('playing#topbutton_level_sct', undefined, (key: string, newValue: any, oldValue: any) => { canvas.value?.showLv(newValue) })
})

onUnmounted(() => { canvas.value?.dispose() })


function doClick(e: MouseEvent) {
    const tile = canvas.value!.isHover(e.offsetX, e.offsetY)!
    console.log('click  ele: ', tile?.data, wid2xyStr(tile.data!.id))
}

function updateHover(tile: MapTile | undefined) {
    console.log('hover ele', tile?.data)
}

</script>
<template>
    <canvas id="world-map" @click="doClick">
    </canvas>
    <WorldMapCtrl />
</template>
<style lang="less" scoped>
#world-map {
    position: absolute;
    width: 731px;
    height: 550px;
    user-select: none;
}
</style>