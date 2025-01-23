import { ref, shallowReactive, shallowRef, type App, type Ref, type ShallowRef } from "vue";
import { DataMgr } from "./dataMgr";
import { SgApi } from "./api";
import { SingleComp, type FunPanComp, type Traceable } from "./model";
import { SgRes } from "./res";
import { RadioGroup, StageMgr } from "./stage";
import { AxiosError } from "axios";
import { CfgStr } from "./cfg";
import { DivBg, installClickout, installMsg } from "./directives";
import { gFunPanComps } from "./constant";



export class SanGuo {

    el: Ref<HTMLDivElement>
    dataMgr: DataMgr
    api: SgApi
    bar = ref<Traceable>({ pct: 0, msg: '' })
    res: SgRes
    ready = ref(false)
    stageMgr: StageMgr
    funPanMgr = new SingleComp<FunPanComp>(gFunPanComps)

    constructor(el: Ref<HTMLDivElement>) {
        this.el = el
        this.dataMgr = new DataMgr(this)
        this.api = new SgApi(this.dataMgr, this)
        this.res = new SgRes(this.bar, this.api)
        this.stageMgr = new StageMgr(this.dataMgr)

        CfgStr.imgGroupGetter = shallowRef((key: string) => {
            return this.res.getImgMgr().getGroup(key)
        })
        RadioGroup.dataMgr = shallowRef(this.dataMgr)

    }

    testStage() { this.stageMgr.testStage() }

    async setup(tr: Ref<Traceable | undefined> | ShallowRef<Traceable | undefined>) {
        tr.value = this.bar.value
        const st = new Date().getTime()
        await this.res.loadCfg()
        this.stageMgr.updateCfg(this.res.getStageCfgMgr())
        await this.refreshPlay()
        this.ready.value = true
    }

    async refreshPlay() {
        let play = undefined;
        try {
            play = await this.api.playApi.getPlay()
            this.dataMgr.setPlay(play)
            this.stageMgr.testStage()
        } catch (error) {
            error instanceof AxiosError && this.handlerErr(error)
        }
    }

    async handlerErr(err: AxiosError) {
        if (err.response && err.response.data) {
            if (err.response.status == 401) {
                this.api.clear()
                this.dataMgr.setPlay(undefined)
            } else if ((err.response.data as any).message) {
                this.dataMgr.pushMsg((err.response?.data as any).message)
            }
        }
        this.stageMgr.testStage()
    }

    pushMsg(msg: string) {
        msg && this.dataMgr.pushMsg(msg)
    }

}

namespace sgGame {

    export const sg = shallowRef<SanGuo>()

    export function install(app: App) {
        installMsg(app, sg)
        installClickout(app, sg)
        DivBg.installBg(app, sg)
        DivBg.installSize(app, sg)
    }
}
export default sgGame
