<script setup lang="ts">
import { CfgStr } from '@/app/cfg';
import { ref } from 'vue';
import KeyValue from '../comps/KeyValue.vue';
import TextInput from '../comps/TextInput.vue';
import ChatPan from '../comps/playing/chat/ChatPan.vue';

const cfg = ref(new CfgStr("T:KV;K:playing#player_name;S:86,16,162,22,10;TIT:T:名字：,F:12,C:#AA8EF2,;"))
const cfg2 = ref(new CfgStr("T:KV;K:playing#player_name;S:86,56,262,22,10;TIT:T:名字：,F:12,C:#AA8EF2,;"))

// TEXT_I
const textCfg = ref(new CfgStr("K:regplay#player_name;T:TEXT_I;S:150,295,146,25,11;INTT:text;TXT:T:,F:14,C:#9575CD,;"))

const chatCfg = ref(new CfgStr("HIDE;K:playing#chatpan;T:CST_C;RFC:chatPan;S:266,560,420,36,150;RFI:playing#chat_pan;"))
</script>
<template>
    <div class="comm-test sg-comm">
        <!-- <KeyValue :cfg="cfg" /> -->
        <!-- <KeyValue :cfg="cfg2" /> -->
        <!-- <TextInput :cfg="textCfg" /> -->
        <ChatPan :cfg="chatCfg" :id="chatCfg.key()" />
    </div>
</template>
<style lang="less" scoped>
.comm-test {
    position: absolute;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    z-index: 10000;
}
</style>