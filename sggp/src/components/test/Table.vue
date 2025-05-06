<script lang="ts" setup>
import { ScrollHelper } from '@/app/res'
import type { SanGuo } from '@/app/sg'
import { computed, inject, onMounted, onUnmounted, ref, watch } from 'vue'
import TableHeader from './TableHeader.vue'
import { TableHeaderDef, TableDataRowDef, TableDataCellDef, defaultConverHeader, defaultConverDatas } from '@/app/model'


const emit = defineEmits(['change'])
const props = defineProps({
    headerHeight: { default: 25 },
    rowHeight: { default: 30 },
    headers: { type: Array, default: [] },
    datas: { type: Array, default: [] },
    headersConvert: { type: Function, default: defaultConverHeader },
    datasConver: { type: Function, default: defaultConverDatas },
})

const { sg } = inject('sg') as { sg: SanGuo }

const empty = new TableDataRowDef([], true)
const headers = ref<TableHeaderDef[]>([])
const datas = ref<TableDataRowDef[]>([])

const tableEl = ref<HTMLDivElement>()
const dataEl = ref<HTMLDivElement>()

const showScroll = ref(false)
const scrollHelper = ref<ScrollHelper>()
const bgUrl = ref('')
const obs = new ResizeObserver(entries => {
    for (const en of entries) {
        updateBg()
        calcDatas()
    }
})

onMounted(() => {
    obs.observe(tableEl.value!)
    scrollHelper.value = new ScrollHelper(sg)
    // updateBg()
    // calcDatas()
})
onUnmounted(() => {
    obs.disconnect()
})

watch(props.headers, () => { headers.value = props.headersConvert(props.headers) }, { immediate: true })
watch(props.datas, () => calcDatas())

function calcDatas() {
    if (dataEl.value) {
        const dataRect = dataEl.value.getBoundingClientRect()

        const dataHeight = props.datas.length * props.rowHeight
        showScroll.value = dataHeight > dataRect.height

        scrollHelper.value?.scrollImg(dataEl.value, showScroll.value, dataHeight)

        // 
        const rowDatas: TableDataRowDef[] = props.datasConver(props.datas)
        while (rowDatas.length < Math.max(Math.ceil(dataRect.height / props.rowHeight), props.datas.length)) {
            rowDatas.push(empty)
        }
        datas.value = rowDatas

    }
}

function doClick(row: TableDataRowDef, idx: number, e: MouseEvent) {
    emit('change', { row, idx, e })
}

function updateBg() {
    const rect = tableEl.value!.getBoundingClientRect()
    console.log(rect.width, tableEl.value?.parentElement?.getBoundingClientRect().width)
    bgUrl.value = sg.img('common#table_board', { w: rect.width, h: rect.height })
}

const styleVars = computed(() => {
    return {
        'background-image': bgUrl.value,
        '--table-bg-line': sg.img('common#table_contentline'),
        '--header-height': `${props.headerHeight}px`,
        '--header-scroll-bg': sg.img('common#table_header', { w: 20, h: props.headerHeight }),
        '--row-height': `${props.rowHeight}px`,
    };
})



</script>
<template>
    <div ref="tableEl" class="table" :style="styleVars">
        <div class="table-headers bg-line">
            <TableHeader v-for="h in headers" class="table-header-cell" :header="h" :height="props.headerHeight" />
            <div class="table-header-scroll-flag" v-show="showScroll"></div>
        </div>
        <div ref="dataEl" class="table-datas scroll_container" :style="scrollHelper?.styles">
            <div class="table-row  bg-line" :class="[row.datas.length > 0 ? 'table-row-not-empty' : '']"
                v-for="row, rowIdx in datas" @click="doClick(row, rowIdx, $event)">
                <div v-if="row.datas.length" class="table-data-cell" v-for="data, colIdx in row.datas"
                    :style="{ width: `${headers[colIdx].realWidth}px` }">
                    <div :style="data.styles" v-html="data.content"> </div>
                </div>
            </div>
        </div>
    </div>
</template>
<style lang="less" scoped>
.table {
    position: absolute;
    left: 0px;
    top: 0px;
    padding: 2.5px;
    height: calc(100% - 5px);
    width: calc(100% - 5px);
    display: flex;
    flex-direction: column;
    font-size: 12px;

    .table-headers {
        flex: 0 0 var(--header-height);
        display: flex;
        height: var(--header-height);
        font-weight: 800;
        background-color: var(--rxsg-table-row-bg-color);

        .table-header-scroll-flag {
            width: 19px;
            min-width: 19px;
            max-width: 19px;
            background: var(--header-scroll-bg);
        }
    }

    .table-datas {
        flex: 1;
        font-size: 12px;
        overflow-y: auto;

        .table-row {
            width: 100%;
            height: var(--row-height);
            display: flex;
            flex-direction: row;
            background-color: var(--rxsg-table-row-bg-color);
        }

        .table-row-not-empty {

            &:hover {
                background-color: var(--rxsg-table-row-hover-bg-color);
            }
        }

    }

    .bg-line {
        background-image: var(--table-bg-line);
        background-repeat: repeat-x;
        background-position-y: 100%;
    }
}
</style>