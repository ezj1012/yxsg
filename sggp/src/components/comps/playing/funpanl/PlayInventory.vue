<script setup lang="ts">
import { CompMsg } from '@/app/msg/hoverMsgMgr';
import type { SgRes } from '@/app/res';
import type { SanGuo, Sg, SgCtx } from '@/app/sg';
import { heroStates } from '@/app/constant';
import { computed, inject, onMounted, onUnmounted, ref, type Ref } from 'vue';
import type { CityBuilding, CityHero, DefenceCfg, PlayGoods, SoldierCfg } from '@/app/api/apiModel';
import Scroll from '../../Scroll.vue';
import type { FunPanComp } from '@/app/commModel';
import { CfgStr } from '@/app/cfg';
import PBtn from '../../PBtn.vue';
import { reg } from '@/app/action';
import Text from '../../Text.vue';
const { sg, ctx, res, btnRed } = inject('sg') as Sg
const goodsType = ref(0)
const page = ref({
    pageNum: 1,
    totalPages: 0,
})
const datas = ref<PlayGoods[]>([])
const showPage = computed(() => page.value.totalPages > 1)
const showUse = ref(false)
const usePoint = ref({ x: 0, y: 0 })
const marketLv = ref(0)
onMounted(() => {
    marketLv.value = ctx.buildMgr.marketLv()
    reg('inventoryChangeType', async ({ sg, cfg }) => {
        const type = cfg!.key().split("_")[1]
        const gt = Number(type)
        if (goodsType.value != gt) {
            page.value.pageNum = 1
            page.value.totalPages = 0
        }
        goodsType.value = gt

        await getPlayGoods()
    })
    reg('inventoryChangePage', async ({ sg, cfg }) => {
        const isNextPage = cfg?.key() === 'funpanl#inventory_page_next'
        if (isNextPage) {
            page.value.pageNum < page.value.totalPages && (page.value.pageNum += 1)
        } else {
            page.value.pageNum > 1 && (page.value.pageNum -= 1)
        }
        await getPlayGoods()
    })
})

async function getPlayGoods() {
    const goods = await ctx.api.playApi.goods(page.value.pageNum, 21, goodsType.value)
    const cfgs = await res.globarMgr.getGoods(goods.data.map(g => g.goodsId));
    goods.data.forEach(g => {
        g.cfg = cfgs[g.goodsId]
        g.img = `url(${res.globarMgr.goodsImg(g.cfg)})`
    });
    // console.log(goods.data[0])
    page.value.pageNum = goods.pageNum
    page.value.totalPages = goods.totalPages
    datas.value = goods.data
}


const btns = ref([
    new CfgStr("K:funpanl#inventory_0;T:I_BTN;S:0,20,60,32,4;RFI:common#btn_red;ACT:inventoryChangeType;TXT:T:宝物类,F:13,C:#e1ea73,;"),
    new CfgStr("K:funpanl#inventory_1;T:I_BTN;S:64,20,60,32,4;RFI:common#btn_red;ACT:inventoryChangeType;TXT:T:加速类,F:13,C:#e1ea73,;"),
    new CfgStr("K:funpanl#inventory_2;T:I_BTN;S:128,20,60,32,4;RFI:common#btn_red;ACT:inventoryChangeType;TXT:T:生产类,F:13,C:#e1ea73,;"),
    new CfgStr("K:funpanl#inventory_3;T:I_BTN;S:192,20,60,32,4;RFI:common#btn_red;ACT:inventoryChangeType;TXT:T:古匣类,F:13,C:#e1ea73,;"),
    new CfgStr("K:funpanl#inventory_4;T:I_BTN;S:256,20,60,32,4;RFI:common#btn_red;ACT:inventoryChangeType;TXT:T:材料类,F:13,C:#e1ea73,;"),
    new CfgStr("K:funpanl#inventory_5;T:I_BTN;S:320,20,60,32,4;RFI:common#btn_red;ACT:inventoryChangeType;TXT:T:任务类,F:13,C:#e1ea73,;"),
    new CfgStr("K:funpanl#inventory_6;T:I_BTN;S:384,20,60,32,4;RFI:common#btn_red;ACT:inventoryChangeType;TXT:T:技能书,F:13,C:#e1ea73,;"),
])

