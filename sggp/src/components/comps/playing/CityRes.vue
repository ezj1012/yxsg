<script setup lang="ts">
import type { SanGuo } from '@/app/sg';
import { inject, ref, watch } from 'vue';
import type { CfgStr } from '@/app/cfg';
import InnerRes from './cityres/InnerRes.vue';
import InnerArmy from './cityres/InnerArmy.vue';
import InnerDefence from './cityres/InnerDefence.vue';
// import InnerDefence from './cityres/InnerDefence.vue';

const { sg } = inject('sg') as { sg: SanGuo }
const { cfg } = defineProps({ cfg: { required: true } }) as { cfg: CfgStr }
const panId = ref(1)
watch(() => cfg, () => { sg.ctx.dataMgr.subscribeValue("group#cityRes", panId, 1) }, { immediate: true })

</script>

<template>
    <main v-size="cfg" class="city-res">
     
        <div v-if="panId == 2">   {{
            panId
        }} </div>
        <!--<WorldMap />-->
        <!-- <OutCity v-if="panId == 2" /> -->
        <InnerArmy v-else-if="panId == 3" />
        <InnerDefence v-else-if="panId == 4" />
        <InnerRes v-else />
    </main>
</template>
<style lang="less" scoped>
.city-res {
    background-color: #ffffff04;
}
</style>