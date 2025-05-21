import { ref, shallowRef, type App, type Ref } from "vue";
import { SingleComp, type FunPanComp, type Traceable } from "./commModel";
import { SgApi } from "./api";
import type { IUserToken } from "./api/apiComm";
import type { AxiosError } from "axios";
import type { PlayInfo, User } from "./api/apiModel";
import { StageMgr } from "./stage/stage";
import { SgRes, SgResLoader } from "./res";
import { DataMgr } from "./dataMgr";
import { ErrMsgMgr } from "./msg/errMsg";
import { gFunPanComps } from "./funPanMgr";
import { HoverMsgMgr } from "./msg/hoverMsgMgr";
import { DivBg, installClickout, installMsg } from "./directives";
import type { Stage } from "./stage/absStage";
import { CfgStr } from "./cfg";
import { GCfgMgr } from "./global";
import { PlayMgr } from "./playMgr";
import { BuildMgr } from "./buildMgr";

const userCacheKey = 'currentUser'
export interface SgCtx {

    refresh(): Promise<void>

    get user(): User | undefined

    get play(): PlayInfo | undefined

    get stage(): Stage
    get res(): SgRes
    get api(): SgApi
    get userMgr(): UserMgr
    get stageMgr(): StageMgr
    get playMgr(): PlayMgr
    get dataMgr(): DataMgr
    get errMsgMgr(): ErrMsgMgr
    get funPanMgr(): SingleComp<FunPanComp>
    hoverMsgMgr: HoverMsgMgr
    get gCfgMgr(): GCfgMgr
    get buildMgr(): BuildMgr
}

export class SgCtxImpl implements SgCtx {

    private _api: SgApi
    private _stageMgr: StageMgr
    private _userMgr: UserMgr
    private _resLoader: SgResLoader
    private _res: SgRes
    private _dataMgr: DataMgr
    private _funPanMgr = new SingleComp<FunPanComp>(gFunPanComps)
    private _errMsgMgr: ErrMsgMgr
    private _hoverMsgMgr: HoverMsgMgr
    private _playMgr: PlayMgr
    private _gCfgMgr: GCfgMgr
    private _buildMgr: BuildMgr
    constructor() {
        this._userMgr = new UserMgr(this)
        this._api = new SgApi(this._userMgr)
        this._dataMgr = new DataMgr()
        this._stageMgr = new StageMgr(this)
        this._gCfgMgr = new GCfgMgr(this)
        this._res = new SgRes(this._stageMgr, this._gCfgMgr)
        this._resLoader = new SgResLoader(this._res, this._api)
        this._errMsgMgr = new ErrMsgMgr()
        this._hoverMsgMgr = new HoverMsgMgr(this)
        this._playMgr = new PlayMgr(this)
        this._buildMgr = new BuildMgr(this)
    }

    async loadRes(tr: Ref<Traceable>) {
        await this._resLoader.load(false, tr.value)
    }

    async refresh() {
        await this._playMgr.refreshPlay()
        this.stageMgr.stage
    }


    get api() { return this._api }
    get user() { return this._userMgr?.user }
    get play() { return this._playMgr?.play }
    get stage() { return this._stageMgr.stage }
    get userMgr() { return this._userMgr }
    get stageMgr() { return this._stageMgr }
    get dataMgr() { return this._dataMgr }
    get errMsgMgr() { return this._errMsgMgr }
    get funPanMgr() { return this._funPanMgr }
    get hoverMsgMgr() { return this._hoverMsgMgr }
    get res() { return this._res }
    get playMgr() { return this._playMgr }
    get gCfgMgr() { return this._gCfgMgr }
    get buildMgr() { return this._buildMgr }
}

export class SanGuo {
    debug = ref(false)
    ready = ref(false)
    private _ctx: SgCtxImpl
    private _el: Ref<HTMLDivElement>
    constructor(el: Ref<HTMLDivElement>) {
        this._el = el
        this._ctx = new SgCtxImpl()
        CfgStr.imgGroupGetter = shallowRef((key: string) => {
            return this._ctx.res.imgMgr.getGroup(key)
        })
    }

    /**
     * 启动服务,加载资源
     * @param tr 
     */
    async setup(tr: Ref<Traceable> = shallowRef({ msg: '', pct: 0 })) {
        console.log('setup')
        await this._ctx.loadRes(tr)
        await this._ctx.refresh()
        this.ready.value = true
        console.log('setup', this.ready.value)
    }

    get ctx(): SgCtx {
        return this._ctx
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



class UserMgr implements IUserToken {
    private _user?: User
    private _ctx: SgCtx
    constructor(ctx: SgCtx) {
        this._ctx = ctx
        const curUser = localStorage.getItem(userCacheKey)
        if (curUser) {
            try {
                this._user = JSON.parse(curUser)
            } catch (error) {
            }
        }
    }

    async login(username: string, passwd: string) {
        const user = await this._ctx.api.userApi.login(username, passwd)
        this._user = user
        localStorage.setItem(userCacheKey, this._user ? JSON.stringify(this._user) : '')
    }

    async logout() {
        localStorage.removeItem(userCacheKey)
        // this._playMgr.setPlay(undefined)
        this._user = undefined
        this._ctx.stage
    }

    handler401() { this.logout() }
    getToken(): string | undefined { return this._user?.token }
    getTokenKey(): string { return 'yxsg' }
    get user() { return this._user }
}
