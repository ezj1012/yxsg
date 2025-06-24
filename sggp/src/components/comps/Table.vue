<script setup lang="ts">
import { defaultConverDatas, defaultConverHeader, TableDataRowDef, TableHeaderDef } from '@/app/utils/tableHelper';
import { computed, onMounted, onUnmounted, ref, watch } from 'vue';

const emit = defineEmits(['change'])
const props = defineProps({
    w: { default: 200 },
    h: { default: 100 },
    headerHeight: { default: 25 },
    rowHeight: { default: 30 },
    headers: { type: Array, default: [] },
    datas: { type: Array, default: [] },
    headersConvert: { type: Function, default: defaultConverHeader },
    datasConver: { type: Function, default: defaultConverDatas },
})


const headEl = ref()
const dataEl = ref()
const showScroll = ref(false)

const empty = new TableDataRowDef([], {}, true)
const headers = ref<TableHeaderDef[]>([])
const datas = ref<TableDataRowDef[]>([])

watch(props.headers, () => { headers.value = props.headersConvert(props.headers) }, { immediate: true })
watch(props.datas, () => calcDatas())
watch(() => props.h, () => calcDatas())
watch(() => props.w, () => calcDatas())

const obs = new ResizeObserver(entries => {
    for (const entry of entries) {
        // 获取被观察的目标容器
        const container = entry.target;

        // 遍历容器的所有子元素
        Array.from(container.children).forEach((child, idx) => {
            const width = child.clientWidth;
            headers.value[idx] && (headers.value[idx].realWidth = width)
        });
    }
})

onMounted(() => {
    obs.observe(headEl.value)
    calcDatas()
})

onUnmounted(() => obs.disconnect())

function calcDatas() {
    if (dataEl.value) {
        const dataRect = dataEl.value.getBoundingClientRect()

        const dataHeight = props.datas.length * props.rowHeight
        showScroll.value = dataHeight > dataRect.height
        const rowDatas: TableDataRowDef[] = props.datasConver(props.datas)

        while (rowDatas.length < Math.max(Math.ceil(dataRect.height / props.rowHeight), props.datas.length, (props.h - props.headerHeight) / props.rowHeight)) {
            rowDatas.push(empty)
        }
        datas.value = rowDatas
    }
}

function doClick(row: TableDataRowDef, idx: number, e: MouseEvent) {
    !row.empty && emit('change', { row, idx, e })
}

const varStyle = computed(() => {
    const r = {} as any
    r['--h-h'] = `${props.headerHeight}px` // 表头高
    r['--r-h'] = `${props.rowHeight}px`     // 行高
    r['--r-lh'] = `${props.rowHeight - 3}px`
    r['width'] = `${props.w}px`
    r['height'] = `${props.h}px`
    return r
})

function hStyle(header: TableHeaderDef) {
    const s = {
        ...header.styles,
        lineHeight: `${props.headerHeight - 3}px`,
        flex: header.width == 0 ? '1' : `0 0 ${header.width}px`,
    } as any
    return s
}

</script>

<template>
    <div class="sg-table" :style="varStyle">
        <div ref="headEl" class="table-headers">
            <div class="table-header" v-for="h in headers" :key="h.content" v-html="h.content" :style="hStyle(h)"></div>
            <div class="table-header-scroll-flag" v-show="showScroll"></div>
        </div>
        <div ref="dataEl" class="table-datas sg-scroll" :style="{ overflowY: showScroll ? 'scroll' : 'hidden' }">
            <div v-for="row, rowIdx in datas" @click.prevent="doClick(row, rowIdx, $event)" class="table-row  bg-line"
                :class="[row.datas.length > 0 ? 'table-row-not-empty' : '']">
                <div v-if="row.datas.length" class="table-data-cell" v-for="data, colIdx in row.datas"
                    :style="{ width: `${headers[colIdx].realWidth}px` }">
                    <div :style="data.styles" v-html="data.content"> </div>
                </div>
            </div>
        </div>
    </div>
</template>
<style lang="less" scoped>
.sg-table {
    position: absolute;
    padding: 3px;
    box-sizing: border-box;
    display: flex;
    flex-direction: column;

    &::before {
        position: absolute;
        content: '';
        left: 0;
        top: 0;
        right: 0;
        bottom: 0;
        border: 10px solid transparent;
        border-image: url('../../assets/imgs/board_comm.png') 10 fill;
        z-index: -1;
    }

    .table-datas {
        flex: 1;
        font-size: 12px;
        color: #fff;

        .table-row {
            width: 100%;
            height: var(--r-h, 30px);
            line-height: var(--r-lh, 27px);
            display: flex;
            flex-direction: row;
            background-color: var(--rxsg-table-row-bg-color);
            user-select: none;
        }

        .table-row-not-empty {
            &:hover {
                background-color: var(--rxsg-table-row-hover-bg-color);
            }
        }

        .table-data-cell {
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
        }
    }

    .table-headers {
        position: relative;
        flex: 0 0 var(--h-h, 25px);
        display: flex;
        height: var(--h-h, 25px);
        font-weight: 800;

        &::before {
            content: '';
            position: absolute;
            top: 0px;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: #000;
            z-index: -3;
        }

        .table-header-scroll-flag {
            width: 19px;
            min-width: 19px;
            max-width: 19px;
            box-sizing: border-box;
            border: 5px solid transparent;
            border-image: url('../../assets/imgs/table/table_header.png') 5 fill;
        }

        .table-header {
            position: relative;
            text-align: center;
            user-select: none;
            background-repeat: no-repeat;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
            font-size: 13px;

            &::before {
                content: "";
                position: absolute;
                left: 0;
                top: 0;
                width: 100%;
                height: 100%;
                //
                border: 5px solid #000;
                border-image: url('../../assets/imgs/table/table_header.png') 5 fill;
                box-sizing: border-box;
                z-index: -1;
            }
        }
    }

    .bg-line {
        background-image: url('../../assets/imgs/table/table_contentline.png');
        background-repeat: repeat-x;
        background-position-y: 100%;
    }

}
</style>