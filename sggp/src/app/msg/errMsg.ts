import { ref } from "vue";
import type { DataMgr } from "../dataMgr";

export class ErrMsgMgr {
    msgs = ref<any>([])
    constructor() {
    }
    removeMsg() {
        if (this.msgs.value.length > 0) {
            this.msgs.value.pop()
        }
    }
    pushMsg(msg: string | { content: string, ok?: any, cancel?: any }) {

        this.msgs.value.push(typeof msg === 'string' ? {
            content: msg
        } : msg)
        console.log('err msg : ', msg)
    }


}