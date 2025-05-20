import { AxiosError } from "axios"
import type { CfgStr } from "./cfg"
import type { SanGuo, SgCtx } from "./sg"
import sgGame from "./sg"
import type { SgRes } from "./res"
import type { Stage } from "./stage/absStage"

export interface Action {
    key: string
    desc: string
    fun: (s: { sg: SanGuo, ctx: SgCtx, res: SgRes, cfg?: CfgStr, e?: MouseEvent, stage?: Stage }) => Promise<any>
}
const defaultAction: Action = { key: '', desc: '', fun: async () => { } }
export enum ActionExec {
    unExec
}


export class ActionMgr {
    actionCache = new Map<String, Action>()
    register(fun: Action) {
        this.actionCache.set(fun.key, fun)
    }
    reg(key: string, fun: (s: { sg: SanGuo, cfg?: CfgStr, e?: MouseEvent, stage?: Stage }) => Promise<any>) {
        this.register({ key, fun, desc: '' })
    }

    async execBtn(key?: string, cfg?: CfgStr, e?: MouseEvent) {
        console.log(`exec btn:  ${key}`)
        if (!key) return ActionExec.unExec
        const r = this.actionCache.get(key)
        if (!r) return ActionExec.unExec
        return await r.fun({ cfg, e, sg: sgGame.sg.value!, ctx: sgGame.sg.value!.ctx, res: sgGame.sg.value!.ctx.res })
    }

    // async onMounted(stage: Stage) {
    //     const key = `stage#${stage.key}_mounted`;
    //     // console.log(`action change stage ${key}`)
    //     const r = this.actionCache.get(key)
    //     if (r) {
    //         await r.fun({ sg: sgGame.sg.value!, stage })
    //     }
    // }

    // async onUnmounted(stage: Stage) {
    //     const key = `stage#${stage.key}_unmounted`;
    //     const r = this.actionCache.get(key)
    //     if (r) {
    //         r.fun({ sg: sgGame.sg.value!, stage })
    //     }
    // }

}

export const actionMgr = new ActionMgr()
export function reg(key: string, fun: (s: { sg: SanGuo, ctx: SgCtx, res: SgRes, cfg?: CfgStr, e?: MouseEvent, stage?: Stage }) => Promise<any>, desc = "") {
    actionMgr.register({ key, desc, fun })
}

reg('closeFunPan', async ({ sg }) => { sg.ctx.funPanMgr.setComp() })


// {
//     // stageLogin
//     reg('stage#login_unmounted', async ({ sg }) => {
//         // for (const cKey of sg.ctx.dataMgr.dataCache.cache.keys()) {
//         // }
//     });

//     reg('login', async ({ sg }) => {
//         const loginCode = sg.ctx.dataMgr.get('login#login_code')
//         const loginPwd = sg.ctx.dataMgr.get('login#login_pwd')
//         if (!loginCode || !loginPwd) {
//             sg.pushMsg("账号密码不可为空!");
//             return false;
//         }
//         try {
//             await sg.api.userApi.login(loginCode, encode(loginPwd))
//             await sg.refreshPlay()
//         } catch (error) {
//             sg.pushMsg("登录失败!");
//             return false;
//         }
//     })
// }

// {
//     reg("stage#regplay_unmounted", async ({ sg }) => { });
//     reg("stage#regplay_mounted", async ({ sg }) => {
//         await updateProvinces(sg)
//         sg.ctx.dataMgr.subscribe('regplay#province_id', 0, (key: string, newValue: any, oldValue: any) => {
//             const provinceIdMap = sg.ctx.dataMgr.get('regplay#provinces') as any
//             provinceIdMap && sg.ctx.dataMgr.set('regplay#province_info', provinceIdMap[newValue].msg)
//         })

//         const icons = await sg.res.getGlobarMgr().getPalyIcons()
//         const iconIdMap = {} as any
//         iconIdMap['1'] = []
//         iconIdMap['2'] = []
//         icons.forEach(ic => {
//             if (ic.genderType == 1) {
//                 iconIdMap['1'].push(ic.icon)
//             } else {
//                 iconIdMap['2'].push(ic.icon)
//             }
//         });
//         sg.ctx.dataMgr.set('regplay#icons', iconIdMap)
//         sg.ctx.dataMgr.set('regplay#iconId', 0)
//         changeIcon(sg)
//         sg.ctx.dataMgr.subscribe('regplay#btn_gender_girl_sct', 0, (key: string, newValue: any, oldValue: any) => {
//             sg.ctx.dataMgr.set('regplay#iconId', 0)
//             changeIcon(sg)
//         })

