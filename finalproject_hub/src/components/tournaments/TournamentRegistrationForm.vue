<template>
  <n-modal :show="show" @update:show="updateShow" preset="card" title="Tournament Registration" style="width: 1200px; max-height: 95vh; overflow-y: auto;">
    <div class="registration-form-content">
      <div class="tournament-info-header">
        <h3>{{ tournament?.name }}</h3>
        <div class="tournament-meta">
          <span class="meta-item">
            <n-icon size="16" color="#64748b">
              <CalendarOutline />
            </n-icon>
            {{ formatDateRange(tournament?.startDate, tournament?.endDate) }}
          </span>
          <span class="meta-item">
            <n-icon size="16" color="#64748b">
              <LocationOutline />
            </n-icon>
            {{ tournament?.location }}
          </span>
          <span class="meta-item">
            <n-icon size="16" color="#64748b">
              <PeopleOutline />
            </n-icon>
            {{ tournament?.currentParticipants }}/{{ tournament?.maxParticipants }} participants
          </span>
        </div>
      </div>

      <n-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-placement="left"
        label-width="auto"
        require-mark-placement="right-hanging"
        size="medium"
      >
        <n-form-item label="Registration Type">
          <div class="registration-type-display">
            <n-tag 
              :type="tournament?.tournamentType === 'team' ? 'success' : 'info'"
              size="large"
              :color="tournament?.tournamentType === 'team' ? '#10b981' : '#3b82f6'"
            >
              <template #icon>
                <n-icon size="16">
                  <PeopleOutline v-if="tournament?.tournamentType === 'team'" />
                  <PersonOutline v-else />
                </n-icon>
              </template>
              {{ tournament?.tournamentType === 'team' ? 'Team Registration' : 'Individual Registration' }}
            </n-tag>
            <p class="registration-type-description">
              {{ tournament?.tournamentType === 'team' 
                ? 'This tournament requires team registration. Please select your team below.' 
                : 'This tournament is for individual participants only.' 
              }}
            </p>
          </div>
        </n-form-item>

        <n-form-item 
          v-if="tournament?.tournamentType === 'team'"
          label="Select Team" 
          path="teamId"
        >
          <n-select
            v-model:value="formData.teamId"
            :options="userTeams"
            placeholder="Select your team"
            clearable
            :loading="loadingTeams"
          />
        </n-form-item>

        <div class="form-section">
          <h4>Personal Information</h4>
          <div class="profile-note">
            <n-icon size="16" color="#3b82f6">
              <InformationCircleOutline />
            </n-icon>
            <span>Basic information (name, age, email, phone) is taken from your profile and cannot be changed here. All other fields need to be filled for this specific tournament registration.</span>
          </div>
          
          <div class="form-row">
            <n-form-item label="Full Name" path="fullName">
              <n-input 
                v-model:value="formData.fullName" 
                placeholder="Enter your full name" 
                :disabled="true"
                class="profile-field"
              />
            </n-form-item>
            
            <n-form-item label="Age" path="age">
              <n-input-number 
                v-model:value="formData.age" 
                :min="tournament?.ageRange?.min || 16" 
                :max="tournament?.ageRange?.max || 65"
                placeholder="Enter your age"
                :disabled="true"
                class="profile-field"
              />
            </n-form-item>
          </div>

          <div class="form-row">
            <n-form-item label="Email" path="email">
              <n-input 
                v-model:value="formData.email" 
                placeholder="Enter your email address" 
                :disabled="true"
                class="profile-field"
              />
            </n-form-item>
            
            <n-form-item label="Phone Number" path="phoneNumber">
              <n-input 
                v-model:value="formData.phoneNumber" 
                placeholder="Enter your phone number" 
                :disabled="true"
                class="profile-field"
              />
            </n-form-item>
          </div>

          <n-form-item label="Address" path="address">
            <n-input 
              v-model:value="formData.address" 
              placeholder="Enter your address" 
            />
          </n-form-item>
        </div>

        <div class="form-section">
          <h4>Emergency Contact</h4>
          
          <div class="form-row">
            <n-form-item label="Emergency Contact Name" path="emergencyContact.name">
              <n-input 
                v-model:value="formData.emergencyContact.name" 
                placeholder="Enter emergency contact name" 
              />
            </n-form-item>
            
            <n-form-item label="Relationship" path="emergencyContact.relationship">
              <n-input 
                v-model:value="formData.emergencyContact.relationship" 
                placeholder="e.g., Parent, Spouse, Friend" 
              />
            </n-form-item>
          </div>

          <div class="form-row">
            <n-form-item label="Emergency Contact Phone" path="emergencyContact.phone">
              <n-input 
                v-model:value="formData.emergencyContact.phone" 
                placeholder="Enter emergency contact phone" 
              />
            </n-form-item>
            
            <n-form-item label="Emergency Contact Email" path="emergencyContact.email">
              <n-input 
                v-model:value="formData.emergencyContact.email" 
                placeholder="Enter emergency contact email" 
              />
            </n-form-item>
          </div>
        </div>


        <div class="form-section">
          <h4>Tournament Experience</h4>
          
          <n-form-item label="Previous Tournament Experience" path="previousExperience">
            <n-select
              v-model:value="formData.previousExperience"
              :options="experienceOptions"
              placeholder="Select your experience level"
            />
          </n-form-item>

          <n-form-item label="Skill Level" path="skillLevel">
            <n-select
              v-model:value="formData.skillLevel"
              :options="skillLevelOptions"
              placeholder="Select your skill level"
            />
          </n-form-item>

          <n-form-item label="Previous Achievements" path="previousAchievements">
            <n-input 
              v-model:value="formData.previousAchievements" 
              type="textarea" 
              placeholder="List any previous tournament achievements or awards (optional)"
              :rows="2"
            />
          </n-form-item>
        </div>


        <div class="form-section">
          <h4>Terms and Conditions</h4>
          
          <n-form-item path="acceptTerms">
            <n-checkbox v-model:checked="formData.acceptTerms">
              I accept the tournament terms and conditions
            </n-checkbox>
          </n-form-item>

          <n-form-item path="acceptWaiver">
            <n-checkbox v-model:checked="formData.acceptWaiver">
              I accept the liability waiver and medical release
            </n-checkbox>
          </n-form-item>

          <n-form-item path="acceptRules">
            <n-checkbox v-model:checked="formData.acceptRules">
              I agree to follow all tournament rules and regulations
            </n-checkbox>
          </n-form-item>
        </div>
      </n-form>

      <div class="form-actions">
        <n-button @click="updateShow(false)" size="large">
          Cancel
        </n-button>
        <n-button 
          type="primary" 
          size="large"
          @click="handleSubmit"
          :loading="submitting"
          color="#3b82f6"
        >
          Submit Registration
        </n-button>
      </div>
    </div>
  </n-modal>
