<script setup lang="ts">
import type { HoverMsg } from '@/app/commModel';
import type { SgRes } from '@/app/res';
import type { SanGuo, SgCtx } from '@/app/sg';
import { computed, inject, ref, type Ref } from 'vue';
const { sg, ctx, res } = inject('sg') as { sg: SanGuo, ctx: SgCtx, res: SgRes }
const { hmgs } = inject('msg') as { hmgs: Ref<HoverMsg> }

const morale = ref()
const complaint = ref()


ctx.dataMgr.subscribeValue("playing#player_city_res_morale", morale)
ctx.dataMgr.subscribeValue("playing#player_city_res_complaint", complaint)


</script>
<template>
    <div class="msg">
        <div style="font-size: 15px;font-weight: 600;">民心</div>
        <div>当前民心 <span class="ff">{{ morale }}</span></div>
        <div>当前民怨 <span class="ff" :style="{ color: Number(complaint) > 0 ? 'red' : '' }">{{ complaint }}</span></div>
        <div class="split"></div>
    </div>
</template>
<style lang="less" scoped>
.msg {
    padding: 3px 8px;
    color: white;
    font-size: 12px;
    display: block;
    width: 140px;
    height: 90px;

    .ff {
        width: 30px;
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