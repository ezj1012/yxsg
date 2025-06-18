<script lang="ts" setup>
import { CfgKey, type CfgStr } from '@/app/cfg';
import type { SgRes } from '@/app/res';

import { computed, inject, onUnmounted, ref, watch } from 'vue';

const { res, subscribeValue, unsubscribeValue } = inject('sg') as { res: SgRes, subscribeValue: any, unsubscribeValue: any }
const { cfg } = defineProps({ cfg: { required: true } }) as { cfg: CfgStr }

const styles = ref<{ [key: string]: any }>({})
const imgKey = ref<string>()
watch(() => cfg, () => {
  console.log(cfg.size)
  // cfg.parseCfg({ styles })
  subscribeValue(cfg.key(), imgKey, cfg.imgGroup?.getKey())
}, { immediate: true })

const finalStyles = computed(() => {
  const s = { ...styles.value }
  if (imgKey.value) {
    // const img = sg.ctx.res.getImgGroup(imgKey.value!)?.hasDef()
    const img = undefined
    if (img) {
      // if (cfg.get(CfgKey.imgSourceSize)) {
      //   img && (s.backgroundImage = `url(${img.getImg().imgDataUrl})`)
      // } else {
      //   img && (s.backgroundImage = `url(${img.getImg(cfg.size.w, cfg.size.h).imgDataUrl})`)
      // }
    } else {
      s.backgroundImage = `url(${imgKey.value})`
    }
  }
  return s
})

onUnmounted(() => {
  unsubscribeValue(cfg.key(), imgKey)
})
</script>
<template>
  <div :style="finalStyles" v-size="cfg">{{ imgKey }}</div>
</template>
<style lang="less" scoped></style>
