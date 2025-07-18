<template>
  <n-modal :show="show" @update:show="updateShow" preset="card" title="Create New Tournament" style="width: 1000px; max-height: 95vh; overflow-y: auto;">
    <n-form
      ref="formRef"
      :model="formData"
      :rules="rules"
      label-placement="left"
      label-width="auto"
      require-mark-placement="right-hanging"
      size="medium"
    >
      <div class="form-sections">
        <div class="form-section">
          <h3 class="section-title">Basic Information</h3>
          <div class="form-row">
            <n-form-item label="Tournament Name" path="name">
              <n-input
                v-model:value="formData.name"
                placeholder="Enter tournament name"
                clearable
              />
            </n-form-item>
            <n-form-item label="Sport Type" path="sportType">
              <n-select
                v-model:value="formData.sportType"
                :options="sportTypeOptions"
                placeholder="Select sport type"
                clearable
              />
            </n-form-item>
          </div>
          
          <n-form-item label="Description" path="description">
            <n-input
              v-model:value="formData.description"
              type="textarea"
              placeholder="Describe your tournament..."
              :rows="3"
              clearable
            />
          </n-form-item>
        </div>

        <div class="form-section">
          <h3 class="section-title">Format & Type</h3>
          <div class="form-row">
            <n-form-item label="Tournament Format" path="format">
              <n-select
                v-model:value="formData.format"
                :options="formatOptions"
                placeholder="Select format"
                clearable
              />
            </n-form-item>
            <n-form-item label="Tournament Type" path="tournamentType">
              <n-select
                v-model:value="formData.tournamentType"
                :options="tournamentTypeOptions"
                placeholder="Individual or Team"
                clearable
              />
            </n-form-item>
          </div>
        </div>

        <div class="form-section">
          <h3 class="section-title">Location & Schedule</h3>
          <div class="form-row">
            <n-form-item label="Location" path="location">
              <n-input
                v-model:value="formData.location"
                placeholder="Enter tournament location"
                clearable
              />
            </n-form-item>
            <n-form-item label="Start Date" path="startDate">
              <n-date-picker
                v-model:value="formData.startDate"
                type="datetime"
                placeholder="Select start date and time"
                clearable
              />
            </n-form-item>
          </div>
          
          <div class="form-row">
            <n-form-item label="End Date" path="endDate">
              <n-date-picker
                v-model:value="formData.endDate"
                type="datetime"
                placeholder="Select end date and time"
                clearable
              />
            </n-form-item>
            <n-form-item label="Registration Deadline" path="registrationDeadline">
              <n-date-picker
                v-model:value="formData.registrationDeadline"
                type="datetime"
                placeholder="Select registration deadline"
                clearable
              />
            </n-form-item>
          </div>
        </div>

        <div class="form-section">
          <h3 class="section-title">Participants & Entry</h3>
          <div class="form-row">
            <n-form-item label="Max Participants" path="maxParticipants">
              <n-input-number
                v-model:value="formData.maxParticipants"
                placeholder="Enter max participants"
                :min="2"
                :max="1000"
                clearable
              />
            </n-form-item>
            <n-form-item label="Entry Fee (₾)" path="entryFee">
              <n-input-number
                v-model:value="formData.entryFee"
                placeholder="Enter entry fee"
                :min="0"
                :max="10000"
                clearable
              />
            </n-form-item>
          </div>
          
          <div class="form-row">
            <n-form-item label="Prize Pool (₾)" path="prizePool">
              <n-input-number
                v-model:value="formData.prizePool"
                placeholder="Enter prize pool amount"
                :min="0"
                :max="100000"
                clearable
              />
            </n-form-item>
            <n-form-item label="Age Range" path="ageRange">
              <div class="age-range-inputs">
                <n-input-number
                  v-model:value="formData.ageRange.min"
                  placeholder="Min age"
                  :min="16"
                  :max="65"
                  size="medium"
                  clearable
                  show-button
                />
                <span class="age-separator">to</span>
                <n-input-number
                  v-model:value="formData.ageRange.max"
                  placeholder="Max age"
                  :min="16"
                  :max="65"
                  size="medium"
                  clearable
                  show-button
                />
                <span class="age-unit">years</span>
              </div>
            </n-form-item>
          </div>
        </div>

        <div class="form-section">
          <h3 class="section-title">Tournament Rules</h3>
          <n-form-item label="Rules" path="rules">
            <div class="rules-container">
              <div
                v-for="(rule, index) in formData.rules"
                :key="index"
                class="rule-item"
              >
                <n-input
                  v-model:value="formData.rules[index]"
                  placeholder="Enter rule"
                  clearable
                />
                <n-button
                  type="error"
                  size="small"
                  @click="removeRule(index)"
                  :disabled="formData.rules.length <= 1"
                >
                  <template #icon>
                    <n-icon><CloseOutline /></n-icon>
                  </template>
                </n-button>
              </div>
              <n-button
                type="primary"
                size="small"
                @click="addRule"
                class="add-rule-btn"
              >
                <template #icon>
                  <n-icon><AddOutline /></n-icon>
                </template>
                Add Rule
              </n-button>
            </div>
          </n-form-item>
        </div>
      </div>
    </n-form>

    <template #footer>
      <div class="modal-footer">
        <n-button @click="handleCancel" size="large">
          Cancel
        </n-button>
        <n-button
          type="primary"
          size="large"
          @click="handleSubmit"
          :loading="isSubmitting"
          color="#3b82f6"
        >
          Create Tournament
        </n-button>
      </div>
    </template>
  </n-modal>
