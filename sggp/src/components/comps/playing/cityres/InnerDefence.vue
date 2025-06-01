<script setup lang="ts">
// import Ctrl from '@/app/ctrl';
// import { CityBuildState, type PlayerCityBuild } from '@/app/model/comm';
// import { GCfg } from '@/app/res/GloabalCfgMgr';
// import ActionMgr from '@/app/stage/ActionMgr';
// import { CompType } from '@/app/stage/CompMappingCache';
// import type { CompInfo } from '@/app/stage/CompMgr';
// import type { BgVcfg, InnerPanVCfg, OutCityVcfg, ProvincesMapVcfg } from '@/app/stage/CompModel';
// import DataMgr from '@/app/stage/DataMgr';
// import type Img from '@/app/utils/Img';
// import Msg from '@/app/utils/Msg';
// import Res from '@/app/utils/Res';
// import { Content } from 'view-ui-plus';
// import Scroll from '@/components/comps/Scroll.vue';
// import { onMounted, ref } from 'vue';
// interface Defence {
//     id: number
//     lv: number
//     count: number
//     url?: string
//     msg?: any
// }
// const btnKey = 'playing#btn_left_defence_sct'
// const defencesKey = 'playing#player_city_defences'
// const { compInfo } = defineProps({ compInfo: { required: true } }) as { compInfo: CompInfo }
// const cfgV = ref<InnerPanVCfg>(compInfo.getCfg() as InnerPanVCfg)
// const show = ref(DataMgr.get(btnKey) ? true : false)
// const defences = ref<Defence[]>([])
// DataMgr.subscribe(btnKey, undefined, (key: string, newValue: any, oldValue: any) => { show.value = newValue ? true : false })
// DataMgr.subscribe(defencesKey, undefined, (key: string, newValue: any, oldValue: any) => { refreshDefences() })

// onMounted(() => {
//     refreshDefences()

// })

// function refreshDefences() {
//     const datas = DataMgr.get(defencesKey) as Defence[] || []
//     datas.forEach(d => {
//         d.url = `url(${Res.imgCfgMgr.getGroup(`playing#comm#defence_${d.id}`)?.getDef()?.getDataUrl()})`
//         d.msg = { msg: Msg.of({ type: 'LeftCityDefenceMsg', content: { id: d.id } }) }
//     })
//     defences.value = datas
// }


import { CompMsg } from '@/app/msg/hoverMsgMgr';
import type { SgRes } from '@/app/res';
import type { SanGuo, SgCtx } from '@/app/sg';
import { computed, inject, onMounted, onUnmounted, ref, type Ref } from 'vue';
import type { DefenceCfg, SoldierCfg } from '@/app/api/apiModel';
const { sg, ctx, res } = inject('sg') as { sg: SanGuo, ctx: SgCtx, res: SgRes }

const datas = ref<Record<string, { img: string, id: number, msg: any }>>({})
const defences = ref<DefenceCfg[]>([])
const counts = ref<Record<string, number>>({})
onMounted(() => {
    sg.ctx.dataMgr.subscribe('player#time', undefined, updateCount)

    for (const k in ctx.gCfgMgr.cfg?.defencesMap) {
        defences.value.push((ctx.gCfgMgr.cfg!.defencesMap as any)[k])
    }

    defences.value.forEach(s => {
        datas.value[s.id] = {
            img: res.img(`playing#comm#defence_${s.id}`),
            id: s.id,
            msg: new CompMsg('LeftCityDefenceMsg', { id: s.id }),
        }
    })
})


function updateCount() {
    const cs = {} as any
    ctx.play?.city.defences.forEach(s => {
        cs[s.id] = s.count
    })
    counts.value = cs
}

onUnmounted(() => {
    sg.ctx.dataMgr.unsubscribe('player#time', updateCount)
})

</script>

<template>
    <div class="inner-pan-defence">
        <div class="scroll12" :max-height="283" :scorll="true">
            <div class="defences">
                <div class="defence" v-for="defence in defences" :key="defence.id">
                    <div v-msg="datas[defence.id].msg" class="icon" :style="{ backgroundImage: datas[defence.id].img }">
                    </div>
                    <div class="info">
                        <div class="txt">{{ defence.name }}</div>
                        <div class="count stant-count ">{{ counts[defence.id] || 0 }}</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<style lang="less" scoped>
.inner-pan-defence {
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

    .defences {
        width: 100%;
        height: 100%;
        margin-top: 5px;

        .defence {
            width: 117px;
            height: 40px;
            padding: 2px;
            float: left;
            position: relative;
            box-sizing: border-box;

            .icon {
                width: 44px;
                height: 36px;
                box-shadow: 0px 0px 0px .5px #7E6A49 inset, 0px 0px 0px 1px #111A00 inset,
                    0px 0px 0px 1.5px #271B25 inset;
                background-repeat: no-repeat;
                background-position: 0px 2px;
                background-color: #3228;
            }

            .info {
                position: absolute;
                width: 65px;
                height: 36px;
                left: 50px;
                top: 2px;
                box-shadow: 0px 0px 0px .5px #7E6A49 inset, 0px 0px 0px 1px #111A00 inset,
                    0px 0px 0px 1.5px #271B25 inset;
                background-color: #3228;

                .txt {
                    font-size: 12px;
                    color: #c0c02d;
                    font-weight: 500;
                    position: absolute;
                    width: 100%;
                    height: 24px;
                    top: 0px;
                    left: -1px;
                    transform: scale(.9);
                }

                .count {
                    position: absolute;
                    width: calc(100% - 3px);
                    height: 24px;

                    left: 0px;
                    color: #ffffff;
                    text-align: right;
                    transform: scale(.9);
                }

                .stant-count {
                    top: 15px;
                    font-size: 15px;
                }

                .smart-count {
                    top: 12px;
                    font-size: 12px;
                    line-height: 12px;
                    word-wrap: break-word;
                    overflow-wrap: break-word;
                }
            }


        }
    }

    .scroll12 {
        position: absolute;
        left: 10px;
        top: 30px;
        width: 246px;
        height: 250px;
        background-color: transparent;
    }

}
</style>
