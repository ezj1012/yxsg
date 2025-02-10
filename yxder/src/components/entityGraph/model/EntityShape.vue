<script setup lang="ts">
import { attrToRlt, nodeToGraphNodeCfg, type CoderGraph } from '@/app/graph';
import type { EntityModel, GraphModel, YXCoder } from '@/app/yxcoder';
import { computed, inject, onMounted, ref, shallowRef, watch, type Ref, type ShallowRef } from 'vue';
import type { Node } from '@antv/x6';
import EntityName from './EntityName.vue';
import EntityAttr from './EntityAttr.vue';
import BtnEntityToEditor from './BtnEntityToEditor.vue';
import BtnEntityToGen from './BtnEntityToGen.vue';
const { models, coder, graph } = inject('entityCoder') as { models: Ref<GraphModel[]>, coder: Ref<YXCoder>, graph: ShallowRef<CoderGraph> }

const node = shallowRef<Node>()
const data = ref<EntityModel>()
const width = ref<number>(200)
const style = computed(() => { return { width: `${width.value}px` } })

const createTimeStr = 'createTime!: long // 创建时间'
const modifyTimeStr = 'modifyTime!: long // 修改时间'

onMounted(() => {
    node.value = (inject('getNode') as any)()!;
    width.value = node.value?.getSize().width || 200
    data.value = models.value.find(m => m.id === node.value?.id)?.model

})
const redefIdB = ref(false)

watch(() => data.value?.attrs, () => {
    resize()
    let redefId = false
    const rlts = data.value?.attrs.map((attr, index) => { if (attr.trim().startsWith('id> ')) { redefId = true; } return attrToRlt(node.value!.id, attr, index) }).filter(a => a !== undefined)
    redefIdB.value = redefId
    if (rlts != undefined) {
        graph.value.updateNodeRlt(node.value!, rlts, redefId)
    }

}, { immediate: true })

async function addWidth() {
    width.value += 5
    resize()
    node.value && await coder.value?.updatePosition(nodeToGraphNodeCfg(node.value!))
}

async function incWidth() {
    if (width.value - 5 >= 200) {
        width.value -= 5
        resize()
        node.value && await coder.value?.updatePosition(nodeToGraphNodeCfg(node.value!))
    }
}

function resize() {
    if (node.value && data.value) {
        const height = 102 + (data.value!.attrs.length || 0) * 22
        const size = node.value.getSize()
        node.value.getPorts().forEach(p => {
            node.value?.setPortProp(p.id!, 'size', { width: width.value - 2 })
        })
        if (height !== size.height || width.value != size.width) {
            node.value.setSize({ width: width.value, height })
        }
    }
}

</script>
<template>
    <div class="line-layout entity-shape ">
        <div class="line class-name">
            <div class="line-container">
                <EntityName :class-name="data?.name" />
            </div>
            <div class="line-actions" style="margin-right: 10px;">
                <BtnEntityToGen :nid="node?.id" />
                <BtnEntityToEditor :nid="node?.id" />
                <div class="line-actions-item codicon codicon-chevron-left" @click="incWidth()"></div>
                <div class="line-actions-item codicon codicon-chevron-right" @click="addWidth()"></div>
            </div>
        </div>
        <div class="attrs">
            <template v-if="data">
                <EntityAttr v-if="!redefIdB"
                    :attr="`id: ${data.extendsType == 0 || data.extendsType == 2 ? 'int' : 'long'} // auto`"
                    :idx="-1" />
                <template v-for="(attr, idx) in data.attrs">
                    <EntityAttr :attr="attr" :idx="idx" />
                </template>
                <template v-if="data.extendsType <= 1">
                    <EntityAttr :attr="createTimeStr" :idx="data.attrs.length" />
                    <EntityAttr :attr="modifyTimeStr" :idx="data.attrs.length + 1" />
                </template>
            </template>
        </div>
    </div>
</template>

<style scoped lang="less">
.entity-shape {
    width: v-bind('style.width');
    min-height: 100px;
    background-color: #303030;
    box-sizing: border-box;
    border: 1px solid #444;
    position: absolute;
    border-radius: 5px;
    font-size: 12px;
    padding: 5px 0 5px 0;

    .class-name {
        padding-left: 10px;
        margin-bottom: 2px;
    }

    .attrs {
        margin-top: 0px;
    }
}
</style>
