import { ref, shallowRef, type Ref } from "vue"
import { CfgKey, CfgStr, CfgType } from "./cfg"
import { Img } from "../app/utils/img"
import type { FrameCfg, FrameStageCfg, Traceable } from "./model"
import type { SgApi } from "./api"
import { StageCfgInfo } from "./stage"
import { GCfgMgr } from "./global"


export class ImgGroupInfo {
    key: string
    cfg: CfgStr
    mgr: ImgMgr
    constructor(cfg: CfgStr, mgr: ImgMgr) {
        this.cfg = cfg
        this.key = cfg.key()
        this.mgr = mgr
    }

    getKey() {
        if (this.hasDef()) {
            return this.key
        } else if (this.hasOn()) {
            return this.key + "_on"
        } else if (this.hasDown()) {
            return this.key + "_down"
        } else if (this.hasAlarm()) {
            return this.key + "_alarm"
        } else if (this.hasSct()) {
            return this.key + "_sct"
        } else if (this.hasSctOn()) {
            return this.key + "_son"
        } else if (this.hasDis()) {
            return this.key + "_dis"
        }
        return ''
    }

    hasDef() { return this.mgr.get(this.key) }
    hasOn() { return this.mgr.get(this.key + "_on") }
    hasSctOn() { return this.mgr.get(this.key + "_son") }
    hasDown() { return this.mgr.get(this.key + "_down") }
    hasAlarm() { return this.mgr.get(this.key + "_alarm") }
    hasSct() { return this.mgr.get(this.key + "_sct") }
    hasDis() { return this.mgr.get(this.key + "_dis") }
}


export class ImgMgr {
    clear() {
        this.imgs.clear()
        this.imgGroups.clear()
        this.lvImgs.value = undefined
    }
    private imgs = new Map<string, Img.ImgDef>()
    private imgGroups = new Map<string, ImgGroupInfo>()
    private lvImgs = shallowRef<Map<number, Img.ImgDef>>()

    getGroup(key: string) { return this.imgGroups.get(key) }
    getGroups() { return this.imgGroups }
    get(key: string) { return this.imgs.get(key) }
    getImgs() { return this.imgs }

}


export class StageCfgMgr {
    private cfgMap = new Map<string, StageCfgInfo>()
    clear() { this.cfgMap.clear() }
    get(stage: string): StageCfgInfo { return this.cfgMap.get(stage)! }
    getRadioGroup(stageKey: string, groupKey: string) { return this.cfgMap.get(stageKey)?.radioGroup.get(groupKey) }
    addStageCfg(cfg: FrameStageCfg) { this.cfgMap.set(cfg.key, new StageCfgInfo(cfg)) }
    appendStageChildrenCfg(cfg: CfgStr) { this.cfgMap.get(cfg.stage)?.addCfgStr(cfg) }
}

class FrameCfgLoader {
    ctx: SgRes
    constructor(ctx: SgRes) {
        this.ctx = ctx
    }
    async load(force: boolean, bar: Traceable): Promise<void> {
        await new Promise(resolve => {
            this.ctx.getApi().cfgApi.loadFrameCfg().then(data => {
                bar.pct = 10
                const imgMgr = this.ctx.getImgMgr()
                const fsMgr = this.ctx.getStageCfgMgr()
                if (force) {
                    imgMgr.clear()
                    fsMgr.clear()
                }
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
            this.ctx.getApi().cfgApi.loadGlobalCfg().then(data => {
                bar.pct = 10
                const gCfgMgr = this.ctx.getGlobarMgr()
                gCfgMgr.configuer(data)
                bar.pct = 99
                resolve(bar)
            })
        })

        bar.msg = '加载图片资源'
        bar.pct = 0
        return new Promise((inject) => {
            const imgMgr = this.ctx.getImgMgr()
            const defs = Array.from(imgMgr.getImgs().values())
            let count = 0;
            const st = new Date().getTime()
            Img.loadImages(defs, (def: Img.ImgDef, err: any) => {
                count++;
                const temp = (count * 100) / defs.length;
                bar.pct = temp > 100 ? 100 : temp
                if (count == defs.length) {
                    inject()
                }
            })
        })

    }
}

export class SgRes {
    private bar: Ref<Traceable>
    private api: SgApi
    private imgMgr: ImgMgr
    private cfgLoader: FrameCfgLoader
    stageCfgMgr: StageCfgMgr
    gCfgMgr: GCfgMgr

    constructor(bar: Ref<Traceable>, api: SgApi) {
        this.bar = bar
        this.api = api
        this.imgMgr = new ImgMgr()
        this.stageCfgMgr = new StageCfgMgr()
        this.gCfgMgr = new GCfgMgr()
        this.cfgLoader = new FrameCfgLoader(this)
    }

    async loadCfg() {
        this.bar.value.msg = '加载配置资源'
        this.bar.value.pct = 10
        await this.cfgLoader.load(false, this.bar.value)
    }

    getImgMgr() { return this.imgMgr }
    getStageCfgMgr() { return this.stageCfgMgr }
    getApi() { return this.api }
    getGlobarMgr() { return this.gCfgMgr }
    getImg(key: string) { return this.imgMgr.get(key) }
    getImgGroup(key: string) { return this.imgMgr.getGroup(key)! }
}