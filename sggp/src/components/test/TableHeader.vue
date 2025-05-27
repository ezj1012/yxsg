<script lang="ts" setup>
import { TableHeaderDef } from '@/app/commModel';
import type { SanGuo } from '@/app/sg';
import { computed, inject, onMounted, ref } from 'vue';

const { sg } = inject('sg') as { sg: SanGuo }

const { header, height } = defineProps({ header: { required: true, default: new TableHeaderDef() }, height: { default: 25 } })

const hEl = ref<HTMLElement>()
const width = ref()
const bg = ref('')
const obs = new ResizeObserver(entries => {
    for (const en of entries) {
        if (width.value != en.contentRect.width) {
            width.value = en.contentRect.width
            bg.value = sg.ctx.res.img('common#table_header', { w: width.value, h: height })
            header.realWidth = width.value
        }
    }
})

onMounted(() => {
    obs.observe(hEl.value!)
})

const styles = computed(() => {
    const s = {
        ...header.styles,
        backgroundImage: bg.value,
        lineHeight: `${height - 3}px`,
        flex: header.width == 0 ? '1' : `0 0 ${header.width}px`,
    } as any
    return s
})

</script>
<template>
    <div ref="hEl" class="table-header" :style="styles" v-html="header.content">
    </div>
</template>
<style lang="less" scoped>
.table-header {
    text-align: center;
    user-select: none;
    background-repeat: no-repeat;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
}
</style>