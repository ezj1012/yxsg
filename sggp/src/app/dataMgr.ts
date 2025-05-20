import { defineStore } from "pinia"
import type { DebuggerEventExtraInfo, Ref } from "vue"

export type CallBack = (key: string, newValue?: any, oldValue?: any) => void


const globalDataCacheDef = defineStore("SgDataCache", {
    state: () => {
        return {
            cache: new Map()
        }
    },
    actions: {
        set(key: string, value: any) {
            this.cache.set(key, value)
        },
        get(key: string) {
            return this.cache.get(key)
        }
    }
})

export class DataMgr {
    private subLists = new Map<string, CallBack[]>()
    dataCache = globalDataCacheDef()
    constructor() {
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
    }

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

}


export function toKey(cfg: any) {
    return cfg as string
}