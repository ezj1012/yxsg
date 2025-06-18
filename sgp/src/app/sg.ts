import { shallowRef, type App } from "vue"
import { DivBg } from "./directives"

export class SanGuo { }

namespace sgGame {

    export const sg = shallowRef<SanGuo>()

    export function install(app: App) {
        // installMsg(app, sg)
        // installClickout(app, sg)
        // DivBg.installBg(app, sg)
        DivBg.installSize(app, sg)
    }
}
export default sgGame