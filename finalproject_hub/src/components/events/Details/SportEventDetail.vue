<template>
  <div class="event-detail-page">
    <div class="event-header">
      <div class="event-hero">
        <div class="event-badge">
          <n-icon size="24" color="#065f46">
                          <FootballOutline v-if="event?.sportType === 'Football'" />
              <BasketballOutline v-else-if="event?.sportType === 'Basketball'" />
              <FitnessOutline v-else-if="event?.sportType === 'Running'" />
              <TennisballOutline v-else-if="event?.sportType === 'Tennis'" />
              <BicycleOutline v-else-if="event?.sportType === 'Cycling'" />
              <BasketballOutline v-else-if="event?.sportType === 'Volleyball'" />
              <WaterOutline v-else-if="event?.sportType === 'Swimming'" />
              <FootballOutline v-else />
          </n-icon>
          <span class="sport-type">{{ event?.sportType }}</span>
        </div>
        <h1 class="event-title">{{ event?.description }}</h1>
        <div class="event-meta">
          <div class="meta-item">
            <n-icon size="16" color="white">
              <LocationOutline />
            </n-icon>
            <span>{{ event?.location }}</span>
          </div>
          <div class="meta-item">
            <n-icon size="16" color="white">
              <CalendarOutline />
            </n-icon>
            <span>{{ formattedDate }}</span>
          </div>
        </div>
      </div>
    </div>

    <div class="event-content">
      <div class="content-grid">
        <div class="left-column">
          <n-card class="detail-card" title="Event Information">
            <div class="info-grid">
              <div class="info-item">
                <div class="info-label">Host</div>
                <div class="info-value">{{ event?.hostName }}</div>
              </div>
              <div class="info-item" v-if="event?.hostEmail">
                <div class="info-label">Host Email</div>
                <div class="info-value">
                  <a :href="`mailto:${event.hostEmail}`" class="contact-link">
                    {{ event.hostEmail }}
                  </a>
                </div>
              </div>
              <div class="info-item" v-if="event?.hostPhone">
                <div class="info-label">Host Phone</div>
                <div class="info-value">
                  <a :href="`tel:${event.hostPhone}`" class="contact-link">
                    {{ event.hostPhone }}
                  </a>
                </div>
              </div>
              <div class="info-item">
                <div class="info-label">Age Range</div>
                <div class="info-value">{{ event?.ageRange }} years</div>
              </div>
              <div class="info-item">
                <div class="info-label">Sport Type</div>
                <div class="info-value">{{ event?.sportType }}</div>
              </div>
              <div class="info-item">
                <div class="info-label">Capacity</div>
                <div class="info-value">{{ event?.numberOfParticipantsTotal }} participants</div>
              </div>
            </div>
            
            <div class="description-section">
              <h3>About this event</h3>
              <p>{{ event?.description }}</p>
            </div>
          </n-card>

          <!-- Map Section -->
          <n-card class="map-card" title="Location">
            <div id="map" class="google-map" />
          </n-card>
        </div>

        <div class="right-column">
          <n-card class="participants-card" title="Participants">
            <div class="participants-header">
              <div class="participants-count">
                <div class="count-circle">
                  <span class="count-number">{{ event?.numberOfParticipantsRegistered }}</span>
                  <span class="count-total">/{{ event?.numberOfParticipantsTotal }}</span>
                </div>
                <div class="count-label">Joined</div>
              </div>
              <div class="progress-bar">
                <div 
                  class="progress-fill" 
                  :style="{ width: `${percentage}%` }"
                ></div>
              </div>
            </div>

            <div class="participants-list">
              <div v-if="event?.participantsList && event.participantsList.length > 0" class="participants-grid">
                <div 
                  v-for="participant in event.participantsList" 
                  :key="participant.userId"
                  class="participant-item"
                >
                  <div class="participant-avatar">
                    {{ participant.name.charAt(0).toUpperCase() }}
                  </div>
                  <div class="participant-info">
                    <router-link 
                      :to="`/profile/${participant.userId}`" 
                      class="participant-name-link"
                    >
                      {{ participant.name }}
                    </router-link>
                    <div class="participant-email">{{ participant.email }}</div>
                    <div class="participant-age">{{ participant.age }} years old</div>
                  </div>
                  <div class="participant-actions" v-if="isHost && participant.userId !== event.hostId">
                    <n-button 
                      type="error" 
                      size="small" 
                      @click="onRemoveParticipant(participant.userId, participant.name)"
                      :loading="isUpdating"
                      class="remove-button"
                    >
                      <template #icon>
                        <n-icon><CloseCircleOutline /></n-icon>
                      </template>
                      Remove
                    </n-button>
                  </div>
                </div>
              </div>
              <div v-else class="no-participants">
                <n-icon size="48" color="#cbd5e1">
                  <PeopleOutline />
                </n-icon>
                <p>No participants yet</p>
                <span>Be the first to join!</span>
              </div>
            </div>

            <div class="action-section">
              <div v-if="isHost" class="host-actions">
                <n-button 
                  type="primary" 
                  color="#f97316" 
                  size="large" 
                  @click="onEditClick"
                  class="action-button"
                >
                  <template #icon>
                    <n-icon><PencilOutline /></n-icon>
                  </template>
                  Edit Event
                </n-button>
                
                <n-button 
                  type="primary" 
                  color="#3b82f6" 
                  size="large" 
                  @click="showInviteModal = true"
                  class="action-button"
                >
                  <template #icon>
                    <n-icon><PersonAddOutline /></n-icon>
                  </template>
                  Invite Friends
                </n-button>
                
                <n-button 
                  type="default" 
                  size="large" 
                  @click="refreshEventData"
                  class="action-button"
                  :loading="isUpdating"
                >
                  <template #icon>
                    <n-icon><RefreshOutline /></n-icon>
                  </template>
                  Refresh
                </n-button>
                
                <n-button 
                  type="error" 
                  size="large" 
                  @click="onDeleteClick"
                  class="action-button"
                  :loading="isUpdating"
                >
                  <template #icon>
                    <n-icon><TrashOutline /></n-icon>
                  </template>
                  Delete Event
                </n-button>
              </div>
              
              <n-button 
                v-else-if="isParticipating" 
                type="primary" 
                color="#3b82f6" 
                size="large" 
                disabled
                class="action-button"
              >
                <template #icon>
                  <n-icon><CheckmarkCircleOutline /></n-icon>
                </template>
                Already Joined
              </n-button>
              
              <n-button 
                v-else-if="isEventFull" 
                type="primary" 
                color="#ef4444" 
                size="large" 
                disabled
                class="action-button"
              >
                <template #icon>
                  <n-icon><CloseCircleOutline /></n-icon>
                </template>
                Event Full
              </n-button>
              
              <n-button 
                v-else 
                type="primary" 
                color="#22c55e" 
                size="large" 
                @click="onJoinClick"
                class="action-button"
                :loading="isUpdating"
              >
                <template #icon>
                  <n-icon><AddCircleOutline /></n-icon>
                </template>
                Join Event
              </n-button>
              
              <n-button 
                v-if="!isHost" 
                type="default" 
                size="large" 
                @click="refreshEventData"
                class="action-button"
                :loading="isUpdating"
              >
                <template #icon>
                  <n-icon><RefreshOutline /></n-icon>
                </template>
                Refresh
              </n-button>
            </div>
          </n-card>
        </div>
      </div>
    </div>

    <CustomModal :show="showEditModal" @close="showEditModal = false">
      <h2 style="margin-bottom: 1rem;">Edit Sport Event</h2>
      <AddSportEventModalContent
        :initial-data="event"
        submit-button-text="Edit Event"
        @submit="handleEditSubmit"
      />
    </CustomModal>

    <!-- Invite Friends Modal -->
    <InviteFriendsModal
      v-model:show="showInviteModal"
      :event-id="route.params.id as string"
      :event-age-range="event?.minAge && event?.maxAge ? { min: event.minAge, max: event.maxAge } : undefined"
      :current-participants="event?.participantsList || []"
      @invitations-sent="handleInvitationsSent"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { 
  NCard, 
  NIcon, 
  NButton, 
  useMessage, 
  NSpin 
} from 'naive-ui'
import { 
  LocationOutline, 
  CalendarOutline, 
  FootballOutline,
  BasketballOutline,
  TennisballOutline,
  PeopleOutline,
  PencilOutline,
  CheckmarkCircleOutline,
  CloseCircleOutline,
  AddCircleOutline,
  FitnessOutline,
  PersonAddOutline,
  RefreshOutline,
  TrashOutline,
  BicycleOutline,
  WaterOutline
} from '@vicons/ionicons5'
import { SportEvent } from '../../../models/SportEvent'
import { useSportEventStore } from '../../../store/events/useSportEventStore'
import { useRoute, useRouter } from 'vue-router'
import CustomModal from '../Dialog/CustomModal.vue'
import AddSportEventModalContent from '../Dialog/AddSportEventModalContent.vue'
import InviteFriendsModal from '../Dialog/InviteFriendsModal.vue'
import { config } from '../../../utils/config'

