<script setup lang="ts">
import { inject, onMounted, ref } from 'vue';
import Table from '../../Table.vue';
import type { Sg } from '@/app/sg';
import type { PlayInfo } from '@/app/api/apiModel';
import { cid2xy } from '@/app/constant';
import type { TableDataRowDef } from '@/app/utils/tableHelper';
import { CfgStr } from '@/app/cfg';
import PBtn from '../../PBtn.vue';
const { sg, ctx, res, btnRed } = inject('sg') as Sg
const headers = ref<any[]>([
    { styles: {}, key: 'name', content: '城池', width: 115 },
    { styles: {}, key: 'position', content: '位置', width: 115 },
    { styles: {}, key: 'people', content: '人口' },
])
const datas = ref<any[]>([])
// const play = ref<PlayInfo>()
const states: any = { 0: '正常', 1: '新手', 2: '免战' }
const keys = ['君主', '官职', '爵位', '所属联盟', '排名', '声望', '城池数', '人口总数', '将领数', '状态', '旗号']
const uptName = new CfgStr("K:funpanl#play_king_change_name;T:I_BTN;S:195,7,55,26,4;RFI:common#btn_green;ACT:changePlayName;TXT:T:更改,F:13,C:#e1ea73,;")
const changeTitle = new CfgStr("K:funpanl#play_king_change_flag;T:I_BTN;S:195,36,55,26,4;RFI:common#btn_gold;ACT:changePlayFlag;TXT:T:称号,F:13,C:#fff,;")
const uptFlag = new CfgStr("K:funpanl#play_king_change_flag;T:I_BTN;S:160,298,55,26,4;RFI:common#btn_green;ACT:changePlayFlag;TXT:T:更改,F:13,C:#e1ea73,;")
const vals = ref<any[]>([])
onMounted(() => {
    const play = ctx.play
    let peopleCount = 0;
    play?.cities.forEach(c => {
        const { x, y } = cid2xy(c.id)
        peopleCount += (c.people || 0)
        datas.value.push({ id: c.id, name: c.name, people: `${c.people || 0}`, position: `(${x},${y})` })
    })


    let i = 0;
    vals.value.push({ v: play?.name || '', s: `85,${i++ * 29 + 7},105,26`, sly: {} })
    vals.value.push({ v: play?.playerOffice?.name || '', s: `85,${i++ * 29 + 7},105,26`, sly: {} })
    vals.value.push({ v: play?.playerNobility?.name || '', s: `85,${i++ * 29 + 7},165,26`, sly: {} })
    vals.value.push({ v: play?.unionName || '', s: `85,${i++ * 29 + 7},165,26`, sly: {} })
    vals.value.push({ v: play?.playerRank || '0', s: `85,${i++ * 29 + 7},165,26`, sly: {} })
    vals.value.push({ v: play?.prestige || '0', s: `85,${i++ * 29 + 7},165,26`, sly: {} })
    vals.value.push({ v: play?.cities.length || '0', s: `85,${i++ * 29 + 7},165,26`, sly: {} })
    vals.value.push({ v: peopleCount, s: `85,${i++ * 29 + 7},165,26`, sly: {} })
    vals.value.push({ v: '0', s: `85,${i++ * 29 + 7},165,26`, sly: {} }) //heros count
    vals.value.push({ v: states[play?.state || 1], s: `85,${i++ * 29 + 7},65,26`, sly: { color: '#0f0', textAlign: 'center', fontWeight: 700 } })
    vals.value.push({ v: play?.flagChar || '', s: `85,${i++ * 29 + 7},65,26`, sly: { color: '#0f0', textAlign: 'center', fontWeight: 700 } })

})

async function changeCity({ row }: { row: TableDataRowDef }) {
    const cityId = row.source.id
    sg.ctx.dataMgr.set("playing#player_lastCity", cityId)
    await sg.ctx.playMgr.refreshPlay()
    ctx.funPanMgr.setComp()
}

</script>

<template>
    <div ref="inEl" class="playerinfo-king">
        <div v-size="`0,20,640,350`" class="sg-comm">
            <div v-size="`10,10,260,330`" class="sg-comm">
                <template v-for="c, idx in keys">
                    <div v-size="`10,${idx * 29 + 7},70,26`" class="sg-comm text">{{ c }}</div>
                </template>
                <template v-for="c, idx in vals">
                    <div v-size="c.s" class="sg-comm textv" :style="c.sly">{{ c.v }}</div>
                </template>
                <PBtn :cfg="uptName" />
                <PBtn :cfg="changeTitle" />
                <PBtn :cfg="uptFlag" />
            </div>
            <Table v-size="`279,10,352,330`" @change="changeCity" :w="352" :h="330" :headers="headers" :datas="datas" />
        </div>
    </div>
</template>

<style lang="less" scoped>
.playerinfo-king {
    position: absolute;
    width: 100%;
    height: 100%;
    user-select: none;

    .text {
        text-align: center;
        color: #01c8ed;
        line-height: 20px;
        font-weight: 700;
    }

    .textv {
        text-align: left;
        line-height: 20px;
        padding-left: 5px;
        color: #fff;
    }
}
</style>
