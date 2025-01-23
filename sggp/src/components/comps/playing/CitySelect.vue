<script setup lang="ts">
import { CfgStr } from '@/app/cfg';
import type { SanGuo } from '@/app/sg';
import { ref, inject, watch, computed, onBeforeUpdate, onUpdated, provide, onMounted } from 'vue';

import { cid2xy } from '@/app/constant';
import Scroll from '../Scroll.vue';
import type { CityIntro } from '@/app/modelData';

const { sg } = inject('sg') as { sg: SanGuo }
const { cfg } = defineProps({ cfg: { required: true } }) as { cfg: CfgStr }

const bg = `url(${sg.res.getImgGroup('common#title')?.hasDef()?.getDataUrl()})`
const sctBg = `url(${sg.res.getImgGroup('common#scroll_downarr')?.hasDef()?.getDataUrl()})`
const sctDownBg = `url(${sg.res.getImgGroup('common#scroll_downarr')?.hasDown()?.getDataUrl()})`

const citys = ref<CityIntro[]>([])
const cityName = computed(() => toCityShowName(citys.value.find(c => c.id == lastCityId.value)))
const lastCityId = ref<number>()
const showOptions = ref(false)

watch(() => cfg, () => {
  sg.dataMgr.subscribeValue("playing#player_lastCity", lastCityId)
  sg.dataMgr.subscribeValue("playing#player_cities", citys)
  console.log(`CitySelect: watch cfg: `, lastCityId.value, citys.value)
}, { immediate: true })

async function changeCity(city: CityIntro) {
  sg.dataMgr.set("playing#player_lastCity", city.id)
  showOptions.value = false
  await sg.refreshPlay()
}

function toCityShowName(city?: CityIntro) {
  if (!city) return ''
  const { x, y } = cid2xy(city.id)
  return `${city.name} (${x},${y})`
}

const opsStyle = computed(() => {
  const style = {} as any
  style.height = citys.value.length >= 7 ? '170px' : `${(citys.value.length) * 24 + 2}px`;
  return style
})

</script>
<template>
  <div v-size="cfg" class="city-select" :style="{ backgroundImage: bg }">
    <div class="selected" :style="{
      '--def': sctBg,
      '--active': sctDownBg,
    }" @click="() => showOptions = !showOptions">
      {{ cityName || '' }}
    </div>
    <Scroll v-show="showOptions" class="scroll" :max-height="160" :style="opsStyle" :scorll="true">
      <ul v-show="showOptions">
        <li v-for="(city, index) in citys" :key="index" :class="[city.id == lastCityId ? 'selected' : '']"
          @click.stop="changeCity(city)">
          <span> {{ toCityShowName(city) }}</span>
        </li>
      </ul>
    </Scroll>
  </div>
</template>
<style lang="less" scoped>
.city-select {
  height: 35px;
  position: absolute;
  background-repeat: no-repeat;
  text-align: center;
  color: #fff;
  line-height: 30px;
  font-size: 13px;

  .selected {
    cursor: pointer;
    width: 100%;
    height: 32px;
    overflow: hidden;
    background-repeat: no-repeat;
    // background-size: 100% 100%;
    background-position: 204px 5px;
    background-image: var(--def);
    user-select: none;

    &:active {
      cursor: pointer;
      background-image: var(--active);
    }
  }

  .scroll {
    border-left: 1px solid #147273;
    border-bottom: 1px solid #147273;
    border-top: 1px solid #147273;
    box-sizing: border-box;
    overflow-y: auto;
    overflow-x: hidden;
    user-select: none;
    z-index: 99999;
    position: absolute;
    left: 47px;
    top: 23px;
    max-height: 160px;
    background-color: #062238;
    width: 173px;

    ul {
      list-style: none;
      padding: 0px;
      margin: 0px;
      border-right: 1px solid #147273;
      color: #fff;

      li {
        height: 24px;
        line-height: 24px;

        span {
          margin: 0 1px;
          overflow: hidden;
          display: block;
        }
      }

      li:hover {
        background-color: #199ebf;
      }

      .selected {
        background-color: #002f3e;
      }
    }
  }
}
</style>