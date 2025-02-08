<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';

const line = defineModel()
const { idx, editable, height, fontSize, keyup } = defineProps({
    idx: { type: Number, required: true },
    editable: { default: true },
    height: { default: 22 },
    fontSize: { default: 12 },
    keyup: { type: Function, default: (e: Event) => { } }
})
const inputEl = ref<HTMLInputElement>()
const styles = computed(() => {
    const styles: Record<string, any> = {}
    styles['--height'] = `${height}px`
    styles['--font-size'] = `${fontSize}px`
    // styles.backgroundColor = 'red'
    return styles
})

onMounted(() => {
})

const focused = ref(false)
async function keyupFun(e: Event) { await keyup(e) }
</script>
<template>
    <div class="editor-line" :style="styles">
        <div class="line">
            <input ref="inputEl" v-model="line" @focusin="() => focused = true" @focusout="() => focused = false"
                @keyup="keyupFun" />
            <slot :focused="focused"></slot>
        </div>
    </div>
</template>

<style scoped lang="less">
.editor-line {
    text-align: left;
    position: relative;
    box-sizing: border-box;
    height: var(--height);
    min-height: var(--height);
    line-height: var(--height);
    font-family: Consolas, "Courier New", monospace;
    font-weight: normal;
    font-feature-settings: "liga" 0, "calt" 0;
    font-variation-settings: normal;
    letter-spacing: 0px;
    display: flex;
    white-space: nowrap;


    .glyph {
        color: #ddd;
        background-color: #333;
        height: var(--height);
        line-height: var(--height);
        width: 17px;
        padding-right: 3px;
        text-align: right;
    }

    .line {
        flex: 1;
    }

    .focus {
        // border: 1px solid #555;
        box-shadow: 0px 0px 1px 1px #555;
    }

    input {
        background-color: transparent;
        border: none;
        outline: none;
        letter-spacing: 0px;
        font-family: Consolas, "Courier New", monospace;
        font-weight: normal;
        font-feature-settings: "liga" 0, "calt" 0;
        font-variation-settings: normal;
        font-size: var(--font-size);
        position: absolute;
        top: 0px;
        left: 0px;
        height: var(--height);
        min-height: var(--height);
        line-height: var(--height);
        width: 100%;
        // color: #3f3f3f;
        color: #0000;
        caret-color: rgb(212, 212, 212);
        z-index: 10;
        padding: 0;

        &::selection {
            color: #0000;
        }
    }

    // .editor-input {
    //     position: absolute;
    //     top: 0px;
    //     left: 0px;
    //     color: #3f3f3f;
    //     caret-color: rgb(212, 212, 212);

    //     &::selection {
    //         color: #0000;
    //     }
    // }
}
</style>
