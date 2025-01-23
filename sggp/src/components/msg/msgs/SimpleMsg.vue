<script setup lang="ts">
import type { SanGuo } from '@/app/sg';
import { computed, inject } from 'vue';
const { sg } = inject('sg') as { sg: SanGuo }

const msg = computed(() => {
    return sg.dataMgr.hoverMsg.value?.content
})
const isHtml = computed(() => {
    return sg.dataMgr.hoverMsg.value?.isHtml
})
</script>
<template>
    <div v-if="isHtml" class="msg" v-html="msg"></div>
    <div v-else class="msg">{{ msg }}</div>
</template>
<style lang="less" scoped>
.msg {
    padding: 2px 15px;
    color: white;
    font-size: 12px;
    display: block;
}
</style>