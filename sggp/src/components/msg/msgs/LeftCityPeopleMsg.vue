<script setup lang="ts">
import type { HoverMsg } from '@/app/commModel';
import type { SgRes } from '@/app/res';
import type { SanGuo, SgCtx } from '@/app/sg';
import { computed, inject, ref, type Ref } from 'vue';
const { sg, ctx, res } = inject('sg') as { sg: SanGuo, ctx: SgCtx, res: SgRes }
const { hmgs } = inject('msg') as { hmgs: Ref<HoverMsg> }


const people = ref() // 人口
const peopleMax = ref()

const peopleWorking = ref()
const peopleBuilding = ref()
const peopleFree = computed(() => {
    try {
        return people.value - peopleWorking.value - peopleBuilding.value
    } catch (error) {
        return 0
    }
})

ctx.dataMgr.subscribeValue("playing#player_city_res_people", people)
ctx.dataMgr.subscribeValue("playing#player_city_res_peopleMax", peopleMax)
ctx.dataMgr.subscribeValue("playing#player_city_res_peopleWorking", peopleWorking)
ctx.dataMgr.subscribeValue("playing#player_city_res_peopleBuilding", peopleBuilding)

</script>
<template>
    <div class="msg">
        <div style="font-size: 15px;font-weight: 600;">人口</div>
        <div>当前人口 <span class="ff">{{ people }}</span></div>
        <div>人口上限 <span class="ff">{{ peopleMax }}</span></div>
        <div class="split-int"></div>
        <div>劳动人口 <span class="ff">{{ peopleWorking }}</span></div>
        <div>建筑人口 <span class="ff">{{ peopleBuilding }}</span></div>
        <div>空闲人口 <span class="ff">{{ peopleFree }}</span></div>
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
    height: 160px;

    .ff {
        width: 100px;
        float: right;
        text-align: right;
    }

    .split-int {
        width: 50px;
        height: 1px;
        margin: 10px 0;
        border-bottom: solid 1px #fff;
    }

    .split {
        width: 50px;
        height: 1px;
        margin-top: 10px;
        border-bottom: solid 1px #fff;
    }
}
</style>