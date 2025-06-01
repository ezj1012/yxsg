<script setup lang="ts">
import type { HoverMsg } from '@/app/commModel';
import type { SgRes } from '@/app/res';
import type { SanGuo, SgCtx } from '@/app/sg';
import { computed, inject, ref, type Ref } from 'vue';
const { sg, ctx, res } = inject('sg') as { sg: SanGuo, ctx: SgCtx, res: SgRes }
const { hmgs } = inject('msg') as { hmgs: Ref<HoverMsg> }

const gold = ref()
const goldMax = ref()
const heroFee = ref()

const tax = ref() // 税率
const people = ref() // 人口

ctx.dataMgr.subscribeValue("playing#player_city_res_gold", gold)
ctx.dataMgr.subscribeValue("playing#player_city_res_goldMax", goldMax)
ctx.dataMgr.subscribeValue("playing#player_city_res_heroFee", heroFee)

ctx.dataMgr.subscribeValue("playing#player_city_res_tax", tax)
ctx.dataMgr.subscribeValue("playing#player_city_res_people", people)

const goldAdd = computed(() => {
    try {
        return Math.floor(people.value * tax.value / 100)
    } catch (error) {
        return 0
    }
})
</script>
<template>
    <div class="msg">
        <div style="font-size: 15px;font-weight: 600;">黄金</div>
        <div>当前数量 <span class="ff">{{ gold }}</span></div>
        <div>容量上限 <span class="ff">{{ goldMax }}</span></div>
        <div>征税收入 <span class="ff">{{ goldAdd }}</span></div>
        <div>将领俸禄 <span class="ff" style="color: red;">{{ heroFee }}</span></div>
        <div class="split"></div>
    </div>
</template>
<style lang="less" scoped>
.msg {
    padding: 3px 8px;
    color: white;
    font-size: 12px;
    display: block;
    width: 180px;
    height: 120px;

    .ff {
        width: 100px;
        float: right;
        text-align: right;
    }

    .split {
        width: 50px;
        height: 1px;
        margin-top: 10px;
        border-bottom: solid 1px #fff;
    }
}
</style>