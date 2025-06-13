<script setup lang="ts">
import type { HoverMsg } from '@/app/commModel';
import type { SgRes } from '@/app/res';
import type { SanGuo, SgCtx } from '@/app/sg';
import { computed, inject, ref, type Ref } from 'vue';
const { sg, ctx, res } = inject('sg') as { sg: SanGuo, ctx: SgCtx, res: SgRes }
const { hmgs } = inject('msg') as { hmgs: Ref<HoverMsg> }

const defence = computed(() => {
    if (ctx.gCfgMgr.cfg && hmgs.value && hmgs.value.content?.data?.id) {
        return ctx.gCfgMgr.cfg?.defencesMap[hmgs.value?.content.data.id]
    }
    return undefined
})

</script>
<template>
    <div class="msg">
        <div style="font-size: 16px;font-weight: 600;margin-bottom: 10px;">
            {{ `${defence?.name}` }}
        </div>
        <div style="margin-bottom: 10px;"> {{ defence?.description }}</div>
        <div>生命 <span class="ff">{{ defence?.hp }}</span></div>
        <div>攻击 <span class="ff">{{ defence?.ap }}</span></div>
        <div>防御 <span class="ff">{{ defence?.dp }}</span></div>
        <div>射程 <span class="ff">{{ defence?.apRange }}</span></div>
        <div style="margin-bottom: 10px;">空间 <span class="ff">{{ defence?.areaNeed }}</span></div>
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