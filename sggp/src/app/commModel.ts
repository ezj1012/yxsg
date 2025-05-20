import { ref, type Component } from "vue"
import type { FrameStageCfg } from "./api/apiModel"
import type { SgRes } from "./res"

export interface Traceable {
    msg: string
    pct: number
}

export interface ErrMsg {
    content: string
    ok?: () => Promise<void> | void
    cancel?: () => Promise<void> | void
}

export interface HoverMsg {
    clientX: number
    clientY: number
    el: any
    content: any
    isHtml?: boolean
}

export interface HoverMsgDef {
    content: any
    isHover?: (e: MouseEvent) => boolean
    in?: (e: MouseEvent, el: any) => void
    out?: (e: MouseEvent, el: any) => void
}




export interface Shapable {
    x?: number
    y?: number
    w?: number
    h?: number
    z?: number
    styles?: { [key: string]: string }
}

export interface Textable {
    color: string
    size: number
    content: string
    caretColor?: string
    html?: boolean
    styles?: { [key: string]: string }
}





export interface Compable extends Shapable {
    id?: string
    key: string
    name?: string
    comp: Component
}

export interface FunPanComp extends Compable, Textable {
    show?: boolean
}

export interface StageComp extends Compable { }

export class SingleComp<T extends Compable> {
    cache = new Map<string, T>()
    curComp = ref<string>()
    constructor(coms: T[]) {
        coms.forEach(com => this.cache.set(com.key, com))
    }

    getComp(): T | undefined {
        return this.curComp.value ? this.cache.get(this.curComp.value) : undefined
    }

    setComp(key?: string): T | undefined {
        this.curComp.value = key
        console.log('fun 显示为 : ' + this.curComp.value)
        return this.getComp()
    }
}

export class ScrollHelper {
    styles: Record<string, any> = {}
    res: SgRes
    constructor(res: SgRes) {
        this.res = res
        this.styles['--track-up'] = this.res.img("common#scroll_uparr")
        this.styles['--track-up-down'] = this.res.img("common#scroll_uparr", { filter: (g: any) => g.hasDown() })
        this.styles['--track-down'] = this.res.img("common#scroll_downarr")
        this.styles['--track-down-up'] = this.res.img("common#scroll_downarr", { filter: (g: any) => g.hasDown() })
    }

    scrollImg(el: HTMLElement, show: boolean, dataHeight: number) {
        if (show) {
            const height = el.getBoundingClientRect().height
            const newHeight = ((height - 40) * height) / dataHeight;
            this.styles['--track-img'] = this.res.img("common#scroll_track", { w: 19, h: height })
            this.styles['--track-bar'] = this.res.img("common#scroll_bar", { w: 13, h: newHeight })
            this.styles['--track-bar-hover'] = this.res.img("common#scroll_bar", { w: 13, h: newHeight })
            this.styles['overflow-y'] = 'auto'
        } else {
            this.styles['overflow-y'] = 'hidden'
            el.scrollTop = 0
        }
    }
}

export class TableHeaderDef {
    realWidth: number = 0
    constructor(public width: number = 0, public content: string = '', public styles: Record<string, any> = {}) { }
}

export class TableDataRowDef {
    constructor(
        public datas: TableDataCellDef[] = [],
        public source: any = {},
        public empty: boolean = false,
    ) { }
}

export class TableDataCellDef {
    constructor(
        public content: string = '',
        public styles: Record<string, any> = {}
    ) { }
}

export const defaultConverHeader = (headers: any[]) => {
    return headers.map((h: any) => {
        const styles: Record<string, any> = h.styles || {}
        h.width && (styles.width = `${h.width}px`)
        h.color && (styles.color = h.color)
        styles.color = h.color || 'var(--rxsg-table-header-font-color)'
        return new TableHeaderDef(h.width || 0, h.content || '', styles)
    })
}

export const defaultConverDatas = (datas: any[]) => {
    const rowDatas: TableDataRowDef[] = []
    datas.forEach((data: any, idx: number) => {
        const cells: TableDataCellDef[] = []
        for (const d of data) {
            const styles = {
                textAlign: 'center'
            } as any
            cells.push(new TableDataCellDef(d.content || '', styles))
        }
        rowDatas.push(new TableDataRowDef(cells, data))
    });

    return rowDatas
}