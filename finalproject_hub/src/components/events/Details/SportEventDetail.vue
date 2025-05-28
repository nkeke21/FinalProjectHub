<template>
  <div class="event-page">
    <n-card class="event-card" bordered>
        <n-spin class="event-content" :show="isUpdating" size="large">

          <div v-if="event" class="event-detail">
            <h1>{{ event.title }}</h1>
            <div class="event-meta">
              <div class="meta-item">
                <n-icon :component="LocationIcon" />
                <span>{{ event.location }}</span>
              </div>
              <div class="meta-item">
                <n-icon :component="CalendarIcon" />
                <span>{{ formattedDate }}</span>
              </div>
          </div>

          <n-split direction="horizontal" style="height: auto; height: 80%;" :max="0.75" :min="0.25">
            <template #1>
              <div class="right-pane">
                <n-card title="Event Details" :bordered="false">
                  <p><strong>Sport:</strong> {{ event.sportType }}</p>
                  <p><strong>Age Range:</strong> {{ event.ageRange }}</p>
                  <p><strong>Host:</strong> {{ event.host }}</p>
                </n-card>
                <n-card title="Description" style="margin-top: 16px;" :bordered="false">
                  <p>{{ event.description }}</p>
                </n-card>
              </div>
            </template>

            <template #2>
              <div class="left-pane">
                <div class="progress">
                  <n-progress type="circle" color="green" :percentage="percentage">
                    <template #default>
                      <div class="progress-text">{{ event.joined }}/{{ event.total }}</div>
                    </template>
                  </n-progress>
                  <div class="progress-label">Participants</div>
                </div>

                <n-card title="Participants" :bordered="false" style="margin-top: 24px;">
                  <ul>
                    <li v-for="(participant, index) in event.participantsList" :key="index">
                      {{ participant.name }} ({{ participant.age }} y/o)
                    </li>
                  </ul>

                  <div style="text-align: right;">
                    <n-button type="primary" color="orange" size="medium" @click="onEditClick">
                      Edit
                    </n-button>
                  </div>
                </n-card>
              </div>
            </template>
          </n-split>

          <!-- Modal for editing -->
          <CustomModal :show="showEditModal" @close="showEditModal = false">
            <h2 style="margin-bottom: 1rem;">Edit Sport Event</h2>
            <AddSportEventModalContent
              :initial-data="event"
              submit-button-text="Edit Event"
              @submit="handleEditSubmit"
            />
          </CustomModal>
        </div>
        <div v-else style="text-align: center; padding: 40px;">
          <n-spin size="large" />
        </div>
      </n-spin>

    </n-card>

    <h2 class="map-title">Location Map</h2>
    <div id="map" class="google-map" />
  </div>
</template>


<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { NProgress, NCard, NIcon, NSplit, NButton, useMessage, NSpin } from 'naive-ui'
import { LocationOutline as LocationIcon, CalendarOutline as CalendarIcon } from '@vicons/ionicons5'
import { SportEvent } from '../../../models/SportEvent'
import { useSportEventStore } from '../../../store/events/useSportEventStore'
import { useRoute } from 'vue-router'
import CustomModal from '../Dialog/CustomModal.vue'
import AddSportEventModalContent from '../Dialog/AddSportEventModalContent.vue'

const route = useRoute();
const store = useSportEventStore()
const message = useMessage()
const isUpdating = ref(false)

const event = computed(() => store.selectedEvent)

onMounted(async () => {
  isUpdating.value = true
  await store.fetchEventById(route.params.id as string)
  isUpdating.value = false
})

watch(isUpdating, async (newVal) => {
  if (!newVal) {
    await initMap()
  }
})

const loadGoogleMapsScript = () => {
  return new Promise((resolve, reject) => {
    if ((window as any).google) {
      return resolve((window as any).google)
    }

    const script = document.createElement('script')
    script.src =
      'https://maps.googleapis.com/maps/api/js?key=&libraries=places'
    script.async = true
    script.onload = () => resolve((window as any).google)
    script.onerror = reject
    document.head.appendChild(script)
  })
}

const initMap = async () => {
  const google = await loadGoogleMapsScript()

  const location = event.value
    ? { lat: event.value.latitude, lng: event.value.longitude }
    : { lat: 37.7866, lng: -122.4133 }

  const map = new google.maps.Map(document.getElementById('map') as HTMLElement, {
    zoom: 13,
    center: location,
    mapId: 'DEMO_MAP_ID'
  })

  new google.maps.Marker({
    position: location,
    map,
    title: 'Event Location'
  })
}

const percentage = computed(() =>
  event.value?.total ? Math.round((event.value.joined / event.value.total) * 100) : 0
)

const formattedDate = computed(() =>
  event.value?.date ? event.value.date.toLocaleString() : ''
)

const showEditModal = ref(false)

const onEditClick = () => {
  showEditModal.value = true
}

const handleEditSubmit = async (eventDetails: SportEvent) => {
  isUpdating.value = true
  try {
    await store.updateEvent(route.params.id as string, eventDetails)
    message.success('Event updated successfully!')
    showEditModal.value = false

    await store.fetchEventById(route.params.id as string)
  } catch (err) {
    message.error('Failed to update the event. Please try again.')
  } finally {
    isUpdating.value = false
  }
}
</script>

<style>
.event-page {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 40px 16px;
}

.event-card {
  width: 65%;
  height: 60%;
  margin: 0 auto 32px;
  box-shadow: 0 4px 14px rgba(0, 0, 0, 0.1);
  border-radius: 12px;
}

.event-content {
  height: 100%;
}

.event-detail {
  width: 100%;
  height: 100%;
  font-family: sans-serif;
}

.event-meta {
    display: flex;
    gap: 16px;
    margin: 8px 0 24px;
}

.meta-item {
    display: flex;
    align-items: center;
    gap: 8px;
    color: #555;
}

.left-pane, .right-pane {
    padding: 16px;
}

.progress {
    height: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
}

.progress-text {
    font-size: 16px;
    font-weight: bold;
}

.progress-label {
    margin-top: 8px;
    font-size: 14px;
    color: #666;
}

.map-wrapper {
  position: relative;
  width: 65%;
  height: 40%;
  margin: 2rem auto 0;
}

.google-map {
  width: 100%;
  height: 100%;
  border-radius: 12px;
  box-shadow: 0 4px 14px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.google-map {
  width: 65%;
  height: 40%;
  margin: 2rem auto 0;
  border-radius: 12px;
  box-shadow: 0 4px 14px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.map-title {
  max-width: 800px;
  margin: 2rem auto 0;
  font-size: 20px;
  font-weight: 600;
  text-align: center;
  color: #333;
}

.n-spin-content {
  height: 100%;
}
</style>
  