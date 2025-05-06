<script lang="ts" setup>
import { ref } from 'vue';
import Table from '../Table.vue';

const headers = ref<any[]>([
    { styles: {}, content: '将领', width: 60, color: 'var(--rxsg-table-header-font-color)' },
    { styles: {}, content: '等级', width: 50, color: 'var(--rxsg-table-header-font-color)' },
    { styles: {}, content: '状态', color: 'var(--rxsg-table-header-font-color)' },
])
const datas = ref<any[]>([])
</script>
<template>
    <div class="fun-main-hero">
        <div class="heros">
            <Table :headers="headers" :datas="datas" />
        </div>
    </div>
</template>
<style lang="less" scoped>
.fun-main-hero {
    position: absolute;
    width: 100%;
    height: 100%;

    .heros {
        position: absolute;
        width: 160px;
        height: 370px;
    }
}
</style>