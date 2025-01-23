import { ref, type Component } from "vue"
import type { CfgStr } from "./cfg"

export class User {
    token: string
    name: string
    id: number
    constructor(token: string, name: string, id: number) {
        this.token = token
        this.name = name
        this.id = id
    }
}

export interface Traceable {
    msg: string
    pct: number
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

export interface FrameStageCfg {
    key: string
    name: string
    comps: string[]
}

export interface FrameCfg {
    rsmHttpRoot: string
    imgs: string[]
    stages: FrameStageCfg[]
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




export interface Compable extends Shapable {
    id?: string
    key: string
    name?: string
    comp: Component
}

export interface FunPanComp extends Compable, Textable {

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