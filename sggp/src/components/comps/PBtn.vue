<script setup lang="ts">

import { actionMgr } from '@/app/action';
import { CfgKey, CfgType, type CfgStr, type RadioCfg } from '@/app/cfg';
import { type HoverMsgDef, type Shapable, type Textable } from '@/app/model';
import type { ImgGroupInfo } from '@/app/res';
import type { SanGuo } from '@/app/sg';
import { computed, inject, onUnmounted, ref, shallowRef, watch, type Ref, type ShallowRef } from 'vue';

const { sg } = inject('sg') as { sg: SanGuo }
const { cfg } = defineProps({ cfg: { required: true } }) as { cfg: CfgStr }
const curEl = ref()

const hovering = ref(false)
const pressing = ref(false)

const sctKey = shallowRef()
const disKey = shallowRef()
const alaKey = shallowRef()

const dis = ref(false)
const sct = ref(false)
const ala = ref(false)
const showAla = ref(false)
const blinkTimerId = shallowRef()

const key = ref<string>('')
const type = ref<CfgType>()
const size = ref<Shapable>()
const imgGroup = ref<ImgGroupInfo>()
const text = ref<Textable>()
const sctable = ref<boolean>(false)
const sctableDefVal = ref<RadioCfg>({ isDefSct: false, isRadioGroup: false })
const clicking = ref(false)
const msg = ref<HoverMsgDef>()

// 初始化
watch(() => cfg, () => {
    cfg.parseCfg({ key, type, size, imgGroup, text, sctable, sctableDefVal, msg, dis })
    setSub(sctKey, sct, sctable.value ? `${key.value}_sct` : undefined, sctableDefVal.value.isDefSct)
    setSub(disKey, dis, imgGroup.value && imgGroup.value.hasDis() ? `${key.value}_dis` : undefined, dis.value)
    setSub(alaKey, ala, imgGroup.value && imgGroup.value.hasAlarm() ? `${key.value}_ala` : undefined, alaKey.value)
}, { immediate: true })

function setSub(keyCache: ShallowRef<string | undefined>, val: Ref<any>, newKey: string | undefined, defVal = false) {
    keyCache.value && sg.dataMgr.unsubscribe(keyCache.value)
    keyCache.value = newKey
    newKey && sg.dataMgr.subscribeValue(newKey, val, defVal)
}

watch(ala, () => {
    if (ala.value) {
        !blinkTimerId.value && (blinkTimerId.value = setInterval(() => showAla.value = !showAla.value, 800))
    } else {
        blinkTimerId.value && clearInterval(blinkTimerId.value)
        blinkTimerId.value = undefined
        showAla.value = false
    }
}, { immediate: true })

watch(dis, () => { })

onUnmounted(() => {
    sctKey.value && sg.dataMgr.unsubscribe(sctKey.value)
    disKey.value && sg.dataMgr.unsubscribe(disKey.value)
    alaKey.value && sg.dataMgr.unsubscribe(alaKey.value)
    blinkTimerId.value && clearInterval(blinkTimerId.value)
})

const img = computed(() => {
    // console.log(`btn ${key.value}`, sct.value)
    if (!imgGroup.value) { return undefined }
    let img: any = undefined
    // console.log('imgggg11', img)
    if (dis.value && imgGroup.value.hasDis()) {
        img = imgGroup.value.hasDis()
        // console.log('imgggg22', img)
    } else {
        if (hovering.value) {
            if (sct.value && imgGroup.value.hasSct()) {
                img = imgGroup.value.hasSct()
            } else if (pressing.value && imgGroup.value.hasDown()) {
                img = imgGroup.value.hasDown()
            }

            !img && (img = imgGroup.value.hasOn())
        } else {
            if (sct.value && imgGroup.value.hasSct()) {
                img = imgGroup.value.hasSct()
            }
            if (showAla.value && imgGroup.value.hasAlarm()) {
                img = imgGroup.value.hasAlarm()
            }
        }
    }
    img = (img || imgGroup.value?.hasDef())
    // console.log('imgggg33', img)
    if (img && size.value && size.value.w && size.value.h && size.value.w != img.width && size.value.h != img.height) {
        // console.log('imgggg55', img)
        img = img.getImg(size.value.w, size.value.h)
    }
    // console.log('imgggg44', img, dis.value)
    return img
})



