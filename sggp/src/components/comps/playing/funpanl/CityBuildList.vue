<script lang="ts" setup>
import type { SanGuo } from '@/app/sg';
import { computed, inject, onMounted, ref, watch } from 'vue';
import Scroll from '../../Scroll.vue';
import PBtn from '../../PBtn.vue';
import CityBuildItem from './CityBuildItem.vue';
import { CfgStr } from '@/app/cfg';
import Text from '../../Text.vue';
import type { CityBuildingCfg } from '@/app/api/apiModel';
import { reg } from '@/app/action';
const datas = ref<any[]>([])
const inner = ref(false)

const { sg } = inject('sg') as { sg: SanGuo }
const builds = ref<CityBuildingCfg[]>([])
const buildState = ref<Record<number, boolean>>({})
const buildItem = ref({ bid: 0, pos: 0, inner: false })
const showOnly = ref(false)
onMounted(async () => {
    const bs = []
    const buildingsMap = sg.ctx.gCfgMgr.cfg!.buildingsMap
    for (const k in buildingsMap) {
        if (inner && buildingsMap[k].place == 1) {
            // 城内建筑
            if (buildingsMap[k].id != 20 && buildingsMap[k].id != 6
            ) {
                const only = sg.ctx.gCfgMgr.isOnlyBuild(buildingsMap[k].id)
                if (!only || only && sg.ctx.play?.city.buildings.findIndex(b => b.bid === buildingsMap[k].id) === -1) {
                    bs.push(buildingsMap[k])
                }
            }
        } else if (!inner && buildingsMap[k].place == 0) {
            // 城外建筑
            bs.push(buildingsMap[k])
        }
    }
    builds.value = bs


    buildItem.value = sg.ctx.dataMgr.getByKey('fun_pan#city_builds_params')
    inner.value = buildItem.value.inner
    // console.log("建筑位置: ", buildItem.value)
    sg.ctx.dataMgr.subscribe('player#time', undefined, updateBuildState)
    await updateBuildState()
    sg.ctx.dataMgr.subscribeValue('funpanl#build_list_canOnly_sct', showOnly)
})

const updateBuildState = async (key?: string, newValue?: any, oldValue?: any) => {
    if (sg.ctx.playMgr.play && sg.ctx.playMgr.play.city) {
        const res = sg.ctx.playMgr.play.city.res
        const r = await sg.ctx.buildMgr.canBuilds(builds.value)
        buildState.value = {}
        r.forEach(b => {
            console.log("bid ", b.bid, b.can(res))
            buildState.value[b.bid] = b.can(res)
        })
    }
}

const sbs = computed(() => {
    return showOnly.value ? builds.value.filter(b => buildState.value[b.id]) : builds.value
})

const sctBtn = new CfgStr("K:funpanl#build_list_canOnly;T:I_BTN;S:0,400,25,24,11;RFI:common#btn_osct;SCTABLE:TRUE;")
const txt = new CfgStr("K:funpanl#build_list_canOnly_txt;T:TEXT;S:30,405,60,22,11;TXT:T:显示可建造的,F:12,C:#FFDA99,;")

reg('createBuildFunPan', async ({ cfg }) => {
    try {
        const bk: string = cfg!.key() as string
        const bid = bk.substring(bk.lastIndexOf('_') + 1);
        await sg.ctx.playMgr.op('upgradeBuilding', {
            pos: buildItem.value.pos,
            bId: Number(bid),
        })
        sg.ctx.funPanMgr.setComp('')
    } catch (error) {

    }


})

</script>
<template>
    <div class="fun-main-city-builds">
        <Scroll :scroll="'scroll'" class="main-scroll  bor">
            <div class="builds">
                <CityBuildItem v-for="b in sbs" :build="b" :key="b.id" :can="buildState[b.id]" />
            </div>
            <PBtn :cfg="sctBtn" />
            <Text :cfg="txt" />
        </Scroll>
    </div>
</template>
<style lang="less" scoped>
.fun-main-city-builds {
    position: absolute;
    width: 100%;
    height: 100%;

    .main-scroll {
        width: 100%;
        height: 390px;
        background-color: #303030a0;
        // width: calc(100% - 12px);
        // height: 370px;
        // &::before{
        //     content: '';
        //     position: absolute;
        //     left: 0;
        //     top: 0;
        //     width: 100%;
        //     height: 370px;
        //     z-index: 12;

        //     border-top: 10px solid #303030a0;
        //     border-bottom: 10px solid #303030a0;
        // }

        .builds {
            position: relative;
            padding: 10px;

            .build {
                width: 100%;
                height: 70px;
                // background-color: red;
                position: relative;
                margin-bottom: 5px;
                display: flex;
                color: #ffffffd5;
                background-color: #203030a0;
                border: 1px solid #203030;

                .img {
                    width: 72px;
                    min-width: 72px;
                    margin-left: 8px;
                    height: 60px;
                    margin-top: 5px;
                    background-repeat: no-repeat;
                    background-size: 103px 79px;
                    background-position-x: -15px;
                    background-position-y: -12px;
                    overflow: hidden;
                    background-color: #203030a0;
                    box-shadow: 0px 0px 0px 0.5px #80867C30, 0px 0px 0px 1px #7D6F6630;
                    border: 1px solid #203030;
                }

                .info {
                    margin-left: 8px;
                    width: 60px;
                    min-width: 60px;
                    height: 100%;
                    position: relative;
                    user-select: none;

                    .name {
                        font-weight: 600;
                        font-size: 12px;
                        height: 40px;
                        line-height: 40px;
                        text-align: center;
                    }

                }

                .msg {
                    flex: 1;
                    font-size: 12px;
                    margin: 8px;
                    height: 46px;
                    overflow-y: auto;
                    padding: 5px;
                    background-color: #103030a0;
                    box-shadow: 0px 0px 0px 0.5px #80867C30, 0px 0px 0px 1px #7D6F6630;
                    border: 1px solid #202330;

                    &::-webkit-scrollbar {
                        width: 3px;
                        height: 3px;
                        background-color: #252526;
                    }

                    &::-webkit-scrollbar-thumb {
                        border-radius: 1psx;
                        background: #cccccc33;
                    }
                }
            }
        }
    }
}
</style>