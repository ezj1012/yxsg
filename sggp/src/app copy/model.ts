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



