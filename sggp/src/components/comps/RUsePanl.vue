<script setup lang="ts">
import type { SanGuo, Sg } from '@/app/sg';
import Empty from './Empty.vue';
import { computed, inject, provide, ref, watch } from 'vue';
import type { FunPanComp, UsePanComp } from '@/app/commModel';
import { CfgStr } from '@/app/cfg';
import PBtn from './PBtn.vue';
const { sg } = inject('sg') as Sg

const empty: UsePanComp = { key: 'use_pan#empty', comp: Empty, content: '', size: 12, color: '--c-', x: 0, y: 0, w: 0, h: 0 }
const comp = computed(() => (sg.ctx.usePanMgr.getComp() || empty) as UsePanComp)
const show = computed(() => comp.value.key != empty.key)
const cfg = ref({
    w: 293,
    h: 308,
    x: 510,
    y: 145,
    content: ''
})
provide('usepan', { cfg })
watch(comp, () => {
    cfg.value.content = comp.value.content
    cfg.value.x = comp.value.x || 510
    cfg.value.y = comp.value.y || 145
    cfg.value.w = comp.value.w || 293
    cfg.value.h = comp.value.h || 308
})

const w = computed(() => cfg.value.w || 293)
const h = computed(() => cfg.value.h || 308)
const x = computed(() => cfg.value.x || 510)
const y = computed(() => cfg.value.y || 145)
const content = computed(() => cfg.value.content || '')


const closeBtn = new CfgStr("K:funpanl#close;T:I_BTN;S:0,0,60,32,4;RFI:common#btn_red;ACT:closeFunPan;TXT:T:关闭,F:15,C:#FFF,;")

</script>

<template>
    <main v-show="show" class="use-main sg-comm-use" :style="{
        left: `${x}px`, top: `${y}px`, width: `${w}px`, height: `${h}px`,
    }">
        <!---->
        <div class="title">
            <div class="bg sg-comm-use-title" :style="{ color: `var(${comp.color})`, fontSize: `${comp.size}px` }">
                {{ content }}
            </div>
        </div>
        <div class="body">
            <component class="fun-body" :is="comp.comp" :id="comp.key"></component>
            <div class="closeBtn">
                <PBtn :cfg="closeBtn" />
            </div>
        </div>
    </main>
</template>
<style lang="less">
.use-main {
    z-index: var(--use-pan-z);
    position: absolute;

    .title {
        position: absolute;
        left: 0;
        top: 16px;
        width: 100%;
        height: 27px;
        display: flex;
        justify-content: center;

        .bg {
            height: 27px;
            line-height: 27px;
            padding: 0 18px;
            text-align: center;
            user-select: none;
        }
    }

    .body {
        position: absolute;
        left: 20px;
        top: 50px;
        width: calc(100% - 40px);
        height: calc(100% - 65px);
        overflow: hidden;
        .fun-body {
            width: 100%;
            height: 100%;
            left: 0;
            top: 0;
            position: absolute;
        }

        .closeBtn {
            position: absolute;
            width: 100px;
            height: 38px;
            left: 580px;
            bottom: 0px;
        }
    }
}
</style>