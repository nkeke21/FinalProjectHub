<template>
    <div class="auth-container">
        <div class="left-panel">
            <img src="@/assets/Join2Play.png" alt="Join2Play Logo" class="auth-icon" />
        </div>

        <div class="right-panel">
            <n-card title="Sign In" class="auth-card">
                <n-form>
                    <n-form-item label="Username">
                        <n-input v-model:value="username" placeholder="Enter username" />
                    </n-form-item>

                    <n-form-item label="Password">
                        <n-input type="password" v-model:value="password" placeholder="Enter password" />
                    </n-form-item>

                    <n-button type="primary" @click="signIn">Sign In</n-button>
                </n-form>

                <div class="signup-switch">
                    Don't have an account?
                    <n-button text @click="goToSignUp">Sign Up</n-button>
                </div>
            </n-card>
        </div>
    </div>
</template>
  
<script setup>
import { NInput, NButton, NForm, NFormItem, NCard } from 'naive-ui'

</script>
  
<style scoped>
.auth-container {
    display: flex;
    height: 100vh;
    width: 100vw;
}

.left-panel {
    background-color: var(--dark);
    width: 40%;
    display: flex;
    align-items: center;
    justify-content: center;
}

.auth-icon {
    width: 160px;
    height: auto;
}

.right-panel {
    width: 60%;
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: white;
}
</style>
  