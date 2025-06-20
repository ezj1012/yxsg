<script lang="ts" setup>
import Table from '@/components/comps/Table.vue';
import { onMounted, ref } from 'vue';

const headers = ref<any[]>([
    { styles: {}, content: '将领', width: 60, color: 'var(--rxsg-table-header-font-color)' },
    { styles: {}, content: '等级', width: 50, color: 'var(--rxsg-table-header-font-color)' },
    { styles: {}, content: '状态', color: 'var(--rxsg-table-header-font-color)' },
])
const datas = ref<any[]>([])
const heroId = ref<number>(-1)
onMounted(() => {
})

function changeHero() {
    console.log('changeHero')
}

</script>
<template>
    <div class="fun-main-hero">
        <!-- <div class="heros"> -->
        <Table :headers="headers" :datas="datas" v-size="`0,0,160,375`" :w="160" :h="375" @change="changeHero" />
        <div class="sg-comm" v-size="`162,0,250,375,1`"></div>
        <div class="sg-comm" v-size="`411,0,230,375,0`"></div>
        <!-- </div> -->
    </div>
</template>
<style lang="less" scoped>
.fun-main-hero {
    position: absolute;
    width: 100%;
    height: 100%;
}
</style>