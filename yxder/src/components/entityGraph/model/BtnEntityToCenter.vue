<script setup lang="ts">
import type { CoderGraph } from '@/app/graph';
import { inject, type ShallowRef } from 'vue';

const { graph } = inject('entityCoder') as { graph: ShallowRef<CoderGraph> }
const { nid } = defineProps({ nid: { type: String } })

</script>
<template>
    <div class="line-actions-item codicon codicon-eye" @click="() => nid && graph.setCenter(nid)"></div>
</template>