const route = useRoute();
const router = useRouter();
const store = useSportEventStore()
const message = useMessage()
const isUpdating = ref(false)
const showInviteModal = ref(false)

const event = computed(() => store.selectedEvent)

const loggedInUserId = computed(() => {
  const userStr = localStorage.getItem('user')
  if (!userStr) return null
  try {
    return JSON.parse(userStr).id
  } catch {
    return null
  }
})

const isHost = computed(() => {
  return event.value && loggedInUserId.value && event.value.hostId === loggedInUserId.value
})

const isParticipating = ref(false)

const isEventFull = computed(() => {
  return event.value && event.value.numberOfParticipantsRegistered >= event.value.numberOfParticipantsTotal
})

const percentage = computed(() =>
  event.value?.numberOfParticipantsTotal ? Math.round((event.value.numberOfParticipantsRegistered / event.value.numberOfParticipantsTotal) * 100) : 0
)

const formattedDate = computed(() =>
  event.value?.eventTime ? new Date(event.value.eventTime).toLocaleDateString('en-US', {
    weekday: 'long',
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  }) : ''
)

onMounted(async () => {
  isUpdating.value = true
  await store.fetchEventById(route.params.id as string)
  
  if (loggedInUserId.value && !isHost.value) {
    isParticipating.value = await store.checkParticipation(route.params.id as string)
  }
  
  isUpdating.value = false
  
  setTimeout(() => {
    initMap()
  }, 100)
})

