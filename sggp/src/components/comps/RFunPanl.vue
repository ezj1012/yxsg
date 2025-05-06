<script setup lang="ts">
import type { SanGuo } from '@/app/sg';
import Empty from './Empty.vue';
import { computed, inject, watch } from 'vue';
import type { FunPanComp } from '@/app/model';
const { sg } = inject('sg') as { sg: SanGuo }

// const { title, pos } = defineProps({ title: { default: { content: '' } }, pos: { default: { w: 680, h: 490, x: 290, y: 50 } } }) as { title: Textable, pos: Shapable }

const bgUrl = computed(() => sg.img('common#title'))
const empty: FunPanComp = { key: 'fun_pan#empty', comp: Empty, content: '', size: 12, color: '--c-', x: 0, y: 0, w: 0, h: 0 }
const comp = computed(() => (sg.funPanMgr.getComp() || empty) as FunPanComp)
const w = computed(() => comp.value.w || 680)
const h = computed(() => comp.value.h || 490)
const x = computed(() => comp.value.x || 290)
const y = computed(() => comp.value.y || 50)
</script>

<template>
    <main v-show="comp.key != empty.key" class="fun-main" :style="{
        left: `${x}px`, top: `${y}px`, width: `${w}px`, height: `${h}px`,
    }">
        <div v-show="comp.show" class="bg"
            :style="{ color: `var(${comp.color})`, fontSize: `${comp.size}px`, backgroundImage: bgUrl }">
            {{ comp.content }}
        </div>
        <div class="body">
            <component class="fun-body" :is="comp.comp" :id="comp.key"></component>
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
    }
}
</style>