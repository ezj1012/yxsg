import { encode } from "@/app/constant";
import { reg } from "../action";
import type { SgCtx } from "../sg";
import { Stage } from "./absStage";

function regAll() {
    reg('login', async ({ sg, ctx, res }) => {
        const loginCode = ctx.dataMgr.get('login#login_code')
        const loginPwd = ctx.dataMgr.get('login#login_pwd')
        if (!loginCode || !loginPwd) {
            ctx.errMsgMgr.pushMsg("账号密码不可为空!");
            return false;
        }
        try {
            await ctx.userMgr.login(loginCode, encode(loginPwd))
            await ctx.playMgr.refreshPlay()
        } catch (error) {
            ctx.errMsgMgr.pushMsg("登录失败!");
            return false;
        }
    })
}


export class LoginStage extends Stage {
    constructor(ctx: SgCtx) {
        super('login', ctx)
        regAll()
    }
    async onUnmounted() {
    }
    async onMounted() {
    }

}

