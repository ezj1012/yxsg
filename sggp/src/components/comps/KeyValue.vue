<script setup lang="ts">

import { actionMgr } from '@/app/action';
import { CfgKey, type CfgStr } from '@/app/cfg';
import { type Textable } from '@/app/model';
import type { SanGuo } from '@/app/sg';

import { inject, ref, watch, } from 'vue';

const { sg } = inject('sg') as { sg: SanGuo }
const { cfg } = defineProps({ cfg: { required: true } }) as { cfg: CfgStr }

const bgUrl = ref<string>()
const text = ref<Textable>()
const title = ref<Textable>()
const data = ref('')

// 初始化
watch(() => cfg, () => {
  cfg.parseCfg({ text, title })
  bgUrl.value = `url(${sg.res.getImgGroup('common#board_input').hasDef()?.getImg(cfg.size.w, cfg.size.h).imgDataUrl})`
  const defVal = text.value ? text.value.content : ''
  sg.dataMgr.subscribeValue(cfg.key(), data, defVal)
}, { immediate: true })


async function doClick(e: MouseEvent) {
  if (cfg.cfgMap.has(CfgKey.action)) {
    const act = cfg.get(CfgKey.action)
    await actionMgr.execBtn(act, cfg, e)
  }
}

</script>
<template>
  <div v-size="cfg" :style="{ backgroundImage: bgUrl }" class="comm_input_container">
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
  line-height: 24px;
  user-select: none;

  .title {
    text-align: left;
    padding-left: 7px;
    font-weight: 500;
    width: 45px;
  }
}
</style>