// Function to refresh event data
const refreshEventData = async () => {
  isUpdating.value = true
  try {
    await store.fetchEventById(route.params.id as string)
    if (loggedInUserId.value && !isHost.value) {
      isParticipating.value = await store.checkParticipation(route.params.id as string)
    }
  } catch (error) {
    console.error('Failed to refresh event data:', error)
  } finally {
    isUpdating.value = false
  }
}

const loadGoogleMapsScript = () => {
  return new Promise((resolve, reject) => {
    if ((window as any).google) {
      return resolve((window as any).google)
    }

    const script = document.createElement('script')
    script.src = `https://maps.googleapis.com/maps/api/js?key=${config.googleMapsApiKey}&libraries=places`
    script.async = true
    script.onload = () => resolve((window as any).google)
    script.onerror = reject
    document.head.appendChild(script)
  })
}

const initMap = async () => {
  try {
    const google = await loadGoogleMapsScript()

    const location = event.value
      ? { lat: event.value.latitude, lng: event.value.longitude }
      : { lat: 37.7866, lng: -122.4133 }

    const map = new google.maps.Map(document.getElementById('map') as HTMLElement, {
      zoom: 15,
      center: location,
      mapId: 'DEMO_MAP_ID',
      styles: [
        {
          featureType: 'poi',
          elementType: 'labels',
          stylers: [{ visibility: 'off' }]
        }
      ]
    })

    new google.maps.Marker({
      position: location,
      map,
      title: 'Event Location',
      icon: {
        url: 'data:image/svg+xml;charset=UTF-8,' + encodeURIComponent(`
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M12 2C8.13 2 5 5.13 5 9c0 5.25 7 13 7 13s7-7.75 7-13c0-3.87-3.13-7-7-7zm0 9.5c-1.38 0-2.5-1.12-2.5-2.5s1.12-2.5 2.5-2.5 2.5 1.12 2.5 2.5-1.12 2.5-2.5 2.5z" fill="#22c55e"/>
          </svg>
        `),
        scaledSize: new google.maps.Size(24, 24)
      }
    })
  } catch (error) {
    console.error('Failed to initialize map:', error)
  }
}

