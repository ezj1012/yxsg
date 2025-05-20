import { shallowRef, type Ref } from "vue";
import type { SgApi } from "./api"
import type { Traceable } from "./commModel";
import { Img } from "./utils/img";
import { CfgStr } from "./cfg";
import { configuerImgs, ImgMgr } from "./res/imgRes";
import type { FrameStageCfg } from "./api/apiModel";


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

export class StageCfgMgr {
    private cfgMap = new Map<string, StageCfgInfo>()
    clear() { this.cfgMap.clear() }
    // get(stage: string): StageCfgInfo { return this.cfgMap.get(stage)! }
    // getRadioGroup(stageKey: string, groupKey: string) { return this.cfgMap.get(stageKey)?.radioGroup.get(groupKey) }

    addStageCfg(cfg: FrameStageCfg) { this.cfgMap.set(cfg.key, new StageCfgInfo(cfg)) }
    appendStageChildrenCfg(cfg: CfgStr) { this.cfgMap.get(cfg.stage)?.addCfgStr(cfg) }
}


export class SgRes {
    private _imgMgr: ImgMgr
    private _cfgMgr: StageCfgMgr
    constructor() {
        this._imgMgr = new ImgMgr()
        this._cfgMgr = new StageCfgMgr()
    }

    async clear() {
        this._imgMgr.clear()
        this._cfgMgr.clear()
    }

    get imgMgr() { return this._imgMgr }
    get stageCfgMgr() { return this._cfgMgr }
    get globarMgr() { return }
    
}


export class SgResLoader {
    private _res: SgRes
    private _api: SgApi
    constructor(res: SgRes, api: SgApi) {
        this._api = api;
        this._res = res;
    }

    async load(force: boolean, bar: Traceable) {
        bar.msg = '加载配置资源'
        bar.pct = 10
        force && await this._res.clear()

        const imgMgr = this._res.imgMgr
        const fsMgr = this._res.stageCfgMgr
        const gCfgMgr = this._res.globarMgr
        await new Promise(resolve => {
            this._api.cfgApi.loadFrameCfg().then(data => {
                bar.pct = 10
                bar.pct = 30
                const imgCfs = configuerImgs(data, imgMgr)
                // 更新舞台配置
                data.stages?.forEach(cfg => { fsMgr.addStageCfg(cfg) })
                // 更新舞台图片配置
                imgCfs.forEach(cfg => { fsMgr.appendStageChildrenCfg(cfg) })
                bar.pct = 50
                resolve(bar)
            })
        })

        await new Promise(resolve => {
            bar.pct = 30
            this._api.cfgApi.loadGlobalCfg().then(data => {
                bar.pct = 10

                gCfgMgr.configuer(data)
                bar.pct = 99
                resolve(bar)
            })
        })

        bar.msg = '加载图片资源'
        bar.pct = 0
        await new Promise(resolve => {
            const defs = Array.from(imgMgr.getImgs().values())
            let count = 0;
            const st = new Date().getTime()
            Img.loadImages(defs, (def: Img.ImgDef, err: any) => {
                count++;
                const temp = (count * 100) / defs.length;
                bar.pct = temp > 100 ? 100 : temp
                if (count == defs.length) {
                    resolve(bar)
                }
            })
        })
        return new Promise(resolve => { resolve(bar) })
    }
}
