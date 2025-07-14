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
import { onMounted, computed } from 'vue'
import { NSpin } from 'naive-ui'
import EventListTable from '@/components/events/List/EventListTable.vue'
import { useProfileEventStore } from '@/store/profile/profileEventStore'
import { storeToRefs } from 'pinia'
import { useRoute } from 'vue-router'

const profileEventStore = useProfileEventStore()
const { registeredEvents, isLoading, error } = storeToRefs(profileEventStore)

const route = useRoute()

const isOwnProfile = computed(() => {
    return !route.params.id
})

const getUserId = () => {
    return route.params.id as string
}

const userId = getUserId()

onMounted(() => {
  if (isOwnProfile.value) {
    profileEventStore.fetchCurrentUserRegisteredEvents()
  } else if (userId) {
    profileEventStore.fetchRegisteredEvents(userId)
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