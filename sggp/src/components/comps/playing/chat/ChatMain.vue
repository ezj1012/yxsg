<script lang="ts" setup>
import type { SanGuo } from '@/app/sg';
import { ref, shallowRef, onUpdated, inject } from 'vue';

const { sg } = inject('sg') as { sg: SanGuo }

</script>
<template>
    <div class="chat-main" v-msg="'input'">
        hello
    </div>
</template>
<style lang="less">
.chat-main {
    width: 100%;
    height: 100%;
}
</style>