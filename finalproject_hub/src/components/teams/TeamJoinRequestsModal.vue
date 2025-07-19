<template>
  <n-modal :show="show" preset="card" title="Team Join Requests" style="width: 600px" @update:show="updateShow">
    <div class="join-requests-modal">
      <div v-if="loading" class="loading-state">
        <p>Loading requests...</p>
      </div>

      <div v-else-if="requests.length === 0" class="empty-state">
        <n-icon size="48" color="#cbd5e1">
          <CheckmarkCircleOutline />
        </n-icon>
        <p>No pending join requests</p>
      </div>

      <div v-else class="requests-list">
        <div v-for="request in requests" :key="request.requestId" class="request-item">
          <div class="request-info">
            <div class="user-info">
              <n-icon size="20" color="#3b82f6">
                <PersonOutline />
              </n-icon>
              <span class="user-name">{{ getUserName(request.fromUserId) }}</span>
            </div>
            <div class="request-date">
              {{ formatDate(request.sentAt) }}
            </div>
          </div>
          
          <div class="request-actions">
            <n-button 
              type="primary" 
              color="green" 
              size="small"
              :loading="processingRequest === request.requestId"
              @click="approveRequest(request.requestId)"
            >
              Approve
            </n-button>
            <n-button 
              type="error" 
              size="small"
              :loading="processingRequest === request.requestId"
              @click="rejectRequest(request.requestId)"
            >
              Reject
            </n-button>
          </div>
        </div>
      </div>
    </div>
  </n-modal>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { NModal, NButton, NIcon, useMessage } from 'naive-ui'
import { PersonOutline, CheckmarkCircleOutline } from '@vicons/ionicons5'
import { TeamJoinRequestService } from '@/services/apis/TeamJoinRequestService'
import type { TeamJoinRequest } from '@/services/apis/TeamJoinRequestService'

interface Props {
  show: boolean
  teamId: string
}

interface Emits {
  (e: 'update:show', value: boolean): void
  (e: 'request-processed'): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

const message = useMessage()

const updateShow = (value: boolean) => {
  emit('update:show', value)
}
const requests = ref<TeamJoinRequest[]>([])
const loading = ref(false)
const processingRequest = ref<string | null>(null)

const getUserName = (userId: string) => {
  return `User ${userId.slice(0, 8)}`
}

const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleDateString('en-US', { 
    year: 'numeric', 
    month: 'short', 
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const loadRequests = async () => {
  if (!props.teamId) return
  
  try {
    loading.value = true
    requests.value = await TeamJoinRequestService.getPendingRequestsForTeam(props.teamId)
  } catch (error) {
    console.error('Failed to load join requests:', error)
    message.error('Failed to load join requests')
  } finally {
    loading.value = false
  }
}

const approveRequest = async (requestId: string) => {
  try {
    processingRequest.value = requestId
    await TeamJoinRequestService.respondToRequest(requestId, 'APPROVED')
    message.success('Request approved successfully!')
    await loadRequests()
    emit('request-processed')
  } catch (error) {
    console.error('Failed to approve request:', error)
    message.error('Failed to approve request')
  } finally {
    processingRequest.value = null
  }
}

const rejectRequest = async (requestId: string) => {
  try {
    processingRequest.value = requestId
    await TeamJoinRequestService.respondToRequest(requestId, 'REJECTED')
    message.success('Request rejected successfully!')
    await loadRequests()
    emit('request-processed')
  } catch (error) {
    console.error('Failed to reject request:', error)
    message.error('Failed to reject request')
  } finally {
    processingRequest.value = null
  }
}

watch(() => props.show, (newShow) => {
  if (newShow) {
    loadRequests()
  }
})
</script>

<style scoped>
.join-requests-modal {
  min-height: 300px;
}

.loading-state {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 200px;
  color: #64748b;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 200px;
  color: #64748b;
  gap: 1rem;
}

.requests-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.request-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  background: #f8fafc;
}

.request-info {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.user-name {
  font-weight: 600;
  color: #1e293b;
}

.request-date {
  font-size: 0.875rem;
  color: #64748b;
}

.request-actions {
  display: flex;
  gap: 0.5rem;
}
</style> 