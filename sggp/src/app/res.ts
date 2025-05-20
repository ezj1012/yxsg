import { shallowRef, type Ref } from "vue";
import type { SgApi } from "./api"
import type { Traceable } from "./commModel";
import { Img } from "./utils/img";
import { CfgStr } from "./cfg";
import { configuerImgs, ImgGroupInfo, ImgMgr } from "./res/imgRes";
import type { FrameStageCfg } from "./api/apiModel";
import type { GCfgMgr } from "./global";

export interface StageCfgAdd {
    clear(): Promise<void>;
    addStageCfg(cfg: FrameStageCfg): void
    appendStageChildrenCfg(cfg: CfgStr): void
}

export class SgRes {
    private _imgMgr: ImgMgr
    private _stageCfgAdd: StageCfgAdd
    private _globarMgr: GCfgMgr
    constructor(cfgMgr: StageCfgAdd, globarMgr: GCfgMgr) {
        this._imgMgr = new ImgMgr()
        this._stageCfgAdd = cfgMgr
        this._globarMgr = globarMgr
    }

    async clear() {
        this._imgMgr.clear()
        this._stageCfgAdd.clear()
    }

    get imgMgr() { return this._imgMgr }
    get stageCfgMgr() { return this._stageCfgAdd }
    get globarMgr() { return this._globarMgr }
    getImg(key: string) { return this.imgMgr.get(key) }
    getImgGroup(key: string) { return this.imgMgr.getGroup(key)! }

    img(key: string, scale?: { w?: number, h?: number, filter?: (g: ImgGroupInfo) => Img.ImgDef }) {
        const g = this.getImgGroup(key)
        const img = scale?.filter ? scale.filter(g) : g.hasDef()
        const imgUrl = img?.getImg(scale?.w, scale?.h).imgDataUrl
        return `url(${imgUrl})`
    }
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