const showEditModal = ref(false)

const onEditClick = () => {
  showEditModal.value = true
}

const handleEditSubmit = async (eventDetails: any) => {
  isUpdating.value = true
  try {
    // Convert form data to SportEvent format
    const sportEvent = {
      eventId: event.value?.eventId || null,
      hostId: event.value?.hostId || null,
      hostName: event.value?.hostName || null,
      hostEmail: event.value?.hostEmail || null,
      hostPhone: event.value?.hostPhone || null,
      latitude: eventDetails.locationLat || eventDetails.latitude, // Handle both formats
      longitude: eventDetails.locationLng || eventDetails.longitude, // Handle both formats
      location: eventDetails.location,
      participants: eventDetails.participants,
      numberOfParticipantsTotal: eventDetails.participants,
      numberOfParticipantsRegistered: event.value?.numberOfParticipantsRegistered || 0,
      minAge: eventDetails.minAge,
      maxAge: eventDetails.maxAge,
      eventTime: eventDetails.eventTime,
      sportType: eventDetails.sportType,
      description: eventDetails.description,
      participantsList: event.value?.participantsList || []
    }

    await store.updateEvent(route.params.id as string, sportEvent)
    message.success('Event updated successfully!')
    showEditModal.value = false
    await store.fetchEventById(route.params.id as string)
  } catch (err) {
    message.error('Failed to update the event. Please try again.')
  } finally {
    isUpdating.value = false
  }
}

const handleInvitationsSent = () => {
  message.success('Invitations sent successfully!')
  // Optionally refresh event data or update UI
}

// Debug function to test event invitations
const debugEventInvitations = async () => {
  try {
    const { EventInvitationService } = await import('../../../services/apis/EventInvitationService')
    const invitations = await EventInvitationService.getPendingInvitations()
    console.log('Debug - Pending invitations:', invitations)
    message.info(`Found ${invitations.length} pending invitations`)
  } catch (error) {
    console.error('Debug - Error loading invitations:', error)
    message.error('Failed to load invitations')
  }
}

const onJoinClick = async () => {
  try {
    isUpdating.value = true
    await store.joinEvent(route.params.id as string)
    isParticipating.value = true
    message.success('Successfully joined the event!')
  } catch (err: any) {
    message.error(err.message || 'Failed to join the event. Please try again.')
  } finally {
    isUpdating.value = false
  }
}

const onRemoveParticipant = async (participantId: string, participantName: string) => {
  try {
    isUpdating.value = true
    await store.removeParticipant(route.params.id as string, participantId)
    message.success(`Successfully removed ${participantName} from the event!`)
  } catch (err: any) {
    message.error(err.message || 'Failed to remove participant. Please try again.')
  } finally {
    isUpdating.value = false
  }
}

const onDeleteClick = async () => {
  try {
    isUpdating.value = true
    await store.deleteEvent(route.params.id as string)
    message.success('Event deleted successfully!')
    // Navigate to user's hosted events page
    router.push('/profile')
  } catch (err: any) {
    message.error(err.message || 'Failed to delete the event. Please try again.')
  } finally {
    isUpdating.value = false
  }
}
</script>

<style scoped>
.event-detail-page {
  width: 100%;
  min-height: 100vh;
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
}

