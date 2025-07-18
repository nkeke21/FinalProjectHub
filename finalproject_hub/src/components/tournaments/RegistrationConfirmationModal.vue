<template>
  <n-modal :show="show" @update:show="updateShow" preset="card" style="width: 500px;">
    <div class="confirmation-content">
      <div class="icon-section">
        <n-icon :size="48" :color="iconColor">
          <CheckmarkCircleOutline v-if="type === 'success'" />
          <AlertCircleOutline v-else />
        </n-icon>
      </div>
      
      <div class="message-section">
        <h3 class="modal-title">{{ title }}</h3>
        <p class="modal-message">{{ message }}</p>
      </div>
      
      <div class="actions-section">
        <n-button 
          type="primary" 
          size="large"
          @click="handleConfirm"
          :loading="loading"
          color="#3b82f6"
        >
          {{ confirmText }}
        </n-button>
      </div>
    </div>
  </n-modal>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { NModal, NIcon, NButton } from 'naive-ui'
import { CheckmarkCircleOutline, AlertCircleOutline } from '@vicons/ionicons5'

interface Props {
  show: boolean
  type: 'success' | 'error'
  title: string
  message: string
  confirmText?: string
  loading?: boolean
}

interface Emits {
  (e: 'update:show', value: boolean): void
  (e: 'confirm'): void
}

const props = withDefaults(defineProps<Props>(), {
  confirmText: 'OK',
  loading: false
})

const emit = defineEmits<Emits>()

const iconColor = computed(() => {
  return props.type === 'success' ? '#10b981' : '#ef4444'
})

const updateShow = (value: boolean) => {
  emit('update:show', value)
}

const handleConfirm = () => {
  emit('confirm')
  emit('update:show', false)
}
</script>

<style scoped>
.confirmation-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  padding: 1rem;
}

.icon-section {
  margin-bottom: 1.5rem;
}

.message-section {
  margin-bottom: 2rem;
}

.modal-title {
  margin: 0 0 0.75rem 0;
  font-size: 1.25rem;
  font-weight: 600;
  color: #1e293b;
}

.modal-message {
  margin: 0;
  color: #64748b;
  font-size: 1rem;
  line-height: 1.5;
}

.actions-section {
  width: 100%;
  display: flex;
  justify-content: center;
}
</style> 