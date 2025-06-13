<script setup lang="ts">
import { CompMsg } from '@/app/msg/hoverMsgMgr';
import type { SgRes } from '@/app/res';
import type { SanGuo, SgCtx } from '@/app/sg';
import { computed, inject, ref, type Ref } from 'vue';
import Text from '../../Text.vue';
import { CfgStr } from '@/app/cfg';
import PBtn from '../../PBtn.vue';
const { sg, ctx, res } = inject('sg') as { sg: SanGuo, ctx: SgCtx, res: SgRes }

const food = res.img('playing#city#res_food')
const iron = res.img('playing#city#res_iron')
const rock = res.img('playing#city#res_rock')
const wood = res.img('playing#city#res_wood')

const textStyle = btoa("box-shadow: 0px 0px 0px 0.5px #7E6A49 inset, 0px 0px 0px 1px #111A00 inset, 0px 0px 0px 1.5px #271B25 inset;background-color: #3228;line-height: 25px;text-align: right;padding: 0 5px;box-sizing: border-box;")
function createText(key: string, x: number, y: number, w: number, h: number, msg: string) {
    const cfg = `K:playing#${key};T:TEXT;S:${x},${y},${w},${h},10;TXT:T:,F:15,C:#fff,;SMSG: ${msg};STYL:${textStyle}`
    return new CfgStr(cfg)
}

function configureCommPlus(x: number, y: number, w: number, h: number, clickKey: string, msg: string) {
    const cfg = `K:playing#${clickKey};T:I_PTN;S:${x},${y},${w},${h},25;RFI:common#btn_plus;TXT:T:,F:12,C:#fff,;;SMSG:${msg};`
    return new CfgStr(cfg)
}

const cityReses = ref<any[]>([
    { msg: new CompMsg('LeftCityResFood'), bg: food, y: 35, cur: createText('player_city_res_food', 30, 0, 100, 25, '粮食数量'), add: createText('player_city_res_foodAdd', 130, 0, 95, 25, '粮食产量'), plus: configureCommPlus(228, 8, 11, 11, 'player_city_res_foodAdd', '使用宝物增加粮食产量') },
    { msg: new CompMsg('LeftCityResWood'), bg: wood, y: 60, cur: createText('player_city_res_wood', 30, 0, 100, 25, '木材数量'), add: createText('player_city_res_woodAdd', 130, 0, 95, 25, '木材产量'), plus: configureCommPlus(228, 8, 11, 11, 'player_city_res_woodAdd', '使用宝物增加木材产量') },
    { msg: new CompMsg('LeftCityResRock'), bg: rock, y: 85, cur: createText('player_city_res_rock', 30, 0, 100, 25, '石料数量'), add: createText('player_city_res_rockAdd', 130, 0, 95, 25, '石料产量'), plus: configureCommPlus(228, 8, 11, 11, 'player_city_res_rockAdd', '使用宝物增加石料产量') },
    { msg: new CompMsg('LeftCityResIron'), bg: iron, y: 110, cur: createText('player_city_res_iron', 30, 0, 100, 25, '铁锭数量'), add: createText('player_city_res_ironAdd', 130, 0, 95, 25, '铁锭产量'), plus: configureCommPlus(228, 8, 11, 11, 'player_city_res_ironAdd', '使用宝物增加铁锭产量') },
]);



</script>

<template>
    <!--  :style="{ left: `${cfgV.x}px`, top: `${cfgV.y}px`, zIndex: cfgV.z }" -->
    <div class="inner-pan-res">

        <div class="res" v-for="res in cityReses" :style="{ top: `${res.y}px` }">
            <div v-msg="res.msg" class="icon" :style="{ backgroundImage: res.bg }"></div>
            <Text :cfg="res.cur" :id="res.cur.key()" />
            <Text :cfg="res.add" :id="res.add.key()" />
            <PBtn :cfg="res.plus" :id="res.add.key()" />

        </div>
    </div>
</template>
<style lang="less" scoped>
.inner-pan-res {
    position: absolute;
    width: 256px;
    height: 283px;
    background-color: #ffffff04;
    user-select: none;
    box-shadow: 0px 0px 0px 1px #80867C inset,
        0px 0px 0px 2px #7D6F66 inset,
        0px 0px 0px 3px #271B25 inset,
        0px 0px 0px 4px #111A00 inset,
        0px 0px 0px 5px #7E6A49 inset,
        0px 0px 0px 6px #7B7472 inset;

    .res {
        position: absolute;
        left: 10px;
        width: 100%;
        height: 25px;

        .icon {
            width: 30px;
            height: 25px;
            box-shadow: 0px 0px 0px .5px #7E6A49 inset, 0px 0px 0px 1px #111A00 inset,
                0px 0px 0px 1.5px #271B25 inset;
            background-repeat: no-repeat;
            background-position: 1px -2px;
            background-color: #3228;
        }
    }
}
</style>
