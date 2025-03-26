<script setup lang="ts">
import { CfgStr } from '@/app/cfg';
import type { SanGuo } from '@/app/sg';
import { inject, onMounted, ref, watch, type Ref, type ShallowRef } from 'vue';

import { actionMgr } from '@/app/action';
import { cid2xy } from '@/app/constant';
import PBtn from '../comps/PBtn.vue';
import type { MapCtx } from '@/app/worldMap';


const { sg } = inject('sg') as { sg: SanGuo }
const { mapCtx  } = inject('worldMap') as {  mapCtx: Ref<MapCtx>  }

const bgUrl = `url(${sg.res.getImgGroup('playing#world#ctrl#map_controller').hasDef()?.getDataUrl()})`
const moveBtn = new CfgStr(`K:playing#worldctrl#map_move;T:I_BTN;S:103,104,42,23,100;RFI:playing#world#ctrl#map_move;NOM;ACT:WM_mapCtx.value.moveTo`);
const centerBtn = new CfgStr(`K:playing#worldctrl#center;T:I_BTN;S:73.5,31,42,42,100;RFI:playing#world#ctrl#map_mycity;NOM;ACT:WM_MoveCity`);
const mapBtn = new CfgStr(`K:playing#worldctrl#open_map;T:I_BTN;S:156,107,19,18,100;RFI:playing#world#ctrl#world_map;NOM;SMSG:世界地图;`);

const upBtn = new CfgStr(`K:playing#worldctrl#up;T:I_BTN;S:73,4,43,28,100;RFI:playing#world#ctrl#map_upm;NOM;ACT:WM_MoveUp;`);
const uprightBtn = new CfgStr(`K:playing#worldctrl#up;T:I_BTN;S:101,4,41,40,100;RFI:playing#world#ctrl#map_upright;NOM;ACT:WM_MoveUpRight;`);
const upleftBtn = new CfgStr(`K:playing#worldctrl#up;T:I_BTN;S:46,4,41,40,100;RFI:playing#world#ctrl#map_upleft;NOM;ACT:WM_MoveUpLeft;`);

const rightBtn = new CfgStr(`K:playing#worldctrl#up;T:I_BTN;S:114,30,28,43,100;RFI:playing#world#ctrl#map_right;NOM;ACT:WM_MoveRight;`);
const downrightBtn = new CfgStr(`K:playing#worldctrl#up;T:I_BTN;S:102,59,41,40,100;RFI:playing#world#ctrl#map_downright;NOM;ACT:WM_MoveDownRight;`);

const downBtn = new CfgStr(`K:playing#worldctrl#up;T:I_BTN;S:74,72,43,28,100;RFI:playing#world#ctrl#map_down;NOM;ACT:WM_MoveDown;`);
const downleftBtn = new CfgStr(`K:playing#worldctrl#up;T:I_BTN;S:47,60,41,40,100;RFI:playing#world#ctrl#map_downleft;NOM;ACT:WM_MoveDownLeft;`);
const leftBtn = new CfgStr(`K:playing#worldctrl#up;T:I_BTN;S:47,32,28,43,100;RFI:playing#world#ctrl#map_left;NOM;ACT:WM_MoveLeft;`);

actionMgr.reg('WM_mapCtx.value.moveTo', async (sg) => {
    await mapCtx.value.moveTo(mapCtx.value.x, mapCtx.value.y)
})
actionMgr.reg('WM_MoveCity', async ({ sg }) => {
    const cityId = sg.dataMgr.getByKey('playing#player_lastCity')
    const xy = cid2xy(cityId)
    await mapCtx.value.moveTo(xy.x, xy.y)
})
actionMgr.reg('WM_MoveUp', async ({ sg }) => { await mapCtx.value.moveTo(Number(mapCtx.value.x) - 4, Number(mapCtx.value.y) - 4) })

actionMgr.reg('WM_MoveUpRight', async ({ sg }) => { await mapCtx.value.moveTo(Number(mapCtx.value.x), Number(mapCtx.value.y) - 5) })
actionMgr.reg('WM_MoveUpLeft', async ({ sg }) => { await mapCtx.value.moveTo(Number(mapCtx.value.x) - 5, Number(mapCtx.value.y)) })

actionMgr.reg('WM_MoveRight', async ({ sg }) => { await mapCtx.value.moveTo(Number(mapCtx.value.x) + 3, Number(mapCtx.value.y) - 3) })
actionMgr.reg('WM_MoveDownRight', async ({ sg }) => { await mapCtx.value.moveTo(Number(mapCtx.value.x) + 5, Number(mapCtx.value.y)) })

actionMgr.reg('WM_MoveDown', async ({ sg }) => { await mapCtx.value.moveTo(Number(mapCtx.value.x) + 4, Number(mapCtx.value.y) + 4) })
actionMgr.reg('WM_MoveDownLeft', async ({ sg }) => { await mapCtx.value.moveTo(Number(mapCtx.value.x), Number(mapCtx.value.y) + 5) })
actionMgr.reg('WM_MoveLeft', async ({ sg }) => { await mapCtx.value.moveTo(Number(mapCtx.value.x) - 3, Number(mapCtx.value.y) + 3) })


function input() {
    if (Number.isNaN(Number(mapCtx.value.x))) {
        mapCtx.value.x = 1
    } else {
        mapCtx.value.x < 1 && (mapCtx.value.x = 1)
        mapCtx.value.x > 500 && (mapCtx.value.x = 500)
    }
    if (Number.isNaN(Number(mapCtx.value.y))) {
        mapCtx.value.y = 1
    } else {
        mapCtx.value.y < 1 && (mapCtx.value.y = 1)
        mapCtx.value.y > 500 && (mapCtx.value.y = 500)
    }
}

</script>

<template>
    <div id="world-map-ctrl">
        <div class="bg" :style="{ backgroundImage: bgUrl }"></div>
        <input v-model="mapCtx.x" maxlength="3" @input="input" style="left: 36px;" />
        <input v-model="mapCtx.y" maxlength="3" @input="input" style="left: 72px;" />
        <PBtn :cfg="moveBtn" />
        <PBtn :cfg="mapBtn" />
        <PBtn :cfg="centerBtn" />

        <PBtn :cfg="upBtn" />
        <PBtn :cfg="uprightBtn" />
        <PBtn :cfg="rightBtn" />
        <PBtn :cfg="downrightBtn" />

        <PBtn :cfg="downBtn" />
        <PBtn :cfg="downleftBtn" />
        <PBtn :cfg="leftBtn" />
        <PBtn :cfg="upleftBtn" />
    </div>
</template>
<style lang="less" scoped>
#world-map-ctrl {
    position: absolute;
    left: 540px;
    top: 410px;
    z-index: 100;
    width: 182px;
    height: 132px;
    user-select: none;

    .bg {
        width: 100%;
        height: 100%;
        pointer-events: none;
    }

    input {
        background: transparent;
        position: absolute;
        outline: none;
        border: 0;
        background-color: transparent;
        width: 22px;
        top: 108px;
        text-align: right;
        color: var(--gold);
    }
}
</style>
