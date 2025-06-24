<script setup lang="ts">
import type { HoverMsg } from '@/app/commModel';
import type { SgRes } from '@/app/res';
import type { SanGuo, SgCtx } from '@/app/sg';
import { computed, inject, ref, watch, type Ref } from 'vue';
const { sg, ctx, res } = inject('sg') as { sg: SanGuo, ctx: SgCtx, res: SgRes }
const { hmgs } = inject('msg') as { hmgs: Ref<HoverMsg> }

// watch(hmgs, () => {
//     console.log(`hmgs111`, hmgs.value)
// }, { deep: true })

const msg = computed(() => {
    return hmgs.value?.content?.msg?.cfg || {}
})

</script>

<template>
    <div class="msg">
        <div class="title">{{ msg?.name }}</div>
        <div style="padding-bottom: 5px;"> {{ msg?.description }} </div>
    </div>
</template>
<style lang="less" scoped>
.msg {
    .title {
        font-size: 12px;
        font-weight: 600;
        color: var(--light-blue);
        padding-bottom: 5px;
    }

    padding: 3px 8px;
    color: white;
    font-size: 12px;
    display: block;
    max-width: 220px;
    min-height: 60px;
}
</style>