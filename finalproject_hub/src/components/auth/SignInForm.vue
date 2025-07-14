<template>
    <n-card title="Sign In" class="auth-card">
        <n-form>
            <n-form-item label="Username">
                <n-input v-model:value="username" placeholder="Enter username" />
            </n-form-item>

            <n-form-item label="Password">
                <n-input type="password" v-model:value="password" placeholder="Enter password" />
            </n-form-item>

            <n-button type="primary" @click="signIn" color="orange">Sign In</n-button>
        </n-form>

        <div class="signup-switch">
            Don't have an account?
            <n-button text color="orange" @click="$emit('switchToSignUp')">Sign Up</n-button>
        </div>
    </n-card>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { NInput, NButton, NForm, NFormItem, NCard, useMessage } from 'naive-ui'
import { AuthService } from '@/services/apis/AuthService'

const username = ref('')
const password = ref('')
const message = useMessage()
const router = useRouter()

const signIn = async () => {
    try {
        const response = await AuthService.signIn({
            username: username.value,
            password: password.value
        })
        
        localStorage.setItem('user', JSON.stringify({
            id: response.userId,
            name: response.name,
            email: response.email
        }))
        
        message.success('Login successful!')
        console.log('Login successful:', response)
        
        router.push('/homepage')
    } catch (error) {
        message.error(error.message || 'Login failed')
        console.error('Login error:', error)
    }
}
</script>

<style scoped>
.auth-card {
    width: 450px;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
}

.signup-switch {
    margin-top: 16px;
    text-align: center;
}
</style>