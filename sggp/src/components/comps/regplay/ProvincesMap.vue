<script setup lang="ts">
import type { CfgStr } from '@/app/cfg';
import type { ImgGroupInfo } from '@/app/res';
import type { SanGuo } from '@/app/sg';
import { ref, inject, watch } from 'vue';

const { sg } = inject('sg') as { sg: SanGuo }
const { cfg } = defineProps({ cfg: { required: true } }) as { cfg: CfgStr }


const pmEl = ref<HTMLDivElement>() // htm
const provinceItems = ref<{ img: ImgGroupInfo, id: number, x: number, y: number }[]>([])
const hoverProvinceId = ref()
const prId = ref()

initProvince()
function initProvince() {
    provinceItems.value = []
    provinceItems.value.push({ img: sg.res.getImgGroup('regplay#silv'), id: 1, x: 203, y: 153 })
    provinceItems.value.push({ img: sg.res.getImgGroup('regplay#jizhou'), id: 2, x: 356, y: 84 })
    provinceItems.value.push({ img: sg.res.getImgGroup('regplay#yuzhou'), id: 3, x: 327, y: 185 })
    provinceItems.value.push({ img: sg.res.getImgGroup('regplay#yunzhou'), id: 4, x: 369, y: 149 })
    provinceItems.value.push({ img: sg.res.getImgGroup('regplay#xuzhou'), id: 5, x: 416, y: 178 })
    provinceItems.value.push({ img: sg.res.getImgGroup('regplay#qingzhou'), id: 6, x: 423, y: 116 })
    provinceItems.value.push({ img: sg.res.getImgGroup('regplay#jingzhou'), id: 7, x: 248, y: 229 })
    provinceItems.value.push({ img: sg.res.getImgGroup('regplay#yangzhou'), id: 8, x: 368, y: 233 })
    provinceItems.value.push({ img: sg.res.getImgGroup('regplay#yizhou'), id: 9, x: 33, y: 214 })
    provinceItems.value.push({ img: sg.res.getImgGroup('regplay#liangzhou'), id: 10, x: -5, y: 2 })
    provinceItems.value.push({ img: sg.res.getImgGroup('regplay#bingzhou'), id: 11, x: 213, y: 40 })
    provinceItems.value.push({ img: sg.res.getImgGroup('regplay#youzhou'), id: 12, x: 354, y: 0 })
    provinceItems.value.push({ img: sg.res.getImgGroup('regplay#jiaozhou'), id: 13, x: 128, y: 356 })
}
// 初始化
watch(() => cfg, () => { }, { immediate: true })

function isHoverItem(e: MouseEvent) {
    const provinceIdMap = sg.dataMgr.get('regplay#provinces') as any
    for (let i = 0; i < provinceItems.value.length; i++) {
        const pCfg = provinceItems.value[i]
        const x = e.offsetX - pCfg.x
        const y = e.offsetY - pCfg.y

        if (pCfg.img && pCfg.img.hasDef() && pCfg.img.hasDef()!.isHover(x, y) && provinceIdMap[pCfg.id]) {
            hoverProvinceId.value != pCfg.id && (hoverProvinceId.value = pCfg.id)

            sg.dataMgr.setHoverMsg(pmEl.value, { clientX: e.clientX, clientY: e.clientY, el: pmEl.value, content: provinceIdMap[pCfg.id].msg, isHtml: true })
            return true
        }
    }
    sg.dataMgr.setHoverMsg(pmEl.value)
    hoverProvinceId.value = undefined
    return false
}

function mouseleave() {
    hoverProvinceId.value && (hoverProvinceId.value = undefined)
    pmEl.value && sg.dataMgr.setHoverMsg(pmEl.value)
}

function mousedown() {
    if (hoverProvinceId.value) {
        prId.value = hoverProvinceId.value
        sg.dataMgr.set('regplay#province_id', hoverProvinceId.value)
    }
}

function mouseup() { prId.value = undefined }

</script>

<template>
    <div class="province-map" ref="pmEl" v-size="cfg" @mousemove="isHoverItem($event)" @mousedown="mousedown"
        @mouseup="mouseup" @mouseleave="mouseleave">
        <div class="world-map-item" v-for="item in provinceItems  " :key="item.id" :style="{
            top: item.y + 'px',
            left: item.x + 'px',
            width: item.img?.hasDef()?.width + 'px',
            height: item.img?.hasDef()?.height + 'px',
            opacity: hoverProvinceId != item.id ? 0 : 1,
            backgroundPosition: prId == item.id ? '2px 2px' : '0 0',
            backgroundImage: `url('${item.img?.hasDef()?.getDataUrl()}')`
        }
            ">
        </div>
    </div>
</template>
<style lang="less" scoped>
.province-map {
    cursor: pointer;

    .world-map-item {
        position: absolute;
        pointer-events: none;
        background-repeat: no-repeat;
    }
}
</style>