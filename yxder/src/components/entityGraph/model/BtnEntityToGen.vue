<script setup lang="ts">
import type { CoderGraph } from '@/app/graph';
import type { GraphModel, YXCoder } from '@/app/yxcoder';
import { inject, type Ref, type ShallowRef } from 'vue';

const { graph, editEntity, coder, models } = inject('entityCoder') as { graph: ShallowRef<CoderGraph>, editEntity: Function, coder: ShallowRef<YXCoder>, models: Ref<GraphModel[]> }
const { nid } = defineProps({ nid: { type: String } })

async function genCode() {
    const m = models.value.find(m => m.id == nid)
    if (m) {
        coder.value.gen([m.model])
    }
}


</script>
<template>
    <div class="line-actions-item codicon codicon-code" @click.stop="genCode()"></div>
</template>