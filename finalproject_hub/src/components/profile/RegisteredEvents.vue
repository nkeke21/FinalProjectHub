<template>
    <div class="loader-wrapper" v-if="isLoading">
        <n-spin size="large" />
    </div>

    <EventListTable
        v-else
        :events="registeredEvents"
        :show-add-event="false"
    />
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { NSpin } from 'naive-ui'
import EventListTable from '@/components/events/List/EventListTable.vue'
import { useProfileEventStore } from '@/store/profile/profileEventStore'
import { storeToRefs } from 'pinia'

const profileEventStore = useProfileEventStore()
const { registeredEvents, isLoading, error } = storeToRefs(profileEventStore)

const userId = '13fa5e4e-1d9e-4a2a-9a20-7385f24e9097'

onMounted(() => {
  profileEventStore.fetchRegisteredEvents(userId)
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