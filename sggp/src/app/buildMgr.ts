import type { CfgBuildingLevel, CfgGoods, CityBuilding, CityTechnic, GiCityResource } from "./api/apiModel";
import { UseRes } from "./commModel";
import { CfgRes } from "./constant";
import type { SgCtx } from "./sg";
const baseResDerateFid = [
    60, // 0-5
    61,// 0-12
    62// 0-?
]
const heroBuildDerateRate = 0.5
const buildTechId = 46;//
const buildTechDerateRate = 30;

export class BuildDepItem {
    type: 0 | 1 | 2 | 3
    id: number
    source: CityBuilding | CfgGoods | CityTechnic | CfgRes
    name: string
    need: number
    have: number
    constructor(type: 0 | 1 | 2 | 3, source: CityBuilding | CfgGoods | CityTechnic | CfgRes, need: number, have: number) {
        this.type = type
        this.have = have
        this.need = need
        this.id = source.id
        this.source = source
        this.name = source.name
    }
    get ok() { return this.need <= this.have }
}

export class BuildDep {

    items: BuildDepItem[] = []
    private _res?: UseRes
    can(res: GiCityResource) {
        for (let i = 0; i < this.items.length; i++) {
            if (!this.items[i].ok) {
                return false
            }
        }
        if (res.food < this._res!.upgradeFood) { return false }
        if (res.wood < this._res!.upgradeWood) { return false }
        if (res.rock < this._res!.upgradeRock) { return false }
        if (res.iron < this._res!.upgradeIron) { return false }
        if (res.gold < this._res!.upgradeGold) { return false }
        if (res.people < this._res!.upgradePeople) { return false }

        return true
    }

    setRes(res: UseRes) { this._res = res }

    get res() {
        return this._res;
    }
}

export class BuildMgr {
    private _ctx: SgCtx
    constructor(ctx: SgCtx) {
        this._ctx = ctx
    }

    async canBuilds(bs: CityBuilding[]) {
        const r: BuildDep[] = []
        for (let i = 0; i < bs.length; i++) {
            const b = bs[i];
            const dep = new BuildDep()
            r.push(dep)
            const goalLv = this._ctx.gCfgMgr.getBuildLv(b.id, 1)!
            if (goalLv) {
                const playMgr = this._ctx.playMgr
                const bcs = this._ctx.gCfgMgr.getBuildPreCdt(b.id, 1)!
                if (bcs) {
                    if (bcs[2]) {
                        const fids: number[] = []
                        const goods = await this._ctx.gCfgMgr.getGoods(fids)
                        fids.forEach(fid => {
                            dep.items.push(new BuildDepItem(2, goods[fid], bcs[2][fid], playMgr.play!.foods[fid]))
                        })
                    }
                    // if (bcs[1]) {
                    //     for (const tid in bcs[1]) {
                    //         dep.items.push(new BuildDepItem(1, this._ctx.gCfgMgr.getTechnic(Number(tid))!, bcs[1][tid], playMgr.getTechnicLv(Number(tid))))
                    //     }
                    // }
                    if (bcs[0]) {
                        for (const bid in bcs[0]) {
                            dep.items.push(new BuildDepItem(0, this._ctx.gCfgMgr.cfg!.buildingsMap[Number(bid)]!, bcs[0][bid], playMgr.getBuildLv(Number(bid))))
                        }
                    }
                }
                const useRes = this.derateRes(goalLv)
                dep.setRes(useRes)
            }
        }
        return r
    }

    derateRes(goalLv: CfgBuildingLevel, heroAffairs: number = 30, useGoods: Map<number, number> = new Map()) {
        const playMgr = this._ctx.playMgr

        const res = UseRes.from(goalLv, useGoods);

        let hasBaseResDerate = false;
        if (goalLv.level <= 5) {
            hasBaseResDerate = playMgr.hasUsingGoods(baseResDerateFid);
        } else if (goalLv.level <= 12) {
            hasBaseResDerate = playMgr.hasUsingGoods([baseResDerateFid[1], baseResDerateFid[2]]);
        } else {
            hasBaseResDerate = playMgr.hasUsingGoods([baseResDerateFid[2]]);
        }

        if (hasBaseResDerate) {
            res.derateBaseRes(30);
        }
        if (heroAffairs != null && heroAffairs != 0) {
            res.derateTime(heroAffairs, heroBuildDerateRate);
        }
        res.derateTime(this._ctx.gCfgMgr.getTechLv(buildTechId), buildTechDerateRate);
        return res;
    }
}