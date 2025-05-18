<template>
    <div>
        <div class="loader-wrapper" v-if="isLoading">
            <n-spin size="large" />
        </div>

        <EventListTable
            v-else
            :events="events"
            :show-add-event="true"
        />
    </div>
</template>
  
<script setup lang="ts">
import { ref, onMounted } from 'vue'
import EventListTable from '@/components/events/List/EventListTable.vue'
import { NSpin } from 'naive-ui'

const events = ref<any[]>([])
const isLoading = ref(true)

onMounted(async () => {
    await new Promise(resolve => setTimeout(resolve, 1500)) 
    const json = await import('./events.json')
    events.value = json.default
    isLoading.value = false
})
</script>
  
<style scoped>
.loader-wrapper {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 60vh;
}
</style>
