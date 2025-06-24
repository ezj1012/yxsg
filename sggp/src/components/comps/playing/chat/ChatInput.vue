<script lang="ts" setup>
import { reg } from '@/app/action';
import type { SanGuo, Sg } from '@/app/sg';
import { ref, shallowRef, onUpdated, inject, onMounted } from 'vue';

const { sg, chatMgr } = inject('sg') as Sg
// const { sendMsg } = inject('chat') as { sendMsg: (msg: string) => Promise<void> }

const ii = ref(0)
const chatEdit = ref<HTMLDivElement>()
const anchorNodeRef = shallowRef<{ el: Node, offset: number }>()
defineExpose({ addMsgItem })

function addMsgItem() {
    // ars.value.push({id:`id_${ii.value++}`,msg:'asad + ' + (ii.value),name:"asd" + ii.value})
    const node = createNode()
    let rangeNode: any = node
    const anchorNode = anchorNodeRef.value?.el
    const anchorOffset = anchorNodeRef.value ? anchorNodeRef.value.offset : 0
    if (anchorNode) {
        if (anchorNode == chatEdit.value) {
            const bef = chatEdit.value?.childNodes[anchorOffset]!
            rangeNode = chatEdit.value!.insertBefore(node, bef)
        } else if (anchorNode.nodeType === Node.TEXT_NODE) {

            const text = anchorNode.textContent!;
            const beforeText = text.slice(0, anchorOffset);
            const afterText = text.slice(anchorOffset);
            const beforeNode = document.createTextNode(beforeText);
            const afterNode = document.createTextNode(afterText);
            const parent = anchorNode.parentNode!;

            // 用插入的内容替换原有的文本节点
            parent.replaceChild(afterNode, anchorNode); // 先替换掉原文本节点
            rangeNode = parent.insertBefore(node, afterNode); // 在后面文本节点前插入 <span>
            parent.insertBefore(beforeNode, node); // 在 <span> 前插入之前的文本
        }
    } else {
        rangeNode = chatEdit.value?.appendChild(node)
    }

    const selection = document.getSelection()
    if (selection && rangeNode) {
        if (!selection.rangeCount) {
            const position = document.createRange()
            position.setStart(chatEdit.value!, findIdx(rangeNode) + 1);
            position.collapse(true);
            selection.removeAllRanges();
            selection.addRange(position);
            return
        } else {
            const range = selection.getRangeAt(0)
            const idx = findIdx(rangeNode) + 1
            range.setStart(chatEdit.value!, idx);
            range.setEnd(chatEdit.value!, idx);
        }
    }
    chatEdit.value?.focus()
    checkOffset()
}


function findIdx(n: Node) {
    const cs = chatEdit.value?.childNodes
    if (cs) {
        for (let i = 0; i < cs.length; i++) {
            const c = chatEdit.value?.childNodes[i];
            if (c == n) {
                return i
            }
        }
    }
    return 0
}

function checkOffset() {
    const selection = document.getSelection()
    if (selection && selection.type == 'Caret' && selection.anchorNode && (selection.anchorNode?.parentElement == chatEdit.value || selection?.anchorNode == chatEdit.value)) {
        anchorNodeRef.value = {
            el: selection.anchorNode,
            offset: selection.anchorOffset
        }
    }
}

function createNode() {
    const span = document.createElement('span');
    span.textContent = 'hello' + ii.value++; // 你可以自定义插入的内容
    span.contentEditable = "false"
    return span
}

async function keydown(e: KeyboardEvent) {
    if (e.key == 'Enter') {
        const msg = getMsg()
        msg && await chatMgr.sendMsg(msg)
        e.stopPropagation()
        e.preventDefault()
    }
}

function getMsg() {
    return chatEdit.value?.textContent || ''
}

onMounted(async () => {
    reg('playing#chat_send_msg', async (sg) => {
        const msg = getMsg()
        msg && await chatMgr.sendMsg(msg)
    })
})


</script>
<template>
    <div v-msg="'input'" ref="chatEdit" class="chat-edit" contenteditable="true" @keydown="keydown" @keyup="checkOffset"
        @mouseup="checkOffset">
    </div>
</template>
<style lang="less">
.chat-edit {
    position: absolute;
    // box-shadow: inset 0 0 0 1px red;
    left: 90px;
    top: 6px;
    width: 245px;
    height: 22px;
    line-height: 22px;
    border: 0;
    outline: none;
    background-color: transparent;
    caret-color: #fff;
    color: #fff;
    font-size: 14px;
    padding-left: 2px;
    white-space: nowrap;
    overflow-y: hidden;
    overflow-x: auto;

    &::-webkit-scrollbar {
        width: 4px;
        height: 4px;
        background-color: rgba(0, 0, 0, 0.6);
    }

    &::-webkit-scrollbar-thumb {
        border-radius: 1psx;
        background: #cccccc33;
    }

    span {
        // box-shadow: 0 0 0 1px red;
        padding: 0 2px;
        display: inline-block;
        margin: 0 2px;
        user-select: none;
    }
}
</style>