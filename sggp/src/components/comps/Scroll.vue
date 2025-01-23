<script setup lang="ts">
import type { SanGuo } from '@/app/sg';
import { inject, onMounted, ref, watch } from 'vue';


const { sg } = inject('sg') as { sg: SanGuo }

const { scroll, maxHeight } = defineProps({ scroll: { default: 'hidden' }, maxHeight: { type: Number } })
const scrollContainer = ref<HTMLDivElement>()
const tarImageSrc = ref('')
const barImageSrc = ref('')
const barHoverImageSrc = ref('')
const scrollUp = ref('')
const scrollUpDown = ref('')
const scrollDown = ref('')
const scrollDownUp = ref('')
const style = ref({} as any)
onMounted(() => {
    updateStyle()
})

watch(() => scrollContainer.value?.offsetHeight, () => {
    updateStyle()
})

function updateStyle() {
    let sc = scrollContainer.value as HTMLDivElement;
    if (maxHeight || sc.offsetHeight > 0) {
        let hh: number = maxHeight ? maxHeight : sc.offsetHeight;
        hh = hh - 4
        if (hh > 40) {


            tarImageSrc.value = sg.res.getImgGroup("common#scroll_track")?.hasDef()?.getImg(19, hh).imgDataUrl || '123'
            let newHeight = ((hh - 40) * hh) / (maxHeight ? hh + 40 : sc.scrollHeight);
            barImageSrc.value = sg.res.getImgGroup("common#scroll_bar")?.hasDef()?.getImg(13, newHeight).imgDataUrl || '123'
            barHoverImageSrc.value = sg.res.getImgGroup("common#scroll_bar")?.hasOn()?.getImg(13, newHeight).imgDataUrl || '123'
            scrollDown.value = sg.res.getImgGroup("common#scroll_downarr")?.hasDef()?.getDataUrl() || '123'
            scrollDownUp.value = sg.res.getImgGroup("common#scroll_downarr")?.hasDown()?.getDataUrl() || '123'
            scrollUp.value = sg.res.getImgGroup("common#scroll_uparr")?.hasDef()?.getDataUrl() || '123'
            scrollUpDown.value = sg.res.getImgGroup("common#scroll_uparr")?.hasDown()?.getDataUrl() || '123'
            var t = {
                '--track-img': `url(${tarImageSrc.value})`,
                '--track-bar': `url(${barImageSrc.value})`,
                '--track-bar-hover': `url(${barHoverImageSrc.value})`,
                '--track-up': `url(${scrollUp.value})`,
                '--track-up-down': `url(${scrollUpDown.value})`,
                '--track-down': `url(${scrollDown.value})`,
                '--track-down-up': `url(${scrollDownUp.value})`,
                'overflow-y': scroll,
            } as any
            if (maxHeight) {
                t.maxHeight = `${maxHeight}px`
            }
            style.value = t
        } else {
            style.value = { 'overflow-y': 'hidden' }
        }
    }
}

defineExpose({ updateStyle })

</script>
<template>
    <div class="com_scroll_container" :style="style" ref="scrollContainer">
        <slot></slot>
    </div>
</template>
<style lang="less" scoped>
.com_scroll_container {
    overflow-y: auto;
    overflow-x: hidden;

    &::-webkit-scrollbar-thumb {
        width: 16px;
        background: var(--track-bar) no-repeat;
        background-position-x: 4px;
    }

    &::-webkit-scrollbar-thumb:hover {
        background: var(--track-bar-hover) no-repeat;
        background-position-x: 4px;
    }

    &::-webkit-scrollbar {
        width: 20px;
        background: var(--track-img);
        background-position: 1px 1px;
        background-color: #373530fe;
    }

    &::-webkit-scrollbar-button:decrement {
        height: 20px;
        background: var(--track-up) no-repeat scroll 3px 1px;

        &:active {
            background: var(--track-up-down) no-repeat scroll 3px 1px;
        }
    }

    &::-webkit-scrollbar-button:increment {
        height: 20px;
        background: var(--track-down-up) no-repeat scroll 3px 1px;

        &:active {
            background: var(--track-down) no-repeat scroll 3px 1px;
        }
    }
}
</style>
