<template>
    <div class="account-container">
        <div class="topbar">
            <div class="topbar-center">
                <n-menu
                    mode="horizontal"
                    :options="menuOptions"
                    :default-value="selectedKey"
                    @update:value="handleMenuChange"
                    class="menu-bar"
                />
            </div>

            <div class="topbar-right">
                <n-button type="error" ghost @click="logout">
                    Logout
                </n-button>
            </div>
        </div>

        <div class="main-content">
            <component :is="currentComponent" />
        </div>
    </div>
</template>
  
<script setup lang="ts">
import { ref, computed } from 'vue'
import { NButton, NMenu, NTag } from 'naive-ui'

import AccountDetails from '@/components/profile/AccountDetails.vue'
import HostedEvents from '@/components/profile/HostedEvents.vue'
import RegisteredEvents from '@/components/profile/RegisteredEvents.vue'

const logout = () => {
  console.log('Logging out...')
}

const menuOptions = [
    { label: 'Hosted Events', key: 'hosted-events' },
    { label: 'Account Details', key: 'account-details' },
    { label: 'Registered Events', key: 'registered-events' },
]

const selectedKey = ref('hosted-events')

const handleMenuChange = (key: string) => {
  selectedKey.value = key
}

const currentComponent = computed(() => {
  switch (selectedKey.value) {
    case 'hosted-events':
      return HostedEvents
    case 'account-details':
      return AccountDetails
    case 'registered-events':
      return RegisteredEvents
    default:
      return AccountDetails
  }
})

</script>

<style scoped>
.account-container {
  display: flex;
  flex-direction: column;
  width: 100%;
  min-height: 100vh;
  background-color: #f8fafc;
}

.topbar {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: flex-end; 
  padding: 20px 32px;
  background-color: #f1f5f9;
  border-bottom: 1px solid #e2e8f0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.03);
  position: sticky;
  top: 0;
  z-index: 10;
}

.topbar-center {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
}

.menu-bar :deep(.n-menu-item-content) {
  font-weight: 500;
  font-size: 16px;
  padding: 10px 24px;
}

.topbar-right {
  display: flex;
  align-items: center;
}

.main-content {
  flex-grow: 1;
  padding: 48px 64px;
  background-color: #f7f9fc;
}
</style>
  