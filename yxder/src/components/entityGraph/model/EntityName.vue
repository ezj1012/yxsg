<script setup lang="ts">
import { computed } from 'vue';
const {
    className, focused
} = defineProps({
    className: { type: String, default: '' },
    focused: { type: Boolean, default: false },
})

const group = computed(() => {
    if (className == '') { return '' }
    let g = '';
    let falg = false
    for (let i = 1; i < className.length; i++) {
        if (className[i] === className[i].toUpperCase()) {
            g = className.substring(0, i)
            falg = true
            break
        }
    }
    if (!falg) {
        g = className
    }
    return g.length == 0 || focused ? g : g + ':';
})

const name = computed(() => {
    if (className == '') { return '' }
    const l = group.value.length;
    return l == 0 ? '' : className.substring(focused ? l : l - 1);
})

const showTip = computed(() => !focused && group.value.length == 0)
</script>
<template>
    <div class="entity-class-name">
        <span v-show="showTip" class="group">请输入类名</span>
        <span class="group">{{ `${group}` }}</span>
        <span class="name">{{ name }}</span> <!---->
    </div>
</template>

<style scoped lang="less">
.entity-class-name {
    height: 22px;
    position: relative;
    color: #4ec9b0;

    span,
    div {
        height: 22px;
        line-height: 22px;

        letter-spacing: 0px;
        font-family: Consolas, "Courier New", monospace;
        font-weight: normal;
        font-feature-settings: "liga" 0, "calt" 0;
        font-variation-settings: normal;
    }

    .group {
        opacity: .9;
        color: #88b6ad;
    }

    .title-edit {
        position: absolute;
        width: 100%;
        left: 0px;
        top: 0;
        caret-color: rgb(212, 212, 212);
        color: #0000;
        z-index: 10;
    }
}


.entity-actions {
    height: 22px;
    display: flex;
    align-items: center;
    justify-content: center;
    row-gap: 5px;
    color: #bbb;
    opacity: 0;

    .action-item {
        padding: 2px;
        border-radius: 3px;
        cursor: pointer;

        &:hover:not(.disabled) {
            background-color: rgba(90, 93, 94, 0.31);
        }

        &:active:not(.disabled) {
            background-color: rgba(90, 93, 94, 0.31);
            transform: scale(0.9);
        }
    }
}

&:hover {
    .entity-actions {
        opacity: 1;
    }
}
</style>
