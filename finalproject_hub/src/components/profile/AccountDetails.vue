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
                @focus="startEditing"
                />
            </n-form-item>
    
            <n-form-item label="Email *">
                <n-input v-model:value="formData.email" disabled />
            </n-form-item>
    
            <n-form-item label="Phone Number *">
                <n-input
                v-model:value="formData.phoneNumber"
                placeholder="Phone Number"
                @focus="startEditing"
                />
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
                @focus="startEditing"
                />
            </n-form-item>
        </div>
  
            <div v-if="isEditing" class="action-buttons">
                <n-button type="primary" :loading="isSubmitting" @click="submitChanges">
                    Submit
                </n-button>
                <n-button @click="cancelChanges" tertiary>Cancel</n-button>
            </div>
        </div>
    </div>
  </template>
  
<script setup lang="ts">
import { ref, reactive, onMounted, watch } from 'vue'
import { useUserStore } from '@/store/profile/userStore'
import { NFormItem, NInput, NButton, NSkeleton, useMessage  } from 'naive-ui'
import { storeToRefs } from 'pinia'
import { UserUpdateDTO } from '@/models/UserUpdateDTO'

const userStore = useUserStore()
const { profile, isLoading } = storeToRefs(userStore)

const isSubmitting = ref(false)
const isEditing = ref(false)
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

const userId = '13fa5e4e-1d9e-4a2a-9a20-7385f24e9097'

onMounted(async () => {
    userStore.fetchProfile(userId)
})

watch(profile, (data) => {
    if (data) {
        Object.assign(originalData, data)
        Object.assign(formData, data)
    }
})

function startEditing() {
    isEditing.value = true
}

async function submitChanges() {
    isSubmitting.value = true
    try {
        const updateData: UserUpdateDTO = {
            name: formData.name,
            phoneNumber: formData.phoneNumber,
            description: formData.description
        }

        const updated = await userStore.updateProfile(userId, updateData)
        
        isEditing.value = false
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
    gap: 6px;
}

.skeleton-full {
    grid-column: span 2;
    display: flex;
    flex-direction: column;
    gap: 6px;
}
</style>
