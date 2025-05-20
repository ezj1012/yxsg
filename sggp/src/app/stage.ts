import { ref } from "vue";
import type { SgCtx } from "./sg";

export interface Stage {
    onUnmounted?(): Promise<any>;
    onMounted?(): Promise<any>;
    get key(): string
    info: any
}

function useLoginStage() {
    return {
        key: 'login',
        info: {},
        async onUnmounted() { },
        async onMounted() { },
    }
}

function useRegplayStage() {
    return {
        key: 'regplay',
        info: {},
        async onUnmounted() { },
        async onMounted() { },
    }
}

function usePlayingStage() {
    return {
        key: 'playing',
        info: {},
        async onUnmounted() { },
        async onMounted() { },
    }
}


export class StageMgr {
    private _ctx: SgCtx
    private _cahce = new Map<string, Stage>()
    stageKey = ref('login')
    constructor(ctx: SgCtx) {
        this._ctx = ctx
        const loginStage = useLoginStage();
        const regplayStage = useRegplayStage();
        const playStage = usePlayingStage();
        this._cahce.set(loginStage.key, loginStage)
        this._cahce.set(regplayStage.key, regplayStage)
        this._cahce.set(playStage.key, playStage)
        this.stage
    }

    get stage() {
        let stage = 'login'
        if (this._ctx.play) {
            stage = 'playing'
        } else if (this._ctx.user) {
            stage = 'regplay'
        }
        this.stageKey.value = stage
        return this._cahce.get(stage)!
    }

    getStage(key?: string) {
        if (key) {
            return this._cahce.get(key)
        }
        return undefined
    }

}