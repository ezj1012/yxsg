<script setup lang="ts">
import { actionMgr } from '@/app/action';
import { CfgStr } from '@/app/cfg';
import type { SanGuo } from '@/app/sg';
import { inject, computed } from 'vue';
import PBtn from '../comps/PBtn.vue';
const { sg } = inject('sg') as { sg: SanGuo }
const msgs = computed(() => sg.dataMgr.dataCache.showErrorMsgs)
const key = 'any#msg_ok'
const key2 = 'any#msg_cancel'
actionMgr.register({
    key, desc: key, fun: async () => {
        try {
            const msg = msgs.value[msgs.value.length - 1]
            msg.ok && await msg.ok()
        } finally {
            sg.dataMgr.removeMsg()
        }
    }
})
actionMgr.register({
    key: key2, desc: key2, fun: async () => {
        try {
            const msg = msgs.value[msgs.value.length - 1]
            msg.cancel && await msg.cancel()
        } finally {
            sg.dataMgr.removeMsg()
        }
    }
})


const cfg = new CfgStr(`K:${key};T:I_BTN;S:0,0,52,26,4;RFI:common#btn_red;ACT:${key};TXT:T:确定,F:12,C:#E3EA03,;`)
const cfg2 = new CfgStr(`K:${key2};T:I_BTN;S:70,0,52,26,4;RFI:common#btn_red;ACT:${key};TXT:T:取消,F:12,C:#E3EA03,;`)

</script>

<template>
    <main v-show="msgs.length > 0" class="err-main"
        :style="{ backgroundColor: `rgba(100, 100, 100, ${0.3 + 0.12 * msgs.length})` }">
        <div v-for="(item, idx) in msgs" :key="idx" :style="{ top: `calc(50% - ${idx * 3}px)` }" class="err-info">
            <div class="text" v-html="item.content"></div>
            <div class="btn">
                <div v-if="item.cancel" class="btn-w2">
                    <PBtn :cfg="cfg" />
                    <PBtn :cfg="cfg2" />
                </div>
                <div v-else class="btn-w">
                    <PBtn :cfg="cfg" />
                </div>
            </div>
        </div>
    </main>
</template>
<style lang="less" scoped>
.err-main {
    width: 1000px;
    height: 600px;
    position: absolute;
    left: 0;
    top: 0;
    z-index: 20000;

    .err-info {
        min-width: 168px;
        min-height: 73px;
        max-width: 280px;
        font-family: Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu,
            Cantarell, 'Fira Sans', 'Droid Sans', 'Helvetica Neue', sans-serif;
        font-size: 15px;
        text-rendering: optimizeLegibility;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
        color: #111;
        position: absolute;
        left: 50%;
        transform: translate(-50%, -50%);
        box-shadow: 0 0 0px 1px #619c8a;
        border: 1px solid #355c52;
        border-radius: 1px;
        background-color: #3e6f56;
        filter: brightness(1.2);
        user-select: none;

        .text {
            font-size: 12px;
            padding: 15px 18px 42px 18px;
        }
    }

    .btn {
        position: absolute;
        bottom: 0;
        left: 0;
        width: 100%;
        height: 30px;


        .btn-w {
            position: absolute;
            left: calc(50% - 26px);
            width: 52px;
            height: 26px;
        }

        .btn-w2 {
            position: absolute;
            left: calc(50% - 61px);
            width: 122px;
            height: 26px;
        }

    }
}
</style>