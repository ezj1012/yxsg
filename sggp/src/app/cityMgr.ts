import type { CityBuilding, CityBuildingCfg } from "./api/apiModel"
import type { SgCtx } from "./sg"
import type { Img } from "./utils/img"
export class BuildItem {
    static bg?: Img.ImgDef
    static ctx?: SgCtx

    _x: number
    _y: number
    _lx?: number
    _ly?: number
    z: number = 15
    bid: number = 0
    cb?: CityBuilding
    constructor(x: number, y: number) {
        this._x = x
        this._y = y
    }

    get bgUrl() {
        return `url(${this.bgImg?.getDataUrl()})`
    }

    get bgImg() {
        return BuildItem.ctx?.res.getImg(`playing#innercity#${this.buildCfg?.typeName}`) || BuildItem.bg!
    }

    get buildCfg(): CityBuildingCfg | undefined {
        return BuildItem.ctx?.gCfgMgr.cfg?.buildingsMap[this.bid]
    }
    get x() {
        if (this.bid !== 0 && this.bid != 20 && this.bid !== 6 && this.lv > 0 && this.buildCfg) {
            return this._x + 3;
        }
        return this._x
    }


    get y() {
        if (this.bid != 0 && this.bid != 20 && this.bid !== 6 && this.lv > 0 && this.buildCfg) {
            return this._y - 30;
        }
        return this._y
    }


    setBuild(cb?: CityBuilding) {
        this.cb = cb
        this.bid = cb ? cb.bid : 0
    }
    get xp() { return `${this.x}px` }
    get yp() { return `${this.y}px` }
    get wp() { return `${this.bgImg.width}px` }
    get hp() { return `${this.bgImg.height}px` }

    setLvXy(x: number, y: number) {
        this._lx = x
        this._ly = y
        return this
    }

    get lv() { return this.cb?.lv || 0 }
    get lvImg() { return BuildItem.ctx?.res.img(`playing#comm#level_${this.lv}`) }
    get lxp() { return `${this._lx || this.bgImg.width / 2 - 12}px` }
    get lyp() { return `${this._ly || this.bgImg.height / 2}px` }

    get upd() { return this.cb && this.cb.status != 0 }

    getMsg() {
        let msg = '空地'
        if (this.cb && this.cb.id !== 0) {
            msg = this.buildCfg!.name
            if (this.cb.lv == 0) {
                if (this.cb.id == 20) {
                    msg = '无' + msg
                }
                return msg
            } else {
                return `${msg} ${this.cb.lv}级`
            }
        }
        return '空地'
    }
}

export function useInnerBuilds() {
    const bs: BuildItem[] = []
    bs.push(
        new BuildItem(370, 180).setLvXy(155, 35),
        new BuildItem(0, 25).setLvXy(565, 140),
        // 
        new BuildItem(297, 124),
        new BuildItem(352, 152),
        new BuildItem(517, 236),
        new BuildItem(572, 264),

        new BuildItem(242, 152),
        new BuildItem(297, 180),
        new BuildItem(462, 264),
        new BuildItem(517, 292),

        new BuildItem(187, 180),
        new BuildItem(242, 208),
        new BuildItem(297, 236),
        new BuildItem(352, 264),
        new BuildItem(407, 292),
        new BuildItem(462, 320),

        new BuildItem(132, 208),
        new BuildItem(187, 236),
        new BuildItem(242, 264),
        new BuildItem(297, 292),
        new BuildItem(352, 320),
        new BuildItem(407, 348),


        new BuildItem(77, 236),
        new BuildItem(132, 264),
        new BuildItem(187, 292),
        new BuildItem(242, 320),
        new BuildItem(297, 348),
        new BuildItem(352, 376),


        new BuildItem(22, 264),
        new BuildItem(77, 292),
        new BuildItem(132, 320),
        new BuildItem(187, 348),
        new BuildItem(242, 376),
        new BuildItem(297, 404),
    )
    bs[0].z = 1
    bs[1].z = 1
    return bs
}