import type { CfgBuildingLevel, CfgGoods, CityTechnic, GlobalCfg, SoldierCfg } from "./api/apiModel"
import type { SgCtx } from "./sg"


class GCfgDb {
    static DB_KEY = 'DB_cache'
    static STORE_NAME_GOODS = 'goods'
    db?: IDBDatabase;
    private dbReady: Promise<IDBDatabase>;
    constructor() {
        const that = this;
        const request = indexedDB.open(GCfgDb.DB_KEY, 1);
        this.dbReady = new Promise((resolve, reject) => {

            request.onsuccess = function (event: Event) {
                that.db = request.result as IDBDatabase;
                resolve(that.db);
            };

            request.onupgradeneeded = function (event: Event) {
                that.db = request.result as IDBDatabase;
                if (!that.db.objectStoreNames.contains(GCfgDb.STORE_NAME_GOODS)) {
                    that.db.createObjectStore(GCfgDb.STORE_NAME_GOODS, { keyPath: 'id' });
                }
            };

            request.onerror = function (event) {
                reject(request.error);
            };
        });
    }

    async add(value: any, storeName: string) {
        const db = await this.dbReady;
        const transaction = db.transaction(storeName, 'readwrite');
        const objectStore = transaction.objectStore(storeName);
        return new Promise<void>((resolve, reject) => {
            const request = objectStore.put(value);
            request.onsuccess = () => resolve();
            request.onerror = () => reject(request.error);
        });
    }

    async getGoods(key: any): Promise<CfgGoods | undefined> {
        return this.get(key, GCfgDb.STORE_NAME_GOODS) as Promise<CfgGoods | undefined>
    }

    async get(key: any, storeName: string): Promise<any> {
        const db = await this.dbReady;
        const transaction = db.transaction(storeName, 'readonly');
        const objectStore = transaction.objectStore(storeName);
        return new Promise<any>((resolve, reject) => {
            const request = objectStore.get(key)
            request.onsuccess = () => resolve(request.result);
            request.onerror = () => reject(request.error);
        });
    }
}

export class GCfgMgr {

    uniqueBids = [6, 7, 13, 14, 16, 15, 10, 17, 8, 11, 12, 18, 19, 20];

    cfg?: GlobalCfg
    buildImgs = {}
    private _db: GCfgDb
    private _ctx: SgCtx
    constructor(ctx: SgCtx) {
        this._ctx = ctx
        this._db = new GCfgDb()
    }
    configuer(data: GlobalCfg) {
        this.cfg = data
    }

    getBuildLv(bid: number, lv: number): CfgBuildingLevel | undefined {
        return this.cfg!.buildingsMap[bid]?.levels[lv]
    }

    getBuildPreCdt(bid: number, lv: number) {
        return this.cfg!.buildingsMap[bid]?.preCdts[lv]
    }

    getPalyIcons() {
        return this.cfg?.playerIcons || []
    }

    async getGoods(ids: number[]): Promise<Record<number, CfgGoods>> {
        const nids: number[] = []
        const rData: Record<number, CfgGoods> = {}
        for (let i = 0; i < ids.length; i++) {
            const r = await this._db.getGoods(ids[i])
            if (r == undefined) {
                nids.push(ids[i])
            } else {
                rData[r.id] = r
            }
        }
        if (nids.length > 0) {
            const datas = await this._ctx.api.cfgApi.getGoods(nids)
            for (let i = 0; i < datas.length; i++) {
                const r = datas[i]
                await this._db.add(r, GCfgDb.STORE_NAME_GOODS)
                rData[r.id] = r
            }
        }
        return rData
    } 
    
    /**
       * 通过配置获取相对地址
       * @param cfg 
       * @returns 
       */
    goodsImg(cfg: CfgGoods | { id: number, imageType: number, image: number }) {
        if (!cfg) return ''
        const idx = cfg.image == 0 ? cfg.id : cfg.image
        let prx = ''
        if (cfg.imageType == 0) {
            prx = `item`
        } else if (cfg.imageType == 1) {
            prx = `pack`
        }
        return `/rsm/images/goods/${prx}_${idx}.png`
    }

    getTechnic(tid: number): CityTechnic | undefined {
        return undefined
    }

    getTechLv(buildTechId: number): any {
    }

    isOnlyBuild(bid: number): boolean {
        return this.uniqueBids.findIndex(b => b == bid) !== -1
    }

    getCitySoldiers(): SoldierCfg[] {
        const r = []
        for (const k in this.cfg!.soldiersMap) {
            const s = this.cfg!.soldiersMap[k]
            if (s.fromCity == 1) {
                r.push(s)
            }
        }
        return r
    }
}