import { AbsApi, type IUserToken } from "./apiComm";
import type { CfgGoods, FrameCfg, GlobalCfg, MemMapTile, PlayInfo, Province } from "./apiModel";

export class SgApi extends AbsApi {
    // userApi: UserApi
    // cfgApi: CfgApi
    // playApi: PlayApi;
    // envApi: EnvApi;

    constructor(token: IUserToken, public rsmApi = new RsmApi()) {
        super(token);
        // this.userApi = new UserApi(this)
        // this.cfgApi = new CfgApi(this)
        // this.playApi = new PlayApi(this)
        // this.envApi = new EnvApi(this)
    }

}

export class RsmApi {
    constructor(public rsmHttpRoot: string = "/rsm/") { }

    async loadFrameCfg(): Promise<FrameCfg> {
        return (await fetch(`${this.rsmHttpRoot}cfgFrame.json`)).text().then(a => {
            const s = a.split("\n").filter(line => !line.trimStart().startsWith('//')).join("")
            return JSON.parse(s)
        })
    }

}



// export class UserApi {
//     sgApi: AbsApi
//     constructor(sgApi: AbsApi) {
//         this.sgApi = sgApi
//     }

//     async login(username: string, password: string): Promise<{ id: number, name: string, token: string }> {
//         return (await this.sgApi.post('/play/login', { username, password })).data as { id: number, name: string, token: string }
//     }

// }

// // 静态数据
// export class CfgApi {
//     sgApi: SgApi
//     constructor(sgApi: SgApi) {
//         this.sgApi = sgApi
//     }

//     async loadFrameCfg(): Promise<FrameCfg> {
//         return (await this.sgApi.get('/cfg/frame')).data
//     }

//     async loadGlobalCfg(): Promise<GlobalCfg> {
//         return (await this.sgApi.get('/cfg/global')).data
//     }

//     async getGoods(params: number[]): Promise<CfgGoods[]> {
//         return (await this.sgApi.post('/cfg/goods', params)).data
//     }
// }

// // 动态数据
// export class EnvApi {

//     sgApi: SgApi
//     constructor(sgApi: SgApi) {
//         this.sgApi = sgApi
//     }

//     async getProvinces(): Promise<Province[]> {
//         return (await this.sgApi.get('/env/provinces')).data
//     }

//     async getMapTiles(params: { x: number, y: number, xw: number, yw: number }): Promise<MemMapTile[]> {
//         return (await this.sgApi.post('/env/maptiles', params)).data
//     }


// }

// export class PlayApi {

//     sgApi: SgApi
//     constructor(sgApi: SgApi) {
//         this.sgApi = sgApi
//     }

//     async getPlay(params: Record<string, any> = {}): Promise<PlayInfo> {
//         return (await this.sgApi.post('/play/info', params)).data
//     }

//     async regplay(regParams: { name: any; gender: number; icon: any; provinceId: any; agreeRules: boolean; }): Promise<PlayInfo> {
//         return (await this.sgApi.post('/play/reg', regParams)).data
//     }

//     async op(op: string, cityId: number, opParams: Record<string, any> = {}): Promise<PlayInfo> {
//         return (await this.sgApi.post('/play/op', { cityId, op, opParams: JSON.stringify(opParams) })).data
//     }

// }