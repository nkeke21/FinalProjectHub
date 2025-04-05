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

        <AddSportEventModalContent  @submit="showModal = false"/>
        </n-modal>
    </div>
</template>
  
<script lang="ts">
import { defineComponent, ref } from 'vue'
import { NModal, NButton } from 'naive-ui'
import AddSportEventModalContent from './AddSportEventModalContent.vue'

export default defineComponent({
    name: 'AddSportEventModal',
    components: {
        NModal,
        NButton,
        AddSportEventModalContent
    },
        setup() {
            const showModal = ref(false)

            return {
            showModal
            }
        }
    })
</script>
  
<style>
.n-dialog.n-modal {
    width: 650px
}

</style>
