<script setup lang="ts">
import { computed, ref, type Ref } from 'vue';

enum MoveDir {
  UP = 0,
  RIGHT = 1,
  DOWN = 2,
  LEFT = 3,
}

const { width, minWidth, direction, moveHide, show, hide, step } = defineProps({
  width: { default: 300 }, minWidth: { default: 300 }, step: { default: 110 }, direction: { default: MoveDir.RIGHT },
  moveHide: { default: false }, show: { required: false, type: Function }, hide: { required: false, type: Function }
})
const curEl = ref()
const realLen = ref(width)
const lastRealLen = ref(width)
const isShowing = ref(true)
const startFlag = ref(0)
function moveStart(e: MouseEvent) {
  let realElLen = 0
  if (direction == MoveDir.RIGHT || direction == MoveDir.LEFT) {
    startFlag.value = e.clientX
    realElLen = curEl.value!.getBoundingClientRect().width
  } else {
    startFlag.value = e.clientY
    realElLen = curEl.value!.getBoundingClientRect().height
  }
  console.log(`realElLen ${realElLen}  realLen: ${realLen.value}`)
  if (realElLen != realLen.value) { // 修复真实大小和width不一致问题.
    realLen.value = realElLen
  }

  console.log('move start')
  document.addEventListener('mouseup', moveEnd);
  document.addEventListener('mousemove', moveSplit);
  e.stopPropagation()
  e.preventDefault()
}
function moveEnd() {
  startFlag.value = 0
  document.removeEventListener('mouseup', moveEnd);
  document.removeEventListener('mousemove', moveSplit);
}
function moveSplit(e: MouseEvent) {
  e.stopPropagation()
  e.preventDefault()
  if (e.buttons && startFlag.value) {
    const rc = curEl.value!.getBoundingClientRect();
    var lenWidth = 0
    if (direction == MoveDir.RIGHT) {
      lenWidth = e.clientX - rc.x
    } else if (direction == MoveDir.LEFT) {
      lenWidth = rc.x + rc.width - e.clientX
    } else if (direction == MoveDir.UP) {
      lenWidth = rc.y + rc.height - e.clientY
    } else if (direction == MoveDir.DOWN) {
      lenWidth = e.clientY - rc.y
    }
    if (lenWidth >= minWidth) {
      realLen.value = lenWidth
    } else if (moveHide) { // 拖拽隐藏或显示
      if (lenWidth < step) {
        moveToHide()
        return
      } else if (!isShowing.value && lenWidth > step + 5) { // +5 防止手抖动
        moveToShow()
      }
    }
  }
}

function moveToHide() {
  lastRealLen.value = realLen.value
  realLen.value = 0
  isShowing.value = false
  hide && hide()
}

function moveToShow() {
  realLen.value = lastRealLen.value > minWidth ? lastRealLen.value : minWidth
  isShowing.value = true
  show && show()
}

defineExpose({ hide: moveToHide, show: moveToShow })

const splitStyle = computed(() => {
  const style: Record<string, any> = {}
  if (direction == MoveDir.RIGHT || direction == MoveDir.LEFT) {
    style.width = '5px'
    style.height = '100%'
    style.top = '0px'
    if (direction == MoveDir.RIGHT) {
      style.right = '0px'
    } else {
      style.left = '0px'
    }

    style.cursor = 'w-resize'
  } else {
    style.width = '100%'
    style.height = '5px'
    style.left = '0px'
    if (direction == MoveDir.UP) {
      style.top = '0px'
    } else {
      style.bottom = '0px'
    }

    style.cursor = 'n-resize'
  }
  return style
})

const panStyle = computed(() => {
  const style: Record<string, any> = {}
  const curMinWidth = isShowing.value ? minWidth : 0
  if (direction == MoveDir.RIGHT || direction == MoveDir.LEFT) {
    style.width = `${realLen.value}px`
    style.minWidth = `${curMinWidth}px`
    style.height = '100%'
  } else {
    style.width = '100%'
    style.height = `${realLen.value}px`
    style.minHeight = `${curMinWidth}px`
  }
  return style;
})
</script>

<template>
  <div ref="curEl" class="mgr-split" :style="panStyle">
    <slot></slot>
    <div class="split" :style="splitStyle" @mousedown="moveStart($event)"></div>
  </div>
</template>
<style lang="less" scoped>
.mgr-split {
  position: relative;

  .split {
    position: absolute;
    z-index: 1000;

    &:active::before {
      content: '';
      position: fixed;
      left: -100px;
      right: -100px;
      top: -100px;
      bottom: -100px;
    }

    &:hover {
      background-color: #5f61e767;

    }

    &:active {
      background-color: #5f61e7cb;
    }
  }
}
</style>