<script setup lang="ts">
import type { HoverMsg } from '@/app/commModel';
import type { SgRes } from '@/app/res';
import type { SanGuo, SgCtx } from '@/app/sg';
import { computed, inject, ref, type Ref } from 'vue';
const { sg, ctx, res } = inject('sg') as { sg: SanGuo, ctx: SgCtx, res: SgRes }
const { hmgs } = inject('msg') as { hmgs: Ref<HoverMsg> }

const soldier = computed(() => ctx.gCfgMgr.cfg?.soldiersMap[hmgs.value.content.data.id])

</script>
<template>
    <div class="msg">
        <div style="font-size: 16px;font-weight: 600;margin-bottom: 10px;">
            {{ `${soldier?.name}` }}
        </div>
        <div style="margin-bottom: 10px;"> {{ soldier?.description }}</div>
        <div>生命 <span class="ff">{{ soldier?.hp }}</span></div>
        <div>攻击 <span class="ff">{{ soldier?.ap }}</span></div>
        <div>防御 <span class="ff">{{ soldier?.dp }}</span></div>
        <div>射程 <span class="ff">{{ soldier?.apRange }}</span></div>
        <div>速度 <span class="ff">{{ soldier?.speed }}</span></div>
        <div>负重 <span class="ff">{{ soldier?.carry }}</span></div>
        <div>军费 <span class="ff">{{ soldier?.goldNeed }}</span></div>
        <div style="margin-bottom: 10px;">人口 <span class="ff">{{ soldier?.peopleNeed }}</span></div>
        <!--  -->
    </div>
</template>
<style lang="less" scoped>
.msg {
    padding: 3px 8px;
    color: white;
    font-size: 12px;
    display: block;
    width: 200px;

    .ff {
        width: 100px;
        margin-left: 30px;
        text-align: left;
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