const yuanbaoCfg = new CfgStr("K:funpanl#inventory_text_yuanbao;T:TEXT;S:0,400,38,26,12;TXT:T:元宝,F:13,C:#01caef,;SMSG:元宝可以购买各种强大道具;")
const yuanbaoCountCfg = new CfgStr("K:playing#player_money;T:TEXT;S:45,400,110,26,12;TXT:T:,F:13,C:#e1ea73,;")
const lijinCfg = new CfgStr("K:funpanl#inventory_text_lijin;T:TEXT;S:160,400,38,26,12;TXT:T:礼金,F:13,C:#01caef,;SMSG:礼金是系统赠送的;")
const lijinCountCfg = new CfgStr("K:playing#player_money;T:TEXT;S:205,400,110,26,12;TXT:T:,F:13,C:#e1ea73,;")
const goShopcfg = new CfgStr("K:funpanl#inventory;T:I_BTN;S:340,395,72,32,4;RFI:common#btn_red;ACT:inventoryChangeType;TXT:T:购买宝物,F:13,C:#e1ea73,;")

const prePageCfg = new CfgStr("K:funpanl#inventory_page_pre;T:I_BTN;S:512,20,60,32,4;RFI:common#btn_red;ACT:inventoryChangePage;TXT:T:上一页,F:13,C:#e1ea73,;")
const nextPageCfg = new CfgStr("K:funpanl#inventory_page_next;T:I_BTN;S:582,20,60,32,4;RFI:common#btn_red;ACT:inventoryChangePage;TXT:T:下一页,F:13,C:#e1ea73,;")

const inEl = ref<HTMLDivElement>()
onMounted(async () => {
    await getPlayGoods()
    window.addEventListener('click', aa)
})
function aa() {
    showUse.value = false
}
const sctGoods = ref<PlayGoods>()
function openUsePan(g: PlayGoods, e: MouseEvent) {
    showUse.value = true
    sctGoods.value = g
    const pr = inEl.value?.getBoundingClientRect()!

    const r = (e.target as HTMLDivElement).getBoundingClientRect()
    usePoint.value = {
        x: r.left - pr.left + e.offsetX - 5,
        y: r.top - pr.top + e.offsetY - 5
    }
}

onUnmounted(() => {
    window.removeEventListener('click', aa)
})
const useStyle = computed(() => {
    const s = {} as any
    s['left'] = `${usePoint.value.x}px`
    s['top'] = `${usePoint.value.y}px`
    return s
})

async function useGoods(type: number) {
    if (!sctGoods.value) {
        return
    }

    const g = sctGoods.value
    const gc = g.cfg!
    console.log(g.cfg)

    if (type === 0) {
        // use
        if (gc.autoUse === 0) {
            sg.ctx.errMsgMgr.pushMsg(gc.useGuide)
            return
        } else {
            sg.ctx.errMsgMgr.pushMsg({
                content: `<div>${gc.description}</div><div>是否使用1个 ${gc.name}?</div>`,
                ok: async () => {
                    try {
                        await ctx.api.playApi.op('use_goods', ctx.play!.lastCity, {
                            count: 1
                        })
                    } catch (err) {

                    }
                },
                cancel: async () => { }
            })
            return
        }
    } else if (type === 3) {
        if (gc.batch === 0) {
            sg.ctx.errMsgMgr.pushMsg("该道具不能进行合成操作！")
            return
        }
    } else {
        if (type === 1 && gc.batch === 0) {
            sg.ctx.errMsgMgr.pushMsg("该物品不能批量使用！")
            return
        }
        if (ctx.buildMgr.marketLv() < 5) {
            sg.ctx.errMsgMgr.pushMsg("市场达到5级批量使用功能才开启。")
            return
        }

        //TODO 弹出框框
        // sg.ctx.

    }

}

</script>

