<script setup lang="ts">
import type { CoderGraph } from '@/app/graph';
import type { EntityCfg, GraphModel, YXCoder } from '@/app/yxcoder';
import { computed, inject, onMounted, onUnmounted, ref, type Ref, type ShallowRef } from 'vue';
import EntityMonaco from './model/EntityMonaco.vue';
import BtnEntityToCenter from './model/BtnEntityToCenter.vue';

const { coder, graph, models, sctNode } = inject('entityCoder') as
    {
        graph: ShallowRef<CoderGraph>,
        coder: Ref<YXCoder>,
        models: Ref<GraphModel[]>,
        sctNode: Ref<string | undefined>
    }
const entityMonacoEl = ref()
const editing = ref(false)
async function newModel() { await coder.value?.newEntity() }

function doSct(nodeId?: string) {
    sctNode.value = nodeId
}

async function doEdit(node: GraphModel, focusName = false) {
    if (editing.value) {
        await entityMonacoEl.value.doClose()
    }

    sctNode.value = node.id
    editing.value = true
    await entityMonacoEl.value.doEdit(node, focusName)
}

defineExpose({ doEdit })

const filterdModels = computed(() => {
    return models.value || [];
})

async function keyup(e: KeyboardEvent) {
    // if (!sctNode.value) {
    //     return
    // }
    if (e.key == 'Tab') {
        e.stopPropagation()
        e.preventDefault()
        let idx = filterdModels.value.findIndex(m => m.id == sctNode.value)
        if (e.shiftKey) {
            idx--
            if (idx < 0) {
                idx = filterdModels.value.length - 1;
            }
        } else {
            idx++
            if (idx >= filterdModels.value.length) {
                idx = 0;
            }
        }
        doSct(filterdModels.value[idx].id)
        return
    } else if (sctNode.value) {
        if (e.ctrlKey && e.key == 'd' || e.key == 'F2') {
            e.stopPropagation()
            e.preventDefault()
            const m = filterdModels.value.find(n => n.id == sctNode.value)
            m && doEdit(m)
        } else if (e.ctrlKey && e.key == 's') {
            e.stopPropagation()
            e.preventDefault()
            const m = filterdModels.value.find(n => n.id == sctNode.value)
            m && await coder.value.gen([m.model])
        }
        return
    } else {
        if (e.ctrlKey && e.key == 's') {
            e.stopPropagation()
            e.preventDefault()
            await coder.value.save()
        }
    }

}

onMounted(() => {
    document.addEventListener('keydown', keyup)
})

onUnmounted(() => {
    document.removeEventListener('keydown', keyup)
})

</script>
<template>
    <div class="entity-ctrl">
        <div class="title-line">
            <div class="title">模型管理: {{ coder?.cfg?.project?.name }}</div>
            <ul class="ops">
                <li class="action-item ">
                    <a class="action-label codicon codicon-references" @click="newModel"></a>
                </li>
            </ul>
        </div>
        <div class="line-layout entities">
            <EntityMonaco v-show="editing" ref="entityMonacoEl" :close="() => { editing = false }" />
            <div v-show="!editing">
                <div class="line entity" :class="[sctNode == gm.id ? 'sct' : '']" v-for=" gm, idx in filterdModels"
                    @click="doSct(gm.id)">
                    <div class="line-container">
                        <div class="name"> {{ `${gm.model?.name || '未命名'} (${gm.x},${gm.y})` }}</div>
                    </div>
                    <div class="line-actions">
                        <BtnEntityToCenter :nid="gm.id" />
                        <div class="line-actions-item codicon codicon-edit" @click="doEdit(gm)"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
<style scoped lang="less">
.entity-ctrl {
    overflow: hidden;
    height: 100%;
    background-color: #252526;
    position: relative;
    font-size: 12px;
    color: rgb(204, 204, 204);
    display: flex;
    flex-direction: column;

    .entities {
        flex: 1;
        overflow-x: hidden;
        margin-bottom: 10px;

        .entity {
            padding: 0 20px;

            &:hover:not(.sct) {
                box-shadow: inset 0 0 0px 1px #007fd450;
                background-color: rgb(61, 61, 63, 0.3);
            }
        }

        .sct {
            box-shadow: inset 0 0 0px 1px #007fd4;
            background-color: rgb(61, 61, 63, 0.5);
        }
    }

    .title-line {
        height: 35px;
        min-height: 35px;
        max-height: 35px;
        line-height: 35px;
        overflow: hidden;
        user-select: none;
        display: flex;
        flex-direction: row;
        align-items: center;
        justify-content: space-between;

        .title {
            user-select: none;
            padding-left: 20px;
            height: 100%;
            line-height: 35px;
            color: #bbbbbb;
            font-size: 11px;
        }

        ul {
            margin: 0;
            padding: 0;

            li {
                list-style: none;
            }
        }

        .ops {
            height: 100%;
            align-items: center;
            display: flex;
            margin-right: 8px;

            .action-item {
                align-items: center;
                cursor: pointer;
                display: block;
                justify-content: center;
                position: relative;
                margin-right: 4px;

                .action-label {
                    color: #c5c5c5;

                    &:hover:not(.disabled) {
                        background-color: rgba(90, 93, 94, 0.31);
                    }

                    align-items: center;
                    background-position: 50%;
                    background-repeat: no-repeat;
                    background-size: 16px;
                    display: flex;
                    justify-content: center;
                    margin-right: 0;
                    border-radius: 5px;
                    padding: 3px;
                    box-sizing: content-box;
                }
            }
        }

    }

}
</style>