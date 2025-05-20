import type { GlobalCfg } from "./api/apiModel"
import type { SgCtx } from "./sg"

export class GCfgMgr {
    cfg?: GlobalCfg
    buildImgs = {}
    private _ctx: SgCtx
    constructor(ctx: SgCtx) {
        this._ctx = ctx
    }
    configuer(data: GlobalCfg) {
        this.cfg = data
    }

    getPalyIcons() {
        return this.cfg?.playerIcons || []
    }

}