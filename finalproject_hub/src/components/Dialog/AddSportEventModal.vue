<template>
    <div>
      <n-button color="orange" @click="showModal = true">Start me up</n-button>
  
      <CustomModal :show="showModal" @close="showModal = false">
        <h2 style="margin-bottom: 1rem;">Add Sport Event</h2>
        <AddSportEventModalContent @submit="handleAddEvent" />
      </CustomModal>
    </div>
</template>
  
<script setup lang="ts">
import { ref } from 'vue'
import CustomModal from './CustomModal.vue'
import { NModal, NButton } from 'naive-ui'

import AddSportEventModalContent from './AddSportEventModalContent.vue'
import { useSportEventStore } from '../../store/events/useSportEventStore'

const showModal = ref(false)
const store = useSportEventStore()

const handleAddEvent = (eventDetails: any) => {
    store.addEvent(eventDetails)
    showModal.value = false
}
</script>

  
<style>
.vfm--fixed.vfm--inset.vfm-center {
  display: flex !important;
  justify-content: center !important;
  align-items: center !important;
  padding: 20px; /* for some space on mobile */
  z-index: 1000 !important;
}

.custom-modal {
  width: 650px;
  background: white;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
  max-height: 90vh;
  overflow-y: auto;
}


  
.close-btn {
background-color: orange;
color: white;
padding: 8px 16px;
border: none;
border-radius: 6px;
cursor: pointer;
}
</style>
  