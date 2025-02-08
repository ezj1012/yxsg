<script setup lang="ts">
import { computed, inject, onMounted, ref, toRaw, type Ref, shallowRef, type ShallowRef, onUnmounted, watch } from 'vue';

import * as monaco from 'monaco-editor/esm/vs/editor/editor.api';

import { type Node } from '@antv/x6';
import { EntityModelType, type EntityCfg, type EntityModel, type GraphModel, type YXCoder } from '@/app/yxcoder';
import { coderLanguageName } from '@/app/monaco';
import EditorLine from './EditorLine.vue';
import EntityName from './EntityName.vue';
import { AttrType, EntityMonacoCode } from '@/app/coder';
import BtnEntityToCenter from './BtnEntityToCenter.vue';

const { close } = defineProps({
    close: { type: Function, default: () => { } }
})
const { coder } = inject('entityCoder') as { coder: Ref<YXCoder> }

const className = ref('')
const codeEditor = shallowRef<EntityMonacoCode>()
watch(className, () => { gm.value && (gm.value.model.name = className.value) })

onMounted(() => {
    codeEditor.value = new EntityMonacoCode('entity-monaco', changeContent)
    codeEditor.value.modelEditor.addCommand(monaco.KeyMod.Alt | monaco.KeyMod.Shift | monaco.KeyCode.KeyF, () => {
        codeEditor.value?.formatContent()
    })
    codeEditor.value.modelEditor.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyCode.KeyS, async () => await doSave())
    codeEditor.value.modelEditor.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyCode.KeyD, async () => await doClose())
})

onUnmounted(() => {
    codeEditor.value?.dispose()
})

const gm = ref<GraphModel>()
const data = ref<EntityModel>()

function doEdit(gmData: GraphModel) {
    gm.value = gmData
    className.value = gmData.model.name || ''
    codeEditor.value!.setValue(gmData.model)
    data.value = gmData.model
}

defineExpose({ doEdit })

async function doClose() {
    await doSave()
    gm.value = undefined
    data.value = undefined
    close && (await close())
}

async function doSave() {
    gm.value && await coder.value.updateEntity(gm.value)
}

async function changeType() {
    if (gm.value) {
        let t = data.value!.extendsType + 1
        t = t > 3 ? 0 : t
        data.value!.extendsType = t
        await doSave()
    }
}

async function changeContent(content: string) {
    data.value && (data.value.attrs = content.split('\n'))
}

async function keyupListener(e: KeyboardEvent) {
    if (e.key == 'Enter') {
        e.preventDefault()
        e.stopPropagation()
        codeEditor.value?.focus()
    }
}
const tpyeName = computed(() => {
    const idx = data.value?.extendsType || 0
    return idx == 0 ? '   int' : idx == 1 ? '  long' : idx == 2 ? 'N  int' : 'N long'
})
</script>

<template>
    <div class="editor-info line-layout">
        <div class="line" style="min-height: 25px;">
            <div class="extend-type" @click="changeType()">
                {{ tpyeName }}
            </div>
            <div class="line-container">
                <EditorLine v-model="className" :idx="0" v-slot="{ focused }" :keyup="keyupListener">
                    <EntityName :class-name="className" :focused="focused" />
                </EditorLine>
            </div>
            <div class="line-actions" style="opacity: 1">
                <BtnEntityToCenter :nid="gm?.id" />
                <div class="line-actions-item codicon codicon-reply" @click="() => doClose()"></div>
            </div>
        </div>
        <div id="entity-monaco" class="entity-monaco-editor"></div>
    </div>
</template>

<style scoped lang="less">
.editor-info {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    min-height: 200px;

    .line {
        padding: 0 20px;

        .extend-type {
            height: 22px;
            width: 35px;
            min-width: 35px;
            margin-right: 5px;
            line-height: 25px;
            user-select: none;
            color: #9cdcfe;
            // background-color: rgba(128, 128, 128, 0.1);
            text-align: right;
        }

        .line-container {
            font-size: 12px;

        }
    }

    .entity-monaco-editor {
        flex: 1;
        width: 100%;
        height: 100%;
    }
}

// .model-editor-hide {
//     position: fixed;
//     width: 150px;
//     height: 15px;
//     z-index: 2000;
//     left: calc(50% - 75px);
//     bottom: 20px;
//     background-color: rgb(207, 207, 207, 0.5);
//     border-radius: 10px;
//     cursor: pointer;
//     user-select: none;

//     &:hover {
//         background-color: rgba(105, 105, 105, 0.5);
//     }
// }

// .model-editor {
//     position: fixed;
//     width: 800px;
//     height: 100%;
//     top: 0px;
//     right: 0px;
//     background-color: rgb(207, 207, 207, 0.5);
//     padding: 10px;
//     display: flex;
//     flex-direction: column;

//     .btn-group {
//         width: 100%;
//         height: 32px;
//         margin-bottom: 10px;
//     }

//     .editor-info {
//         flex: 1;
//     }
// }</style>