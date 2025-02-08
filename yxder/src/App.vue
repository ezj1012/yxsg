<script setup lang="ts">
import EntityCoder from './views/EntityCoder.vue';

</script>

<template>
  <RouterView />
</template>

<style lang="less">
div[contenteditable] {
  border: none;
  outline: none;
  letter-spacing: 0px;
  font-family: Consolas, "Courier New", monospace;
  font-weight: normal;
  font-feature-settings: "liga" 0, "calt" 0;
  font-variation-settings: normal;
  cursor: text;
}

.line-layout {
  display: flex;
  flex-direction: column;
  position: relative;

  .line {
    height: 22px;
    line-height: 22px;
    display: flex;
    align-items: center;
    cursor: pointer;

    &:hover {
      .line-actions {
        opacity: 1;

        .op:not(.disabled) {
          color: #75beff;
        }
      }
    }

    .line-container {
      flex: 1;
      height: 20px;
      line-height: 20px;
    }

    .line-actions {
      height: 20px;
      line-height: 20px;
      display: flex;
      align-items: center;
      justify-content: center;
      column-gap: 5px;
      color: #bbb;
      opacity: 0;

      .line-actions-item {
        padding: 2px;
        border-radius: 3px;
        cursor: pointer;

        &:hover:not(.disabled) {
          background-color: rgba(90, 93, 94, 0.31);
        }

        &:active:not(.disabled) {
          background-color: rgba(90, 93, 94, 0.31);
          transform: scale(0.9);
        }
      }
    }
  }

  .line-sct {
    box-shadow: inset 0 0 0px 1px #007fd4;
    background-color: rgb(61, 61, 63, 0.5);
  }


  &::-webkit-scrollbar {
    width: 6px;
    height: 6px;
    // background-color: #fafafa;
    background-color: #252526;
  }

  &::-webkit-scrollbar-thumb {
    border-radius: 6px;
    background: #cccccc33;
  }
}
</style>
