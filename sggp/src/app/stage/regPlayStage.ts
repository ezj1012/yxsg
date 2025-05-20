import { reg } from "../action";
import type { SgCtx } from "../sg";
import { Stage } from "./absStage";

export class RegPlayStage extends Stage {
    constructor(ctx: SgCtx) {
        super('regplay', ctx)
        this.regAll()
    }
    async onUnmounted() {

    }
    async onMounted() {
        await this.updateProvinces()
        this.ctx.dataMgr.subscribe('regplay#province_id', 0, (key: string, newValue: any, oldValue: any) => {
            const provinceIdMap = this.ctx.dataMgr.get('regplay#provinces') as any
            provinceIdMap && this.ctx.dataMgr.set('regplay#province_info', provinceIdMap[newValue].msg)
        })

        const icons = await this.ctx.res.globarMgr.getPalyIcons()
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
        this.ctx.dataMgr.set('regplay#icons', iconIdMap)
        this.ctx.dataMgr.set('regplay#iconId', 0)
        this.changeIcon()
        this.ctx.dataMgr.subscribe('regplay#btn_gender_girl_sct', 0, (key: string, newValue: any, oldValue: any) => {
            this.ctx.dataMgr.set('regplay#iconId', 0)
            this.changeIcon()
        })
    }

    private regAll() {
        reg('regplay#prev_icon', async ({ sg }) => {
            const icons = this.getIcons()
            if (icons) {
                let i = this.ctx.dataMgr.get('regplay#iconId')
                i = i ? i : 0
                i--
                if (i < 0) {
                    i = icons.length - 1;
                }
                this.ctx.dataMgr.set('regplay#iconId', i)
            } else {
                this.ctx.dataMgr.set('regplay#iconId', 0)
            }
            this.changeIcon()
        });
        reg('regplay#next_icon', async ({ sg }) => {
            const icons = this.getIcons()
            if (icons) {
                let i = this.ctx.dataMgr.get('regplay#iconId')
                i = i ? i : 0
                i++
                if (i >= icons.length) {
                    i = 0;
                }
                this.ctx.dataMgr.set('regplay#iconId', i)
            } else {
                this.ctx.dataMgr.set('regplay#iconId', 0)
            }
            this.changeIcon()
        });

        reg('regplay#start_gaming', async ({ sg, ctx }) => {
            const name = this.ctx.dataMgr.get('regplay#player_name')
            const gender = this.ctx.dataMgr.get('regplay#btn_gender_girl_sct') ? 0 : 1 // 0女 1男
            const icon = this.ctx.dataMgr.get('regplay#user_icon')
            const provinceId = this.ctx.dataMgr.get('regplay#province_id')

            const regParams = {
                name,
                gender,
                icon,
                provinceId,
                agreeRules: true
            }

            if (!regParams.name) {
                this.ctx.errMsgMgr.pushMsg("君主名不可为空!");
                return false;
            }
            try {
                await ctx.api.playApi.regplay(regParams)
                await ctx.playMgr.refreshPlay()
            } catch (error) {
                // ctx.errMsgMgr.pushMsg(error.)
                return false;
            }
        })
    }

    private async updateProvinces() {
        try {
            const provinces = (await this.ctx.api.envApi.getProvinces()).filter(a => !a.minority)
            const provinceIdMap = {} as any
            const provinceKV = [{ key: 0, value: '随机' }] as any

            provinceIdMap['0'] = {
                id: 0,
                name: "随机",
                msg: "<span style='font-size:13px'>系统会 <span style='font-size:16px'>随机</span> 选择一个州</span>",
            }
            provinces.forEach(p => {
                let c = p.playerMaxCount === 0
                    ? 100
                    : Math.floor(p.playerCount / p.playerMaxCount);
                (p as any).msg = `<span style='font-size:13px'><span style='font-size:16px'>${p.name}</span> 的城池情况:<br/>君主数: ${p.playerCount}<br/>城池数: ${p.cityCount}<br/>拥挤度: ${c}%</span>`;
                provinceIdMap[p.id + ""] = p
                provinceKV.push({ key: p.id, value: p.name })
            })
            this.ctx.dataMgr.set('regplay#province_id_options', provinceKV)
            this.ctx.dataMgr.set('regplay#province_id', 0)
            this.ctx.dataMgr.set('regplay#provinces', provinceIdMap)

            // this.ctx.dataMgr.set('regplay#icons', iconIdMap)
            this.ctx.dataMgr.set('regplay#province_info', provinceIdMap['0'].msg)

        } catch (error) {
            this.ctx.errMsgMgr.pushMsg('')
        }
    }

    getIcons() {
        return this.ctx.dataMgr.get('regplay#icons')[this.ctx.dataMgr.get('regplay#btn_gender_girl_sct') ? '2' : '1']
    }

    changeIcon() {
        try {
            const icons = this.getIcons()
            let i = this.ctx.dataMgr.get('regplay#iconId')
            i = i ? i : 0
            const ic = icons[i]
            this.ctx.dataMgr.set('regplay#user_icon', ic)
        } catch (error) {
            console.log(error)
        }
    }
}
