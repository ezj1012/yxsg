import { ref, type Ref, type ShallowRef } from "vue"
import { CfgStr } from "./cfg"
import type { FrameStageCfg } from "./model"
import type { DataMgr } from "./dataMgr"
import type { StageCfgMgr } from "./res"

export const gStageComps = [
    { key: 'login', },
    { key: 'regplay', },
    { key: 'playing', },
]

export class RadioGroup {
    static dataMgr: ShallowRef<DataMgr>
    groupKey: string
    sctKey = ref<string>()
    cfgs: { cfg: CfgStr, value: string }[] = []
    fun = (key: string, newValue: any, oldValue: any) => {
        this.call(key, newValue, oldValue)
    }
    constructor(groupKey: string) {
        this.groupKey = groupKey
    }

    addCfg(cfg: CfgStr, value: string, isDefaultSct: boolean = false) {
        const sctKey = `${cfg.key()}_sct`
        this.cfgs.push({ cfg, value })
        RadioGroup.dataMgr.value?.subscribe(sctKey, undefined, this.fun)
        if (isDefaultSct) {
            this.call(sctKey, true, false)
        }
    }

    removeCfg(cfg: CfgStr) {
        for (let i = 0; i < this.cfgs.length; i++) {
            if (this.cfgs[i].cfg.key() == cfg.key()) {
                this.cfgs.splice(i, 1)
                RadioGroup.dataMgr.value?.unsubscribe(`${cfg.key()}_sct`, this.fun)
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
                const bef = RadioGroup.dataMgr.value?.get(oKey)
                if (oKey !== key) {
                    RadioGroup.dataMgr.value?.set(oKey, false)
                }
            }
            RadioGroup.dataMgr.value?.set(`group#${this.groupKey}`, this.getSctValue())
        }
    }
}


export class StageCfgInfo {
    cfg: FrameStageCfg
    cfgs: CfgStr[] = []
    radioGroup = new Map<string, RadioGroup>()

    constructor(cfg: FrameStageCfg) {
        this.cfg = cfg
        cfg.comps.forEach(c => this.addCfgStr(new CfgStr(c)))
    }

    updateGroup(cfgStr: CfgStr) {
        const groupCfg = cfgStr.radio()
        if (!groupCfg || !groupCfg.groupKey) { return }
        let rg = this.radioGroup.get(groupCfg.groupKey)
        if (!rg) {
            rg = new RadioGroup(groupCfg.groupKey)
            this.radioGroup.set(groupCfg.groupKey, rg)
        }
        rg.addCfg(cfgStr, groupCfg.value || cfgStr.key(), groupCfg.isDefSct)
    }

    addCfgStr(cfg: CfgStr) {
        this.updateGroup(cfg)
        this.cfgs.push(cfg)
    }
}

export interface StageCfg {
    key: string
    info?: StageCfgInfo
}

export class StageMgr {
    stage = ref<string>()
    dataMgr: DataMgr
    cfgMap = new Map<string, StageCfg>()
    constructor(dataMgr: DataMgr, cfgs?: StageCfg[]) {
        this.dataMgr = dataMgr
        cfgs = cfgs || gStageComps
        cfgs.forEach(cfg => this.cfgMap.set(cfg.key, cfg))
    }

    testStage() {
        if (this.dataMgr.hasPlay()) {
            this.stage.value = 'playing'
        } else if (this.dataMgr.isLogin()) {
            this.stage.value = 'regplay'
        } else {
            this.stage.value = 'login'
        }
    }

    getStageCfg(key?: string) {
        return key ? this.cfgMap.get(key) : undefined
    }

    updateCfg(scm: StageCfgMgr) {
        this.cfgMap.forEach((value: StageCfg, key: string) => { value.info = scm.get(key) })
    }
}
