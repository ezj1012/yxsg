import { ref, shallowRef, type DebuggerEventExtraInfo, type Ref } from "vue";
import type { UserCache } from "./api";
import { User, type ErrMsg, type HoverMsg } from "./model";
import type { SanGuo } from "./sg";
import { defineStore } from "pinia";
import type { PlayInfo } from "./modelData";


export type CallBack = (key: string, newValue: any, oldValue: any) => void


export const globalDataCacheDef = defineStore("SgDataCache", {
    state: () => {
        return {
            showErrorMsgs: [] as ErrMsg[],
            cache: new Map()
        }
    },
    actions: {
        set(key: string, value: any) {
            this.cache.set(key, value)
        },
        get(key: string) {
            return this.cache.get(key)
        },
        pushErr(msg: ErrMsg) {
            this.showErrorMsgs.push(msg)
        },
        errMsgSize() {
            return this.showErrorMsgs.length
        },
        removeErr(idx?: number) {
            idx = idx ? idx : this.errMsgSize() - 1
            this.showErrorMsgs.splice(idx, 1)
        },
    }
})

export class PlayMgr {
    dataMgr: DataMgr
    play?: PlayInfo
    playerKes = new Set<string>();
    constructor(dataMgr: DataMgr) {
        this.dataMgr = dataMgr
    }

    set(player?: PlayInfo) {
        this.play = player
        this.dataMgr.set("player#id", player == null ? null : player.id)
        this.dataMgr.set("player#state", player == null ? null : player.state)
        this.dataMgr.set("player#lastCity", player == null ? null : player.lastCity)

        if (player) {
            // console.log(player, Object.keys(player))

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
                    this.playSet(`playing#player_${k}`, v || '')
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

    playSet(cfg: string, data: any) {
        this.playerKes.add(cfg)
        this.dataMgr.set(cfg, data)
    }

    playerSetCalc(player: PlayInfo) {
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
}


export class DataMgr implements UserCache {
    sg: SanGuo
    private subLists = new Map<string, CallBack[]>()
    playMgr: PlayMgr
    dataCache = globalDataCacheDef()
    hoverMsg = ref<HoverMsg>()
    user = shallowRef<User>()

    constructor(sg: SanGuo) {
        this.sg = sg
        this.dataCache.$subscribe((mutation, state) => {
            const event = mutation.events as DebuggerEventExtraInfo
            if (event && event.key) {
                const key = event.key
                const callbacks = this.subLists.get(key);
                if (callbacks) {
                    const { newValue, oldValue } = event;
                    callbacks.forEach(callback => {
                        callback(key, newValue, oldValue)
                    });
                }
            }
        }, {
            detached: true,
            flush: 'sync',
            deep: true,
            immediate: true
        })
        this.playMgr = new PlayMgr(this)
    }

    isLogin() { return this.user.value ? true : false }
    hasPlay() { return this.playMgr.play ? true : false }
    setPlay(play?: PlayInfo) { this.playMgr.set(play) }

    getCurrentUser(): User | undefined { return this.user.value }
    setCurrentUser(user?: User): void { this.user.value = user }

    subscribe(cfg: any, defVal: any, call: CallBack) {
        const key = toKey(cfg)
        if (this.getByKey(key) === undefined && defVal !== undefined) {
            this.setByKey(key, defVal)
        }
        let calls = this.subLists.get(key) || [];
        calls.push(call);
        this.subLists.set(key, calls)
    }

    unsubscribe(cfg: any, call?: CallBack) {
        const key = toKey(cfg);
        if (call) {
            const calls = this.subLists.get(key);
            if (calls) {
                const index = calls.indexOf(call);
                if (index !== -1) {
                    calls.splice(index, 1);
                    if (calls.length === 0) {
                        this.subLists.delete(key);
                    }
                }
            }
        } else {
            this.subLists.delete(key);
        }
    }

    subscribeValue(cfg: any, data: Ref<any>, defVal?: any) {
        const oldV = this.get(toKey(cfg))
        if (oldV === undefined && defVal != undefined) {
            data.value = defVal
            this.set(toKey(cfg), defVal)
        } else {
            data.value = oldV
        }
        this.subscribe(cfg, undefined, (key: string, newValue: any, oldValue: any) => {
            data.value = this.get(toKey(cfg))
        })
    }

    getByKey(key: string) { return this.dataCache.get(key) }
    setByKey(key: string, data: any) { return this.dataCache.set(key, data) }
    set(cfg: any, data: any) { return this.dataCache.set(toKey(cfg), data) }

    get(cfg: any, data?: any) {
        const key = toKey(cfg);
        const r = this.dataCache.get(key)
        if (r !== undefined) { return r }
        if (data) {
            this.setByKey(key, data)
        }
        return data
    }





    pushMsg(content: string) { this.dataCache.pushErr({ content }) }

    pushOkMsg(content: string, ok?: () => Promise<void>) {
        this.dataCache.pushErr({
            content, ok: async () => {
                ok && await ok()
            }
        })
    }

    pushConfirmMsg(content: string, ok: () => Promise<void>) {
        // content: string
        // ok?: () => Promise<void>
        // cancel?: () => Promise<void>
        this.dataCache.pushErr({
            content,
            ok,
            cancel: () => { }
        })
    }

    removeMsg(idx?: number) {
        this.dataCache.removeErr(idx)
    }

    setHoverMsg(el: any, msg?: HoverMsg) {
        if (msg) {
            this.hoverMsg.value = msg
        } else if (this.hoverMsg.value && this.hoverMsg.value.el == el) {
            this.hoverMsg.value = undefined
        }
    }
}

export function toKey(cfg: any) {
    return cfg as string
}