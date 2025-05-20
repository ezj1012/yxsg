import type { FrameStageCfg } from "../api/apiModel"
import { CfgStr } from "../cfg"
import type { SgCtx } from "../sg"
import { RadioGroup } from "./stage"

export abstract class Stage {
    private _key: string
    private _cfg?: FrameStageCfg
    private _cfgs: CfgStr[] = []
    private _ctx: SgCtx
    radioGroup = new Map<string, RadioGroup>()

    private info: any
    constructor(key: string, ctx: SgCtx) {
        this._key = key
        this._ctx = ctx
    }

    get key() { return this._key }
    get cfgs() { return this._cfgs }
    get ctx() { return this._ctx }
    setCfg(cfg: FrameStageCfg) {
        this._cfg = cfg
        cfg.comps.forEach(c => this.addCfgStr(new CfgStr(c)))
    }

    addCfgStr(cfg: CfgStr) {
        this.updateGroup(cfg)
        this.cfgs.push(cfg)
    }

    updateGroup(cfgStr: CfgStr) {
        const groupCfg = cfgStr.radio()
        if (!groupCfg || !groupCfg.groupKey) { return }
        let rg = this.radioGroup.get(groupCfg.groupKey)
        if (!rg) {
            rg = new RadioGroup(groupCfg.groupKey, this._ctx.dataMgr)
            this.radioGroup.set(groupCfg.groupKey, rg)
        }
        rg.addCfg(cfgStr, groupCfg.value || cfgStr.key(), groupCfg.isDefSct)
    }

    abstract onUnmounted(): Promise<any>
    abstract onMounted(): Promise<any>
}