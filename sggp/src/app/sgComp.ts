import PBtn from "@/components/comps/PBtn.vue";
import { h } from "vue";
import { SgRes } from "./res";
import { CfgStr } from "./cfg";




export function useSgComm(res: SgRes) {
    function btnRed(key: string, size: string = "0,0,61,31,10", act: string = "empty") {
        const cfgStr = `K:${key};S:${size};T:I_BTN;RFI:common#btn_red;ACT:${act};TXT:T:你好,F:14,C:#E3EA03,;`
        return h(PBtn, { cfg: new CfgStr(cfgStr) })
    }
    return { btnRed }
}
