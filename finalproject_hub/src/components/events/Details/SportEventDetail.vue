<template>
  <div>
    <n-card class="event-card" bordered>
      <div class="event-detail">
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

        <n-split direction="horizontal" style="height: auto;" :max="0.75" :min="0.25">
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
    </n-card>
    <h2 class="map-title">Location Map</h2>
    <div id="map" class="google-map"></div>
  </div>
</template>


<script setup lang="ts">
import { ref, computed, onMounted  } from 'vue'
import { NProgress, NCard, NIcon, NSplit, NButton } from 'naive-ui'
import { LocationOutline as LocationIcon, CalendarOutline as CalendarIcon } from '@vicons/ionicons5'
import CustomModal from '../Dialog/CustomModal.vue'
import AddSportEventModalContent from '../Dialog/AddSportEventModalContent.vue'
import { useSportEventStore } from '../../../store/events/useSportEventStore'
import events from '../List/events.json'
import { useRoute } from 'vue-router'


const loadGoogleMapsScript = () => {
  return new Promise((resolve, reject) => {
    if ((window as any).google) {
      return resolve((window as any).google)
    }

    const script = document.createElement('script')
    script.src =
      'https://maps.googleapis.com/maps/api/js?key=YOUR_API_KEY&libraries=places'
    script.async = true
    script.onload = () => resolve((window as any).google)
    script.onerror = reject
    document.head.appendChild(script)
  })
}

const initMap = async () => {
  const google = await loadGoogleMapsScript()

  const map = new google.maps.Map(document.getElementById('map') as HTMLElement, {
    zoom: 13,
    center: { lat: 37.7866, lng: -122.4133 },
    mapId: 'DEMO_MAP_ID'
  })

  new google.maps.Marker({
    position: { lat: 37.7866, lng: -122.4133 },
    map,
    title: 'Event Location'
  })
}

onMounted(() => {
  initMap()
})


const route = useRoute()

const eventId = Number(route.params.id)
const event = ref(events.find(e => e.id === eventId))

const store = useSportEventStore()

const percentage = computed(() => Math.round((event.value.joined / event.value.total) * 100))
const formattedDate = computed(() => event.value.date.toLocaleString())

const showEditModal = ref(false)

const onEditClick = () => {
  showEditModal.value = true
}

const handleEditSubmit = (eventDetails: any) => {
  store.updateEvent(eventDetails)
  showEditModal.value = false
}
</script>

<style scoped>
  .event-detail {
      width: 800px;
      margin: auto;
      font-family: sans-serif;
  }

  .event-card {
      box-shadow: 0 4px 14px rgba(0, 0, 0, 0.1);
      border-radius: 12px;
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

  .google-map {
    width: 800px;
    height: 400px;
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

</style>
  