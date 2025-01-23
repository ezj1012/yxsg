import type { GlobalCfg } from "./modelData"



export class GCfgMgr {
    cfg?: GlobalCfg
    configuer(data: GlobalCfg) {
        this.cfg = data
    }

    getPalyIcons() {
        return this.cfg?.playerIcons || []
    }
}