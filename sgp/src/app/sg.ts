import { shallowRef, type App, type Component, type Ref } from "vue"
import { DivBg } from "./directives"
import type { SgRes } from "./res"
import type { CallBack } from "./dataMgr"
import type { Shapable, Textable } from "./commModel"

export type Sg = {
    res: SgRes,
    // data mgr 
    get(key: any): any,
    set(cfg: any, data: any): void,
    getByKey(key: string): any,
    setByKey(key: string, data: any): void,
    subscribe(cfg: any, call: CallBack, defVal: any): void,
    unsubscribe(cfg: any, call?: CallBack): void,
    subscribeValue(cfg: any, data: Ref<any>, defVal?: any): void,
    unsubscribeValue(cfg: any, data: Ref<any>): void,
    btnRed(key: string, size: string, text: string, act?: string): Component,
}

export class SanGuo { }

namespace sgGame {

    export const sg = shallowRef<SanGuo>()

    export function install(app: App) {
        // installMsg(app, sg)
        // installClickout(app, sg)
        // DivBg.installBg(app, sg)
        DivBg.installSize(app, sg)
    }
}
export default sgGame