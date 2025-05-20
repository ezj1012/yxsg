import { shallowRef } from "vue";
import type { FrameCfg } from "../api/apiModel";
import { CfgKey, CfgStr, CfgType } from "../cfg";
import { Img } from "../utils/img";

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

export function configuerImgs(cfgs: FrameCfg, imgMgr: ImgMgr): CfgStr[] {
    const comps: CfgStr[] = []

    cfgs.imgs.forEach(url => {
        const ir = new CfgStr(url, true);
        const key = ir.key()
        const scale = ir.get(CfgKey.imgSacle)
        const pixel = ir.isPixel()

        const defs: Img.ImgDef[] = []
        const imgPath = key.replaceAll("#", "/");

        genImgAndPutCache(imgMgr, defs, key, `${cfgs.rsmHttpRoot}${imgPath}.png`, pixel, scale)
        ir.get(CfgKey.imgOn) && (genImgAndPutCache(imgMgr, defs, key + "_on", `${cfgs.rsmHttpRoot}${imgPath}_on.png`, pixel, scale))
        ir.get(CfgKey.imgSctOn) && (genImgAndPutCache(imgMgr, defs, key + "_son", `${cfgs.rsmHttpRoot}${imgPath}_son.png`, pixel, scale))
        ir.get(CfgKey.imgDown) && (genImgAndPutCache(imgMgr, defs, key + "_down", `${cfgs.rsmHttpRoot}${imgPath}_down.png`, pixel, scale))
        ir.get(CfgKey.imgAlarm) && (genImgAndPutCache(imgMgr, defs, key + "_alarm", `${cfgs.rsmHttpRoot}${imgPath}_alarm.png`, pixel, scale))
        ir.get(CfgKey.imgSct) && (genImgAndPutCache(imgMgr, defs, key + "_sct", `${cfgs.rsmHttpRoot}${imgPath}_sct.png`, pixel, scale))
        ir.get(CfgKey.imgDis) && (genImgAndPutCache(imgMgr, defs, key + "_dis", `${cfgs.rsmHttpRoot}${imgPath}_dis.png`, pixel, scale))
        let grInfo = imgMgr.getGroups().get(key)
        if (grInfo) {
            grInfo.cfg = ir
        } else {
            grInfo = new ImgGroupInfo(ir, imgMgr)
            imgMgr.getGroups().set(key, grInfo)
        }
        if (ir.type() !== CfgType.img) {
            ir.imgGroup = grInfo
            comps.push(ir)
        }
    })
    return comps
}

function genImgAndPutCache(imgMgr: ImgMgr, defs: Img.ImgDef[], key: string, url: string, pixel: boolean, scale?: string) {
    const img = Img.of(url, pixel, scale)
    imgMgr.getImgs().set(key, img)
    defs.push(img)
}


export class ImgGroupInfo {
    key: string
    cfg: CfgStr
    mgr: { get(key: string): Img.ImgDef | undefined }
    constructor(cfg: CfgStr, mgr: { get(key: string): Img.ImgDef | undefined }) {
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

