import type { App, DirectiveBinding, ShallowRef } from "vue"
import type { SanGuo } from "./sg"
import { CfgStr } from "./cfg"
import { Img } from "./img"
import type { HoverMsgDef } from "./model"


export function installMsg(app: App, sg: ShallowRef<SanGuo | undefined>) {
    app.directive('msg', {
        mounted(el, binding) {
            if (binding.value == undefined) { return }
            let msg: HoverMsgDef
            if (typeof binding.value === 'string') {
                msg = { content: binding.value } as HoverMsgDef
            } else {
                msg = binding.value as HoverMsgDef
            }
            const inTestFun = (e: MouseEvent) => {
                if (msg.isHover ? msg.isHover(e) : true) {
                    hover = true
                    msg.in && msg.in(e, el)
                    let clientX = e.clientX
                    let clientY = e.clientY
                    sg.value?.dataMgr.setHoverMsg(el, { clientX, clientY, el, content: msg.content })
                } else {
                    outFun(e)
                }
            }
            const outFun = (e: MouseEvent) => {
                if (!hover) { return }

                msg.out && msg.out(e, el)
                hover = false
                sg.value?.dataMgr.setHoverMsg(el)
            }
            let hover = false
            el.addEventListener('mouseover', inTestFun)
            el.addEventListener('mousemove', inTestFun)
            el.addEventListener('mouseout', outFun)
        }
    })
}
export function installClickout(app: App, sg: ShallowRef<SanGuo | undefined>) {
    app.directive('clickout', {
        mounted(el, binding) {
            function handler(e: Event) {
                if (el.contains(e.target as Node)) return false;
                if (binding.value) {
                    (binding.value as () => void)();
                }
            }
            el.handler = handler;
            document.addEventListener("click", el.handler);
        }
        , unmounted(el, binding) { document.removeEventListener("click", el.handler); }
    })
}


export namespace DivBg {

    export function installSize(app: App, sg: ShallowRef<SanGuo | undefined>) {
        app.directive('size',
            {
                async mounted(el, binding) { setSize(el, binding, sg) },
                async beforeUpdate(el, binding) { setSize(el, binding, sg) },
                deep: true
            },
        )
    }

    function setSize(el: HTMLDivElement, binding: DirectiveBinding<any>, sg: ShallowRef<SanGuo | undefined>) {
        if (!binding.value || !sg.value) {
            return
        }
        if (binding.value instanceof CfgStr) {
            const size = binding.value.size
            el.style.position = 'absolute';
            el.style.width = `${size.w}px`
            el.style.height = `${size.h}px`
            el.style.left = `${size.x}px`
            el.style.top = `${size.y}px`
            el.style.zIndex = `${size.z}`
        }
    }

    export function installBg(app: App, sg: ShallowRef<SanGuo | undefined>) {
        app.directive('bg',
            {
                async mounted(el, binding) { setBg(el, binding, sg) },
                async beforeUpdate(el, binding) { setBg(el, binding, sg) },
                deep: true
            },
        )
    }

    function setBg(el: HTMLDivElement, binding: DirectiveBinding<any>, sg: ShallowRef<SanGuo | undefined>) {
        if (!binding.value || !sg.value) {
            return
        }
        let img: Img.ImgDef
        if (typeof binding.value === 'string') {
            img = sg.value.res.getImg(binding.value as string)!
            el.style.backgroundImage = `url(${img.getBaseImg().imgDataUrl})`
            return
        }

        if (binding.value instanceof Img.ImgDef) {
            img = binding.value
            el.style.backgroundImage = `url(${img.getBaseImg().imgDataUrl})`
        } else if (binding.value instanceof CfgStr) {
            const size = binding.value.size
            img = binding.value.imgGroup?.hasDef()!
            el.style.position = 'absolute';
            el.style.width = `${size.w}px`
            el.style.height = `${size.h}px`
            el.style.left = `${size.x}px`
            el.style.top = `${size.y}px`
            el.style.zIndex = `${size.z}`
            img && (el.style.backgroundImage = `url(${img.getImg(size.w, size.h).imgDataUrl})`)
        }
    }
}