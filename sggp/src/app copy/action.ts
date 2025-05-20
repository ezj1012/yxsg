import { AxiosError } from "axios"
import type { CfgStr } from "./cfg"
import { encode } from "../app/constant"
import type { StageCfg } from "./stage"
import type { SanGuo } from "./sg"
import sgGame from "./sg"

export interface Action {
    key: string
    desc: string
    fun: (s: { sg: SanGuo, cfg?: CfgStr, e?: MouseEvent, stage?: StageCfg }) => Promise<any>
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
    reg(key: string, fun: (s: { sg: SanGuo, cfg?: CfgStr, e?: MouseEvent, stage?: StageCfg }) => Promise<any>) {
        this.register({ key, fun, desc: '' })
    }

    async execBtn(key?: string, cfg?: CfgStr, e?: MouseEvent) {
        console.log(`exec btn:  ${key}`)
        if (!key) return ActionExec.unExec
        const r = this.actionCache.get(key)
        if (!r) return ActionExec.unExec
        return await r.fun({ cfg, e, sg: sgGame.sg.value! })
    }

    async onMounted(stage: StageCfg) {
        const key = `stage#${stage.key}_mounted`;
        // console.log(`action change stage ${key}`)
        const r = this.actionCache.get(key)
        if (r) {
            await r.fun({ sg: sgGame.sg.value!, stage })
        }
    }

    async onUnmounted(stage: StageCfg) {
        const key = `stage#${stage.key}_unmounted`;
        const r = this.actionCache.get(key)
        if (r) {
            r.fun({ sg: sgGame.sg.value!, stage })
        }
    }

}

export const actionMgr = new ActionMgr()
export function reg(key: string, fun: (s: { sg: SanGuo, cfg?: CfgStr, e?: MouseEvent, stage?: StageCfg }) => Promise<any>, desc = "") {
    actionMgr.register({ key, desc, fun })
}

reg('closeFunPan', async ({ sg }) => { sg.funPanMgr.setComp() })


{
    // stageLogin
    reg('stage#login_unmounted', async ({ sg }) => {
        // for (const cKey of sg.ctx.dataMgr.dataCache.cache.keys()) {
        // }
    });

    reg('login', async ({ sg }) => {
        const loginCode = sg.ctx.dataMgr.get('login#login_code')
        const loginPwd = sg.ctx.dataMgr.get('login#login_pwd')
        if (!loginCode || !loginPwd) {
            sg.pushMsg("账号密码不可为空!");
            return false;
        }
        try {
            await sg.api.userApi.login(loginCode, encode(loginPwd))
            await sg.refreshPlay()
        } catch (error) {
            sg.pushMsg("登录失败!");
            return false;
        }
    })
}

{
    reg("stage#regplay_unmounted", async ({ sg }) => { });
    reg("stage#regplay_mounted", async ({ sg }) => {
        await updateProvinces(sg)
        sg.ctx.dataMgr.subscribe('regplay#province_id', 0, (key: string, newValue: any, oldValue: any) => {
            const provinceIdMap = sg.ctx.dataMgr.get('regplay#provinces') as any
            provinceIdMap && sg.ctx.dataMgr.set('regplay#province_info', provinceIdMap[newValue].msg)
        })

        const icons = await sg.res.getGlobarMgr().getPalyIcons()
        const iconIdMap = {} as any
        iconIdMap['1'] = []
        iconIdMap['2'] = []
        icons.forEach(ic => {
            if (ic.genderType == 1) {
                iconIdMap['1'].push(ic.icon)
            } else {
                iconIdMap['2'].push(ic.icon)
            }
        });
        sg.ctx.dataMgr.set('regplay#icons', iconIdMap)
        sg.ctx.dataMgr.set('regplay#iconId', 0)
        changeIcon(sg)
        sg.ctx.dataMgr.subscribe('regplay#btn_gender_girl_sct', 0, (key: string, newValue: any, oldValue: any) => {
            sg.ctx.dataMgr.set('regplay#iconId', 0)
            changeIcon(sg)
        })

    });
   


    

}