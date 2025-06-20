import { ref, type Component } from "vue"
import type { CfgBuildingLevel, FrameStageCfg } from "./api/apiModel"
import type { SgRes } from "./res"
import { calculateDerateAndMin1 } from "./constant"

export interface Traceable {
    msg: string
    pct: number
}

export interface ErrMsg {
    content: string
    ok?: () => Promise<void> | void
    cancel?: () => Promise<void> | void
}

export interface HoverMsg {
    clientX: number
    clientY: number
    el: any
    content: any
    isHtml?: boolean
}

export interface HoverMsgDef {
    content: any
    isHover?: (e: MouseEvent) => boolean
    in?: (e: MouseEvent, el: any) => void
    out?: (e: MouseEvent, el: any) => void
}




export interface Shapable {
    x?: number
    y?: number
    w?: number
    h?: number
    z?: number
    styles?: { [key: string]: string }
}

export interface Textable {
    color: string
    size: number
    content: string
    caretColor?: string
    html?: boolean
    styles?: { [key: string]: string }
}





export interface Compable extends Shapable {
    id?: string
    key: string
    name?: string
    comp: Component
}

export interface FunPanComp extends Compable, Textable {
    show?: boolean
}

export interface StageComp extends Compable { }

export class SingleComp<T extends Compable> {
    cache = new Map<string, T>()
    curComp = ref<string>()
    constructor(coms: T[]) {
        coms.forEach(com => this.cache.set(com.key, com))
    }

    getComp(): T | undefined {
        return this.curComp.value ? this.cache.get(this.curComp.value) : undefined
    }

    setComp(key?: string): T | undefined {
        this.curComp.value = key
        console.log('fun 显示为 : ' + this.curComp.value)
        return this.getComp()
    }
}

export class ScrollHelper {
    styles: Record<string, any> = {}
    res: SgRes
    constructor(res: SgRes) {
        this.res = res
        this.styles['--track-up'] = this.res.img("common#scroll_uparr")
        this.styles['--track-up-down'] = this.res.img("common#scroll_uparr", { filter: (g: any) => g.hasDown() })
        this.styles['--track-down'] = this.res.img("common#scroll_downarr")
        this.styles['--track-down-up'] = this.res.img("common#scroll_downarr", { filter: (g: any) => g.hasDown() })
    }

    scrollImg(el: HTMLElement, show: boolean, dataHeight: number) {
        if (show) {
            const height = el.getBoundingClientRect().height
            const newHeight = ((height - 40) * height) / dataHeight;
            this.styles['--track-img'] = this.res.img("common#scroll_track", { w: 19, h: height })
            this.styles['--track-bar'] = this.res.img("common#scroll_bar", { w: 13, h: newHeight })
            this.styles['--track-bar-hover'] = this.res.img("common#scroll_bar", { w: 13, h: newHeight })
            this.styles['overflow-y'] = 'auto'
        } else {
            this.styles['overflow-y'] = 'hidden'
            el.scrollTop = 0
        }
    }
}

// UseRes 类
export class UseRes {
    // 消耗资源
    upgradeWood: number = 0;
    upgradeRock: number = 0;
    upgradeIron: number = 0;
    upgradeFood: number = 0;
    upgradeGold: number = 0;

    // 升级依赖人口
    upgradePeople: number = 0;
    // 升级耗时（秒）
    upgradeTime: number = 0;
    // 占用人口
    usingPeople: number = 0;

    // 物品消耗（key: 物品ID, value: 数量）
    useGoods: Map<number, number> = new Map();

    /**
     * 对基础资源进行减免
     * @param rate 减免百分比（如 30 表示 30%）
     */
    derateBaseRes(rate: number): void {
        this.upgradeFood = calculateDerateAndMin1(this.upgradeFood, 1, rate);
        this.upgradeWood = calculateDerateAndMin1(this.upgradeWood, 1, rate);
        this.upgradeRock = calculateDerateAndMin1(this.upgradeRock, 1, rate);
        this.upgradeIron = calculateDerateAndMin1(this.upgradeIron, 1, rate);
        this.upgradeGold = calculateDerateAndMin1(this.upgradeGold, 1, rate);
    }

    /**
     * 对升级耗时进行减免（基于等级）
     * @param level 等级
     * @param rate 减免百分比
     */
    derateTime(level: number = 0, rate: number): void {
        this.upgradeTime = calculateDerateAndMin1(this.upgradeTime, level, rate);
    }

    /**
     * 从配置对象创建 UseRes 实例
     * @param lv 配置对象（需与 UseRes 具有相同属性）
     * @param useGoods 可选的物品消耗
     * @returns UseRes 实例
     */
    public static from(lv: CfgBuildingLevel, useGoods?: Map<number, number>): UseRes {
        const useRes = new UseRes();

        // 简单属性赋值（假设 lv 包含所有需要的字段）
        if (lv) {
            useRes.upgradeWood = lv.upgradeWood || 0;
            useRes.upgradeRock = lv.upgradeRock || 0;
            useRes.upgradeIron = lv.upgradeIron || 0;
            useRes.upgradeFood = lv.upgradeFood || 0;
            useRes.upgradeGold = lv.upgradeGold || 0;
            useRes.upgradePeople = lv.upgradePeople || 0;
            useRes.upgradeTime = lv.upgradeTime || 0;
            useRes.usingPeople = lv.usingPeople || 0;
        }

        // 合并物品消耗
        if (useGoods && useGoods.size > 0) {
            for (const [key, value] of useGoods.entries()) {
                useRes.useGoods.set(key, value);
            }
        }

        return useRes;
    }

    /**
     * 获取升级完成时间戳（毫秒）
     * @returns 升级完成时间戳
     */
    getUpgradeEndTimeMillis(): number {
        return Date.now() + this.upgradeTime * 1000;
    }
}