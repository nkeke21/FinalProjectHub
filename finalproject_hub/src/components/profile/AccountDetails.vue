<template>
    <div>
        <div v-if="isLoading">
            <div class="form-skeleton">
                <div class="skeleton-pair" v-for="n in 4" :key="n">
                    <n-skeleton height="18px" width="60px" round />
                    <n-skeleton height="40px" round :sharp="false" />
                </div>

                <div class="skeleton-full">
                    <n-skeleton height="18px" width="140px" round />
                    <n-skeleton height="90px" round :sharp="false" />
                </div>
            </div>
        </div>
  
        <div v-else class="account-details-container">
            <h2 class="section-title">ACCOUNT DETAILS</h2>
            <div class="form-grid">
            <n-form-item label="Name *">
                <n-input
                v-model:value="formData.name"
                placeholder="Enter your name"
                :disabled="!isOwnProfile"
                @focus="isOwnProfile ? startEditing() : null"
                />
            </n-form-item>
    
            <n-form-item label="Email *">
                <n-input v-model:value="formData.email" disabled />
            </n-form-item>
    
            <n-form-item label="Phone Number *" :feedback="phoneError" :validation-status="phoneError ? 'error' : undefined">
                <div class="phone-input-container">
                    <span class="country-code">+995</span>
                    <n-input
                        v-model:value="formData.phoneNumber"
                        placeholder="5XX XXX XXX"
                        class="phone-input-field"
                        :disabled="!isOwnProfile"
                        @focus="isOwnProfile ? startEditing() : null"
                        @input="validatePhone"
                    />
                </div>
            </n-form-item>
    
            <n-form-item label="Birth Date *">
                <n-input v-model:value="formData.birthDate" disabled />
            </n-form-item>
    
            <n-form-item label="Profile Description">
                <n-input
                v-model:value="formData.description"
                type="textarea"
                placeholder="Write a bit about yourself..."
                :autosize="{ minRows: 3, maxRows: 6 }"
                :disabled="!isOwnProfile"
                @focus="isOwnProfile ? startEditing() : null"
                />
            </n-form-item>
        </div>
  
            <div v-if="isOwnProfile && isEditing" class="action-buttons">
                <n-button type="primary" :loading="isSubmitting" @click="submitChanges">
                    Submit
                </n-button>
                <n-button @click="cancelChanges" tertiary>Cancel</n-button>
            </div>
        </div>
    </div>
  </template>
  
<script setup lang="ts">
import { ref, reactive, onMounted, watch, computed, defineProps } from 'vue'
import { useUserStore } from '@/store/profile/userStore'
import { NFormItem, NInput, NButton, NSkeleton, useMessage  } from 'naive-ui'
import { storeToRefs } from 'pinia'
import { UserUpdateDTO } from '@/models/UserUpdateDTO'
import { useRoute } from 'vue-router'

const props = defineProps({
    isOwnProfile: Boolean
})

const userStore = useUserStore()
const { profile, isLoading } = storeToRefs(userStore)

const isSubmitting = ref(false)
const isEditing = ref(false)
const phoneError = ref('')
const message = useMessage()

const originalData = reactive({
    name: '',
    email: '',
    phoneNumber: '',
    birthDate: '',
    description: ''
})

const formData = reactive({
    name: '',
    email: '',
    phoneNumber: '',
    birthDate: '',
    description: ''
})

const route = useRoute()

const isOwnProfile = computed(() => props.isOwnProfile)

const getUserId = () => {
    return route.params.id as string
}

const userId = getUserId()

const validatePhone = () => {
    formData.phoneNumber = formData.phoneNumber.replace(/\D/g, '')
    
    if (formData.phoneNumber.length > 0 && (!formData.phoneNumber.startsWith('5') || formData.phoneNumber.length !== 9)) {
        phoneError.value = 'Please enter a valid Georgian mobile number (5XX XXX XXX)'
    } else {
        phoneError.value = ''
    }
}

onMounted(async () => {
    if (isOwnProfile.value) {
        userStore.fetchCurrentUserProfile()
    } else if (userId) {
        userStore.fetchProfile(userId)
    }
})

watch(profile, (data) => {
    if (data) {
        Object.assign(originalData, data)
        Object.assign(formData, data)
        
        if (formData.phoneNumber && formData.phoneNumber.startsWith('+995')) {
            formData.phoneNumber = formData.phoneNumber.substring(4)
        }
    }
})

function startEditing() {
    if (isOwnProfile.value) {
        isEditing.value = true
    }
}

async function submitChanges() {
    // Validate phone number before submitting
    if (!formData.phoneNumber || formData.phoneNumber.length !== 9 || !formData.phoneNumber.startsWith('5')) {
        phoneError.value = 'Please enter a valid Georgian mobile number (5XX XXX XXX)'
        return
    }
    
    isSubmitting.value = true
    try {
        const updateData: UserUpdateDTO = {
            name: formData.name,
            phoneNumber: `+995${formData.phoneNumber}`,
            description: formData.description
        }

        if (isOwnProfile.value) {
            await userStore.updateCurrentUserProfile(updateData)
        } else if (userId) {
            await userStore.updateProfile(userId, updateData)
        }
        
        isEditing.value = false
        phoneError.value = ''
        message.success('Your account details were updated successfully!')
    } catch (error) {
        console.error("Update failed:", error)
        message.error('Failed to update account details.')
    } finally {
        isSubmitting.value = false
    }
}

function cancelChanges() {
    Object.assign(formData, originalData)
    isEditing.value = false
    phoneError.value = ''
}
</script>

<style scoped>
.account-details-container {
    padding: 40px;
    width: 100%;
}

.section-title {
    font-size: 22px;
    font-weight: 600;
    margin-bottom: 24px;
}

.form-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
    gap: 24px;
    max-width: 900px;
}

.action-buttons {
    margin-top: 24px;
    display: flex;
    gap: 12px;
}

.form-skeleton {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
    gap: 24px;
    max-width: 900px;
    margin: 40px auto;
    padding: 40px;
}

.skeleton-pair {
    display: flex;
    flex-direction: column;
    gap: 8px;
}

.skeleton-full {
    display: flex;
    flex-direction: column;
    gap: 8px;
    grid-column: 1 / -1;
}

.phone-input-container {
    display: flex;
    align-items: center;
    border: 1px solid #e2e8f0;
    border-radius: 6px;
    overflow: hidden;
}

.country-code {
    background-color: #f8fafc;
    padding: 0.5rem 0.75rem;
    border-right: 1px solid #e2e8f0;
    color: #64748b;
    font-weight: 500;
    font-size: 14px;
}

.phone-input-field {
    flex: 1;
}

.phone-input-field :deep(.n-input) {
    border: none;
    box-shadow: none;
}

.phone-input-field :deep(.n-input:focus) {
    border: none;
    box-shadow: none;
}
</style>
