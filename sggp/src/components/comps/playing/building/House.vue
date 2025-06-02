<script setup lang="ts">
import { CompMsg } from '@/app/msg/hoverMsgMgr';
import type { SgRes } from '@/app/res';
import type { SanGuo, SgCtx } from '@/app/sg';
import { heroStates } from '@/app/constant';
import { computed, inject, onMounted, onUnmounted, ref, type Ref } from 'vue';
import type { CityBuilding, CityHero, DefenceCfg, SoldierCfg } from '@/app/api/apiModel';
import Scroll from '../../Scroll.vue';
import type { FunPanComp } from '@/app/commModel';
import { CfgStr } from '@/app/cfg';
import PBtn from '../../PBtn.vue';
const { sg, ctx, res } = inject('sg') as { sg: SanGuo, ctx: SgCtx, res: SgRes }
const heros = ref<CityHero[]>([])
const data = ref<{ inner: boolean, build: CityBuilding }>()
const { cfg } = inject('funpan') as { cfg: Ref<FunPanComp> }
const img = ref('')
const msg = ref({ content: '' })
const des = ref('')
onMounted(() => {
    data.value = sg.ctx.dataMgr.getByKey('fun_pan#city_builds_params') as { inner: boolean, build: CityBuilding }
    const b = ctx.gCfgMgr.cfg?.buildingsMap[data.value.build.bid]!
    cfg.value!.content = `${b.name}(等级${data.value.build.lv == data.value.build.goalLv ? data.value.build.lv - 1 : data.value.build.lv})`
    cfg.value!.h = 165
    des.value = b.description
    msg.value.content = b.levels[data.value.build.lv]?.description || ''
    if (b.place == 1) {
        img.value = sg.ctx.res.img(`playing#innercity#${b.typeName}`)
    }
})
const upBtn = new CfgStr("K:funpanl#close;T:I_BTN;S:0,0,48,25,40;RFI:common#btn_green;ACT:closeFunPan;TXT:T:升级,F:12,C:var(--gold),;")
const downBtn = new CfgStr("K:funpanl#close;T:I_BTN;S:0,30,48,25,40;RFI:common#btn_red;ACT:closeFunPan;TXT:T:拆除,F:12,C:var(--gold),;")


</script>

<template>
    <div class="inner-pan-hero">
        <div class="b-info">
            <div class="img br" v-msg="msg" :style="{ backgroundImage: img }"></div>
            <div class=" op">
                <PBtn :cfg="upBtn" />
                <PBtn :cfg="downBtn" />
            </div>
            <div class="info br">{{ des }}</div>
        </div>
    </div>
</template>

<style lang="less" scoped>
.inner-pan-hero {
    position: absolute;
    width: 100%;
    height: 100%;
    background-color: #ffffff04;
    user-select: none;

    .b-info {
        width: 100%;
        height: 72px;
        position: relative;
        margin-bottom: 5px;
        display: flex;
        color: #ffffff;
        background-color: #203030a0;
        // border: 1px solid #203030;
        box-sizing: border-box;


        box-shadow:
            0px 0px 0px 0.4px #80867C inset,
            0px 0px 0px 0.8px #7D6F66 inset,
            0px 0px 0px 1.2px #271B25 inset,
            0px 0px 0px 1.6px #111A00 inset,
            0px 0px 0px 2.0px #7E6A49 inset,
            0px 0px 0px 2.5px #7B7472 inset;

        .br {
            box-shadow:
                0px 0px 0px 0.8px #7D6F66 inset,
                0px 0px 0px 1.2px #271B25 inset,
                0px 0px 0px 1.6px #111A00 inset,
                0px 0px 0px 2.0px #7E6A49 inset;
            box-sizing: border-box;
        }

        .op {
            position: absolute;
            width: 480px;
            left: 90px;
            height: 60px;
            top: 9px;
            font-weight: 100;
            font-size: 12px;
        }

        .img {
            width: 72px;
            min-width: 72px;
            margin-left: 8px;
            height: 60px;
            margin-top: 6px;
            background-repeat: no-repeat;
            background-size: 103px 79px;
            background-position-x: -15px;
            background-position-y: -18px;
            overflow: hidden;
            background-color: #203030a0;
        }

        .info {
            background-color: #203030e3;
            position: absolute;
            padding: 5px;
            width: 480px;
            font-weight: 100;
            left: 150px;
            height: 60px;
            top: 6px;
            font-size: 12px;
        }
    }


}
</style>
