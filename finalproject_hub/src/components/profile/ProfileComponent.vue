<template>
    <div class="account-container">
        <!-- Sidebar -->
        <div class="sidebar">
            <n-menu
                :options="menuOptions"
                :default-value="selectedKey"
                @update:value="handleMenuChange"
            />
            <n-button block type="error" ghost class="logout-button">Logout</n-button>
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
    width: 100%;
    min-height: 100vh;
}

.sidebar {
    width: 250px;
    padding: 20px;
    border-right: 1px solid #eee;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
}

.logout-button {
    margin-top: auto;
    margin-top: 40px;
}

.main-content {
    flex-grow: 1;
    padding: 40px;
}

.current-sub {
    margin: 12px 0 4px;
    font-weight: bold;
}

.price-text {
    margin: 0 0 30px;
    color: #888;
}

.plan-section {
    display: flex;
    gap: 20px;
}
</style>
  