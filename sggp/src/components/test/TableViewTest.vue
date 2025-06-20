<script lang="ts" setup>
import { ref } from 'vue';
import Table from '../comps/Table.vue';

function aa(data: any) {
    console.log('change: ' + JSON.stringify(data))
}
const headers = ref<any[]>([
    { styles: {}, content: '名字', width: 100 },
    { styles: {}, content: '年龄' },
])
const datas = ref<any[]>([
    [{ content: 'aa' }, { content: 'bb' },],
    [{ content: 'aa' }, { content: 'cc' },],
    [{ content: 'aa' }, { content: 'dd' },],
    [{ content: 'aa' }, { content: 'dd' },],
])

</script>
<template>
    <!---->
    <div class="table-wrap">
        <Table @change="aa" :headers="headers" :datas="datas" />
    </div>
    <div @click="() => datas.length > 0 && datas.splice(datas.length - 1, 1)">删除元素</div>
    <div @click="() => datas.push([{ content: 'aa' }, { content: 'bb' },])">添加元素</div>
</template>
<style lang="less" scoped>
.table-wrap {
    width: 400px;
    height: 130px;
    position: relative;
}

// .table {
//     display: grid;
//     grid-template-columns: 1fr 2fr 1fr;
//     /* 指定每列的宽度比例 */
//     width: 100%;
// }

// .table-row {
//     display: contents;
//     /* 让.grid-row不生成额外的盒子模型，使得.cell可以直接作为grid的子元素 */
// }

// .table-cell {
//     border: 1px solid #ddd;
//     padding: 8px;
//     box-sizing: border-box;
// }

// .header .table-cell {
//     font-weight: bold;
//     background-color: #f4f4f4;
// }</style>