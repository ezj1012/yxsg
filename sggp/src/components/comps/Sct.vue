<script lang="ts" setup>
import { CfgKey, type CfgStr } from '@/app/cfg';
import type { Shapable, Textable } from '@/app/model';


import type { SanGuo } from '@/app/sg';
import { computed, inject, onUnmounted, ref, watch } from 'vue';
import Scroll from './Scroll.vue';

const { sg } = inject('sg') as { sg: SanGuo }
const { cfg } = defineProps({ cfg: { required: true } }) as { cfg: CfgStr }

const optionsKey = ref()
const options = ref<{ key: string, value: string }[]>([])
const sctOp = ref('随机')
const showOptions = ref(false)

const key = ref<string>()
const style = ref({})
const textStyle = ref({})
const opsStyle = ref({})
const maxHeight = ref(0)

const size = ref<Shapable>()
const text = ref<Textable>()
// 初始化
watch(() => cfg, () => {
    cfg.parseCfg({ key, size, text })
    optionsKey.value = cfg.key() + '_options';
    sg.dataMgr.subscribe(key.value, 0, (key: string, newValue: any, oldValue: any) => {
        for (let i = 0; i < options.value.length; i++) {
            const kv = options.value[i];
            if (kv.key == newValue) {
                sctOp.value = kv.value
                return
            }
        }
        sctOp.value = '随机'
    })
    sg.dataMgr.subscribeValue(optionsKey.value, options)
    // setSub(sctKey, sct, sctable.value ? `${key.value}_sct` : undefined, sctableDefVal.value.isDefSct)
    // setSub(disKey, dis, imgGroup.value && imgGroup.value.hasDis() ? `${key.value}_dis` : undefined)
    // setSub(alaKey, ala, imgGroup.value && imgGroup.value.hasAlarm() ? `${key.value}_ala` : undefined)
    const tempStyles: { [key: string]: string } = {}
    const opMaxSize = cfg.get(CfgKey.optionMaxSize) ? 6 : Number(cfg.get(CfgKey.optionMaxSize))
    if (size.value && size.value.styles) {
        Object.assign(tempStyles, size.value.styles)

        const onBg = sg.res.getImgGroup('common#select')?.hasDef()?.getImg(size.value.w, size.value.h).imgDataUrl
        const downBg = sg.res.getImgGroup('common#select')?.hasDown()?.getImg(size.value.w, size.value.h).imgDataUrl
        tempStyles['--bg'] = `url(${onBg})`
        tempStyles['--abg'] = `url(${downBg})`
        tempStyles['min-height'] = `${size.value.h}px`

        tempStyles.maxHeight = `${opMaxSize * 24 + size.value.h!}px`
        delete tempStyles['height']
    }
    style.value = tempStyles

    const tempTextstyles: { [key: string]: string } = {}
    text.value && text.value.styles && Object.assign(tempTextstyles, text.value.styles)
    textStyle.value = tempTextstyles

    // opsStyle.value = opMaxSize > 0 ? { height: opMaxSize * 24 + "px" } : {}
    maxHeight.value = opMaxSize * 24

}, { immediate: true })


</script>
<template>
    <div :style="style" class="sct-style" v-clickout="() => { showOptions = false }">
        <div @click="() => showOptions = !showOptions" :style="{ height: `${size?.h}px`, lineHeight: `${size?.h}px` }"
            class="show-value"><span :style="textStyle">{{ sctOp }}</span>
        </div>
        <Scroll v-show="showOptions" class="scroll12" :max-height="maxHeight" :scroll="`auto`">
            <ul v-show="showOptions">
                <li v-for="(item, index) in options" :class="[sg.dataMgr.get(key) == item.key ? 'selected' : '']"
                    @click.stop="() => { sg.dataMgr.set(key, item.key); showOptions = false }">
                    <span :style="textStyle"> {{ item.value }}</span>
                </li>
            </ul>
        </Scroll>
    </div>
</template>
<style lang="less" scoped>
.sct-style {
    position: absolute;
    text-align: center;
    outline: none;
    border: 0;
    text-align: center;
    text-align-last: center;
    overflow: hidden;
    // background-color: transparent;
    font-family: Consolas, "Courier New", monospace;
    font-weight: normal;
    font-feature-settings: "liga" 0, "calt" 0;
    font-variation-settings: normal;
    letter-spacing: 0px;
    white-space: nowrap;
    user-select: none;
    text-align: center;

    .show-value {
        width: 100%;
        text-align: center;
        text-align-last: center;
        overflow: hidden;
        background: var(--bg) no-repeat;

        &:active {
            background: var(--abg) no-repeat;
        }

        span {
            margin: 0 3px 0 6px;
            overflow: hidden;
            padding-right: 26px;
            display: block;
        }
    }

    .scroll12 {
        overflow-y: auto;
        overflow-x: hidden;
        user-select: none;
        z-index: 99999;
        border-bottom: 2px solid #735942;
        border-left: 2px solid #735942;
        border-top: 2px solid #735942;

        ul {
            list-style: none;
            padding: 0px;
            margin: 0px;
            width: 100%;
            // width: calc(100% - 4px);
            background-color: #1b241e;

            li {
                height: 24px;
                line-height: 24px;

                span {
                    margin: 0 1px;
                    overflow: hidden;
                    display: block;
                }
            }

            li:hover {
                background-color: #35483b;
            }

            .selected {
                background-color: #1d3e28;
            }
        }
    }
}
</style>
