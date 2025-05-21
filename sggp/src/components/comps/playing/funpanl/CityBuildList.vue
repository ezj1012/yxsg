<script lang="ts" setup>
import type { SanGuo } from '@/app/sg';
import { computed, inject, onMounted, ref, watch } from 'vue';
import Scroll from '../../Scroll.vue';
import PBtn from '../../PBtn.vue';
import CityBuildItem from './CityBuildItem.vue';
const datas = ref<any[]>([])
const inner = ref(false)

const { sg } = inject('sg') as { sg: SanGuo }
const buildState = ref<Record<number, boolean>>({})
onMounted(async () => {
    sg.ctx.dataMgr.subscribe('player#time', undefined, updateBuildState)
    updateBuildState()
    console.log(builds.value)
})

const updateBuildState = (key?: string, newValue?: any, oldValue?: any) => {
    if (sg.ctx.playMgr.play && sg.ctx.playMgr.play.city) {
        const res = sg.ctx.playMgr.play.city.res
        const r = sg.ctx.buildMgr.canBuilds(builds.value);
        console.log(r)
    }
}

const builds = computed(() => {
    const bs = []
    const buildingsMap = sg.ctx.gCfgMgr.cfg!.buildingsMap
    for (const k in buildingsMap) {
        if (inner && buildingsMap[k].place == 1) {
            if (buildingsMap[k].id != 20 && buildingsMap[k].id != 6) {
                bs.push(buildingsMap[k])
            }
        } else if (!inner && buildingsMap[k].place == 0) {
            bs.push(buildingsMap[k])
        }
    }
    return bs
})



</script>
<template>
    <div class="fun-main-city-builds">
        <!-- <Scroll :scroll="'scroll'" class="main-scroll  bor"> -->
            <!-- <div class="builds">
                <CityBuildItem v-for="b in builds" :build="b" :key="b.id" />
            </div> -->
        <!-- </Scroll> -->
    </div>
</template>
<style lang="less" scoped>
.fun-main-city-builds {
    position: absolute;
    position: fixed;
    left: 400px;
    width: 1000px;
    height: 600px;
    background-color: #fff;

    .main-scroll {
        width: 100%;
        height: 390px;
        background-color: #303030a0;

        .builds {
            padding: 10px;
            position: relative;

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