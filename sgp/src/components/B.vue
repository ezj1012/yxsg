<script setup lang="ts">

import { useRedBtn } from '@/app/sgComp';
import { useMouse } from '@/app/utils/mouse';
const { x, y } = useMouse()


</script>

<template>
    <div>
        B {{ x }} {{ y }}

    </div>
</template>

<style scoped></style>
