<script setup lang="ts">
import { CoderGraph } from '@/app/graph';
import { GraphModel, YXCoder } from '@/app/yxcoder';
import Splitable from '@/components/der/Splitable.vue';
import EntityCtrl from '@/components/entityGraph/EntityCtrl.vue';
import EntityGraph from '@/components/entityGraph/EntityGraph.vue';
import { getTeleport } from '@antv/x6-vue-shape';
import { onMounted, provide, ref, shallowRef } from 'vue';



const TeleportContainer = getTeleport();
const graph = shallowRef<CoderGraph>()
const models = ref<GraphModel[]>([])
const coder = shallowRef<YXCoder>()
const ctrlEl = ref()
const sctNode = ref('')

provide('entityCoder', { coder, graph, models, sctNode, editEntity })

async function editEntity(id: string) {
    const node = models.value.find(c => c.id == id)
    if (node) {
        await ctrlEl.value?.doEdit(node)
    }
}

onMounted(async () => {
    const cg = new CoderGraph('graph-container')
    graph.value = cg
    coder.value = new YXCoder(cg, models)
    await coder.value.loading()
})
</script>
<template>
    <div class="entity-coder">
        <Splitable :width="450">
            <EntityCtrl ref="ctrlEl" />
        </Splitable>
        <div class="gr">
            <EntityGraph id="graph-container" />
        </div>
    </div>
    <TeleportContainer />
</template>

<style scoped lang="less">
.entity-coder {
    width: 100%;
    height: 100%;
    display: flex;

    .gr {
        flex: 1;
        overflow: hidden;
    }

    #graph-container {
        width: 100%;
        height: 100%;
    }
}
</style>
