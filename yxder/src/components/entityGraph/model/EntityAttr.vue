<script setup lang="ts">

import { SimpleModelAttrHint } from '@/app/coder';
import { ref, watch } from 'vue';


const { idx, attr, } = defineProps({
    idx: { type: Number, required: true },
    attr: { type: String, default: "" }
})

const hint = ref(new SimpleModelAttrHint())
watch(() => attr, () => hint.value.update(attr || ''), { immediate: true })

</script>
<template>
    <div class="line">
        <div class="glyph" :style="{ color: '#bbb' }">{{ idx! + 1 }}</div>
        <div class="field-info">
            <span class="name" style="color: #9cdcfe;">{{ hint.getName() }}</span>
            <span v-show="hint.attr.notNullSymbol" style="color: #da70d6;">!</span>
            <span v-show="hint.attr.typeSymbol" style="color: #d4d4d4;">:</span>
            <span v-show="hint.attr.type" class="type" style="color: #4ec9b0;">
                {{ hint.getType() }}
            </span>
            <span v-show="hint.attr.defaultValueSymbol" style="color: #d4d4d4;">=</span>
            <span v-show="hint.attr.defaultValue" class="def-val"
                :style="{ color: hint.isNumber() ? '#b5cea8' : '#ce9178' }">
                {{
                    hint.getDefaultValue()
                }}
            </span>
            <span v-show="hint.attr.zhNameSymbol" style="color: #6a9955;">{{ '//' }}</span>
            <span v-show="hint.attr.zhName" style="color: #6a9955;">
                {{ hint.getZhName() }}
            </span>
        </div>
    </div>
</template>

<style scoped lang="less">
.glyph {
    color: #ddd;
    background-color: #333;
    height: var(--height);
    line-height: var(--height);
    width: 17px;
    min-width: 17px;
    padding-right: 5px;
    text-align: right;

    // border-right: 1px solid #111;
}

.field-info {
    overflow: hidden;
    max-height: 22px;
}
</style>
