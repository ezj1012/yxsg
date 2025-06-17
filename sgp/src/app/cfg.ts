import type { Shapable } from "./commModel"
import type { ImgDefCache, ImgGroupCache } from "./utils/img"

export enum CfgType {
    img = 'I',
    imgComp = 'IMG',
    imgBtn = 'I_BTN',
    imgPanBtn = 'I_PTN',
    text = 'TEXT',
    textInput = 'TEXT_I',
    keyValue = 'KV',
    bg = 'BG',//
    select = 'SCT', //
    customComp = 'CST_C',
}


export enum CfgKey {
    refImg = 'RFI',
    refComp = 'RFC',
    type = "T",
    text = 'TXT', // TXT:T:asasadqw,F:12,C:red
    title = "TIT",
    key = 'K',
    size = 'S',
    sctable = 'SCTABLE', //SCTABLE:group1,值,默认值
    action = "ACT",
    styles = "STYL",
    hide = "HIDE",
    disable = "DISABLE",
    simpleMsg = "SMSG",
    pressNoMove = "NOM",
    // 
    textContent = "T",
    textFontSize = "F",
    textColor = "C",
    textCaretColor = "CC",
    textHtml = "H",
    // img
    imgOn = "ION", imgSctOn = "ISON", imgDown = "IDOWN", imgSct = "ISCT", imgDis = "IDIS", imgAlarm = "IALA", //
    imgSacle = "IS", imgPixel = "IP", imgSourceSize = "ISS",
    //
    inputType = "INTT",
    //
    optionMaxSize = "SCT_MS",
}

export interface RadioCfg {
    groupKey?: string
    value?: string
    isRadioGroup: boolean
    isDefSct: boolean
}
export interface CfgImgStr {
    get(key: CfgKey): string | undefined
    key(): string
    isPixel(): boolean
    sacle(): string | undefined
}

export interface CfgStr {
    size: Shapable
    get(key: CfgKey): string | undefined
    key(): string
    type(): string
    isPixel(): boolean
    isBtn(): boolean
    isImg(): boolean
    show(): boolean
    refComp(): string | undefined
    radio(): RadioCfg | undefined

}

class CommCfg implements CfgStr, CfgImgStr {
    _size?: Shapable

    constructor(private cfgMap = new Map<string, string>(), private imgCache: ImgDefCache, private imgGroupCache: ImgGroupCache,) {
    }

    sacle() { return this.cfgMap.get(CfgKey.imgSacle) }
    get(key: CfgKey) { return this.cfgMap.get(key) }
    key() { return this.cfgMap.get(CfgKey.key) || '' }
    type() { return this.cfgMap.get(CfgKey.type) || 'I' }
    isPixel() { return this.cfgMap.get(CfgKey.imgPixel) === undefined ? false : true }
    isBtn() { return isBtn(this.type()) }
    isImg() { return isImg(this.type()) }
    show() { return !this.cfgMap.has(CfgKey.hide) }
    group() { return this.imgGroupCache.get(this.key()) }
    refComp() { return this.cfgMap.get(CfgKey.refComp) }

    get size() { return this._size || {} }

    radio(): RadioCfg | undefined {
        if (this.cfgMap.has(CfgKey.sctable)) {
            const v = this.cfgMap.get(CfgKey.sctable)!
            if (v.indexOf(',') != -1) {
                const r = this.cfgMap.get(CfgKey.sctable)!.split(',')
                return {
                    groupKey: r[0],
                    value: r.length > 1 ? r[1] : this.key(),
                    isRadioGroup: true,
                    isDefSct: r.length > 2 ? r[2] == 'TRUE' : false
                }
            } else {
                return {
                    isRadioGroup: false,
                    isDefSct: v === 'TRUE'
                }
            }
        }

        return undefined
    }

}

export function useCfgStr(imgCache: ImgDefCache, imgGroupCache: ImgGroupCache) {
    function parseImg(cfg: string): CfgStr {
        return {}
    }
    return { parseImg }
}

