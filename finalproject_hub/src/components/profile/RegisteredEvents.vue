<template>
    <div class="loader-wrapper" v-if="isLoading">
        <n-spin size="large" />
    </div>

    <EventListTable
        v-else
        :events="events"
        :show-add-event="false"
    />
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { NSpin } from 'naive-ui'
import EventListTable from '@/components/events/List/EventListTable.vue'

const events = ref<any[]>([])
const isLoading = ref(true)

onMounted(async () => {
    isLoading.value = true
    try {
        await new Promise(resolve => setTimeout(resolve, 1500))
        const json = await import('./events.json')
        events.value = json.default
    } finally {
        isLoading.value = false
    }
})
</script>

<style scoped>
.loader-wrapper {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 70vh;
}
</style>