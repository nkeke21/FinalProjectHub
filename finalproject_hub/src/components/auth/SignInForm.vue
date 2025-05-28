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
import { NInput, NButton, NForm, NFormItem, NCard } from 'naive-ui'

const username = ref('')
const password = ref('')

const signIn = () => {
    console.log('Sign-in with', username.value, password.value)
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