<script setup lang="ts">

import { ref } from 'vue';
import { useMouse } from './app/utils/mouse';
import A from './components/A.vue';
import B from './components/B.vue';
import C from './components/C.vue';
const { x, y } = useMouse()

const cc = ref(0)
function aaa() {
  cc.value = (cc.value + 1) % 3
}
</script>

<template>
  <div class="test  sg-scroll" @click="aaa">
    hello world {{ x }} {{ y }} - {{ cc }}
    <!-- <A v-if="cc === 0"></A>
    <B v-else-if="cc === 1"></B>
    <C v-else></C> -->
    <A></A>
    <B></B>
    <C></C>
    <A></A>
    <B></B>
    <C></C>
    <A></A>
    <B></B>
    <C></C>
  </div>
  <div class="test sg-scroll" style="height: 200px;left: 300px;" @click="aaa">
    hello world {{ x }} {{ y }} - {{ cc }}
    <!-- <A v-if="cc === 0"></A>
    <B v-else-if="cc === 1"></B>
    <C v-else></C> -->
    <A></A>
    <B></B>
    <C></C>
    <A></A>
    <B></B>
    <C></C>
    <A></A>
    <B></B>
    <C></C>
  </div>
  <div class="test2  sg-scroll" @click="aaa">

  </div>

  <div class="test4 cc"></div>
  <div class="test5 "></div>
</template>

<style lang="less" scoped>
.test4 {
  left: 50px;
  top: 220px;
  position: absolute;
  height: 200px;
  width: 20px;
}

.test5 {
  left: 150px;
  top: 220px;
  position: absolute;
  height: 60px;
  width: 13px;
  background: url('./assets/imgs/scroll_bar.png') no-repeat;

  &::before {
    content: '';
    width: 100%;
    height: 100%;
    position: absolute;
    left: 0;
    top: 0;
    border: 6px solid transparent;
    border-top: 10px solid transparent;
    border-bottom: 10px solid transparent;
    border-image: url('./assets/imgs/scroll_bar.png') 10 6 fill;
    box-sizing: border-box;
  }
}

.cc {
  border: 6px solid;
  border-top: 10px solid;
  border-bottom: 10px solid;
  border-image: url('./assets/imgs/scroll_bar.png') 10 6 fill;
  box-sizing: border-box;
}

.test2 {
  width: 100px;
  height: 100px;
  left: 200px;
  top: 20px;
  position: absolute;
  background-color: #333;
  overflow-y: auto;
}

.test {
  left: 50px;
  top: 20px;
  position: absolute;
  width: 100px;
  height: 100px;
  background-color: transparent;
  background-color: #333;
  overflow-y: scroll;

}
</style>
