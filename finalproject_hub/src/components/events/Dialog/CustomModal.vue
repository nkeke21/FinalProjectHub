<template>
    <Teleport to="body">
      <div v-if="show" class="modal-overlay" @click.self="close">
        <div class="modal-container">
          <slot />
        </div>
      </div>
    </Teleport>
  </template>
  
<script setup lang="ts">
  defineProps<{ show: boolean }>()
  const emit = defineEmits(['close'])
  
  const close = () => emit('close')
</script>
  
<style scoped>
  .modal-overlay {
    position: fixed;
    inset: 0;
    background: rgba(0, 0, 0, 0.3);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
  }
  
  .modal-container {
    background: white;
    padding: 24px;
    width: 650px;
    max-height: 90vh;
    overflow-y: auto;
    border-radius: 12px;
    box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
  }
  </style>
  