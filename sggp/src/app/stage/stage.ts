import { ref, type ShallowRef } from "vue";
import type { SgCtx } from "../sg";
import type { StageCfgAdd } from "../res";
import type { FrameStageCfg } from "../api/apiModel";
import { CfgStr } from "../cfg";
import type { DataMgr } from "../dataMgr";
import { LoginStage } from "./loginStage";
import { Stage } from "./absStage";
import { RegPlayStage } from "./regPlayStage";
import { PlayStage } from "./playingStage";


export class StageMgr implements StageCfgAdd {
    private _ctx: SgCtx
    private _cahce = new Map<string, Stage>()
    stageKey = ref('login')
    constructor(ctx: SgCtx) {
        this._ctx = ctx
        const loginStage = new LoginStage(ctx);
        const regplayStage = new RegPlayStage(ctx);
        const playStage = new PlayStage(ctx);
        this._cahce.set(loginStage.key, loginStage)
        this._cahce.set(regplayStage.key, regplayStage)
        this._cahce.set(playStage.key, playStage)
        this.stage
    }

    get stage() {
        let stage = 'login'
        if (this._ctx.play) {
            stage = 'playing'
        } else if (this._ctx.user) {
            stage = 'regplay'
        }
        this.stageKey.value = stage
        return this._cahce.get(stage)!
    }

    getStage(key?: string) {
        if (key) {
            return this._cahce.get(key)
        }
        return undefined
    }

    async clear() { }

    addStageCfg(cfg: FrameStageCfg) {
        this.getStage(cfg.key)!.setCfg(cfg)
    }

    appendStageChildrenCfg(cfg: CfgStr): void {
        this.getStage(cfg.stage)?.addCfgStr(cfg)
    }

}


export class RadioGroup {
    _dataMgr: DataMgr
    groupKey: string
    sctKey = ref<string>()
    cfgs: { cfg: CfgStr, value: string }[] = []
    fun = (key: string, newValue: any, oldValue: any) => {
        this.call(key, newValue, oldValue)
    }
    constructor(groupKey: string, dataMgr: DataMgr) {
        this.groupKey = groupKey
        this._dataMgr = dataMgr
    }

    addCfg(cfg: CfgStr, value: string, isDefaultSct: boolean = false) {
        const sctKey = `${cfg.key()}_sct`
        this.cfgs.push({ cfg, value })
        this._dataMgr.subscribe(sctKey, undefined, this.fun)
        if (isDefaultSct) {
            this.call(sctKey, true, false)
        }
    }

    removeCfg(cfg: CfgStr) {
        for (let i = 0; i < this.cfgs.length; i++) {
            if (this.cfgs[i].cfg.key() == cfg.key()) {
                this.cfgs.splice(i, 1)
                this._dataMgr.unsubscribe(`${cfg.key()}_sct`, this.fun)
                return
            }
        }
    }

    getSctCfg() {
        for (let i = 0; i < this.cfgs.length; i++) {
            if (this.cfgs[i].cfg.key() == this.sctKey.value) {
                return this.cfgs[i]
            }
        }
        return undefined
    }

    getSctValue() {
        const cfg = this.getSctCfg()
        return cfg ? cfg.value : undefined
    }

    call(key: string, newValue: any, oldValue: any) {
        if (newValue == true) {
            this.sctKey.value = key.substring(0, key.length - 4)
            for (let i = 0; i < this.cfgs.length; i++) {
                const oKey = `${this.cfgs[i].cfg.key()}_sct`
                const bef = this._dataMgr.get(oKey)
                if (oKey !== key) {
                    this._dataMgr.set(oKey, false)
                }
            }
            this._dataMgr.set(`group#${this.groupKey}`, this.getSctValue())
        }
    }
}
