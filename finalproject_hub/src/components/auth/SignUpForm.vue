<template>
    <n-card title="Sign Up" class="auth-card">
        <n-form>
            <n-form-item label="Name">
                <n-input v-model:value="name" placeholder="Full Name" />
            </n-form-item>

            <div class="form-row">
                <n-form-item label="Email" class="half">
                    <n-input v-model:value="email" placeholder="Email address" />
                </n-form-item>

                <n-form-item label="Phone Number" class="half">
                    <n-input v-model:value="phoneNumber" placeholder="Phone Number" />
                </n-form-item>
            </div>

            <div class="form-row">
                <n-form-item label="Age" class="half">
                    <n-input v-model:value="age" type="number" placeholder="Age" />
                </n-form-item>

                <n-form-item label="Birth Date" class="half">
                    <n-date-picker v-model:value="birthDate" type="date" placeholder="Birth Date" />
                </n-form-item>
            </div>

            <n-form-item label="Description">
                <n-input v-model:value="description" type="textarea" placeholder="Short Description" />
            </n-form-item>

            <div class="form-row">
                <n-form-item label="Password" class="half">
                    <n-input type="password" v-model:value="password" placeholder="Create password" />
                </n-form-item>

                <n-form-item
                    label="Repeat Password"
                    class="half"
                    :feedback="passwordMismatch ? 'Passwords do not match' : ''"
                    :validation-status="passwordMismatch ? 'error' : undefined"
                >
                    <n-input type="password" v-model:value="repeatPassword" placeholder="Repeat password" />
                </n-form-item>
            </div>

            <n-button type="primary" color="orange" class="mt-4" @click="signUp">
                Register
            </n-button>
        </n-form>

        <div class="signin-switch">
            Already have an account?
            <n-button text color="orange" @click="$emit('switchToSignIn')">Sign In</n-button>
        </div>
    </n-card>
</template>
  
<script setup>
import { ref, computed } from 'vue'
import { NInput, NButton, NForm, NFormItem, NCard, NDatePicker, useMessage } from 'naive-ui'
import { AuthService } from '@/services/apis/AuthService'

const name = ref('')
const email = ref('')
const phoneNumber = ref('')
const age = ref(null)
const birthDate = ref(null)
const description = ref('')
const password = ref('')
const repeatPassword = ref('')
const message = useMessage()

const passwordMismatch = computed(() => {
  return repeatPassword.value && password.value !== repeatPassword.value
})

const signUp = async () => {
    if (passwordMismatch.value) {
        message.error('Passwords do not match')
        return
    }

    try {
        const response = await AuthService.signUp({
            name: name.value,
            email: email.value,
            phoneNumber: phoneNumber.value,
            age: age.value,
            birthDate: birthDate.value,
            description: description.value,
            password: password.value
        })
        
        localStorage.setItem('user', JSON.stringify({
            id: response.userId,
            name: response.name,
            email: response.email
        }))
        
        message.success('Registration successful!')
        console.log('Registration successful:', response)
    } catch (error) {
        message.error(error.message || 'Registration failed')
        console.error('Registration error:', error)
    }
}
</script>

<style scoped>
.form-row {
    display: flex;
    gap: 20px;
    flex-wrap: wrap;
}

.form-row .half {
    flex: 1 1 calc(50% - 10px);
}

.auth-card {
    width: 600px;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
}

.signin-switch {
    margin-top: 20px;
    text-align: center;
}
</style>
