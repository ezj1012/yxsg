<script setup lang="ts">
import { CompMsg } from '@/app/msg/hoverMsgMgr';
import type { SgRes } from '@/app/res';
import type { SanGuo, SgCtx } from '@/app/sg';
import { computed, inject, onMounted, onUnmounted, ref, type Ref } from 'vue';
import Text from '../../Text.vue';
import { CfgStr } from '@/app/cfg';
import PBtn from '../../PBtn.vue';
import Scroll from '../../Scroll.vue';
import type { SoldierCfg } from '@/app/api/apiModel';
const { sg, ctx, res } = inject('sg') as { sg: SanGuo, ctx: SgCtx, res: SgRes }
const datas = ref<Record<string, { img: string, id: number, msg: any }>>({})

const armys = ref<SoldierCfg[]>([])
const counts = ref<Record<string, number>>({})
onMounted(() => {
    sg.ctx.dataMgr.subscribe('player#time', undefined, updateCount)
    armys.value = ctx.gCfgMgr.getCitySoldiers();
    armys.value.forEach(s => {
        datas.value[s.id] = {
            img: res.img(`playing#comm#army_${s.id}`),
            id: s.id,
            msg: new CompMsg('LeftCityArmyMsg', { id: s.id }),
        }
    })
})


function updateCount() {
    const cs = {} as any
    ctx.play?.city.soldiers.forEach(s => {
        cs[s.id] = s.count
    })
    counts.value = cs
}

onUnmounted(() => {
    sg.ctx.dataMgr.unsubscribe('player#time', updateCount)
})





</script>

<template>
    <!-- :style="{ left: `${cfgV.x}px`, top: `${cfgV.y}px`, zIndex: cfgV.z }" -->
    <div class="inner-pan-army">
        <Scroll class="scroll12" :max-height="283" scroll="scroll">
            <div class="armys">
                <!---->
                <div class="army" v-for="army in armys" :key="army.id">
                    <div v-msg="datas[army.id].msg" class="icon" :style="{ backgroundImage: datas[army.id].img }"></div>
                    <div class="info">
                        <div class="txt">{{ army.name }}</div>
                        <div class="count stant-count ">{{ counts[army.id] || 0 }}</div>
                    </div>
                </div>
            </div>
        </Scroll>
    </div>
</template>

<style lang="less" scoped>
.inner-pan-army {
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

    .armys {
        width: 100%;
        height: 100%;
        margin-top: 5px;

        .army {
            width: 112px;
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
                width: 62px;
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
        left: 6px;
        top: 28px;
        width: 246px;
        height: 250px;
        background-color: transparent;
    }

}
</style>