<template>
    <div ref="inEl" class="playerinfo-inventory">
        <template v-for="cfg, idx in btns" :key="cfg.key()">
            <PBtn :cfg="cfg" :style="{ filter: idx == goodsType ? 'brightness(1.4)' : '' }" />
        </template>
        <div v-show="showPage" v-size="`560,-16,80,28,10`" class="sg-comm page-info">
            {{ `${page.pageNum}/${page.totalPages}` }}
        </div>
        <PBtn v-show="showPage" :cfg="prePageCfg" />
        <PBtn v-show="showPage" :cfg="nextPageCfg" />

        <div class="playerinfo-inventory-main sg-comm sg-scroll">
            <div v-for="g in datas" v-msg="{
                content: {
                    type: 'GoodsMsg',
                    msg: g
                }
            }" class="grid-item" :key="g.cfg?.id" @click.stop="openUsePan(g, $event)">
                <div class="sg-comm " style="width: 78px;height: 90px;">
                    <div v-size="`5,4,68,68,1`" class="sg-comm ">
                        <div v-size="`2,2,64,64,1`" :style="{ backgroundImage: g.img }"></div>
                    </div>
                    <div v-size="`7,54,64,18,10`" class="count">{{ g.count }}</div>
                    <div class="name">{{ g.cfg?.name }}</div>
                </div>
            </div>
        </div>
        <div class="inventory-use" v-show="showUse" :style="useStyle">
            <div class="use-item" @click="useGoods(0)">使用</div>
            <div class="use-item" v-show="marketLv > 5" @click="useGoods(1)">批量</div>
            <div class="use-item" v-show="marketLv > 5" @click="useGoods(2)">出售</div>
            <div class="use-item" v-show="sctGoods && sctGoods.cfg?.compose === 1" @click="useGoods(3)">合成</div>
        </div>
        <Text :cfg="yuanbaoCfg" class="sg-comm" style="text-align: center;font-weight: 800;line-height: 22px;" />
        <Text :cfg="yuanbaoCountCfg" class="sg-comm" style="line-height: 22px;padding-left: 10px;" />
        <Text :cfg="lijinCfg" class="sg-comm" style="text-align: center;font-weight: 800;line-height: 22px;" />
        <Text :cfg="lijinCountCfg" class="sg-comm" style="line-height: 22px;padding-left: 10px;" />
        <PBtn :cfg="goShopcfg" />
    </div>
</template>

<style lang="less" scoped>
.playerinfo-inventory {
    position: absolute;
    width: 100%;
    height: 100%;
    background-color: #ffffff04;
    user-select: none;

    .inventory-use {
        position: absolute;
        width: 70px;
        // height: 94px;
        border: 1px solid #735942;
        box-sizing: border-box;
        background-color: #1b241e;
        z-index: 100;

        .use-item {
            text-align: center;
            color: #e1ea73;
            font-size: 12px;
            height: 23px;
            line-height: 23px;

            &:hover {
                filter: brightness(1.4);
                background-color: #4448;
            }
        }
    }

    .page-info {
        font-size: 13px;
        font-weight: 700;
        text-align: center;
        line-height: 23px;
        color: #e1ea73;
    }

    .playerinfo-inventory-main {
        width: 100%;
        height: 330px;
        top: 60px;
        left: 0px;
        display: grid;
        grid-template-columns: repeat(7, 78px);
        /* 每行 7 个格子 */
        grid-auto-rows: 90px;
        /* 每个格子高度为 90px */
        gap: 10px;
        /* 可选：间距 */
        padding: 18px;
        overflow-y: auto;

        /* 需要时添加滚动条 */
        .grid-item {
            width: 78px;
            height: 90px;
            box-sizing: border-box;
            display: flex;
            align-items: center;
            justify-content: center;
            position: relative;

            .count {
                text-align: right;
                color: #fff;
                // -webkit-text-stroke: 0.2px #000;
            }

            .name {
                position: absolute;
                left: 0;
                right: 0;
                bottom: 0;
                height: 18px;
                text-align: center;
                color: #fff;
            }
        }
    }
}
</style>
