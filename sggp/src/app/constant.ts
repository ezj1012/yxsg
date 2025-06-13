import CryptoJS from "crypto-js";
const AES_KEY = "xiongjianrexuesg"
const key = CryptoJS.enc.Utf8.parse(AES_KEY)
const iv = CryptoJS.enc.Utf8.parse(AES_KEY)

export function encode(msg: string) {
    const srcs = CryptoJS.enc.Utf8.parse(msg)
    const encrypted = CryptoJS.AES.encrypt(srcs, key, {
        iv,
        mode: CryptoJS.mode.CBC,
        padding: CryptoJS.pad.Pkcs7
    })
    return encrypted.toString();
}


export function wid(x: number, y: number): number {
    return Math.floor(((Math.floor(y / 10)) * 10000 + (Math.floor(x / 10)) * 100 + (y % 10) * 10 + (x % 10)));
}

export function cid(x: number, y: number): number {
    return y * 1000 + x;
}

export function wid2cid(wid: number): number {
    let y = (Math.floor(wid / 10000) * 10 + Math.floor(((wid % 100) / 10)));
    let x = (Math.floor((wid % 10000) / 100) * 10 + Math.floor(wid % 10));
    return y * 1000 + x;
}

export function wid2xy(wid: number): { x: number, y: number } {
    let x, y = 0

    y = (Math.floor(wid / 10000) * 10 + Math.floor(((wid % 100) / 10)));
    x = (Math.floor((wid % 10000) / 100) * 10 + Math.floor(wid % 10));

    return { x, y };
}

export function wid2xyStr(wid: number) {
    const { x, y } = wid2xy(wid)
    return `${x},${y}`
}

export function cid2xy(cid: number): { x: number, y: number } {
    let y = Math.floor(cid / 1000);
    let x = (cid % 1000);
    return { x, y };
}


export function cid2wid(cid: number): number {
    let y = Math.floor(cid / 1000);
    let x = (cid % 1000);
    return wid(x, y);
}

export class CfgRes {
    static food = new CfgRes(0, '粮食')
    static wood = new CfgRes(1, '木材')
    static rock = new CfgRes(2, '石头')
    static iron = new CfgRes(3, '铁定')
    static gold = new CfgRes(4, '金币')
    static people = new CfgRes(5, '人口')
    static time = new CfgRes(6, '耗时')
    id: number
    name: string
    constructor(id: number, name: string) {
        this.id = id
        this.name = name
    }
}


export const heroStates = { 0: '空闲', 1: '城守', 2: '出征', 3: '战斗', 4: '驻守', 5: '俘虏' } as any

export const DATA_KEY_FUN_PAN_INVENTORY_PAGE = 'DATA_KEY_FUN_PAN_INVENTORY_PAGE'

export const DATA_KEY_CITY_BUILD_INNER = 'DATA_KEY_CITY_BUILD_INNER'
export const DATA_KEY_CITY_BUILD_INNER_LV_USE = 'DATA_KEY_CITY_BUILD_INNER_LV_USE'



/**
   * 计算减免后的值，至少返回 1
   * @param base 基础值
   * @param level 减免次数（>=0）
   * @param rate 百分比（如 30 表示 30%）
   * @returns 至少返回 1
   */
export function calculateDerateAndMin1(base: number, level: number = 0, rate: number): number {
    const result = calculateDerate(base, level, rate);
    return result === 0 ? 1 : result;
}

/**
 * 计算减免值
 * @param base 基础值
 * @param level 减免次数（>=0）
 * @param rate 百分比（如 30 表示 30%）
 * @returns 减免后的值
 */
export function calculateDerate(base: number, level: number = 0, rate: number): number {
    if (base == null || base < 1) {
        return 1;
    }
    if (level == null || level < 1) {
        return base;
    }
    return Math.floor(base * Math.pow((100 - rate) / 100, level));
}

export function formatSeconds(seconds: number) {
    const hrs = Math.floor(seconds / 3600);
    const mins = Math.floor((seconds % 3600) / 60);
    const secs = Math.floor(seconds % 60);

    // 使用 padStart 保证两位数格式，如 "02:03:05"
    const hh = String(hrs).padStart(2, '0');
    const mm = String(mins).padStart(2, '0');
    const ss = String(secs).padStart(2, '0');

    if (hrs > 0) {
        return `${hh}小时${mm}分钟${ss}秒`;
    } else if (mins > 0) {
        return `${mm}分钟${ss}秒`;
    } else {
        return `${ss}秒`;
    }
}