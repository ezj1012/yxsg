<script lang="ts" setup>
  // import type { CompInfo } from '@/app/stage/CompMgr';
  // import { BgVcfg, TextInputVcfg } from '@/app/stage/CompModel';
  // import DataMgr from '@/app/stage/DataMgr';
  // import Res from '@/app/utils/Res';
  import { CfgKey, type CfgStr } from '@/app/cfg';
  import type { Textable } from '@/app/model';
  import type { SanGuo } from '@/app/sg';
  import { computed, inject, onMounted, onUnmounted, ref, watch } from 'vue';

  const { sg } = inject('sg') as { sg: SanGuo }
  const { cfg } = defineProps({ cfg: { required: true } }) as { cfg: CfgStr }

  const data = ref('')
  const type = ref<string>()
  const text = ref<Textable>()
  watch(() => cfg, () => {
    cfg.parseCfg({ text })
    cfg.imgGroup = sg.res.getImgGroup('common#board_input')
    type.value = cfg.get(CfgKey.inputType)
    sg.dataMgr.subscribeValue(cfg.key(), data, '')
  }, { immediate: true })

  watch(data, () => data.value != sg.dataMgr.get(cfg.key()) && sg.dataMgr.set(cfg.key(), data.value))

  onUnmounted(() => { sg.dataMgr.unsubscribe(cfg.key()) })
  const textStyles = computed(() => text.value ? { ...text.value.styles, '--color': '#fff' } : { '--color': '#fff' })

</script>
<template>
  <div v-bg="cfg" class="comm_input_container ">
    <input v-model.trim="data" :type="type" autocomplete="on" class="input" :style="textStyles" />
  </div>
</template>
<style lang="less" scoped>
  .input {
    width: 100%;
    height: 100%;
    outline: none;
    border: 0;
    background-color: transparent;
    padding: 0 5px;


    // /* 屏蔽chrome浏览器默认项输入框背景色 */
    &:-internal-autofill-previewed,
    &:-internal-autofill-selected {
      transition: background-color 999999999s !important;
      -webkit-text-fill-color: var(--color) !important;
    }
  }
</style>
