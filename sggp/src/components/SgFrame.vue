<script setup lang="ts">

import { computed, onMounted, provide, defineAsyncComponent, type ModelRef } from 'vue';
// // import RFunPanl from '../comps/RFunPanl.vue';
// import LoadingView from './stages/LoadingView.vue';
// import StageView from './stages/StageView.vue';
// import HMsg from './msg/HMsg.vue';
import type { SanGuo } from '@/app/sg';
import LoadingView from './stages/LoadingView.vue';
import StageView from './stages/StageView.vue';
import EMsg from './msg/EMsg.vue';
import HMsg from './msg/HMsg.vue';
import DataShowList from './DataShowList.vue';
// import DataShowList from './datas/DataShowList.vue';
// import EMsg from './msg/EMsg.vue';
// import RFunPanl from './comps/RFunPanl.vue';

const sg: ModelRef<SanGuo> = defineModel({ required: true })
provide('sg', { sg: sg.value })
const isReady = computed(() => sg.value.ready.value)

</script>
<template>
    <div class="sg-main">
        <template v-if="isReady">
            <StageView />
            <!--
            <RFunPanl />
            -->
            <HMsg />
            <EMsg />
        </template>
        <template v-else>
            <LoadingView />
        </template>
    </div>
    <DataShowList />
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