<script setup lang="ts">

import { computed, onMounted, provide, defineAsyncComponent, type ModelRef, ref, watch } from 'vue';

import type { SanGuo } from '@/app/sg';
import LoadingView from './stages/LoadingView.vue';
import StageView from './stages/StageView.vue';
import EMsg from './msg/EMsg.vue';
import HMsg from './msg/HMsg.vue';
import RFunPanl from './comps/RFunPanl.vue';
import FunPanlTestView from './test/FunPanlTestView.vue';

const sg: ModelRef<SanGuo> = defineModel({ required: true })
provide('sg', { sg: sg.value, ctx: sg.value.ctx, res: sg.value.ctx.res })
const isReady = computed(() => sg.value.ready.value)

</script>
<template>
    <div class="sg-main">
        <template v-if="isReady">
            <!--  -->
            <RFunPanl />
            <FunPanlTestView />
            <!-- -->

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