// export function fromImg(cfg: string): CfgImgStr {
//     const cfgMap = new Map<string, string>()
//     cfg.split(';').forEach(c => {
//         const idx = c.indexOf(':')
//         if (idx == -1) {
//             cfgMap.set(c, c)
//         } else {
//             cfgMap.set(c.substring(0, idx), c.substring(idx + 1))
//         }
//     })
//     const commCfg = new CommCfg(cfgMap)
//     return commCfg;
// }


// export class CfgStr1 {
//     cfgMap = new Map<string, string>()
//     cfg: string
//     stage: string = ''
//     imgGroup?: ImgGroupInfo
//     size: Shapable
//     constructor(cfg: string, isFromImgCfg = false) {
//         this.cfg = cfg
//         cfg.split(';').forEach(c => {
//             const idx = c.indexOf(':')
//             if (idx == -1) {
//                 this.cfgMap.set(c, c)
//             } else {
//                 this.cfgMap.set(c.substring(0, idx), c.substring(idx + 1))
//             }
//         })
//         const key = this.key()
//         const idx = key.indexOf("#")
//         if (idx > 0) {
//             this.stage = key.substring(0, idx)
//         }
//         let imgKey = isFromImgCfg ? key : this.cfgMap.has(CfgKey.refImg) ? this.cfgMap.get(CfgKey.refImg)! : isImgType(this.cfgMap) && this.cfgMap.has(CfgKey.key) ? this.cfgMap.get(CfgKey.key)! : undefined
//         imgKey && (this.imgGroup = CfgStr.imgGroupGetter.value ? CfgStr.imgGroupGetter.value(imgKey) : undefined)
//         this.size = parseCfgSize(this.get(CfgKey.size), this.imgGroup)
//     }

//     get(key: CfgKey) { return this.cfgMap.get(key) }
//     key() { return this.cfgMap.get(CfgKey.key) || '' }
//     type() { return this.cfgMap.get(CfgKey.type) || 'I' }
//     isPixel() { return this.cfgMap.get(CfgKey.imgPixel) === undefined ? false : true }
//     isBtn() { return isBtn(this.type()) }
//     isImg() { return isImg(this.type()) }
//     show() { return !this.cfgMap.has(CfgKey.hide) }
//     refComp() { return this.cfgMap.get(CfgKey.refComp) }

//     radio(): RadioCfg | undefined {
//         if (this.cfgMap.has(CfgKey.sctable)) {
//             const v = this.cfgMap.get(CfgKey.sctable)!
//             if (v.indexOf(',') != -1) {
//                 const r = this.cfgMap.get(CfgKey.sctable)!.split(',')
//                 return {
//                     groupKey: r[0],
//                     value: r.length > 1 ? r[1] : this.key(),
//                     isRadioGroup: true,
//                     isDefSct: r.length > 2 ? r[2] == 'TRUE' : false
//                 }
//             } else {
//                 return {
//                     isRadioGroup: false,
//                     isDefSct: v === 'TRUE'
//                 }
//             }
//         }

//         return undefined
//     }

//     parseCfg(compInfo: {
//         key?: Ref<String | undefined>
//         type?: Ref<CfgType | string | undefined>
//         size?: Ref<Shapable | undefined>
//         imgGroup?: Ref<ImgGroupInfo | undefined>
//         text?: Ref<Textable | undefined>
//         title?: Ref<Textable | undefined>
//         html?: Ref<boolean>
//         dis?: Ref<boolean>
//         styles?: Ref<{ [key: string]: any } | undefined>
//         msg?: Ref<HoverMsgDef | undefined>
//         sctable?: Ref<boolean>
//         sctableDefVal?: Ref<RadioCfg>
//     })
// }

export function isBtn(type: string | CfgType) {
    return type == CfgType.imgPanBtn ||
        type == CfgType.imgBtn
}

export function isImg(type: string | CfgType) {
    return type == CfgType.img
        || type == CfgType.imgComp
}