<script setup lang="ts">

import { computed, onMounted, provide, defineAsyncComponent, type ModelRef, ref, watch } from 'vue';

import type { SanGuo } from '@/app/sg';
import LoadingView from './stages/LoadingView.vue';
import EMsg from './msg/EMsg.vue';
import HMsg from './msg/HMsg.vue';
import RFunPanl from './comps/RFunPanl.vue';
import FunPanlTestView from './test/FunPanlTestView.vue';
import CommTest from './test/CommTest.vue';
import type { Img } from '@/app/utils/img';
import type { ImgGroupInfo } from '@/app/res/imgRes';
import StageView from './stages/StageView.vue';
import { useSgComm } from '@/app/sgComp';
import RUsePanl from './comps/RUsePanl.vue';

const sg: ModelRef<SanGuo> = defineModel({ required: true })
const ctx = sg.value.ctx
const res = sg.value.ctx.res
const chatMgr = ctx.chatMgr
const sgComm = useSgComm(res)
provide('sg', {
    sg: sg.value, ctx, res, chatMgr,
    btnRed: sgComm.btnRed.bind(sgComm.btnRed),
    img: res.img.bind(res)
})
const isReady = computed(() => sg.value.ready.value)

</script>
<template>
    <div class="sg-main">
        <!-- <CommTest v-if="isReady" /> -->
        <template v-if="isReady">
            <!--  -->
            <RFunPanl />
            <RUsePanl />
            <FunPanlTestView />
            <!--
-->
            <StageView />

            <!--  <TableViewTest />-->
            <!-- <WorldMap /> -->
            <HMsg />
            <EMsg />
        </template>
        <template v-else>
            <LoadingView />
        </template>
    </div>
    <!--  <CfgShowList />
  <DataShowList /> -->
</template>
<style lang="less">
.sg-main {
    position: absolute;
    width: 1000px;
    min-width: 1000px;
    max-width: 1000px;
    height: 600px;
    min-height: 600px;
    max-height: 600px;
    background-color: rgba(0, 0, 0, 0.2);
    overflow: hidden;
}
</style>