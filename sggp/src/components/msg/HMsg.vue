<script setup lang="ts">
import type { SanGuo, SgCtx } from '@/app/sg';
import { computed, h, inject, onMounted, provide, ref, shallowRef, watch, type Component } from 'vue';
import SimpleMsg from './msgs/SimpleMsg.vue';
import type { HoverMsg } from '@/app/commModel';

const msgComps: { [key: string]: Component } = {

}


const { sg, ctx } = inject('sg') as { sg: SanGuo, ctx: SgCtx }
const hoverMsg = ref<HoverMsg>()
provide('msg', { hmgs: hoverMsg })
const hmsg = ref()
const emptyEl = h('div')
const comp = shallowRef()

const show = ref(false)
const clientX = ref(0)
const clientY = ref(0)
const pw = ref(0)
const ph = ref(0)

onMounted(() => {
  document.addEventListener('resize', refreshRect)
  refreshRect()
  ctx.hoverMsgMgr.subscribeValue(hoverMsg)
})

watch(hoverMsg, changeComp)

function changeComp() {
  var t = hoverMsg.value
  if (!t || !t.content) {
    show.value = false
    comp.value = emptyEl
    show.value = false
    return;
  }
  show.value = true


  let tempComp = t.content.type ? msgComps[t.content.type] : msgComps[t.content.constructor.name]
  // console.log(`hover msg : `, t.content, tempComp)
  if (!tempComp) {
    tempComp = SimpleMsg
  }
  comp.value = tempComp
}

function refreshRect() {
  const rect = hmsg.value!.parentElement!.getBoundingClientRect();
  clientX.value = rect.left
  clientY.value = rect.top
  pw.value = rect.width
  ph.value = rect.height
  changeComp()
}


const customStyle = computed(() => {
  const hMsg = hoverMsg.value
  if (hMsg) {
    let x = hMsg.clientX - clientX.value
    let y = hMsg.clientY - clientY.value
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