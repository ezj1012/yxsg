<script setup lang="ts">
import type { CityBuildingCfg } from '@/app/api/apiModel';
import type { BuildDep } from '@/app/buildMgr';
import type { HoverMsg } from '@/app/commModel';
import { formatSeconds } from '@/app/constant';
import type { SgRes } from '@/app/res';
import type { SanGuo, SgCtx } from '@/app/sg';
import { computed, inject, ref, watch, type Ref } from 'vue';
const { sg, ctx, res } = inject('sg') as { sg: SanGuo, ctx: SgCtx, res: SgRes }
const { hmgs } = inject('msg') as { hmgs: Ref<HoverMsg> }

const oldKey = ref('')
const lv = ref(1)
const bCfg = ref<CityBuildingCfg>()
const bDep = ref<BuildDep>()
const cityRes = computed(() => ctx.play?.city.res)
watch(() => hmgs.value?.content.cfg, async () => {
    refresh()
}, { immediate: true, deep: true })

async function refresh() {
    if (oldKey.value !== hmgs.value?.content.cfg && hmgs.value?.content.cfg) {
        oldKey.value = hmgs.value?.content.cfg
        // BuildTileMsg,5,1
        const sts = oldKey.value.split(',')
        const bid = Number(sts[1])
        const glv = sts[2]
        lv.value = Number(glv)
        bCfg.value = ctx.gCfgMgr.cfg?.buildingsMap[bid]
        const dep = await ctx.buildMgr.canBuild(bid, lv.value)
        bDep.value = dep
    }
}

</script>
<template>

    <div class="msg">
        <div class="ti">{{ `建造：${bCfg?.name}（等级${lv}）` }}</div>
        <div style="margin: 10px 0; "> {{ bCfg?.levels[lv].description }}</div>
        <div style="font-weight: 600;display: flex;color: #fffc;">
            <div class="p1">建造条件</div>
            <div class="p2">需要数量</div>
            <div class="p3">当前数量</div>
        </div>
        <div class="split">
        </div>
        <template v-if="bDep">
            <div class="line" v-for="i in bDep?.items" :key="i.name">
                <template v-if="i.type === 0">
                    <div class="p1">{{ i.type == 0 ? '前提建筑' : i.type == 1 ? '前提资源' : '前提科技' }}</div>
                    <div class="p2" :style="{ color: i.ok ? '' : 'red' }">{{ `${i.source.name}${i.need}级` }}</div>
                    <div class="p3">
                        {{ `${i.have}级` }}
                    </div>
                </template>
                <template v-else-if="i.type === 1">
                    <div class="p1">前提资源</div>
                    <div class="p2">{{ i.need }}</div>
                    <div class="p3" :style="{ color: i.ok ? '' : 'red' }">
                        {{ `${i.have}级` }}
                    </div>
                </template>
                <template v-else>
                    <div class="p1">前提资源</div>
                    <div class="p2">{{ i.need }}</div>
                    <div class="p3" :style="{ color: i.ok ? '' : 'red' }">
                        {{ i.have }}
                    </div>
                </template>
                <!--   CityBuildingCfg | CfgGoods | CityTechnic-->


            </div>
            <div class="line" v-show="bDep?.res?.upgradePeople !== 0">
                <div class="p1">依赖人口</div>
                <div class="p2">{{ bDep?.res?.upgradePeople }}</div>
                <div class="p3" :style="{ color: cityRes!.people < bDep!.res!.upgradePeople ? 'red' : '' }">{{
                    cityRes?.people
                }}
                </div>
            </div>
            <div class="line">
                <div class="p1">消耗粮食</div>
                <div class="p2">{{ bDep?.res?.upgradeFood }}</div>
                <div class="p3" :style="{ color: cityRes!.food < bDep!.res!.upgradeFood ? 'red' : '' }">{{ cityRes?.food
                }}
                </div>
            </div>
            <div class="line">
                <div class="p1">消耗木材</div>
                <div class="p2">{{ bDep?.res?.upgradeWood }}</div>
                <div class="p3" :style="{ color: cityRes!.wood < bDep!.res!.upgradeWood ? 'red' : '' }">{{ cityRes?.wood
                }}
                </div>
            </div>
            <div class="line">
                <div class="p1">消耗石料</div>
                <div class="p2">{{ bDep?.res?.upgradeRock }}</div>
                <div class="p3" :style="{ color: cityRes!.rock < bDep!.res!.upgradeRock ? 'red' : '' }">{{ cityRes?.rock
                }}
                </div>
            </div>
            <div class="line">
                <div class="p1">消耗铁锭</div>
                <div class="p2">{{ bDep?.res?.upgradeIron }}</div>
                <div class="p3" :style="{ color: cityRes!.iron < bDep!.res!.upgradeIron ? 'red' : '' }">{{ cityRes?.iron
                }}
                </div>
            </div>
            <div class="split"></div>
            <div class="line" style="margin-bottom: 10px;">
                <div class="p1">建造时间</div>
                <div class="p2" v-if="bDep?.res">{{ formatSeconds(bDep!.res!.upgradeTime) }}</div>
            </div>
        </template>

    </div>
</template>
<style lang="less" scoped>
.msg {
    padding: 5px 15px;
    color: rgba(255, 255, 255, 0.8);
    font-size: 12px;
    display: block;
    width: 250px;

    .line {
        display: flex;
        height: 18px;
        line-height: 18px;
    }

    .p1 {
        width: 70px;
        text-align: left;
    }

    .p2 {
        flex: 1;
        text-align: left;
    }

    .p3 {
        width: 70px;
        text-align: left;
    }

    .ti {
        font-size: 14px;
        margin-top: 8px;
    }

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
        width: 100%;
        height: 1px;
        border-bottom: solid 0.3px #ffffff71;
        margin-bottom: 3px;
    }
}
</style>