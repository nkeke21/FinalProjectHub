<template>
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
</template>


<script setup lang="ts">
import { ref, computed } from 'vue'
import { NProgress, NCard, NIcon, NSplit, NButton } from 'naive-ui'
import { LocationOutline as LocationIcon, CalendarOutline as CalendarIcon } from '@vicons/ionicons5'
import CustomModal from '../Dialog/CustomModal.vue'
import AddSportEventModalContent from '../Dialog/AddSportEventModalContent.vue'
import { useSportEventStore } from '../../../store/events/useSportEventStore'

const store = useSportEventStore()

const event = ref({
  id: 1, // assuming each event has a unique id
  title: 'Sunday Basketball Match',
  location: 'Didi Dighomi',
  date: new Date('2025-04-30T15:12:08'),
  joined: 5,
  total: 10,
  sportType: 'Basketball',
  ageRange: '27-39',
  host: 'John Doe',
  description: 'Join us for a fun basketball match. Bring your own water and gear. Beginners are welcome!',
  participantsList: [
    { name: 'Luka', age: 28 },
    { name: 'Ana', age: 30 },
    { name: 'Nika', age: 27 },
    { name: 'Giorgi', age: 31 },
    { name: 'Sopo', age: 26 }
  ]
})

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
        padding: 16px;
        max-width: 1000px;
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
</style>
  