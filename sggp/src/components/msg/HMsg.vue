<script setup lang="ts">
import { onMounted, ref, shallowRef, inject, computed, watch, h, type Component } from 'vue';

import SimpleMsg from './msgs/SimpleMsg.vue';
// import ChatHoverMsg from './msgs/ChatHoverMsg.vue';
import type { SanGuo } from '@/app/sg';
// import LeftCityResRock from './msgs/LeftCityResRock.vue';
// import LeftCityResIron from './msgs/LeftCityResIron.vue';
// import LeftCityResWood from './msgs/LeftCityResWood.vue';
// import LeftCityResFood from './msgs/LeftCityResFood.vue';
// import LeftCityMorale from './msgs/LeftCityMorale.vue';
// import LeftCityGoldMsg from './msgs/LeftCityGoldMsg.vue';
// import LeftCityPeopleMsg from './msgs/LeftCityPeopleMsg.vue';
// import BuildTileMsg from './msgs/BuildTileMsg.vue';
// import GoodsMsg from './msgs/GoodsMsg.vue';
const msgComps: { [key: string]: Component } = {
  // ChatHoverMsg: ChatHoverMsg,
  // CompactSimpleMsg: CompactSimpleMsgVue,
  // ProvinceMapMsg: ProvinceMapMsg,
  // LeftCityMorale: LeftCityMorale,
  // LeftCityGoldMsg: LeftCityGoldMsg,
  // LeftCityPeopleMsg: LeftCityPeopleMsg,
  // LeftCityResFood: LeftCityResFood,
  // LeftCityResWood: LeftCityResWood,
  // LeftCityResIron: LeftCityResIron,
  // LeftCityResRock: LeftCityResRock,
  // BuildTileMsg: BuildTileMsg,
  // GoodsMsg: GoodsMsg,
  // LeftCityArmyMsg: LeftCityArmyMsg,
  // LeftCityDefenceMsg: LeftCityDefenceMsg,
  // LeftCityHeroMsg: LeftCityHeroMsg,
  // LeftCityPeopleMsg: LeftCityPeopleMsg,
  // LeftCityPeopleMsg: LeftCityPeopleMsg,
}
const { sg } = inject('sg') as { sg: SanGuo }
const hmsg = ref<HTMLDivElement>()
const emptyEl = h('div')
const comp = shallowRef()
const show = ref(false)
const clientX = ref(0)
const clientY = ref(0)
const pw = ref(0)
const ph = ref(0)

onMounted(() => {
  document.addEventListener('resize', () => refreshRect())
  refreshRect()
})

function refreshRect() {
  const rect = hmsg.value!.parentElement!.getBoundingClientRect();
  clientX.value = rect.left
  clientY.value = rect.top
  pw.value = rect.width
  ph.value = rect.height
  changeComp()
}

watch(sg.dataMgr.hoverMsg, changeComp)
function changeComp() {
  var t = sg.dataMgr.hoverMsg.value
  if (!t || !t.content) {
    show.value = false
    comp.value = emptyEl
    return;
  }
  show.value = true


  let tempComp = t.content.type ? msgComps[t.content.type] : msgComps[t.content.constructor.name]
  console.log(`hover msg : `, t.content, tempComp)
  if (!tempComp) {
    tempComp = SimpleMsg
  }
  comp.value = tempComp
}

const customStyle = computed(() => {
  const hoverMsg = sg.dataMgr.hoverMsg
  if (hoverMsg.value) {
    let x = hoverMsg.value.clientX - clientX.value
    let y = hoverMsg.value.clientY - clientY.value
    const curRect = hmsg.value?.getBoundingClientRect()!;
    let right = true, bottom = true
    if (x + curRect.width > pw.value - 25) {
      right = false
    }
    if (y + curRect.height > ph.value - 30) {
      bottom = false
    }
    x = right ? x + 15 : x - 10 - curRect.width
    y = bottom ? y + 20 : y - curRect.height

    const s: any = {}
    s.left = `${x}px`
    s.top = `${y}px`
    return s
  }
  return {}
})
</script>
<template>
  <div ref="hmsg" v-show="show" :style="customStyle" class="comm_msg">
    <KeepAlive>
      <component style="user-select: none;" :is="comp"></component>
    </KeepAlive>
  </div>
</template>
<style lang="less" scoped>
.comm_msg {
  position: absolute;
  pointer-events: none;
  z-index: 30000;
  background-color: #000;
  box-shadow: 0 0 0px 1px #000;
  border: 1px solid #6c6864;
  border-radius: 2px;
  padding: 1px;
}
</style>