</template>

<script setup lang="ts">
import { ref, reactive, computed, watch } from 'vue'
import { 
  NModal, NForm, NFormItem, NInput, NInputNumber, NSelect, NRadioGroup, NRadio, 
  NCheckbox, NButton, NIcon, useMessage 
} from 'naive-ui'
import { 
  CalendarOutline, LocationOutline, PeopleOutline, PersonOutline, InformationCircleOutline
} from '@vicons/ionicons5'
import type { Tournament } from '@/models/Tournament'
import { TournamentRegistrationService } from '@/services/apis/TournamentRegistrationService'
import { UserTeamService } from '@/services/apis/UserTeamService'
import { UserProfileService, type UserProfile } from '@/services/apis/UserProfileService'

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

const message = useMessage()
const formRef = ref()
const submitting = ref(false)

// Form data
const formData = reactive({
  registrationType: 'individual',
  teamId: null as string | null,
  fullName: '',
  age: null as number | null,
  email: '',
  phoneNumber: '',
  address: '',
  emergencyContact: {
    name: '',
    relationship: '',
    phone: '',
    email: ''
  },
  previousExperience: null as string | null,
  skillLevel: null as string | null,
  previousAchievements: '',
  acceptTerms: false,
  acceptWaiver: false,
  acceptRules: false
})


const experienceOptions = [
  { label: 'First Tournament', value: 'first' },
  { label: '1-3 Tournaments', value: 'beginner' },
  { label: '4-10 Tournaments', value: 'intermediate' },
  { label: '10+ Tournaments', value: 'experienced' },
  { label: 'Professional Level', value: 'professional' }
]

const skillLevelOptions = [
  { label: 'Beginner', value: 'beginner' },
  { label: 'Intermediate', value: 'intermediate' },
  { label: 'Advanced', value: 'advanced' },
  { label: 'Expert', value: 'expert' }
]


const userTeams = ref<{ label: string; value: string }[]>([])
const loadingTeams = ref(false)

const userProfile = ref<UserProfile | null>(null)
const loadingProfile = ref(false)

const rules = computed(() => {
  const baseRules = {
    address: {
      required: true,
      message: 'Address is required',
      trigger: 'blur'
    },
    'emergencyContact.name': {
      required: true,
      message: 'Emergency contact name is required',
      trigger: 'blur'
    },
    'emergencyContact.phone': {
      required: true,
      message: 'Emergency contact phone is required',
      trigger: 'blur'
    },
    previousExperience: {
      required: true,
      message: 'Previous experience is required',
      trigger: 'change'
    },
    skillLevel: {
      required: true,
      message: 'Skill level is required',
      trigger: 'change'
    },
    acceptTerms: {
      required: true,
      message: 'You must accept the terms and conditions',
      trigger: 'change'
    },
    acceptWaiver: {
      required: true,
      message: 'You must accept the liability waiver',
      trigger: 'change'
    },
    acceptRules: {
      required: true,
      message: 'You must agree to follow tournament rules',
      trigger: 'change'
    }
  }

  if (props.tournament?.tournamentType === 'team') {
    baseRules.teamId = {
      required: true,
      message: 'Team selection is required',
      trigger: 'change'
    }
  }

  return baseRules
})

