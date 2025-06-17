<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { useMouse } from './app/utils/mouse';
import { RsmApi, SgApi } from './app/api';
import { Res } from './app/res';
const { x, y } = useMouse()



const api = new SgApi({
  handler401() { },
  getToken() {
    return ''
  },
  getTokenKey() {
    return 'abc'
  }
})

const res = new Res(api.rsmApi)

onMounted(async () => {
  await res.loadRes({ msg: '', pct: 0 })
})


</script>

<template>
  <div class="main">
    <span>{{ `${x},${y}` }}</span>
  </div>
</template>

<style lang="less" scoped></style>
