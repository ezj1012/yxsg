<script lang="ts" setup>
import { encode } from '@/app/constant';
import { gFunPanComps } from '@/app/funPanMgr';
import type { SanGuo } from '@/app/sg';
import { inject, onMounted, ref } from 'vue';

const { sg } = inject('sg') as { sg: SanGuo }
const panKey = ref('')
onMounted(() => {
    panKey.value = gFunPanComps[0].key
})
function doChange(key: any) {
    sg.ctx.funPanMgr.setComp(key === '' ? '' : panKey.value)
}

async function doLogin() {
    try {
        await sg.ctx.userMgr.login('qwe123', encode('qwe1234'))
        await sg.ctx.playMgr.refreshPlay()
    } catch (error) {
        sg.ctx.errMsgMgr.pushMsg("登录失败!");
        return false;
    }
}

</script>
<template>
    <div class="table-wrap">
        <select v-model="panKey">
            <option v-for="pan in gFunPanComps" :key="pan.key" :value="pan.key">{{ pan.content }}</option>
        </select>
        <div @click="doChange" class="btn">确定</div>
        <div @click="doChange('')" class="btn">取消</div>
        <div @click="doLogin()" class="btn">login</div>
    </div>
</template>
<style lang="less" scoped>
.table-wrap {
    width: 200px;
    height: 130px;
    position: fixed;
    top: 0px;
    left: 0px;
    opacity: 0;

    &:hover {
        opacity: 1;
    }

    .btn {
        width: 150px;
        height: 25px;
        margin: auto;
        line-height: 25px;
        text-align: center;
        margin-top: 10px;
        box-shadow: 0 0 1px 1px black;
        cursor: pointer;

        &:hover {
            box-shadow: 0 0 1px 1px black,
                0 0 10px 1px black;
        }
    }
}
</style>