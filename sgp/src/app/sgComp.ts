import PBtn from "@/components/comps/PBtn.vue";
import { h } from "vue";
import { SgRes } from "./res";


export function useRedBtn(res: SgRes, key: string) {
    const cfgStr = `K:${key};T:I_BTN;S:100,542,361,61,4;RFI:common#btn_red;ACT:login;TXT:T:你好,F:14,C:#E3EA03,;`
    return h(PBtn, { cfg: res.parseCfg(cfgStr) })
}


