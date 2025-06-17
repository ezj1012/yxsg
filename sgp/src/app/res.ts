import type { RsmApi } from "./api";
import { CfgKey, CfgType, useCfgStr, type CfgStr } from "./cfg";

import type { Traceable } from "./commModel";
import { ImgDef, ImgGroupInfo, loadImages, type ImgGroupCache } from "./utils/img";


export class Res {
    private imgs = new Map<string, ImgDef>()
    private imgGroups = new Map<string, ImgGroupInfo>()
    private parseImg: (cfg: string) => CfgStr
    constructor(private rsmApi: RsmApi) {
        const { parseImg } = useCfgStr(this.imgs, this.imgGroups)
        this.parseImg = parseImg
    }

    async loadRes(bar: Traceable) {

        await new Promise(resolve => {
            const imgs = this.imgs
            function genImgAndPutCache(key: string, url: string, pixel: boolean, scale?: string) {
                imgs.set(key, ImgDef.of(url, pixel, scale))
            }

            this.rsmApi.loadFrameCfg().then(frameCfg => {
                bar.pct = 10
                const comps: CfgStr[] = []
                // 加载图片类型
                frameCfg.imgs.map(cfg => this.parseImg(cfg)).forEach(ir => {

                    const key = ir.key()
                    const scale = ir.get(CfgKey.imgSacle)
                    const pixel = ir.isPixel()

                    const imgPath = key.replaceAll("#", "/");
                    genImgAndPutCache(key, `${this.rsmApi.rsmHttpRoot}${imgPath}.png`, pixel, scale)
                    ir.get(CfgKey.imgOn) && (genImgAndPutCache(key + "_on", `${this.rsmApi.rsmHttpRoot}${imgPath}_on.png`, pixel, scale))
                    ir.get(CfgKey.imgSctOn) && (genImgAndPutCache(key + "_son", `${this.rsmApi.rsmHttpRoot}${imgPath}_son.png`, pixel, scale))
                    ir.get(CfgKey.imgDown) && (genImgAndPutCache(key + "_down", `${this.rsmApi.rsmHttpRoot}${imgPath}_down.png`, pixel, scale))
                    ir.get(CfgKey.imgAlarm) && (genImgAndPutCache(key + "_alarm", `${this.rsmApi.rsmHttpRoot}${imgPath}_alarm.png`, pixel, scale))
                    ir.get(CfgKey.imgSct) && (genImgAndPutCache(key + "_sct", `${this.rsmApi.rsmHttpRoot}${imgPath}_sct.png`, pixel, scale))
                    ir.get(CfgKey.imgDis) && (genImgAndPutCache(key + "_dis", `${this.rsmApi.rsmHttpRoot}${imgPath}_dis.png`, pixel, scale))

                    // 初始化组
                    !this.imgGroups.has(key) && this.imgGroups.set(key, new ImgGroupInfo(key, imgs))

                    // 图片中有直接定义组件的
                    ir.type() !== CfgType.img && (comps.push(ir))
                })
                bar.pct = 30

                // 更新舞台配置
                // data.stages?.forEach(cfg => { fsMgr.addStageCfg(cfg) })
                // 更新舞台图片配置
                // comps.forEach(cfg => { fsMgr.appendStageChildrenCfg(cfg) })
                // bar.pct = 50

                resolve(bar)
            })



        })


        bar.msg = '加载图片资源'
        bar.pct = 0
        await new Promise(resolve => {
            const defs = Array.from(this.imgs.values())
            let count = 0;
            loadImages(defs, (def: ImgDef, err: any) => {
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
