<template>
  <n-modal :show="show" @update:show="updateShow" preset="card" title="Quick Registration" style="width: 500px;">
    <div class="quick-registration-content">
      <div class="tournament-info">
        <h3>{{ tournament?.name }}</h3>
        <p class="tournament-details">
          <n-icon size="16" color="#64748b">
            <CalendarOutline />
          </n-icon>
          {{ formatDateRange(tournament?.startDate, tournament?.endDate) }}
        </p>
        <p class="tournament-details">
          <n-icon size="16" color="#64748b">
            <LocationOutline />
          </n-icon>
          {{ tournament?.location }}
        </p>
      </div>
      
      <div class="registration-form">
        <p class="registration-note">
          You're about to register for this tournament. Click confirm to proceed.
        </p>
      </div>
      
      <div class="modal-actions">
        <n-button 
          @click="updateShow(false)" 
          size="large"
        >
          Cancel
        </n-button>
        <n-button 
          type="primary" 
          size="large"
          @click="handleRegister"
          :loading="registering"
          color="#3b82f6"
        >
          Register for Tournament
        </n-button>
      </div>
    </div>
  </n-modal>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { NModal, NIcon, NButton } from 'naive-ui'
import { CalendarOutline, LocationOutline } from '@vicons/ionicons5'
import type { Tournament } from '@/models/Tournament'
import { TournamentRegistrationService } from '@/services/apis/TournamentRegistrationService'

interface Props {
  show: boolean
  tournament: Tournament | null
}

interface Emits {
  (e: 'update:show', value: boolean): void
  (e: 'registration-success'): void
  (e: 'registration-error', message: string): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

const registering = ref(false)

const updateShow = (value: boolean) => {
  emit('update:show', value)
}

const handleRegister = async () => {
  if (!props.tournament) return
  
  try {
    registering.value = true
    
    const response = await TournamentRegistrationService.registerForTournament({
      tournamentId: props.tournament.id
    })
    
    if (response.success) {
      emit('registration-success')
      updateShow(false)
    } else {
      emit('registration-error', response.message)
    }
  } catch (error) {
    emit('registration-error', 'Failed to register for tournament')
  } finally {
    registering.value = false
  }
}

const formatDateRange = (startDate?: string, endDate?: string) => {
  if (!startDate || !endDate) return ''
  const start = new Date(startDate).toLocaleDateString()
  const end = new Date(endDate).toLocaleDateString()
  return `${start} - ${end}`
}
</script>

<style scoped>
.quick-registration-content {
  padding: 1rem;
}

.tournament-info {
  margin-bottom: 2rem;
  padding: 1rem;
  background: #f8fafc;
  border-radius: 8px;
}

.tournament-info h3 {
  margin: 0 0 1rem 0;
  color: #1e293b;
  font-size: 1.25rem;
  font-weight: 600;
}

.tournament-details {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin: 0.5rem 0;
  color: #64748b;
  font-size: 0.875rem;
}

.registration-form {
  margin-bottom: 2rem;
}

.registration-note {
  color: #64748b;
  font-size: 0.875rem;
  line-height: 1.5;
  margin: 0;
}

.modal-actions {
  display: flex;
  gap: 1rem;
  justify-content: flex-end;
}
</style> 