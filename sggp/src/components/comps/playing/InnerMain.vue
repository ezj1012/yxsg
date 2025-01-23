<script setup lang="ts">
import type { SanGuo } from '@/app/sg';
import { inject, ref, watch } from 'vue';
import type { CfgStr } from '@/app/cfg';
// import InnerCity from './innerpan/InnerCity.vue';
// import OutCity from './innerpan/OutCity.vue';
import WorldMap from './innerpan/WorldMap.vue';
// import BattleGroup from './innerpan/BattleGroup.vue';
const { sg } = inject('sg') as { sg: SanGuo }
const { cfg } = defineProps({ cfg: { required: true } }) as { cfg: CfgStr }
const panId = ref(1)
watch(() => cfg, () => { sg.dataMgr.subscribeValue("group#mainPan", panId, 1) }, { immediate: true })
</script>

<template>
    <main v-size="cfg" class="city-res">
        <WorldMap />
        <!-- <OutCity v-if="panId == 2" /> -->
        <!-- <WorldMap v-else-if="panId == 3" /> -->
        <!-- <BattleGroup v-else-if="panId == 4" /> -->
        <!-- <InnerCity v-else /> -->
    </main>
</template>
<style lang="less" scoped>
.city-res {
    background-color: #ffffff04;
}
</style>