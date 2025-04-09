<template>
    <div>
        <n-button 
        @click="showModal = true"
        color="orange">
        Start me up
        </n-button>
    
        <n-modal
        v-model:show="showModal"
        :show-icon="false"
        :mask-closable="true"
        preset="dialog"
        title="Add Sport Event"
        >

        <AddSportEventModalContent  @submit="handleAddEvent"/>
        </n-modal>
    </div>
</template>
  
<script setup lang="ts">
import { defineComponent, ref } from 'vue'
import { NModal, NButton } from 'naive-ui'
import { useSportEventStore } from '../../../store/events/useSportEventStore'
import AddSportEventModalContent from './AddSportEventModalContent.vue'

const showModal = ref(false)
const store = useSportEventStore()

const handleAddEvent = (eventDetails: any) => {
  store.addEvent(eventDetails)
  showModal.value = false
}
</script>
  
<style>
.n-dialog.n-modal {
    width: 650px
}

</style>
