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
import { useRoute } from 'vue-router'

const profileEventStore = useProfileEventStore()
const { hostedEvents, isLoading } = storeToRefs(profileEventStore)

const route = useRoute()
const userId = route.params.id as string

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
