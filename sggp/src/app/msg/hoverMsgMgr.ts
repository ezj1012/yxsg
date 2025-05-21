import type { Ref } from "vue";
import type { HoverMsg, HoverMsgDef } from "../commModel";
import type { SgCtx } from "../sg";

const HoverMsgMgrKey = 'HoverMsgMgr'
export class HoverMsgMgr {
    msg?: HoverMsg

    private _ctx: SgCtx
    constructor(ctx: SgCtx) {
        this._ctx = ctx
    }

    setHoverMsg(el: any, msg?: HoverMsg) {
        // console.log('this.setHoverMsg')
        if (msg) {
            this.msg = msg
            this._ctx.dataMgr.setByKey(HoverMsgMgrKey, msg)
        } else if (this.msg && this.msg.el == el) {
            this.msg = undefined
            this._ctx.dataMgr.setByKey(HoverMsgMgrKey, undefined)
        }
    }

    subscribeValue(data: Ref<any>) {
        this._ctx.dataMgr.subscribeValue(HoverMsgMgrKey, data)
    }

}

export class CompactSimpleMsg {
    msg: string
    constructor(msg: string) {
        this.msg = msg;
    }
}