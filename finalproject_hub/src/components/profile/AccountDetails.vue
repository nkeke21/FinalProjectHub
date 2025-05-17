<template>
    <div class="account-details-container">
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
                <n-input v-model:value="formData.email" placeholder="Enter your email" disabled />
            </n-form-item>

            <n-form-item label="Phone Number *">
                <n-input
                    v-model:value="formData.phoneNumber"
                    placeholder="Phone Number"
                    @focus="startEditing"
                />
            </n-form-item>

            <n-form-item label="Birth Date *">
                <n-input
                    v-model:value="formData.birthDate"
                    placeholder="YYYY-MM-DD"
                    disabled
                />
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
            <n-button type="primary" @click="submitChanges">Submit</n-button>
            <n-button @click="cancelChanges" tertiary>Cancel</n-button>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { NFormItem, NInput, NButton } from 'naive-ui'

const originalData = {
    name: 'Nika Kekelishvili',
    email: 'nkeke21@freeuni.edu.ge',
    phoneNumber: '599343842',
    birthDate: '2002-08-25',
    description: ''
}

const formData = reactive({ ...originalData })

const isEditing = ref(false)

function startEditing() {
    isEditing.value = true
}

function submitChanges() {
    Object.assign(originalData, formData)
    isEditing.value = false
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
</style>
  