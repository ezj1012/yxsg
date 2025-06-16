<script setup lang="ts">

import { useMouse } from '@/app/utils/mouse';
const { x, y } = useMouse()
</script>

<template>
    <div>
        C {{ x }} {{ y }}
    </div>
</template>

<style scoped></style>