const btnStyle = computed(() => {
    const styles: { [key: string]: string } = {}
    size.value && size.value.styles && Object.assign(styles, size.value.styles)

    hovering.value && (styles.cursor = 'pointer')
    img.value && (styles['background-image'] = `url(${img.value!.getDataUrl()})`)
    pressing.value && !dis.value && !cfg.get(CfgKey.pressNoMove) && (styles.transform = 'translate(1px, 1px)')
    dis.value && imgGroup.value?.hasDis() && (styles.filter = 'grayscale(95%)')

    return styles;
})


function mousemove(e: MouseEvent) {
    const bakHovering = hovering.value
    if (imgGroup.value && imgGroup.value.hasDef() && imgGroup.value.hasDef()!.isPixel()) {
        hovering.value = imgGroup.value.hasDef()!.isHover(e.offsetX, e.offsetY)
        if (!hovering.value) {
            // cfg.value.out && cfg.value.out(curEl.value)
            pressing.value = false
        } else {
            if (bakHovering != hovering.value) {
                //     btnIn && btnIn(curEl.value)
                //     cfg.value.in && cfg.value.in(curEl.value)
            }
            e.stopPropagation()
        }
    } else {
        hovering.value = true
        if (bakHovering != hovering.value) {
            // cfg.value.in && cfg.value.in(curEl.value)
        }
        e.stopPropagation()
    }
}

function mouseleave(e: MouseEvent) {
    const temp = hovering.value
    hovering.value = false
    pressing.value = false
    if (temp) {
        // cfg.value.out && cfg.value.out(curEl.value)
    }
}

function mousedown(e: MouseEvent) {
    if (hovering.value) {
        e.stopPropagation()
        pressing.value = true
    }
}

function mouseup(e: MouseEvent) {
    if (hovering.value) {
        e.stopPropagation()
    }
    pressing.value = false
}

async function doClick(e: MouseEvent) {
    e.preventDefault()
    e.stopPropagation()
    if (!hovering.value || dis.value) { return }

    if (clicking.value) {
        return
    }
    clicking.value = true
    try {

        if (type.value === CfgType.imgPanBtn) {
            const panKey = `fun_pan#${key.value.substring(key.value.indexOf("#") + 1)}`
            sg.funPanMgr.setComp(panKey)
        } else {
            const act = cfg.get(CfgKey.action)
            await actionMgr.execBtn(act, cfg, e)
        }

        // console.log(sctable.value, sct.value, sctableDefVal.value)
        if (sctable.value) {
            if (sct.value == true && sctableDefVal.value.isRadioGroup) {
                sct.value = true
            } else {
                sct.value = !sct.value
            }
            sg.dataMgr.setByKey(sctKey.value, sct.value)

            // console.log(sctable.value, sct.value, sg.dataMgr.getByKey(sctKey.value), sctableDefVal.value)
        }
    } finally {
        clicking.value = false
    }

    // let rr = true
    // if (typeof cfg.value.click === 'string') {
    //     rr = SgActions.execFun(cfg.click, e)
    // } else {
    //     rr = cfg.click && cfg.click(e)
    // }
    // rr = await ActionMgr.execBtn((cfg.value.cfg as RBtnCfg).clickKey, e, (cfg.value.cfg as RBtnCfg).key)
    // if (rr === undefined || rr === null || rr === true) {
    //     if (cfg.value.sctable && !cfg.value.disable) {
    //         let v = DataMgr.get(sctKey.value, false)
    //         if (v === true) {
    //             v = false
    //         } else {
    //             v = true
    //         }
    //         DataMgr.set(sctKey.value, v)
    //     }
    // }

}
</script>
<template>
    <!-- v-show="op.show" v-msg="cfgMsg"   -->
    <div ref="curEl" v-msg="msg" class="comm_btn_container" :style="btnStyle" @mousemove="mousemove"
        @mouseleave="mouseleave" @mousedown="mousedown" @mouseup="mouseup" @click="doClick">
        <div v-if="text && text.content" class="text" :style="text.styles"> {{ text.content }}</div>
    </div>
</template>
<style lang="less" scoped>
.comm_btn_container {
    user-select: none;
    display: block;
    overflow: hidden;
    background-repeat: no-repeat;
    background-size: 100% 100%;
    position: absolute;
    // z-index: 100;

    .text {
        position: absolute;
        top: 50%;
        left: 50%;
        // transform: translate(calc(-50% - 1px), calc(-50% - 1px));
        transform: translate(calc(-50% - 1px), calc(-50% - 2px));
        font-size: 13px;
        font-weight: bold;
        pointer-events: none;

        width: 100%;
        text-align: center;
    }
}
</style>