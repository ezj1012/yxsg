<script setup lang="ts">
import { actionMgr } from '@/app/action';
import { CfgStr } from '@/app/cfg';
import type { Sg } from '@/app/sg';
import { computed, inject, onMounted, onUnmounted, onUpdated, ref, watch } from 'vue';
import Scroll from '../../Scroll.vue';
import PBtn from '../../PBtn.vue';
import ChatMain from './ChatMain.vue';
import ChatInput from './ChatInput.vue';



const { sg, chatMgr } = inject('sg') as Sg
const { cfg } = defineProps({ cfg: { required: true } }) as { cfg: CfgStr }

const showCfg = [
  { height: 140 },
  { height: 500 },
  { height: 0 },
  { height: 40 },
]
const pmEl = ref<HTMLDivElement>() // htm 
const showSizeIdx = ref(0)
const channelIdx = ref(1) // 1 全部,2联盟,3私聊,4战场
const msg = ref('')
const changeBtnCfg = new CfgStr("K:playing#change_channel;T:I_BTN;S:11,5,78,24,11;RFI:playing#chat#chatbar_channel;ACT:playing#chat_show_channel;")
const sendBtnCfg = new CfgStr("K:playing#chatbutton_send;T:I_BTN;S:345,6,28,22,11;RFI:playing#chat#chatbutton_send;ACT:playing#chat_send_msg;")

// 聊天组 相关配置
const groupBtns = ref<CfgStr[]>([
  new CfgStr("K:playing#channel_tab1;T:I_BTN;S:0,1,71,21,11;RFI:playing#chat#channel_tab1;SCTABLE:chatChannel,1,TRUE;"),
  new CfgStr("K:playing#channel_tab2;T:I_BTN;S:71,1,71,21,11;RFI:playing#chat#channel_tab2;SCTABLE:chatChannel,2,FALSE;"),
  new CfgStr("K:playing#channel_tab3;T:I_BTN;S:142,1,71,21,11;RFI:playing#chat#channel_tab3;SCTABLE:chatChannel,3,FALSE;"),
  new CfgStr("K:playing#channel_tab4;T:I_BTN;S:213,1,71,21,11;RFI:playing#chat#channel_tab4;SCTABLE:chatChannel,4,FALSE;"),
])
// sg.res.stageCfgMgr.addGroups(groupBtns.value as any)
// const radioGroup = sg.res.stageCfgMgr.getGroups('playing', 'chatChannel')
// watch(() => radioGroup?.getSctValue(), (newValue, oldValue) => { channelIdx.value = Number(newValue) })

// 聊天框大小
const showBtns = ref<CfgStr[]>([
  new CfgStr("K:playing#chatbutton_control1;T:I_BTN;S:375,6,28,22,11;RFI:playing#chat#chatbutton_control1;ACT:playing#chat_change_size;"),
  new CfgStr("K:playing#chatbutton_control4;T:I_BTN;S:375,6,28,22,11;RFI:playing#chat#chatbutton_control4;ACT:playing#chat_change_size;"),
  new CfgStr("K:playing#chatbutton_control3;T:I_BTN;S:375,6,28,22,11;RFI:playing#chat#chatbutton_control3;ACT:playing#chat_change_size;"),
  new CfgStr("K:playing#chatbutton_control2;T:I_BTN;S:375,6,28,22,11;RFI:playing#chat#chatbutton_control2;ACT:playing#chat_change_size;"),
])
const showBtnCfg = computed(() => { return showBtns.value[showSizeIdx.value] })
actionMgr.reg('playing#chat_change_size', async ({ sg }) => {
  showSizeIdx.value++ && showSizeIdx.value > 3 && (showSizeIdx.value = 0)
})
const msgScroll = ref() // 控制聊天框
onUpdated(() => msgScroll.value?.updateStyle())

// 初始
watch(() => cfg, () => { }, { immediate: true })

onMounted(() => {
  chatMgr.active()
})

onUnmounted(() => {
  chatMgr.dispose()
})

</script>

<template>
  <div class="chat-pan" ref="pmEl" v-size="cfg">
    <div class="chat-msg" v-show="showSizeIdx != 2" :style="{ height: `${showCfg[showSizeIdx].height}px` }">
      <!-- <div class="btns">
        <PBtn v-for="cfg in groupBtns" :cfg="cfg" />
      </div> -->
      <ChatMain class="sg-scroll msgs" style="overflow-y: scroll;" />
    </div>
    <div class="chat-input">
      <!---->
      <!-- <PBtn :cfg="changeBtnCfg" /> -->
      <PBtn :cfg="sendBtnCfg" />
      <PBtn :cfg="showBtnCfg" />
      <ChatInput />
    </div>
  </div>
</template>
<style lang="less" scoped>
.chat-pan {
  cursor: pointer;

  .chat-msg {
    position: absolute;
    // box-shadow: inset 0 0 0 1px red;
    bottom: 40px;
    left: 0;
    width: 370px;
    display: flex;
    flex-direction: column;
    overflow: hidden;


    .btns {
      height: 22px;
      width: 100%;
    }

    .msgs {
      flex: 1;
      width: 100%;
      border-radius: 0 5px 0 0;
      background: rgba(0, 0, 0, 0.60);
    }
  }

  .chat-input {
    position: absolute;
    // box-shadow: inset 0 0 0 1px red;
    bottom: 1px;
    left: 0;
    width: 100%;
    height: 36px;

  }
}
</style>