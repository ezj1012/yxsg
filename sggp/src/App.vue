<script setup lang="ts">
import { ref } from 'vue';
import SgFrame from './components/SgFrame.vue'
import sgGame, { SanGuo } from './app/sg';

const sgEl = ref()
const sg = new SanGuo(sgEl)
sg.debug.value = true
sgGame.sg.value = sg
</script>

<template>
  <div class="main">
    <SgFrame ref="sgEl" v-model="sg" />
  </div>
</template>

<style lang="less">
.img {
  background-color: transparent;
  background-repeat: no-repeat;
  background-size: 100% 100%;
}

.main {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  position: relative;
  // 底板,城池信息之类的.
  --base-pan-z: 100;
  // 背景,文字
  --front-pan-z: 400;
  // 按钮层
  --btn-pan-z: 500;
  // 军队啊.
  --fun-pan-z: 600;
  // 购买宝物弹出框,使用宝物弹出框
  --pop-pan-z: 700;
  // 确认干什么的通知框
  --aff-pan-z: 800;
  // 异常,通知框
  --msg-pan-z: 900;

  /** */
  /* 绿色 */
  --green: #21fc2b;
  /* 浅蓝色 */
  --light-blue: #00d7ff;
  /* 蓝色 */
  --blue: #2f98fa;
  /* 橙色 */
  --orange: #fc7e21;
  /* 红色 */
  --red: #f43838;
  /* 金色 */
  --gold: #e5c21e;

  --rxsg-table-header-font-color: #2ebdd7;
  --rxsg-table-row-bg-color: #182828d9;
  --rxsg-table-row-hover-bg-color: #2e4e54;
  --rxsg-table-row-select-bg-color: var(--rxsg-table-row-hover-bg-color)
}

.bor {
  box-shadow: 0px 0px 0px 0.5px #80867C inset, 0px 0px 0px 1px #7D6F66 inset,
}

.scroll_container {
  &::-webkit-scrollbar-thumb {
    width: 16px;
    background: var(--track-bar) no-repeat;
    background-position-x: 4px;
  }

  &::-webkit-scrollbar-thumb:hover {
    background: var(--track-bar-hover) no-repeat;
    background-position-x: 4px;
  }

  &::-webkit-scrollbar {
    width: 20px;
    background: var(--track-img);
    background-position: 1px 1px;
    background-color: #373530fe;
  }

  &::-webkit-scrollbar-button:decrement {
    height: 20px;
    background: var(--track-up) no-repeat scroll 3px 1px;

    &:active {
      background: var(--track-up-down) no-repeat scroll 3px 1px;
    }
  }

  &::-webkit-scrollbar-button:increment {
    height: 20px;
    background: var(--track-down-up) no-repeat scroll 3px 1px;

    &:active {
      background: var(--track-down) no-repeat scroll 3px 1px;
    }
  }
}
</style>