.event-header {
  background: linear-gradient(135deg, #22c55e 0%, #16a34a 100%);
  color: white;
  padding: 3rem 2rem;
  text-align: center;
}

.event-hero {
  max-width: 1200px;
  margin: 0 auto;
}

.event-badge {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  background: rgba(255, 255, 255, 0.2);
  padding: 0.5rem 1rem;
  border-radius: 2rem;
  margin-bottom: 1rem;
  backdrop-filter: blur(10px);
}

.sport-type {
  font-weight: 600;
  font-size: 0.875rem;
}

.event-title {
  font-size: 2.5rem;
  font-weight: 700;
  margin-bottom: 1rem;
  line-height: 1.2;
}

.event-meta {
  display: flex;
  justify-content: center;
  gap: 2rem;
  flex-wrap: wrap;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 1rem;
}

.event-content {
  max-width: 80%;
  margin: 0 auto;
  padding: 2rem;
}

.content-grid {
  display: grid;
  grid-template-columns: 1fr 350px;
  gap: 2rem;
  align-items: start;
}

.left-column {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.right-column {
  position: sticky;
  top: 2rem;
}

.detail-card, .participants-card, .map-card {
  border-radius: 1rem;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
  border: none;
  width: 100%;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.info-label {
  font-size: 0.875rem;
  color: #64748b;
  font-weight: 500;
}

.info-value {
  font-size: 1rem;
  font-weight: 600;
  color: #1e293b;
}

.contact-link {
  color: #3b82f6;
  text-decoration: none;
  transition: color 0.2s ease;
}

.contact-link:hover {
  color: #1d4ed8;
  text-decoration: underline;
}

.description-section h3 {
  font-size: 1.125rem;
  font-weight: 600;
  margin-bottom: 0.75rem;
  color: #1e293b;
}

.description-section p {
  color: #475569;
  line-height: 1.6;
}

.participants-header {
  margin-bottom: 2rem;
}

.participants-count {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-bottom: 1rem;
}

.count-circle {
  display: flex;
  align-items: baseline;
  gap: 0.25rem;
}

.count-number {
  font-size: 2rem;
  font-weight: 700;
  color: #22c55e;
}

.count-total {
  font-size: 1.25rem;
  color: #64748b;
}

.count-label {
  font-size: 0.875rem;
  color: #64748b;
  font-weight: 500;
}

.progress-bar {
  width: 100%;
  height: 8px;
  background: #e2e8f0;
  border-radius: 4px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #22c55e 0%, #16a34a 100%);
  transition: width 0.3s ease;
}

.participants-list {
  margin-bottom: 2rem;
}

.participants-grid {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.participant-item {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem;
  background: #f8fafc;
  border-radius: 0.75rem;
  border: 1px solid #e2e8f0;
}

.participant-actions {
  margin-left: auto;
}

.remove-button {
  transition: all 0.2s ease;
}

.remove-button:hover {
  transform: translateY(-1px);
}

.participant-avatar {
  width: 3rem;
  height: 3rem;
  background: linear-gradient(135deg, #22c55e 0%, #16a34a 100%);
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 1.125rem;
}

.participant-info {
  flex: 1;
}

.participant-name-link {
  font-weight: 600;
  color: #3b82f6;
  margin-bottom: 0.25rem;
  text-decoration: none;
  transition: all 0.2s ease;
  display: inline-block;
}

.participant-name-link:hover {
  color: #1d4ed8;
  text-decoration: underline;
  transform: translateY(-1px);
}

.participant-email {
  font-size: 0.875rem;
  color: #64748b;
  margin-bottom: 0.25rem;
}

.participant-age {
  font-size: 0.875rem;
  color: #64748b;
}

.no-participants {
  text-align: center;
  padding: 3rem 1rem;
  color: #64748b;
}

.no-participants p {
  font-size: 1.125rem;
  font-weight: 600;
  margin: 1rem 0 0.5rem;
}

.no-participants span {
  font-size: 0.875rem;
}

.action-section {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.host-actions {
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
  justify-content: center;
}

.action-button {
  width: 100%;
  height: 3rem;
  font-weight: 600;
  border-radius: 0.75rem;
}

.google-map {
  width: 100%;
  height: 350px;
  border-radius: 0.75rem;
  overflow: hidden;
}

@media (max-width: 1200px) {
  .content-grid {
    grid-template-columns: 1fr;
  }
  
  .right-column {
    position: static;
  }
}

@media (max-width: 768px) {
  .event-header {
    padding: 2rem 1rem;
  }
  
  .event-title {
    font-size: 2rem;
  }
  
  .event-meta {
    flex-direction: column;
    gap: 1rem;
  }
  
  .event-content {
    padding: 1rem;
  }
  
  .info-grid {
    grid-template-columns: 1fr;
  }
}
</style>
  