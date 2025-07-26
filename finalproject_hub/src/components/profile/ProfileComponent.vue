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
            <component :is="currentComponent" :is-own-profile="isOwnProfile" />
        </div>
    </div>
</template>
  
<script setup lang="ts">
import { ref, computed, h, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { NButton, NMenu, NTag } from 'naive-ui'
import { useUserStore } from '@/store/profile/userStore'
import { useMessage } from 'naive-ui'
import { useRoute } from 'vue-router'
import { AuthService } from '@/services/apis/AuthService'
import AccountDetails from '@/components/profile/AccountDetails.vue'
import HostedEvents from '@/components/profile/HostedEvents.vue'
import RegisteredEvents from '@/components/profile/RegisteredEvents.vue'

const userStore = useUserStore()
const message = useMessage()
const route = useRoute()
const router = useRouter()

const isOwnProfile = computed(() => {
    const userStr = localStorage.getItem('user')
    const loggedInUser = userStr ? JSON.parse(userStr) : null
    const currentUserId = loggedInUser?.id || ''
    
    if (!route.params.id) {
        return true
    }
    
    return currentUserId === route.params.id
})

const profileUserId = computed(() => {
    return route.params.id as string
})

const showAddFriendButton = ref(true)

const checkExistingFriendRequest = async () => {
    console.log('🔍 Checking friend request...')
    console.log('isOwnProfile.value:', isOwnProfile.value)
    console.log('profileUserId.value:', profileUserId.value)
    
    if (isOwnProfile.value) {
        console.log('✅ Own profile - hiding Add Friend button')
        showAddFriendButton.value = false
        return
    }

    try {
        const userStr = localStorage.getItem('user')
        const loggedInUser = userStr ? JSON.parse(userStr) : null
        const currentUserId = loggedInUser?.id || ''
        
        console.log('currentUserId:', currentUserId)
        console.log('profileUserId.value:', profileUserId.value)
        
        if (!currentUserId || !profileUserId.value) {
            console.log('❌ Missing user IDs - showing Add Friend button')
            showAddFriendButton.value = true
            return
        }

        console.log('🔍 Checking if friend request exists...')
        const existingRequest = await userStore.checkFriendRequest(currentUserId, profileUserId.value)
        console.log('existingRequest:', existingRequest)
        
        showAddFriendButton.value = !existingRequest
        console.log('showAddFriendButton after request check:', showAddFriendButton.value)
        
        if (showAddFriendButton.value) {
            console.log('🔍 Checking if already friends...')
            await userStore.fetchCurrentUserFriends()
            const isFriend = userStore.friends.includes(profileUserId.value)
            console.log('userStore.friends:', userStore.friends)
            console.log('isFriend:', isFriend)
            showAddFriendButton.value = !isFriend
            console.log('Final showAddFriendButton value:', showAddFriendButton.value)
        }
    } catch (error) {
        console.error('❌ Error checking friend request:', error)
        showAddFriendButton.value = true
    }
}

const handleMenuChange = async (key: string) => {
  if (key === 'add-friend') {
    if (!isOwnProfile.value) {
      try {
        const userStr = localStorage.getItem('user')
        const loggedInUser = userStr ? JSON.parse(userStr) : null
        const fromUserId = loggedInUser?.id || ''
        
        await userStore.sendFriendRequest({
          fromUserId: fromUserId,
          toUserId: profileUserId.value
        })
        message.success('Friend request sent!')
        
        showAddFriendButton.value = false
      } catch (err) {
        message.error('Failed to send friend request')
      }
    }
    return
  }

  selectedKey.value = key
}

const logout = async () => {
  try {
    // Use AuthService logout method (client-side only)
    AuthService.logout()
    localStorage.removeItem('user')
    message.success('Logged out successfully')
    router.push('/')
  } catch (error) {
    console.error('Logout error:', error)
    message.error('Logout failed')
    
    // Still remove user data and redirect even if there's an error
    localStorage.removeItem('user')
    router.push('/')
  }
}

const menuOptions = computed(() => {
  const baseOptions = [
    {
      key: 'hosted-events',
      label: () =>
        h('div', { class: 'menu-label' }, [
          h('span', { class: 'material-icons' }, 'event'),
          h('span', { class: 'menu-text' }, 'Hosted Events')
        ])
    },
    {
      key: 'account-details',
      label: () =>
        h('div', { class: 'menu-label' }, [
          h('span', { class: 'material-icons' }, 'account_circle'),
          h('span', { class: 'menu-text' }, 'Account Details')
        ])
    },
    {
      key: 'registered-events',
      label: () =>
        h('div', { class: 'menu-label' }, [
          h('span', { class: 'material-icons' }, 'assignment_turned_in'),
          h('span', { class: 'menu-text' }, 'Registered Events')
        ])
    }
  ]

  console.log('🔍 menuOptions computed - isOwnProfile:', isOwnProfile.value, 'showAddFriendButton:', showAddFriendButton.value)

  if (!isOwnProfile.value && showAddFriendButton.value) {
    console.log('✅ Adding Add Friend button to menu')
    baseOptions.push({
      key: 'add-friend',
      label: () =>
        h('div', { class: 'menu-label' }, [
          h('span', { class: 'material-icons' }, 'person_add'),
          h('span', { class: 'menu-text' }, 'Add Friend')
        ])
    })
  } else {
    console.log('❌ Not adding Add Friend button - isOwnProfile:', isOwnProfile.value, 'showAddFriendButton:', showAddFriendButton.value)
  }

  return baseOptions
})

const selectedKey = ref('hosted-events')

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

onMounted(() => {
  checkExistingFriendRequest()
})

watch(() => route.params.id, () => {
  checkExistingFriendRequest()
})

</script>

<style>
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

.menu-label {
  display: flex;
  align-items: center;
  gap: 4px;
}

.material-icons {
  font-size: 22px; 
  color: inherit; 
  margin-right: 0;
  vertical-align: middle;
  line-height: 1;
  transition: color 0.2s;
}

.menu-text {
  font-weight: 500;
  font-size: 16px;
}

.topbar-right {
  display: flex;
  align-items: center;
}

.main-content {
  flex-grow: 1;
  padding-top: 48px;
  background-color: #f7f9fc;
}
</style>
  