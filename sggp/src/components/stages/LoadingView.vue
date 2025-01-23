<script setup lang="ts">
// import { type LoadingProgressBar, baseUrl } from '@/app/model/comm';
import type { Traceable } from '@/app/model'
import type { SanGuo } from '@/app/sg'
import { Img } from '@/app/img';
// import Ctrl from '@/app/ctrl';
import { computed, onUnmounted, ref, watch, shallowRef, inject, onMounted } from 'vue';
// import { LoginStage, SanGuo, Stage, LoadingStage } from '@/app/sanguo';

const { sg } = inject('sg') as { sg: SanGuo }
const ok = ref(false)
const count = shallowRef(0)
const bgRes = [
    Img.of(`/rsm/images/loading/main_pic.png`, false),
    Img.of(`/rsm/images/loading/progress_bg.png`, false),
    Img.of(`/rsm/images/loading/main_complete.png`, false),
]


const loadState = ref<Traceable>()
const traceState = shallowRef<Traceable>()
const timerId = ref()
// // 加载本身资源
Img.loadImages(bgRes, (def, err) => {
    count.value++;
    if (count.value == bgRes.length) {
        ok.value = true;
        count.value = 0;
    }
});

const style = computed(() => {
    const s: { [key: string]: string } = {}
    if (ok.value) { s['background-image'] = `url(${bgRes[0].getBaseImg().imgDataUrl})` }
    return s;
})


onMounted(async () => {
    traceState.value = { ...loadState.value } as any
    timerId.value = setInterval(() => loadState.value = { ...traceState.value } as any, 100)
    await sg.setup(traceState)
})

onUnmounted(() => {
    if (timerId.value) {
        clearInterval(timerId.value)
    }
})

const show = ref(false)
</script>
<template>
    <div class="sg-loading" :style="style">
        <div v-if="ok && loadState" class="content">
            <div class="loading-msg-1">
                <div>{{ loadState.msg }}</div>
                <div>{{ `${Math.floor(loadState.pct)}%` }}</div>
            </div>
            <div class="loading-msg-2">
                <div>如果你是第一打开游戏, 加载文件可能较大,请耐心等待.</div>
            </div>
            <div class="loading-msg-3">
                <div>快速增加人口的方法: 使用安抚百姓-增丁、使用道具'典民令'</div>
            </div>
            <div class="loading-bg" :style="{ backgroundImage: `url('${bgRes[1].getBaseImg().imgDataUrl}')`, }"></div>
            <div class="loading-main-comp"
                :style="{ backgroundImage: `url('${bgRes[2].getBaseImg().imgDataUrl}')`, width: `${loadState.pct * 3.85}px`, }">
            </div>
        </div>
    </div>
</template>
<style lang="less" scoped>
.sg-loading {
    position: absolute;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 1);
    overflow: hidden;
    display: flex;
    justify-content: space-between;
    background-repeat: no-repeat;
    background-size: 100%;
    font-size: 12px;
    color: white;

    .content {
        width: 1000px;
        height: 600px;
        position: absolute;
        left: calc(50% - 500px);
        top: calc(50% - 300px);
        display: flex;
        justify-content: space-between;


        .loading-msg-1 {
            position: absolute;
            display: flex;
            justify-content: space-between;
            width: 240px;
            left: 380px;
            top: 455px;
        }

        .loading-msg-2 {
            position: absolute;
            width: 300px;
            left: 346px;
            top: 490px;
        }

        .loading-msg-3 {
            position: absolute;
            color: #bfb3b3;
            width: 400px;
            bottom: 15px;
            left: 335px;
        }

        .loading-bg {
            position: absolute;
            user-select: none;
            width: 528px;
            height: 36px;
            left: 236px;
            top: 520px;
            z-index: 100;
        }

        .loading-main-comp {
            position: absolute;
            user-select: none;
            background-size: 100% 100%;
            max-width: 385px;
            height: 10px;
            left: 307px;
            top: 540px;
            z-index: 110;
        }
    }
}
</style>