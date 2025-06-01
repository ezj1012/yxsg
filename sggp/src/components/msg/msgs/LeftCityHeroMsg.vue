<script setup lang="ts">
import type { HoverMsg } from '@/app/commModel';
import type { SgRes } from '@/app/res';
import type { SanGuo, SgCtx } from '@/app/sg';
import { computed, inject, ref, type Ref } from 'vue';
const { sg, ctx, res } = inject('sg') as { sg: SanGuo, ctx: SgCtx, res: SgRes }
const { hmgs } = inject('msg') as { hmgs: Ref<HoverMsg> }

const hero = computed(() => {
    if (ctx.play?.city.heros && hmgs.value && hmgs.value.content?.data?.id) {
        return ctx.play.city.heros.find(h => h.id === hmgs.value.content?.data?.id)
    }
    return undefined
})

</script>
<template>
    <div class="msg">
        <div style="font-size: 16px;font-weight: 600;margin-bottom: 10px;">
            {{ hero?.name }}
        </div>
        <div style="margin-bottom: 10px;"> {{ }}</div>
        <div>等级 <span class="ff">{{ hero?.level }}</span></div>
        <div>统率 <span class="ff">{{ hero?.commandBase || 0 + (hero?.commandAddOn || 0) }}</span></div>
        <div>内政 <span class="ff">{{ hero?.affairsBase || 0 + (hero?.affairsAddOn || 0) }}</span></div>
        <div>勇武 <span class="ff">{{ hero?.braveryBase || 0 + (hero?.braveryAddOn || 0) }}</span></div>
        <div>智谋 <span class="ff">{{ hero?.wisdomBase || 0 + (hero?.wisdomAddOn || 0) }}</span></div>
        <div>体力 <span class="ff">{{ hero?.forceMaxAddOn }}</span></div>
        <div>精力 <span class="ff">{{ hero?.energyMaxAddOn }}</span></div>
        <div>忠诚 <span class="ff">{{ hero?.loyalty }}</span></div>
        <div style="margin-bottom: 10px;">速度 <span class="ff">{{ hero?.speedAddOn }}</span></div>
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