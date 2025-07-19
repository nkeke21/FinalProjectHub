<template>
  <n-modal :show="show" preset="card" title="Create New Team" style="width: 600px" @update:show="updateShow">
    <n-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-placement="left"
      label-width="auto"
      require-mark-placement="right-hanging"
    >
      <n-form-item label="Team Name" path="name">
        <n-input v-model:value="form.name" placeholder="Enter team name" />
      </n-form-item>
      
      <n-form-item label="Description" path="description">
        <n-input
          v-model:value="form.description"
          type="textarea"
          placeholder="Describe your team"
          :rows="3"
        />
      </n-form-item>
      
      <n-form-item label="Sport Type" path="sportType">
        <n-select
          v-model:value="form.sportType"
          :options="sportTypeOptions"
          placeholder="Select sport type"
        />
      </n-form-item>
      
      <n-form-item label="Max Members" path="maxMembers">
        <n-input-number
          v-model:value="form.maxMembers"
          :min="2"
          :max="50"
          placeholder="Maximum team size"
        />
      </n-form-item>
      
      <n-form-item label="Age Range" path="ageRange">
        <div class="age-range-inputs">
          <n-input-number
            v-model:value="form.ageRange.min"
            :min="8"
            :max="80"
            placeholder="Min age"
          />
          <span class="age-separator">to</span>
          <n-input-number
            v-model:value="form.ageRange.max"
            :min="8"
            :max="80"
            placeholder="Max age"
          />
          <span class="age-unit">years</span>
        </div>
      </n-form-item>
      
      <n-form-item label="Team Privacy" path="public">
        <n-switch v-model:value="form.public" />
        <span class="privacy-label">{{ form.public ? 'Public' : 'Private' }}</span>
      </n-form-item>
    </n-form>
    
    <template #footer>
      <div class="modal-footer">
        <n-button @click="close">Cancel</n-button>
        <n-button 
          type="primary" 
          @click="createTeam" 
          :loading="loading"
          class="create-team-btn"
        >
          Create Team
        </n-button>
      </div>
    </template>
  </n-modal>
</template>

<script setup lang="ts">
import { ref, reactive, watch } from 'vue'
import { NModal, NForm, NFormItem, NInput, NInputNumber, NSelect, NSwitch, NButton, useMessage } from 'naive-ui'
import { UserTeamService, type TeamRequest } from '@/services/apis/UserTeamService'
import type { Team } from '@/models/Tournament'
import { SportType, TeamRole } from '@/models/Tournament'

interface Props {
  show: boolean
}

interface Emits {
  (e: 'update:show', value: boolean): void
  (e: 'team-created', team: Team): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

const message = useMessage()
const formRef = ref()
const loading = ref(false)

const form = reactive({
  name: '',
  description: '',
  sportType: '',
  maxMembers: 11,
  ageRange: { min: 18, max: 35 },
  public: true
})

const rules = {
  name: {
    required: true,
    message: 'Team name is required',
    trigger: 'blur'
  },
  description: {
    required: true,
    message: 'Team description is required',
    trigger: 'blur'
  },
  sportType: {
    required: true,
    message: 'Sport type is required',
    trigger: 'change'
  }
}

const sportTypeOptions = [
  { label: 'Football', value: 'Football' },
  { label: 'Basketball', value: 'Basketball' },
  { label: 'Tennis', value: 'Tennis' },
  { label: 'Running', value: 'Running' },
  { label: 'Volleyball', value: 'Volleyball' }
]

const updateShow = (value: boolean) => {
  emit('update:show', value)
}

const close = () => {
  emit('update:show', false)
}

const createTeam = async () => {
  try {
    await formRef.value?.validate()
    loading.value = true
    
    const teamRequest: TeamRequest = {
      name: form.name,
      description: form.description,
      sportType: form.sportType,
      maxMembers: form.maxMembers,
      public: form.public,
      minAge: form.ageRange.min,
      maxAge: form.ageRange.max
    }
    
    const newTeam = await UserTeamService.createTeam(teamRequest)
    
    emit('team-created', newTeam)
    
    Object.assign(form, {
      name: '',
      description: '',
      sportType: '',
      maxMembers: 11,
      ageRange: { min: 18, max: 35 },
      public: true
    })
    
    close()
  } catch (error) {
    console.error('Failed to create team:', error)
    message.error('Failed to create team. Please try again.')
  } finally {
    loading.value = false
  }
}

watch(() => props.show, (newVal) => {
  if (!newVal) {
    Object.assign(form, {
      name: '',
      description: '',
      sportType: '',
      maxMembers: 11,
      ageRange: { min: 18, max: 35 },
      public: true
    })
  }
})
</script>

<style scoped>
.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 0.5rem;
}

.age-range-inputs {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.age-separator {
  color: #64748b;
  font-size: 0.875rem;
  white-space: nowrap;
}

.age-unit {
  color: #64748b;
  font-size: 0.875rem;
  white-space: nowrap;
}

.privacy-label {
  margin-left: 0.5rem;
  color: #64748b;
  font-size: 0.875rem;
}

.create-team-btn {
  background-color: #3b82f6 !important;
  border-color: #3b82f6 !important;
}

.create-team-btn:hover {
  background-color: #2563eb !important;
  border-color: #2563eb !important;
}
</style> 