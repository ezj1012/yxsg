<script setup lang="ts">
import type { CoderGraph } from '@/app/graph';
import { inject, type ShallowRef } from 'vue';

const { graph, editEntity } = inject('entityCoder') as { graph: ShallowRef<CoderGraph>, editEntity: Function }
const { nid } = defineProps({ nid: { type: String } })

</script>
<template>
    <div class="line-actions-item codicon codicon-code" @click="() => nid && editEntity(nid)"></div>
</template>