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

// import FunPanlInventory from "@/components/comps/playing/funpan/FunPanlInventory.vue";
// import FunPanlArmor from "@/components/comps/playing/funpan/FunPanlArmor.vue";
// import FunPanlKing from "@/components/comps/playing/funpan/FunPanlKing.vue";
// import FunPanInnverBuild from "@/components/comps/playing/funpan/builds/FunPanInnverBuild.vue";
export const gFunPanComps = [
    // { key: 'fun_pan#playerinfo_inventory', comp: FunPanlInventory, content: '宝物', size: 13, color: '--gold' },
    // { key: 'fun_pan#playerinfo_armor', comp: FunPanlArmor, content: '装备', size: 13, color: '--gold' },
    // { key: 'fun_pan#playerinfo_king', comp: FunPanlKing, content: '君主', size: 13, color: '--gold' },
    // { key: 'fun_pan#city_build_inner_list', comp: FunPanInnverBuild, content: '建造建筑', size: 13, color: '--gold' },
]


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
    let y = (Math.floor(wid / 10000) * 10 + Math.floor(((wid % 100) / 10)));
    let x = (Math.floor((wid % 10000) / 100) * 10 + Math.floor(wid % 10));
    return { x, y };
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

export const heroStates = { 0: '空闲', 1: '城守', 2: '出征', 3: '战斗', 4: '驻守', 5: '俘虏' } as any

export const DATA_KEY_FUN_PAN_INVENTORY_PAGE = 'DATA_KEY_FUN_PAN_INVENTORY_PAGE'

export const DATA_KEY_CITY_BUILD_INNER = 'DATA_KEY_CITY_BUILD_INNER'
export const DATA_KEY_CITY_BUILD_INNER_LV_USE = 'DATA_KEY_CITY_BUILD_INNER_LV_USE'