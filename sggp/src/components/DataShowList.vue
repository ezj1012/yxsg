<script setup lang="ts">
import { type Ref, inject, onMounted, ref, computed, watch } from 'vue';

import type { SanGuo } from '@/app/sg';

const filter = ref('')
const { sg } = inject('sg') as { sg: SanGuo }

const datas = computed(() => {
    const keys = []

    for (const key of sg.dataMgr.dataCache.cache.keys()) {
        keys.push({
            key,
            value: sg.dataMgr.dataCache.cache.get(key)
        })
    }

    return keys.filter(k => {
        if (filter.value) {
            return k.key.includes(filter.value.trim())
        }
        return true
    })
})

function copyImg(key: string) {
    let input = document.createElement("input");
    //给input的内容复制
    input.value = key;
    // 在body里面插入这个元素
    document.body.appendChild(input);
    // 选中input里面内容
    input.select();
    //执行document里面的复制方法
    document.execCommand("Copy");
    document.body.removeChild(input)
}
const show = ref(true)
</script>
<template>
    <div class="show-btn" @click="() => show = !show">show</div>
    <div class="abcd" v-show="show">
        <div class="data-list-show">
            <div class="filter"> <input v-model.trim="filter" /></div>
            <div class="data-menu-main">
                <template v-for="(  kv ) in datas ">
                    <div class="data-line" @dblclick="copyImg(kv.key)">
                        <div class="key">{{ kv.key }}</div>
                        <div class="line"><span> > </span><span>{{ kv.value }}</span></div>
                    </div>
                </template>
            </div>
        </div>
    </div>
</template>
<style lang="less" scoped>
.show-btn {
    position: absolute;
    width: 140px;
    height: 40px;
    line-height: 40px;
    left: 0;
    top: 0;
    z-index: 100;
    text-align: center;
    cursor: pointer;
}

.abcd {
    position: fixed;
    width: 400px;
    height: 100%;
    left: 0;
    top: 0;
    overflow: hidden;
}

.data-list-show {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    background-color: #242424;
    overflow-x: hidden;
    position: relative;

    .img-info {
        height: 200px;
        border-top: 1px solid #0000005d;
        border-bottom: 1px solid #0000005d;
        position: relative;
        display: flex;
        flex-direction: column;
        align-items: center;

        .img-po {
            margin: 20px 0;
            width: 200px;
            height: 130px;
            user-select: none;
        }
    }

    &::-webkit-scrollbar {
        width: 12px;
        height: 12px;
        background-color: #252526;
    }

    &::-webkit-scrollbar-thumb {
        border-radius: 1psx;
        background: #cccccc33;
    }

    .filter {
        width: calc(100% - 20px);
        height: 30px;
        padding: 0px 10px;
        margin: 5px 0;

        input {
            background: transparent;
            width: 100%;
            outline: none;
            border: 0;
            background-color: transparent;
            padding: 0 2px;
        }

    }


    .data-menu-main {
        flex: 1;
        overflow-x: hidden;
        overflow-y: auto;

        .line-select {
            background-color: #6958ff42;
            border: 1px solid #6958ffa1;

            &:hover {
                background-color: #6958ff88;
            }

        }

        .line-unselect {

            &:hover {
                background-color: #49494988;
            }
        }

        .line-select-rename {
            background-color: transparent;

            .field-editor {
                // box-shadow: 0px 1px 1px #e2e2e2;

                border: 1px solid #a8cdff6e;
            }
        }

        .data-line {

            &:hover {
                border: 1px solid #6958ffa1;
            }

            .key {
                padding-left: 15px;
                user-select: none;
                font-size: 14px;
            }

            .line {
                padding-left: 15px;
                user-select: none;
                cursor: pointer;
                font-size: 16px;
                text-align: left;
                position: relative;
                box-sizing: border-box;
                min-height: 22px;
                height: 22px;
                font-family: Consolas, "Courier New", monospace;
                font-weight: normal;
                font-feature-settings: "liga" 0, "calt" 0;
                font-variation-settings: normal;
                line-height: 22px;
                letter-spacing: 0px;
                white-space: nowrap;
                color: #ccc;

                .left-line {
                    position: absolute;
                    width: 2px;
                    border-left: 1px solid #555555f0;
                }

                .ic {
                    width: 22px;
                    height: 22px;
                }
            }
        }

    }

}



.field-editor {
    display: inline-block;
    border: none;
    outline: none;
    font-size: 14px;
    text-align: left;
    position: relative;
    box-sizing: border-box;
    width: calc(100% - 30px);
    height: 22px;
    font-family: Consolas, "Courier New", monospace;
    font-weight: normal;
    font-feature-settings: "liga" 0, "calt" 0;
    font-variation-settings: normal;
    line-height: 22px;
    letter-spacing: 0px;
    white-space: pre-line;
    caret-color: rgb(212, 212, 212);
}
</style>