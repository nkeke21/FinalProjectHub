<template>
    <n-card title="Sign Up" class="auth-card">
        <n-form>
            <n-form-item label="Name">
                <n-input v-model:value="name" placeholder="Full Name" />
            </n-form-item>

            <n-form-item label="Email">
                <n-input v-model:value="email" placeholder="Email address" />
            </n-form-item>

            <n-form-item label="Phone Number">
                <n-input v-model:value="phoneNumber" placeholder="Phone Number" />
            </n-form-item>

            <n-form-item label="Birth Date">
                <n-date-picker v-model:value="birthDate" type="date" placeholder="Birth Date" />
            </n-form-item>

            <n-form-item label="Description">
                <n-input v-model:value="description" type="textarea" placeholder="Short Description" />
            </n-form-item>

            <n-form-item label="Password">
                <n-input type="password" v-model:value="password" placeholder="Create password" />
            </n-form-item>

            <n-button type="primary" color="orange" @click="signUp">Register</n-button>
        </n-form>

        <div class="signup-switch">
            Already have an account?
            <n-button text color="orange" @click="$emit('switchToSignIn')">Sign In</n-button>
        </div>
    </n-card>
</template>
  
<script setup>
import { ref } from 'vue'
import { NInput, NButton, NForm, NFormItem, NCard, NDatePicker } from 'naive-ui'

const name = ref('')
const email = ref('')
const phoneNumber = ref('')
const birthDate = ref(null)
const description = ref('')
const password = ref('')

const signUp = () => {
    console.log('Register with:', {
        name: name.value,
        email: email.value,
        phoneNumber: phoneNumber.value,
        birthDate: birthDate.value,
        description: description.value,
        password: password.value
    })
}
</script>
  