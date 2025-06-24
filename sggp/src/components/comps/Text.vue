<script lang="ts" setup>
// import type { CompInfo } from '@/app/stage/CompMgr';
// import { BgVcfg, TextInputVcfg } from '@/app/stage/CompModel';
// import DataMgr from '@/app/stage/DataMgr';
// import Res from '@/app/utils/Res';
import { CfgKey, type CfgStr } from '@/app/cfg';
import type { Textable } from '@/app/commModel';
import type { SanGuo, SgCtx } from '@/app/sg';
import { computed, inject, onMounted, onUnmounted, ref, watch } from 'vue';

const { sg, ctx } = inject('sg') as { sg: SanGuo, ctx: SgCtx }
const { cfg } = defineProps({ cfg: { required: true } }) as { cfg: CfgStr }

const data = ref('')
const text = ref<Textable>()
const html = ref(false)
const msg = ref<any>()

const styles = ref<{ [key: string]: any }>({})
watch(() => cfg, () => {
  cfg.parseCfg({ text, styles, html, msg })
  const defVal = text.value ? text.value.content : ''
  ctx.dataMgr.subscribeValue(cfg.key(), data, defVal)
}, { immediate: true })

onUnmounted(() => {
  ctx.dataMgr.unsubscribe(cfg.key())
})

const finalStyles = computed(() => {
  const s = { ...styles.value }
  if (text.value) {
    Object.assign(s, text.value.styles)
  }
  return s
})

</script>
<template>
  <div v-if="html" v-bg="cfg" v-msg="msg" :style="finalStyles" class="font-style" v-html="data"></div>
  <div v-else v-bg="cfg" v-msg="msg" :style="finalStyles" class="font-style">{{ data }}</div>
</template>
<style lang="less" scoped>
.font-style {
  width: 100%;
  height: 100%;
  outline: none;
  border: 0;
  // background-color: transparent;
  font-family: Consolas, "Courier New", monospace;
  font-weight: normal;
  font-feature-settings: "liga" 0, "calt" 0;
  font-variation-settings: normal;
  letter-spacing: 0px;
  white-space: nowrap;
  user-select: none;
}
</style>