</template>

<script setup lang="ts">
import { ref, reactive, computed } from 'vue'
import { 
  NModal, 
  NForm, 
  NFormItem, 
  NInput, 
  NSelect, 
  NDatePicker, 
  NInputNumber, 
  NButton, 
  NIcon,
  useMessage 
} from 'naive-ui'
import { 
  AddOutline, 
  CloseOutline 
} from '@vicons/ionicons5'
import type { Tournament, SportType, TournamentFormat, TournamentType } from '@/models/Tournament'

interface Props {
  show: boolean
}

interface Emits {
  (e: 'update:show', value: boolean): void
  (e: 'tournament-created', tournament: Tournament): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

// Debug logging
console.log('CreateTournamentModal props:', props)
console.log('CreateTournamentModal show value:', props.show)

const message = useMessage()
const formRef = ref()
const isSubmitting = ref(false)

// Form data
const formData = reactive({
  name: '',
  description: '',
  sportType: null as SportType | null,
  format: null as TournamentFormat | null,
  tournamentType: null as TournamentType | null,
  location: '',
  startDate: null as string | null,
  endDate: null as string | null,
  registrationDeadline: null as string | null,
  maxParticipants: null as number | null,
  entryFee: 0,
  prizePool: 0,
  ageRange: {
    min: 18,
    max: 65
  },
  rules: ['All participants must follow fair play rules.']
})

// Form validation rules
const rules = {
  name: [
    { required: true, message: 'Tournament name is required', trigger: 'blur' },
    { min: 3, max: 100, message: 'Name must be between 3 and 100 characters', trigger: 'blur' }
  ],
  description: [
    { required: true, message: 'Description is required', trigger: 'blur' },
    { min: 10, max: 500, message: 'Description must be between 10 and 500 characters', trigger: 'blur' }
  ],
  sportType: [
    { required: true, message: 'Sport type is required', trigger: 'change' }
  ],
  format: [
    { required: true, message: 'Tournament format is required', trigger: 'change' }
  ],
  tournamentType: [
    { required: true, message: 'Tournament type is required', trigger: 'change' }
  ],
  location: [
    { required: true, message: 'Location is required', trigger: 'blur' }
  ],
  startDate: [
    { 
      required: true, 
      message: 'Start date is required', 
      trigger: 'change',
      validator: (rule: any, value: any) => {
        if (!value) {
          return new Error('Start date is required')
        }
        return true
      }
    }
  ],
  endDate: [
    { 
      required: true, 
      message: 'End date is required', 
      trigger: 'change',
      validator: (rule: any, value: any) => {
        if (!value) {
          return new Error('End date is required')
        }
        return true
      }
    }
  ],
  registrationDeadline: [
    { 
      required: true, 
      message: 'Registration deadline is required', 
      trigger: 'change',
      validator: (rule: any, value: any) => {
        if (!value) {
          return new Error('Registration deadline is required')
        }
        return true
      }
    }
  ],
  maxParticipants: [
    { 
      required: true, 
      message: 'Max participants is required', 
      trigger: 'blur',
      validator: (rule: any, value: any) => {
        if (!value || value < 2) {
          return new Error('Max participants must be at least 2')
        }
        return true
      }
    }
  ],
  'ageRange.min': [
    { required: true, message: 'Minimum age is required', trigger: 'blur' },
    { type: 'number', min: 16, max: 65, message: 'Age must be between 16 and 65', trigger: 'blur' }
  ],
  'ageRange.max': [
    { required: true, message: 'Maximum age is required', trigger: 'blur' },
    { type: 'number', min: 16, max: 65, message: 'Age must be between 16 and 65', trigger: 'blur' }
  ]
}

const sportTypeOptions = [
  { label: 'Football', value: 'Football' },
  { label: 'Basketball', value: 'Basketball' },
  { label: 'Tennis', value: 'Tennis' },
  { label: 'Running', value: 'Running' },
  { label: 'Cycling', value: 'Cycling' },
  { label: 'Volleyball', value: 'Volleyball' },
  { label: 'Swimming', value: 'Swimming' }
]

const formatOptions = [
  { label: 'Single Elimination', value: 'Single Elimination' },
  { label: 'Double Elimination', value: 'Double Elimination' },
  { label: 'Round Robin', value: 'Round Robin' },
  { label: 'Swiss System', value: 'Swiss System' }
]

const tournamentTypeOptions = [
  { label: 'Individual', value: 'individual' },
  { label: 'Team', value: 'team' }
]

const addRule = () => {
  formData.rules.push('')
}

const removeRule = (index: number) => {
  if (formData.rules.length > 1) {
    formData.rules.splice(index, 1)
  }
}

const updateShow = (value: boolean) => {
  emit('update:show', value)
  if (value) {
    setTimeout(() => {
      formRef.value?.restoreValidation()
    }, 100)
  }
}

const handleCancel = () => {
  emit('update:show', false)
  resetForm()
}

const resetForm = () => {
  formData.name = ''
  formData.description = ''
  formData.sportType = null
  formData.format = null
  formData.tournamentType = null
  formData.location = ''
  formData.startDate = null
  formData.endDate = null
  formData.registrationDeadline = null
  formData.maxParticipants = null
  formData.entryFee = 0
  formData.prizePool = 0
  formData.ageRange = { min: 18, max: 65 }
  formData.rules = ['All participants must follow fair play rules.']
}

const handleSubmit = async () => {
  try {
    isSubmitting.value = true
    
    console.log('Form data before validation:', formData)
    
    await formRef.value?.validate()
    
    console.log('Form validation passed!')
    
    const tournament: Tournament = {
      id: `tournament-${Date.now()}`,
      name: formData.name,
      description: formData.description,
      sportType: formData.sportType!,
      format: formData.format!,
      status: 'Draft',
      hostId: 'current-user-id', // Mock host ID
      hostName: 'Current User', // Mock host name
      location: formData.location,
      latitude: 0, // Mock coordinates
      longitude: 0,
      startDate: formData.startDate!,
      endDate: formData.endDate!,
      registrationDeadline: formData.registrationDeadline!,
      maxParticipants: formData.maxParticipants!,
      currentParticipants: 0,
      entryFee: formData.entryFee,
      prizePool: formData.prizePool,
      ageRange: formData.ageRange,
      rules: formData.rules.filter(rule => rule.trim() !== ''),
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    }
    
    console.log('Created tournament:', tournament)
    
    // Emit event with created tournament
    emit('tournament-created', tournament)
    
    // Show success message
    message.success('Tournament created successfully!')
    
    // Close modal and reset form
    emit('update:show', false)
    resetForm()
    
  } catch (error) {
    console.error('Form validation failed:', error)
    console.log('Form data that failed validation:', formData)
  } finally {
    isSubmitting.value = false
  }
}
</script>

<style scoped>
.form-sections {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.form-section {
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  padding: 1.5rem;
  background: #f8fafc;
}

.section-title {
  margin: 0 0 1rem 0;
  font-size: 1.1rem;
  font-weight: 600;
  color: #1e293b;
  border-bottom: 2px solid #3b82f6;
  padding-bottom: 0.5rem;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
  margin-bottom: 1rem;
}

.form-row:last-child {
  margin-bottom: 0;
}

.age-range-inputs {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  flex-wrap: wrap;
}

.age-range-inputs .n-input-number {
  min-width: 120px;
}

.age-separator {
  color: #64748b;
  font-weight: 500;
  white-space: nowrap;
}

.age-unit {
  color: #64748b;
  font-size: 0.875rem;
  white-space: nowrap;
}

.rules-container {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.rule-item {
  display: flex;
  gap: 0.5rem;
  align-items: center;
}

.add-rule-btn {
  align-self: flex-start;
  margin-top: 0.5rem;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  padding-top: 1rem;
  border-top: 1px solid #e2e8f0;
}

/* Responsive design */
@media (max-width: 768px) {
  .form-row {
    grid-template-columns: 1fr;
  }
  
  .age-range-inputs {
    flex-wrap: wrap;
  }
}
</style> 