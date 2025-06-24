<script setup lang="ts">
import type { CfgStr } from '@/app/cfg';
import type { SgRes } from '@/app/res';
import type { Sg } from '@/app/sg';
import Img from '@/components/comps/Img.vue';
import PBtn from '@/components/comps/PBtn.vue';
import { inject, onMounted, ref } from 'vue'
const { res, set, btnRed } = inject('sg') as Sg
const imgCfgStr = "K:regplay#user_icon;T:IMG;S:140,135,76,92,21;STYL:YmFja2dyb3VuZC1jb2xvcjogIzA0MDEwMTllOwogICAgYm9yZGVyLXJhZGl1czogMnB4OwogICAgYm94LXNoYWRvdzogcmdiKDU0LCAyOCwgNSkgMHB4IDBweCAwcHggMXB4LCByZ2IoMTY1LCAxNDMsIDEwNSkgMHB4IDBweCAwcHggMnB4OwogICAgYmFja2dyb3VuZC1yZXBlYXQ6IG5vLXJlcGVhdDsKICAgIGJhY2tncm91bmQtcG9zaXRpb246IDVweDs=;"
// const btnCfgStr = "K:login#btn_login;T:I_BTN;S:400,542,200,35,4;RFI:common#btn_red;ACT:login;TXT:T:登入,F:14,C:#E3EA03,;"
// const { cfg } = defineProps({ cfg: { required: true } }) as { cfg: CfgStr }
const cfg = ref<CfgStr>(res.parseCfg(imgCfgStr))
const btnCfg = ref<CfgStr>(res.parseCfg("K:login#btn_login;T:I_BTN;S:100,442,161,36,4;RFI:common#btn_red;ACT:login;TXT:T:你好,F:14,C:#E3EA03,;"))
const btnCfg2 = ref<CfgStr>(res.parseCfg("K:login#btn_login2;T:I_BTN;S:100,342,61,31,4;RFI:common#btn_red;ACT:login;TXT:T:你好,F:14,C:#E3EA03,;"))
const btnCfg3 = ref<CfgStr>(res.parseCfg("K:regplay#btn_next_icon;T:I_BTN;S:241,160,21,30,11;RFI:common#btn_next;ACT:regplay#next_icon;"))


onMounted(() => {
    console.log(cfg.value.key())
    setTimeout(() => {
        set('regplay#user_icon', "99999999999999999")
    }, 3000)
})
const show = ref(true)

const Btn = btnRed('key_acb','0,0,100,35,10',"T:你好,F:18,C:#E3EA03")
const cfgStr = ref('')
</script>

<template>
    <div class="aa">
        <div class="op">
            <input v-model="cfgStr" />
        </div>
        <div class="aa2">
            <div class="img-main" @click="() => show = !show">
                <Img v-if="show" :cfg="cfg" />
                <!-- <PBtn :cfg="btnCfg" /> -->
                <!-- <PBtn :cfg="btnCfg2" /> -->
                <PBtn :cfg="btnCfg3" />
                <Btn />
            </div>
        </div>
    </div>
</template>

<style lang="less" scoped>
.aa {
    position: absolute;
    width: 100%;
    height: 100%;
    background-color: aliceblue;
    display: flex;
    flex-direction: row;

    .op {
        width: 320px;
        height: 100%;
        background-color: #fff;
    }

    .aa2 {
        flex: 1;
        position: relative;

        .img-main {
            width: 100%;
            height: 100%;
            position: absolute;
        }
    }
}
</style>
