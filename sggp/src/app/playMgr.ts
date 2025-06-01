import type { PlayInfo } from "./api/apiModel"
import type { SgCtx } from "./sg"

export class PlayMgr {

    private _ctx: SgCtx
    private _play?: PlayInfo
    private playerKes = new Set<string>()
    private oping: boolean = false
    constructor(ctx: SgCtx) {
        this._ctx = ctx
    }

    async refreshPlay() {
        try {
            const play = await this._ctx.api.playApi.getPlay()
            this.setPlayer(play)
            this._ctx.stage
        } catch (error) {

        }
    }

    private get dataMgr() { return this._ctx.dataMgr }

    private setPlayer(player?: PlayInfo) {
        this._play = player
        this.dataMgr.set("player#id", player == null ? null : player.id)
        this.dataMgr.set("player#state", player == null ? null : player.state)
        this.dataMgr.set("player#lastCity", player == null ? null : player.lastCity)

        if (player) {
            for (const k of Object.keys(player)) {
                // console.log(`set(${k})`)
                let v = (player as any)[k]
                v = v === null || v === undefined ? '0' : v
                // console.log(`set(${k},${v})`)
                if (typeof v == 'object' && !Array.isArray(v)) {
                    for (const k2 of Object.keys(v)) {
                        const v2 = v[k2];
                        if (v2 && typeof v2 == 'object' && !Array.isArray(v2)) {
                            for (const k3 of Object.keys(v2)) {
                                this.playSet(`playing#player_${k}_${k2}_${k3}`, v2[k3])
                            }
                        } else {
                            this.playSet(`playing#player_${k}_${k2}`, v2)
                        }
                    }
                } else {
                    this.playSet(`playing#player_${k}`, v === undefined ? '' : v)
                }
            }
            this.playerSetCalc(player)
        } else {
            if (this.playerKes.size) {
                for (const key of this.playerKes) {
                    this.dataMgr.set(key, undefined)
                }
                this.playerKes.clear()
            }
        }
    }
    private playSet(cfg: string, data: any) {
        this.playerKes.add(cfg)
        this.dataMgr.set(cfg, data)
    }

    private playerSetCalc(player: PlayInfo) {
        const tax = this.dataMgr.get("playing#player_city_res_tax")
        const people = this.dataMgr.get("playing#player_city_res_people")
        const goldAdd = Math.floor(people * tax / 100) - this.dataMgr.get("playing#player_city_res_heroFee")
        this.playSet(`playing#player_city_res_goldAdd`, goldAdd)

        const peopleWorking = this.dataMgr.get("playing#player_city_res_peopleWorking")
        const peopleBuilding = this.dataMgr.get("playing#player_city_res_peopleBuilding")
        const peopleFree = people - peopleWorking - peopleBuilding
        this.playSet(`playing#player_city_res_peopleFree`, peopleFree)

        // // 建筑等级
        // const buildMaxLv = new Map<number, number>()// 建筑id和建筑最高的等级
        // player.city.innerBuilds.forEach(b => {
        //     let lv = buildMaxLv.has(b.id) && buildMaxLv.get(b.id)! > b.lv ? buildMaxLv.get(b.id)! : b.lv
        //     buildMaxLv.set(b.id, lv)
        // })
        // player.city.outBuilds.forEach(b => {
        //     let lv = buildMaxLv.has(b.id) && buildMaxLv.get(b.id)! > b.lv ? buildMaxLv.get(b.id)! : b.lv
        //     buildMaxLv.set(b.id, lv)
        // })
        // this.playSet(`playing#player_city_buildMaxLv`, buildMaxLv)

    }

    hasUsingGoods(foodsIds: number[]): boolean {
        if (!this._play) { return false }
        const fs = this._play.usedFoods
        for (let i = 0; i < foodsIds.length; i++) {
            const fid = foodsIds[i];
            if (fs[fid]) {
                return true;
            }
        }
        return false
    }

    getTechnicLv(tid: number): number {
        return 0;
    }
    getBuildLv(bid: number): number {
        return 0;
    }

    get play() { return this._play }

    async op(op: string, params: Record<any, any>) {
        if (this.oping) {
            this._ctx.errMsgMgr.pushMsg("操作过于平凡稍后再试!")
            return
        }
        this.oping = true
        try {
            await this._ctx.api.playApi.op(op, this.play!.lastCity, params)
        }
        catch (err) {
            console.log(err)
            this._ctx.errMsgMgr.pushMsg(err as string)
        } finally {
            this.oping = false
        }
    }
}
