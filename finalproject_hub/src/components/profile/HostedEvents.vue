<template>
    <div>
        <div class="loader-wrapper" v-if="isLoading">
            <n-spin size="large" />
        </div>

        <EventListTable
            v-else
            :events="hostedEvents"
            :show-add-event="true"
        />
    </div>
</template>
  
<script setup lang="ts">
import { onMounted } from 'vue'
import { storeToRefs } from 'pinia'
import { useProfileEventStore } from '@/store/profile/profileEventStore'
import EventListTable from '@/components/events/List/EventListTable.vue'
import { NSpin } from 'naive-ui'

const profileEventStore = useProfileEventStore()
const { hostedEvents, isLoading } = storeToRefs(profileEventStore)

const userId = '13fa5e4e-1d9e-4a2a-9a20-7385f24e9097'

onMounted(() => {
    profileEventStore.fetchHostedEvents(userId)
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
