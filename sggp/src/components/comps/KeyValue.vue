<script setup lang="ts">

import { actionMgr } from '@/app/action';
import { CfgKey, type CfgStr } from '@/app/cfg';
import type { Textable } from '@/app/commModel';
import type { Sg } from '@/app/sg';

import { inject, onUnmounted, ref, watch, } from 'vue';

const { sg } = inject('sg') as Sg
const { cfg } = defineProps({ cfg: { required: true } }) as { cfg: CfgStr }

const text = ref<Textable>()
const title = ref<Textable>()
const data = ref('')

// 初始化
watch(() => cfg, () => {
  cfg.parseCfg({ text, title })
  sg.ctx.dataMgr.unsubscribe(cfg.key())
  const defVal = text.value ? text.value.content : ''
  sg.ctx.dataMgr.subscribeValue(cfg.key(), data, defVal)
}, { immediate: true })

onUnmounted(() => { sg.ctx.dataMgr.unsubscribe(cfg.key()) })

async function doClick(e: MouseEvent) {
  if (cfg.cfgMap.has(CfgKey.action)) {
    const act = cfg.get(CfgKey.action)
    await actionMgr.execBtn(act, cfg, e)
  }
}

</script>
<template>
  <div v-size="cfg" :style="{ '--h': `${cfg.size.h! - 6}px` }" class="comm_input_container  sg-comm-input">
    <div class="title" :style="title?.styles" @click="doClick">
      {{ title?.content }}
    </div>
    <div :style="text?.styles" style="flex: 1">
      {{ data }}
    </div>
  </div>
</template>
<style lang="less" scoped>
.comm_input_container {
  position: relative;
  background-color: transparent;
  background-repeat: no-repeat;
  background-size: 100% 100%;
  font-size: 13px;
  display: flex;
  line-height: var(--h);
  user-select: none;

  .title {
    text-align: left;
    padding-left: 7px;
    font-weight: 500;
    width: 45px;
  }
}
</style>
