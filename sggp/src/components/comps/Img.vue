<script lang="ts" setup>
import { CfgKey, type CfgStr } from '@/app/cfg';
import type { Textable } from '@/app/model';
import type { SanGuo } from '@/app/sg';
import { computed, inject, onUnmounted, ref, watch } from 'vue';

const { sg } = inject('sg') as { sg: SanGuo }
const { cfg } = defineProps({ cfg: { required: true } }) as { cfg: CfgStr }

const styles = ref<{ [key: string]: any }>({})
const imgKey = ref<string>()
watch(() => cfg, () => {
  cfg.parseCfg({ styles })
  sg.dataMgr.subscribeValue(cfg.key(), imgKey, cfg.imgGroup?.getKey())
}, { immediate: true })

const finalStyles = computed(() => {
  const s = { ...styles.value }
  if (imgKey.value) {
    const img = sg.res.getImgGroup(imgKey.value!)?.hasDef()
    if (img) {
      if (cfg.get(CfgKey.imgSourceSize)) {
        img && (s.backgroundImage = `url(${img.getImg().imgDataUrl})`)
      } else {
        img && (s.backgroundImage = `url(${img.getImg(cfg.size.w, cfg.size.h).imgDataUrl})`)
      }
    } else {
      s.backgroundImage = `url(${imgKey.value})`
    }
  }
  return s
})

onUnmounted(() => { sg.dataMgr.unsubscribe(cfg.key()) })
</script>
<template>
  <div :style="finalStyles" v-size="cfg"> </div>
</template>
<style lang="less" scoped></style>
