import type { GlobalCfg } from "./modelData"

export class GCfgMgr {
    cfg?: GlobalCfg
    buildImgs = {}
    configuer(data: GlobalCfg) {
        this.cfg = data
    }

    getPalyIcons() {
        return this.cfg?.playerIcons || []
    }
    
}