//     });
//     reg('regplay#prev_icon', async ({ sg }) => {
//         const icons = getIcons(sg)
//         if (icons) {
//             let i = sg.ctx.dataMgr.get('regplay#iconId')
//             i = i ? i : 0
//             i--
//             if (i < 0) {
//                 i = icons.length - 1;
//             }
//             sg.ctx.dataMgr.set('regplay#iconId', i)
//         } else {
//             sg.ctx.dataMgr.set('regplay#iconId', 0)
//         }
//         changeIcon(sg)
//     });
//     reg('regplay#next_icon', async ({ sg }) => {
//         const icons = getIcons(sg)
//         if (icons) {
//             let i = sg.ctx.dataMgr.get('regplay#iconId')
//             i = i ? i : 0
//             i++
//             if (i >= icons.length) {
//                 i = 0;
//             }
//             sg.ctx.dataMgr.set('regplay#iconId', i)
//         } else {
//             sg.ctx.dataMgr.set('regplay#iconId', 0)
//         }
//         changeIcon(sg)
//     });

//     reg('regplay#start_gaming', async ({ sg }) => {
//         const name = sg.ctx.dataMgr.get('regplay#player_name')
//         const gender = sg.ctx.dataMgr.get('regplay#btn_gender_girl_sct') ? 0 : 1 // 0女 1男
//         const icon = sg.ctx.dataMgr.get('regplay#user_icon')
//         const provinceId = sg.ctx.dataMgr.get('regplay#province_id')

//         const regParams = {
//             name,
//             gender,
//             icon,
//             provinceId,
//             agreeRules: true
//         }

//         if (!regParams.name) {
//             sg.ctx.dataMgr.pushMsg("君主名不可为空!");
//             return false;
//         }
//         try {
//             await sg.api.playApi.regplay(regParams)
//             await sg.refreshPlay()
//         } catch (error) {
//             console.log(error)
//             if (error instanceof AxiosError) {
//                 sg.handlerErr(error)
//             }
//             return false;
//         }
//     })


//     async function updateProvinces(sg: SanGuo) {
//         try {
//             const provinces = (await sg.api.envApi.getProvinces()).filter(a => !a.minority)
//             const provinceIdMap = {} as any
//             const provinceKV = [{ key: 0, value: '随机' }] as any

//             provinceIdMap['0'] = {
//                 id: 0,
//                 name: "随机",
//                 msg: "<span style='font-size:13px'>系统会 <span style='font-size:16px'>随机</span> 选择一个州</span>",
//             }
//             provinces.forEach(p => {
//                 let c = p.playerMaxCount === 0
//                     ? 100
//                     : Math.floor(p.playerCount / p.playerMaxCount);
//                 (p as any).msg = `<span style='font-size:13px'><span style='font-size:16px'>${p.name}</span> 的城池情况:<br/>君主数: ${p.playerCount}<br/>城池数: ${p.cityCount}<br/>拥挤度: ${c}%</span>`;
//                 provinceIdMap[p.id + ""] = p
//                 provinceKV.push({ key: p.id, value: p.name })
//             })
//             sg.ctx.dataMgr.set('regplay#province_id_options', provinceKV)
//             sg.ctx.dataMgr.set('regplay#province_id', 0)
//             sg.ctx.dataMgr.set('regplay#provinces', provinceIdMap)

//             // sg.ctx.dataMgr.set('regplay#icons', iconIdMap)
//             sg.ctx.dataMgr.set('regplay#province_info', provinceIdMap['0'].msg)

//         } catch (error) {
//             sg.ctx.dataMgr.pushMsg('')
//         }
//     }

//     function getIcons(sg: SanGuo) {
//         return sg.ctx.dataMgr.get('regplay#icons')[sg.ctx.dataMgr.get('regplay#btn_gender_girl_sct') ? '2' : '1']
//     }

//     function changeIcon(sg: SanGuo) {
//         try {
//             const icons = getIcons(sg)
//             let i = sg.ctx.dataMgr.get('regplay#iconId')
//             i = i ? i : 0
//             const ic = icons[i]
//             sg.ctx.dataMgr.set('regplay#user_icon', ic)
//         } catch (error) {
//             console.log(error)
//         }
//     }
// }