<template>
    <div class="homepage">
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
import { onMounted, ref } from 'vue'
import { storeToRefs } from 'pinia'
import { useSportEventStore } from '@/store/events/useSportEventStore'
import EventListTable from '@/components/events/List/EventListTable.vue'
import { NSpin } from 'naive-ui'

const sportEventStore = useSportEventStore()
const { events } = storeToRefs(sportEventStore)

const isLoading = ref(true)

onMounted(async () => {
    await sportEventStore.fetchAllEvents()
    isLoading.value = false
})
</script>

<style scoped>
.homepage {
    padding: 24px 32px;
    width: 100%;
    height: 100%;
    box-sizing: border-box;
}

.loader-wrapper {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 60vh;
}
</style>