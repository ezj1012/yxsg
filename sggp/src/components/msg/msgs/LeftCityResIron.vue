<script setup lang="ts">
import type { HoverMsg } from '@/app/commModel';
import type { SgRes } from '@/app/res';
import type { SanGuo, SgCtx } from '@/app/sg';
import { computed, inject, ref, type Ref } from 'vue';
const { sg, ctx, res } = inject('sg') as { sg: SanGuo, ctx: SgCtx, res: SgRes }
const { hmgs } = inject('msg') as { hmgs: Ref<HoverMsg> }


const iron = ref() // 人口
const ironMax = ref()

ctx.dataMgr.subscribeValue("playing#player_city_res_iron", iron)
ctx.dataMgr.subscribeValue("playing#player_city_res_ironMax", ironMax)

</script>
<template>
    <div class="msg">
        <div style="font-size: 16px;font-weight: 600;">铁锭</div>
        <div>当前数量 <span class="ff">{{ iron }}</span></div>
        <div>容器上限 <span class="ff">{{ ironMax }}</span></div>
    </div>
</template>
<style lang="less" scoped>
.msg {
    padding: 3px 8px;
    color: white;
    font-size: 12px;
    display: block;
    width: 180px;
    height: 70px;

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