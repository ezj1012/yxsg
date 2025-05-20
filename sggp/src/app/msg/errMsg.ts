import type { DataMgr } from "../dataMgr";

export class ErrMsgMgr {
    msgs: any = []
    constructor() {
    }

    pushMsg(msg: string) {
        this.msgs.push(msg)
    }
    removeMsg() {

    }

}