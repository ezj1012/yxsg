<script setup lang="ts">
import type { SanGuo } from '@/app/sg';
import Empty from './Empty.vue';
import { computed, inject, provide, ref, watch } from 'vue';
import type { FunPanComp } from '@/app/commModel';
import { CfgStr } from '@/app/cfg';
import PBtn from './PBtn.vue';
const { sg } = inject('sg') as { sg: SanGuo }

// const { title, pos } = defineProps({ title: { default: { content: '' } }, pos: { default: { w: 680, h: 490, x: 290, y: 50 } } }) as { title: Textable, pos: Shapable }

const bgUrl = computed(() => sg.ctx.res.img('common#title'))
const empty: FunPanComp = { key: 'fun_pan#empty', comp: Empty, content: '', size: 12, color: '--c-', x: 0, y: 0, w: 0, h: 0 }
const comp = computed(() => (sg.ctx.funPanMgr.getComp() || empty) as FunPanComp)
const cfg = ref({
    w: 680,
    h: 490,
    x: 290,
    y: 50,
    content: ''
})
provide('funpan', { cfg })
watch(comp, () => {
    cfg.value.content = comp.value.content
    cfg.value.x = comp.value.x || 290
    cfg.value.y = comp.value.y || 50
    cfg.value.w = comp.value.w || 680
    cfg.value.h = comp.value.h || 490
})

const w = computed(() => cfg.value.w || 680)
const h = computed(() => cfg.value.h || 490)
const x = computed(() => cfg.value.x || 290)
const y = computed(() => cfg.value.y || 50)
const content = computed(() => cfg.value.content || '')


const closeBtn = new CfgStr("K:funpanl#close;T:I_BTN;S:0,0,60,32,4;RFI:common#btn_red;ACT:closeFunPan;TXT:T:关闭,F:15,C:#FFF,;")

</script>

<template>
    <main v-show="comp.key != empty.key" class="fun-main" :style="{
        left: `${x}px`, top: `${y}px`, width: `${w}px`, height: `${h}px`,
    }">
        <!-- <div class="bg" :style="{ color: `var(${comp.color})`, fontSize: `${comp.size}px`, backgroundImage: bgUrl }">
            {{ content }}
        </div> -->
        <div class="body">
            <component class="fun-body" :is="comp.comp" :id="comp.key"></component>
            <div class="closeBtn">
                <PBtn :cfg="closeBtn" />
            </div>
        </div>
    </main>
</template>
<style lang="less">
.fun-main {
    --pan-bg1: #1f3d47cc;
    --pan-bg2: #193139cc;

    position: absolute;
    z-index: var(--fun-pan-z);
    background-color: var(--pan-bg1);
    box-shadow: 0px 0px 0px 1px #80867C inset,
        0px 0px 0px 2px #7D6F66 inset,
        0px 0px 0px 3px #271B25 inset,
        0px 0px 0px 4px #111A00 inset,
        0px 0px 0px 5px #7E6A49 inset,
        0px 0px 0px 6px #7B7472 inset;

    .bg {
        position: absolute;
        width: 252px;
        top: 10px;
        left: calc(50% - 126px);
        line-height: 30px;
        background-repeat: no-repeat;
        text-align: center;
        user-select: none;
    }

    .body {
        position: absolute;
        left: 20px;
        top: 45px;
        width: calc(100% - 40px);
        height: calc(100% - 60px);

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
            bottom: -8px;
        }
    }
}
</style>