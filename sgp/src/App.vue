<script setup lang="ts">
import { onMounted, provide, ref } from 'vue';
import { useMouse } from './app/utils/mouse';
import { RsmApi, SgApi } from './app/api';
import { SgRes } from './app/res';
import Img from './components/comps/Img.vue';
import TestImg from './test/TestImg.vue';
import { useMapStorage } from './app/dataMgr';
import { useSgComm } from './app/sgComp';
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
const ready = ref(false)
const dataMgr = useMapStorage('sgdata')
const res = new SgRes(api.rsmApi)
const sgComm = useSgComm(res)
provide('sg', { res: res, ...dataMgr, ...sgComm })
onMounted(async () => {
  const t = new Date().getTime()
  await res.loadRes({ msg: '', pct: 0 })
  console.log('end: ', (new Date().getTime() - t))
  ready.value = true
})


</script>

<template>
  <div class="main">
    <span style="position: absolute;left: 0;top: 0;z-index: 10000;">{{ `${x},${y}` }}</span>
    <TestImg v-if="ready" />
  </div>
</template>

<style lang="less" scoped>
.main {
  position: relative;
  width: 100%;
  height: 100%;
}
</style>
