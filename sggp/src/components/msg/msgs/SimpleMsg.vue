<script setup lang="ts">
import type { HoverMsg } from '@/app/commModel';
import type { SgRes } from '@/app/res';
import type { SanGuo, SgCtx } from '@/app/sg';
import { computed, inject, type Ref } from 'vue';
const { sg, ctx, res } = inject('sg') as { sg: SanGuo, ctx: SgCtx, res: SgRes }
const { hmgs } = inject('msg') as { hmgs: Ref<HoverMsg> }


const msg = computed(() => {
    return hmgs.value?.content
})
const isHtml = computed(() => {
    return hmgs.value?.isHtml
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