<script setup lang="ts">
import { CompMsg } from '@/app/msg/hoverMsgMgr';
import type { SgRes } from '@/app/res';
import type { SanGuo, SgCtx } from '@/app/sg';
import { heroStates } from '@/app/constant';
import { computed, inject, onMounted, onUnmounted, ref, type Ref } from 'vue';
import type { CityHero, DefenceCfg, SoldierCfg } from '@/app/api/apiModel';
import Scroll from '../../Scroll.vue';
const { sg, ctx, res } = inject('sg') as { sg: SanGuo, ctx: SgCtx, res: SgRes }

const heros = ref<CityHero[]>([])
const datas = ref<Record<string, { img: string, id: number, msg: any }>>({})
onMounted(() => {
    sg.ctx.dataMgr.subscribe('player#time', undefined, updateHeros)
    updateHeros()
})


function updateHeros() {
    heros.value = ctx.play?.city.heros || []
    heros.value.forEach(hero => {
        datas.value[hero.id] = {
            img: res.img(`playing#comm#army_${hero.id}`),
            id: hero.id,
            msg: new CompMsg('LeftCityHeroMsg', { id: hero.id }),
        }
    })
}

onUnmounted(() => {
    sg.ctx.dataMgr.unsubscribe('player#time', updateHeros)
})

</script>

<template>
    <div class="inner-pan-hero">
        <Scroll class="scroll12" :max-height="283" scorll="scorll">
            <div class="heros">
                <div class="hero" v-for="hero in heros" :key="hero.id">
                    <div v-msg="datas[hero.id].msg" class="icon" :style="{ backgroundImage: datas[hero.id].img }"></div>
                    <div class="info">
                        <div class="txt">{{ hero.name }}</div>
                        <div class="count">
                            <div class="lv">{{ `${hero.level}级` }} </div>
                            <div class="st">{{ heroStates[hero.state] }}</div>
                        </div>
                    </div>
                </div>
            </div>
        </Scroll>
    </div>
</template>

<style lang="less" scoped>
.inner-pan-hero {
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

    .heros {
        width: 100%;
        height: 100%;
        margin-top: 5px;

        .hero {
            width: 112px;
            height: 40px;
            padding: 2px 0 2px 4px;
            float: left;
            position: relative;

            .icon {
                width: 34px;
                height: 36px;
                box-shadow: 0px 0px 0px .5px #7E6A49 inset, 0px 0px 0px 1px #111A00 inset,
                    0px 0px 0px 1.5px #271B25 inset;
                background-repeat: no-repeat;
                background-position: 0px 2px;
                background-color: #3228;
            }

            .info {
                position: absolute;
                width: 72px;
                height: 36px;
                left: 40px;
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
                    top: 15px;
                    font-size: 12px;
                    line-height: 22px;
                    text-align: left;
                    transform: scale(.9);
                    display: flex;
                }

                .lv {
                    color: #ffffff;
                    font-size: 12px;
                    width: 30px;
                    flex: 1;
                }

                .st {
                    top: 13px;
                    color: #00dbff;
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
