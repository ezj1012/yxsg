import axios, { AxiosError, AxiosHeaders } from "axios";
import type { SanGuo } from "./sg";
import { User, type FrameCfg } from "./model";
import type { GlobalCfg, MemMapTile, PlayInfo, Province } from "./modelData";

export class Result {
    public success: boolean = false;
    public message: string | null = null;
    public data: any = null;
    public code: number = 200;
}

export interface PageResult<T> {
    pageNum: number;
    pageSize: number;
    totalPages: number;
    totalRows: number;
    data: T[];
}

export interface PageCdt<T> {
    pageNum: number;
    pageSize: number;
    cdt: T;
}

export function emptyPageResult<T>(): PageResult<T> {
    return {
        pageNum: 0,
        pageSize: 0,
        totalPages: 0,
        totalRows: 0,
        data: [] as T[],
    }
}

export const baseURL = "/sg"
axios.defaults.timeout = 120000; // 超时时间
axios.defaults.baseURL = baseURL; // 请求前缀路径
axios.defaults.headers["Content-Type"] = "application/json;charset=UTF-8"; // 请求前缀路径
axios.defaults.headers["x-requested-with"] = "XMLHttpRequest"; // ajax


export interface UserCache {
    getCurrentUser(): User | undefined
    setCurrentUser(user?: User): void
}


export class UserApi {
    sgApi: SgApi
    constructor(sgApi: SgApi) {
        this.sgApi = sgApi
    }


    async login(username: string, password: string): Promise<void> {
        try {
            const data = (await this.sgApi.post('/play/login', { username, password })).data as { id: number, name: string, token: string }

            this.sgApi.userCache.setCurrentUser(new User(data.token, data.name, data.id))
            localStorage.setItem(SgApi.useCacheKey, this.sgApi.userCache.getCurrentUser() ? JSON.stringify(this.sgApi.userCache.getCurrentUser()) : '')
        } catch (error) {
            this.sgApi.clear()
            throw error
        }
    }

}

// 静态数据
export class CfgApi {
    sgApi: SgApi
    constructor(sgApi: SgApi) {
        this.sgApi = sgApi
    }

    async loadFrameCfg(): Promise<FrameCfg> {
        return (await this.sgApi.get('/cfg/frame')).data
    }
    async loadGlobalCfg(): Promise<GlobalCfg> {
        return (await this.sgApi.get('/cfg/global')).data
    }
}

// 动态数据
export class EnvApi {

    sgApi: SgApi
    constructor(sgApi: SgApi) {
        this.sgApi = sgApi
    }

    async getProvinces(): Promise<Province[]> {
        return (await this.sgApi.get('/env/provinces')).data
    }

    async getMapTiles(params: { x: number, y: number, xw: number, yw: number }): Promise<MemMapTile[]> {
        return (await this.sgApi.post('/env/maptiles', params)).data
    }

}


export class PlayApi {

    sgApi: SgApi
    constructor(sgApi: SgApi) {
        this.sgApi = sgApi
    }

    async getPlay(): Promise<PlayInfo> {
        const params: Record<string, any> = {}
        // const dataMgr = this.sgApi.sg.dataMgr
        params.cityId = this.sgApi.sg.dataMgr.getByKey("playing#player_lastCity")
        return (await this.sgApi.post('/play/info', params)).data
    }

    async regplay(regParams: { name: any; gender: number; icon: any; provinceId: any; agreeRules: boolean; }): Promise<PlayInfo> {
        return (await this.sgApi.post('/play/reg', regParams)).data
    }

}


export class SgApi {

    static useCacheKey = 'currentUser'
    userCache: UserCache
    sg: SanGuo
    userApi: UserApi
    cfgApi: CfgApi
    playApi: PlayApi;
    envApi: EnvApi;

    constructor(userCache: UserCache, sg: SanGuo) {
        this.userCache = userCache
        this.sg = sg
        this.userApi = new UserApi(this)
        this.cfgApi = new CfgApi(this)
        this.playApi = new PlayApi(this)
        this.envApi = new EnvApi(this)

        const curUser = localStorage.getItem(SgApi.useCacheKey)
        if (curUser) {
            this.userCache.setCurrentUser(JSON.parse(curUser))
        }
    }

    async get(url: string, data?: any): Promise<Result> {
        return axios.get(url, {
            headers: this.headers()
        }).then(response => response.data as Result)
            .catch(err => {
                if (err instanceof AxiosError) {
                    if (err.response?.status == 401) {
                        this.userCache.setCurrentUser(undefined)
                    }
                }
                throw err
            })
    }

    async post(url: string, data?: any): Promise<Result> {
        return axios.post(url, data, {
            headers: this.headers(),
        }).then(response => response.data as Result)
            .catch(err => {
                if (err instanceof AxiosError) {
                    if (err.response?.status == 401) {
                        this.clear()
                    }
                }
                throw err
            })
    }

    private headers() {
        const h = new AxiosHeaders()
        h.set('yxsg', this.userCache.getCurrentUser()?.token || '')
        return h
    }

    clear() {
        localStorage.removeItem(SgApi.useCacheKey)
        this.userCache.setCurrentUser(undefined)
        this.sg.testStage()
    }
}