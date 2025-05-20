import { ref, shallowRef, type App, type Ref } from "vue";
import type { Traceable } from "./commModel";
import { SgApi } from "./api";
import type { IUserToken } from "./api/apiComm";
import type { AxiosError } from "axios";
import type { PlayInfo, User } from "./api/apiModel";
import { StageMgr, type Stage } from "./stage";
import { SgRes, SgResLoader } from "./res";

const userCacheKey = 'currentUser'
export interface SgCtx {

    refresh(): Promise<void>

    get user(): User | undefined

    get play(): PlayInfo | undefined

    get stage(): Stage

    get stageMgr(): StageMgr
}

export class SgCtxImpl implements SgCtx {

    private _api: SgApi
    private _stageMgr: StageMgr
    private _userMgr: UserMgr
    private _resLoader: SgResLoader
    private _res: SgRes

    constructor() {
        this._userMgr = new UserMgr(this)
        this._api = new SgApi(this._userMgr)
        this._stageMgr = new StageMgr(this)
        this._res = new SgRes()
        this._resLoader = new SgResLoader(this._res, this._api)


    }

    async loadRes(tr: Ref<Traceable>) {
        await this._resLoader.load(tr)
    }

    async refresh() {
        this.stageMgr.stage
    }


    get api() { return this._api }
    get user() { return this._userMgr.user }
    get play() { return undefined }
    get stage() { return this._stageMgr.stage }
    get stageMgr(): StageMgr { return this._stageMgr }
}

export class SanGuo {
    debug = ref(false)
    ready = ref(false)
    private _ctx: SgCtxImpl
    private _el: Ref<HTMLDivElement>
    constructor(el: Ref<HTMLDivElement>) {
        this._el = el
        this._ctx = new SgCtxImpl()
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
        // installMsg(app, sg)
        // installClickout(app, sg)
        // DivBg.installBg(app, sg)
        // DivBg.installSize(app, sg)
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
        throw new Error("Method not implemented.");
        this._ctx.stage
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
