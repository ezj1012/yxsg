<script lang="ts" setup>
import type { SanGuo } from '@/app/sg';
import { computed, inject, onMounted, ref } from 'vue';
import Scroll from '../../Scroll.vue';
import PBtn from '../../PBtn.vue';
import { CfgStr } from '@/app/cfg';
const { sg } = inject('sg') as { sg: SanGuo }
const { build, lv } = defineProps({ build: { required: true }, lv: { default: 1 } }) as { build: any, lv: number }


const buildBtn = new CfgStr(`K:funpanl#build_${build.id};T:I_BTN;S:0,37,60,26,4;RFI:common#btn_red;ACT:closeFunPan;SMSG:BuildTileMsg,${build.id},${lv};TXT:T:建造,F:13,C:var(--gold),;DISABLE;}`)

const img = ref<string>('')
onMounted(() => {
    img.value = sg.img(`playing#innercity#${build.typeName}`)
})

const canBuild = computed(() => {
    return false
})

</script>
<template>
    <div class="build">
        <!-- <div class="img" :style="{ backgroundImage: img }"></div>
        <div class="info">
            <div class="name">{{ build.name }}</div> -->
        <PBtn :cfg="buildBtn" />
        <!-- </div>
        <div class="msg bor">{{ build.description }}</div> -->
    </div>
</template>
<style lang="less" scoped>
.build {
    width: 100%;
    height: 70px;
    // background-color: red;
    position: relative;
    margin-bottom: 5px;
    display: flex;
    color: #ffffffd5;
    background-color: #203030a0;
    border: 1px solid #203030;

    .img {
        width: 72px;
        min-width: 72px;
        margin-left: 8px;
        height: 60px;
        margin-top: 5px;
        background-repeat: no-repeat;
        background-size: 103px 79px;
        background-position-x: -15px;
        background-position-y: -12px;
        overflow: hidden;
        background-color: #203030a0;
        box-shadow: 0px 0px 0px 0.5px #80867C30, 0px 0px 0px 1px #7D6F6630;
        border: 1px solid #203030;
    }

    .info {
        margin-left: 8px;
        width: 60px;
        min-width: 60px;
        height: 100%;
        position: relative;
        user-select: none;

        .name {
            font-weight: 600;
            font-size: 12px;
            height: 40px;
            line-height: 40px;
            text-align: center;
        }

    }

    .msg {
        flex: 1;
        font-size: 12px;
        margin: 8px;
        height: 46px;
        overflow-y: auto;
        padding: 5px;
        background-color: #103030a0;
        box-shadow: 0px 0px 0px 0.5px #80867C30, 0px 0px 0px 1px #7D6F6630;
        border: 1px solid #202330;

        &::-webkit-scrollbar {
            width: 3px;
            height: 3px;
            background-color: #252526;
        }

        &::-webkit-scrollbar-thumb {
            border-radius: 1psx;
            background: #cccccc33;
        }
    }
}
</style>