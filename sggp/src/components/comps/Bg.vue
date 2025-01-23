<script lang="ts" setup>
import { CfgKey, type CfgStr } from '@/app/cfg';
import type { Textable } from '@/app/model';
import { ref, watch } from 'vue';

const { cfg } = defineProps({ cfg: { required: true } }) as { cfg: CfgStr }

const type = ref<string>()
const text = ref<Textable>()
const styles = ref<{ [key: string]: any }>({})
watch(() => cfg, () => {
  cfg.parseCfg({ text, styles })
  type.value = cfg.get(CfgKey.inputType)
}, { immediate: true })

</script>
<template>
  <div class="bg-img" :style="styles" v-bg="cfg">
    <div class="text" v-if="text && text.content" :style="text?.styles">{{ text.content }}</div>
  </div>
</template>
<style lang="less" scoped>
.bg-img {
  .text {
    position: absolute;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    text-align: center;
    user-select: none;
  }
}
</style>