const updateShow = (value: boolean) => {
  emit('update:show', value)
}

const loadUserTeams = async () => {
  if (!props.tournament) return
  
  try {
    loadingTeams.value = true
    const teams = await UserTeamService.getUserTeams(props.tournament.sportType)
    
    userTeams.value = teams.map(team => ({
      label: team.name,
      value: team.id
    }))
  } catch (error) {
    console.error('Error loading user teams:', error)
  } finally {
    loadingTeams.value = false
  }
}

const loadUserProfile = async () => {
  try {
    loadingProfile.value = true
    const profile = await UserProfileService.getUserProfile()
    userProfile.value = profile
  } catch (error) {
    console.error('Error loading user profile:', error)
  } finally {
    loadingProfile.value = false
  }
}

const handleSubmit = async () => {
  try {
    submitting.value = true
    
    await formRef.value?.validate()
    
    const registrationData = {
      tournamentId: props.tournament!.id,
      registrationType: formData.registrationType,
      teamId: formData.teamId,
      participantInfo: {
        fullName: formData.fullName,
        age: formData.age,
        email: formData.email,
        phoneNumber: formData.phoneNumber,
        address: formData.address,
        emergencyContact: formData.emergencyContact,
        previousExperience: formData.previousExperience,
        skillLevel: formData.skillLevel,
        previousAchievements: formData.previousAchievements
      }
    }
    
    const response = await TournamentRegistrationService.registerForTournament(registrationData)
    
    if (response.success) {
      message.success('Registration submitted successfully!')
      emit('registration-success')
      updateShow(false)
    } else {
      message.error(response.message || 'Registration failed')
      emit('registration-error', response.message)
    }
  } catch (error) {
    console.error('Registration error:', error)
    message.error('Registration failed. Please check your information and try again.')
    emit('registration-error', 'Registration failed')
  } finally {
    submitting.value = false
  }
}

const formatDateRange = (startDate?: string, endDate?: string) => {
  if (!startDate || !endDate) return ''
  const start = new Date(startDate).toLocaleDateString()
  const end = new Date(endDate).toLocaleDateString()
  return `${start} - ${end}`
}

watch(() => props.show, (newValue) => {
  if (newValue) {
    const registrationType = props.tournament?.tournamentType === 'team' ? 'team' : 'individual'
    
    loadUserProfile()
    if (registrationType === 'team') {
      loadUserTeams()
    }
  }
})

watch(userProfile, (profile) => {
  if (profile) {
    Object.assign(formData, {
      registrationType: props.tournament?.tournamentType === 'team' ? 'team' : 'individual',
      teamId: null,
      fullName: profile.fullName,
      age: profile.age,
      email: profile.email,
      phoneNumber: profile.phoneNumber,
      address: '',
      emergencyContact: {
        name: '',
        relationship: '',
        phone: '',
        email: ''
      },
      previousExperience: null,
      skillLevel: null,
      previousAchievements: '',
      acceptTerms: false,
      acceptWaiver: false,
      acceptRules: false
    })
  }
})
</script>

<style scoped>
.registration-form-content {
  padding: 1rem;
}

.tournament-info-header {
  margin-bottom: 2rem;
  padding: 1.5rem;
  background: #f8fafc;
  border-radius: 8px;
  border: 1px solid #e2e8f0;
}

.tournament-info-header h3 {
  margin: 0 0 1rem 0;
  color: #1e293b;
  font-size: 1.5rem;
  font-weight: 600;
}

.tournament-meta {
  display: flex;
  gap: 2rem;
  flex-wrap: wrap;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: #64748b;
  font-size: 0.875rem;
}

.form-section {
  margin-bottom: 2rem;
  padding: 1.5rem;
  background: #f8fafc;
  border-radius: 8px;
  border: 1px solid #e2e8f0;
}

.form-section h4 {
  margin: 0 0 1.5rem 0;
  color: #1e293b;
  font-size: 1.125rem;
  font-weight: 600;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
}

.form-actions {
  display: flex;
  gap: 1rem;
  justify-content: flex-end;
  margin-top: 2rem;
  padding-top: 1.5rem;
  border-top: 1px solid #e2e8f0;
}

.registration-type-display {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.registration-type-description {
  margin: 0;
  color: #64748b;
  font-size: 0.875rem;
  line-height: 1.4;
}

.profile-note {
  display: flex;
  align-items: flex-start;
  gap: 0.5rem;
  padding: 0.75rem;
  background: #eff6ff;
  border: 1px solid #dbeafe;
  border-radius: 6px;
  margin-bottom: 1.5rem;
  color: #1e40af;
  font-size: 0.875rem;
  line-height: 1.4;
}

.profile-field {
  background-color: #f8fafc !important;
  color: #64748b !important;
}

@media (max-width: 768px) {
  .form-row {
    grid-template-columns: 1fr;
  }
  
  .tournament-meta {
    flex-direction: column;
    gap: 0.5rem;
  }
  
  .form-actions {
    flex-direction: column;
  }
}
</style> 