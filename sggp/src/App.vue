<script setup lang="ts">
import { ref } from 'vue';
import SgFrame from './components/SgFrame.vue'
import sgGame, { SanGuo } from './app/sg';

const sgEl = ref()
const sg = new SanGuo(sgEl)
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
}
</style>
