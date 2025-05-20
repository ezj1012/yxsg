import type { SgCtx } from "../sg";
import { Stage } from "./absStage";



export class PlayStage extends Stage {
    timer: any
    constructor(ctx: SgCtx) {
        super('playing', ctx)
        // this.regAll()
    }
    async onUnmounted() {
        this.timer = setInterval(() => {
            this.ctx.playMgr.refreshPlay()
        }, 1000);
    }
    async onMounted() {
        this.timer && clearInterval(this.timer)
    }

}
