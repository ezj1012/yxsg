import { type ShallowRef, type App, type DirectiveBinding, ref } from "vue"

const sctEl = ref<HTMLDivElement>()
export function installMsg(app: App) {
    app.directive('sct', {
        mounted(el, binding) {
            function handler(e: Event) {
                if (sctEl.value == el) {
                    return
                } else if (sctEl.value) {
                    sctEl.value.classList.remove('sct');
                }
                // el add class AA
                el.classList.add('sct');
                sctEl.value = el;
            }
            el.handler = handler;
            el.addEventListener("click", el.handler);
        },
        unmounted(el) {
            el.removeEventListener('click', (el as any).handler);
        }
    })

    app.directive('sctout', {
        mounted(el, binding) {
            function handler(e: Event) {
                if (el.contains(e.target as Node)) return false;
                if (sctEl.value) {
                    sctEl.value.classList.remove('sct');
                    sctEl.value = undefined
                }
            }
            el.handler = handler;
            document.addEventListener("click", el.handler);
        }
        , unmounted(el, binding) { document.removeEventListener("click", el.handler); }
    })


}