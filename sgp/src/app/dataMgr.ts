import { defineStore } from "pinia"
import type { DebuggerEventExtraInfo, Ref } from "vue"

export type CallBack = (key: string, newValue?: any, oldValue?: any) => void


class CallBacks {
    cs: CallBack[] = []
    map = new Map<any, CallBack[]>()
    call(key: string, newValue?: any, oldValue?: any) {
        this.cs.forEach(callback => callback(key, newValue, oldValue));
    }
    push(call: CallBack) { this.cs.push(call) }

    pushRef(data: Ref<any, any>, call: CallBack) {
        if (this.map.has(data)) {
            this.map.get(data)?.push(call)
        } else {
            this.map.set(data, [call])
        }

        this.push(call)
    }

    deleteRef(data: Ref<any, any>) {
        this.map.get(data)?.forEach(c => {
            console.log('dddd', JSON.stringify(this.cs))
            this.delete(c)
            console.log('ddddend', JSON.stringify(this.cs))
        })
    }

    delete(call: CallBack) {
        const index = this.cs.indexOf(call);
        if (index !== -1) {
            this.cs.splice(index, 1);
        }
    }
}

export function toKey(cfg: any) {
    return cfg as string
}

/**
 * 可监听的数据存储
 * @param storageName 
 * @returns 
 */
export function useMapStorage(storageName: string) {
    const mapCacheDef = defineStore(storageName, {
        state: () => {
            return { cache: new Map() }
        },
        actions: {
            set(key: string, value: any) { this.cache.set(key, value) },
            get(key: string) { return this.cache.get(key) }
        }
    })
    const subLists = new Map<string, CallBacks>()
    const dataCache = mapCacheDef()
    dataCache.$subscribe((mutation) => {
        const event = mutation.events as DebuggerEventExtraInfo
        if (event && event.key) {
            const { key, newValue, oldValue } = event;
            subLists.get(key)?.call(key, newValue, oldValue)
        }
    }, {
        detached: true,
        flush: 'sync',
        deep: true,
        immediate: true
    })

    function getByKey(key: string) { return dataCache.get(key) }
    function setByKey(key: string, data: any) { dataCache.set(key, data) }
    function set(cfg: any, data: any) { dataCache.set(toKey(cfg), data) }
    function get(cfg: any, data?: any) {
        const key = toKey(cfg);
        const r = dataCache.get(key)
        if (r !== undefined) { return r }
        data && setByKey(key, data)
        return data
    }
    function subscribe(cfg: any, call: CallBack, defVal: any) {
        const key = toKey(cfg)
        if (getByKey(key) === undefined && defVal !== undefined) {
            setByKey(key, defVal)
        }
        let calls = subLists.get(key) || new CallBacks();
        calls.push(call);
        subLists.set(key, calls)
    }

    function unsubscribe(cfg: any, call?: CallBack) {
        const key = toKey(cfg);
        if (call) {
            const calls = subLists.get(key);
            calls && calls.delete(call)
        } else {
            subLists.delete(key);
        }
    }

    function subscribeValue(cfg: any, data: Ref<any>, defVal?: any) {
        const key = toKey(cfg);
        const oldV = get(toKey(cfg))
        if (oldV === undefined && defVal != undefined) {
            data.value = defVal
            set(toKey(cfg), defVal)
        } else {
            data.value = oldV
        }

        let calls = subLists.get(key)
        if (!calls) {
            calls = new CallBacks()
            subLists.set(key, calls)
        }
        const call = (key: string, newValue: any, oldValue: any) => {
            data.value = get(key)
        }
        calls!.pushRef(data, call);
    }

    function unsubscribeValue(cfg: any, data: Ref<any>) {
        const key = toKey(cfg);
        if (subLists.has(key)) {
            const calls = subLists.get(key);
            calls && calls.deleteRef(data)
        }
    }
    return { get, set, getByKey, setByKey, subscribe, subscribeValue, unsubscribe, unsubscribeValue }
}
