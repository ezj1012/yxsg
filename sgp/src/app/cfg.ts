import type { Ref } from "vue"
import type { HoverMsgDef, Shapable, Textable } from "./commModel"
import type { ImgDefCache, ImgGroupCache, ImgGroupInfo } from "./utils/img"

/**
 * 配置描述的内容 
 */
export enum CfgType {
    /**   单纯的图片信息*/
    img = 'I',
    /** 图片实例信息,根据key获取要显示的图片 */
    imgComp = 'IMG', // 
    /** 图片按钮 */
    imgBtn = 'I_BTN',
    /** */
    imgPanBtn = 'I_PTN',
    /** */
    text = 'TEXT',
    /** */
    textInput = 'TEXT_I',
    /** */
    keyValue = 'KV',
    /** */
    bg = 'BG',//
    /** */
    select = 'SCT', //
    /** */
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


export function isBtn(type: string | CfgType) {
    return type == CfgType.imgPanBtn ||
        type == CfgType.imgBtn
}

export function isImg(type: string) {
    return type == CfgType.img
        || type == CfgType.imgComp
}

/** 单选框 */
export interface RadioCfg {
    groupKey?: string
    value?: string
    isRadioGroup: boolean   // false 单个(),true: 多个按钮表示同一个值(男,女)
    isDefSct: boolean
}

export function useCfgStr(imgCache: ImgDefCache, imgGroupCache: ImgGroupCache) {
    function parseCfg(cfg: string): CfgStr {
        const cfgStr = new CfgStr(cfg, imgCache, imgGroupCache)
        return cfgStr
    }
    return { parseCfg }
}


export class CfgStr {
    private cfgMap = new Map<string, string>()
    private _stage: string = ''
    size: Shapable
    constructor(public cfg: string, private imgCache: ImgDefCache, private imgGroupCache: ImgGroupCache) {
        cfg.split(';').forEach(c => {
            const idx = c.indexOf(':')
            if (idx == -1) {
                this.cfgMap.set(c, c)
            } else {
                this.cfgMap.set(c.substring(0, idx), c.substring(idx + 1))
            }
        })
        const key = this.key()
        const idx = key.indexOf("#")
        if (idx > 0) {
            this._stage = key.substring(0, idx)
        }
        this.size = parseCfgSize(this.get(CfgKey.size), this.imgGroup)
    }

    get(key: CfgKey) { return this.cfgMap.get(key) }
    key() { return this.cfgMap.get(CfgKey.key) || '' }
    type() { return this.cfgMap.get(CfgKey.type) || 'I' }
    isPixel() { return this.cfgMap.has(CfgKey.imgPixel) }
    isBtn() { return isBtn(this.type()) }
    isImg() { return isImg(this.type()) }
    show() { return !this.cfgMap.has(CfgKey.hide) }
    refComp() { return this.cfgMap.get(CfgKey.refComp) }
    get stage() { return this._stage }

    get imgGroup() {
        if (this.cfgMap.has(CfgKey.refImg)) {
            return this.imgGroupCache.get(this.cfgMap.get(CfgKey.refImg) || "")
        } else if (isImgType(this.cfgMap)) {
            return this.imgGroupCache.get(this.key())
        }
        return undefined
    }

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

