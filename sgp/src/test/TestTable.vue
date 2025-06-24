<script setup lang="ts">
import { ref } from 'vue'

const headers = ref<any[]>([
    { styles: {}, content: '学生' },
    { styles: {}, content: '分数', width: 40 },
    { styles: {}, content: '状态', width: 40 },
])
const datas = ref<any[]>([
    [{ content: '新安社区as' }, { content: '1111' }, { content: '空闲' },],
    [{ content: 'aa' }, { content: '1111' },],
    [{ content: 'aa' }, { content: '999' },],
    // [{ content: 'aa' }, { content: 'dd' },],
])

for (let i = 0; i < 15; i++) {
    datas.value.push([{ content: 'aa' }, { content: '999' },])
}

async function changeSelect(params: any) {
    console.log(params)
}

</script>

<template>
    <div>
        <Table :w="160" :h="375" :headers="headers" :datas="datas" @change="changeSelect" />
    </div>
</template>

<style scoped></style>