    parseCfg(compInfo: {
        key?: Ref<String | undefined>
        type?: Ref<CfgType | string | undefined>
        size?: Ref<Shapable | undefined>
        imgGroup?: Ref<ImgGroupInfo | undefined>
        text?: Ref<Textable | undefined>
        title?: Ref<Textable | undefined>
        html?: Ref<boolean>
        dis?: Ref<boolean>
        styles?: Ref<{ [key: string]: any } | undefined>
        msg?: Ref<HoverMsgDef | undefined>
        sctable?: Ref<boolean>
        sctableDefVal?: Ref<RadioCfg>
    }) {
        compInfo.key && (compInfo.key.value = this.key())
        compInfo.type && (compInfo.type.value = this.type())
        compInfo.imgGroup && (compInfo.imgGroup.value = this.imgGroup)
        compInfo.size && (compInfo.size.value = this.size)
        compInfo.text && this.cfgMap.has(CfgKey.text) && parseCfgText(this.cfgMap.get(CfgKey.text)!, compInfo.text)
        compInfo.title && this.cfgMap.has(CfgKey.title) && parseCfgText(this.cfgMap.get(CfgKey.title)!, compInfo.title)
        compInfo.html && this.cfgMap.has(CfgKey.textHtml) && (compInfo.html.value = this.cfgMap.get(CfgKey.textHtml) === undefined || this.cfgMap.get(CfgKey.textHtml)! === 'false' ? false : true)
        compInfo.sctable && (compInfo.sctable.value = this.cfgMap.has(CfgKey.sctable) ? true : false)
        compInfo.sctable?.value && compInfo.sctableDefVal && (compInfo.sctableDefVal.value = this.radio()!)

        compInfo.msg && this.cfgMap.has(CfgKey.simpleMsg) && parseMsg(this.cfgMap.get(CfgKey.simpleMsg)!, compInfo.msg)

        compInfo.dis && this.cfgMap.has(CfgKey.disable) && (compInfo.dis.value = true)
        compInfo.styles && this.cfgMap.has(CfgKey.styles) && (compInfo.styles.value = parseStyles(this.cfgMap.get(CfgKey.styles)!))
    }
}

function parseMsg(cfg: string, msg: Ref<HoverMsgDef | undefined>) {
    if (cfg.includes(',')) {
        const cc = cfg.split(',')
        msg.value = {
            content: { type: cc[0], cfg }
        }
    } else {
        msg.value = { content: cfg }
    }
}

function parseStyles(styleLines?: string) {
    const styles: { [key: string]: any } = {}
    styleLines && atob(styleLines).trim().split(";").forEach(line => {
        const idx = line.indexOf(":")
        if (idx > 0 && idx + 1 < line.length) {
            const key = line.substring(0, idx).trim()
            const value = line.substring(idx + 1).trim()
            styles[key] = value
        }
    })
    return styles
}

export function parseCfgText(cfg: string, text: Ref<Textable | undefined>) {
    const t = new Map<string, string>()
    cfg.split(',').forEach(c => {
        const idx = c.indexOf(':')
        if (idx == -1) {
            t.set(c, c)
        } else {
            t.set(c.substring(0, idx), c.substring(idx + 1))
        }
    })
    const txt: Textable = {
        color: t.get(CfgKey.textColor) || '#FFF',
        size: t.get(CfgKey.textFontSize) ? Number(t.get(CfgKey.textFontSize)) : 12,
        content: t.get(CfgKey.textContent) || '',
        caretColor: t.get(CfgKey.textCaretColor),
        html: false,
        styles: {}
    }
    txt.caretColor && (txt.styles!.caretColor = txt.caretColor)
    txt.color && (txt.styles!.color = txt.color)
    txt.size && (txt.styles!.fontSize = `${txt.size}px`)
    text.value = txt
}

function parseCfgSize(cfg?: String, imgGroup?: ImgGroupInfo) {
    const size: Shapable = { x: 0, y: 0, w: 0, h: 0, z: 1, styles: {} }
    if (!cfg) {
        if (imgGroup) {
            size.w = imgGroup.hasDef()?.width
            size.h = imgGroup.hasDef()?.height
        }
        return size
    }
    const is = cfg.split(',');
    if (is.length < 2) {
        return size;
    }
    size.x = Number(is[0])
    size.y = Number(is[1])
    if (is.length == 3) {
        size.z = Number(is[2])
        if (imgGroup) {
            size.w = imgGroup.hasDef()!.width
            size.h = imgGroup.hasDef()!.height
        }
    } else if (is.length > 3) {
        size.w = Number(is[2])
        size.h = Number(is[3])
        if (is.length == 5) {
            size.z = Number(is[4])
        }
    }

    size.styles!.left = `${size.x}px`
    size.styles!.top = `${size.y}px`
    size.styles!.width = `${size.w}px`
    size.styles!.height = `${size.h}px`
    size.styles!.zIndex = `${size.z}`
    return size
}


function isImgType(t: Map<string, string>) {
    return t.get(CfgKey.type) === 'I_PTN'
}
