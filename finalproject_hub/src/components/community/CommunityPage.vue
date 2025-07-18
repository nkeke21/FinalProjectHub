<template>
  <div class="community-page">
    <div class="community-header">
      <div class="header-content">
        <div class="header-title">
          <n-icon size="32" color="#64748b">
            <PeopleOutline />
          </n-icon>
          <h1>Community</h1>
        </div>
        <p class="header-subtitle">Connect with fellow sports enthusiasts</p>
      </div>
      
      <div class="stats-grid">
        <div class="stat-card">
          <div class="stat-icon">
            <n-icon size="24" color="#64748b">
              <PersonOutline />
            </n-icon>
          </div>
          <div class="stat-content">
            <div class="stat-number">{{ friends.length }}</div>
            <div class="stat-label">Total Friends</div>
          </div>
        </div>
      </div>
    </div>

    <div class="search-section">
      <div class="search-container">
        <n-input
          v-model:value="searchQuery"
          placeholder="Search community members..."
          class="search-input"
        >
          <template #prefix>
            <n-icon size="18" color="#64748b">
              <SearchOutline />
            </n-icon>
          </template>
        </n-input>
        
        <n-select
          v-model:value="ageFilter"
          :options="ageOptions"
          placeholder="Filter by age"
          class="filter-select"
        />
      </div>
    </div>

    <!-- Loading State -->
    <div v-if="isLoading" class="loading-state">
      <n-spin size="large" />
      <p>Loading your friends...</p>
    </div>

    <div v-else-if="error" class="error-state">
      <n-icon size="64" color="#ef4444">
        <PersonOutline />
      </n-icon>
      <h3>Error loading friends</h3>
      <p>{{ error }}</p>
      <n-button type="primary" @click="loadFriends">
        Try Again
      </n-button>
    </div>

    <div v-else class="members-grid">
      <div 
        v-for="member in filteredMembers" 
        :key="member.id"
        class="member-card"
        @click="router.push(`/profile/${member.id}`)"
      >
        <div class="member-avatar">
          {{ member.fullName.charAt(0).toUpperCase() }}
        </div>
        
        <div class="member-info">
          <h3 class="member-name">{{ member.fullName }}</h3>
          <div class="member-details">
            <div class="detail-item">
              <n-icon size="14" color="#64748b">
                <MailOutline />
              </n-icon>
              <span>{{ member.email }}</span>
            </div>
            <div class="detail-item">
              <n-icon size="14" color="#64748b">
                <CallOutline />
              </n-icon>
              <span>{{ member.phone }}</span>
            </div>
            <div class="detail-item">
              <n-icon size="14" color="#64748b">
                <PersonOutline />
              </n-icon>
              <span>{{ member.age }} years old</span>
            </div>
          </div>
        </div>
        
        <div class="member-actions">
          <n-button 
            type="default" 
            size="small"
            class="view-profile-btn"
            @click.stop="router.push(`/profile/${member.id}`)"
          >
            View Profile
          </n-button>
        </div>
      </div>
    </div>

    <div v-if="filteredMembers.length === 0" class="empty-state">
      <n-icon size="64" color="#cbd5e1">
        <PeopleOutline />
      </n-icon>
      <h3>No members found</h3>
      <p>{{ searchQuery ? `No members match "${searchQuery}"` : 'No community members available' }}</p>
    </div>

    <div class="pagination-wrapper">
      <n-pagination
        v-model:page="currentPage"
        v-model:page-size="pageSize"
        :item-count="filteredMembers.length"
        show-size-picker
        :page-sizes="[6, 12, 24]"
        @update:page="handlePageChange"
        @update:page-size="handlePageSizeChange"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { 
  NDataTable, 
  NPagination, 
  NInput, 
  NSelect, 
  NButton, 
  NIcon,
  NSpin,
  useMessage
} from 'naive-ui'
import { 
  PeopleOutline,
  PersonOutline,
  FootballOutline,
  LocationOutline,
  SearchOutline,
  MailOutline,
  CallOutline
} from '@vicons/ionicons5'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/profile/userStore'
import { storeToRefs } from 'pinia'

const router = useRouter()
const userStore = useUserStore()
const message = useMessage()

const { friends, isLoading, error } = storeToRefs(userStore)

const friendsWithDetails = ref([
  { id: '1', fullName: 'Alice Johnson', email: 'alice@example.com', phone: '+995 123-456-789', age: 25 },
  { id: '2', fullName: 'Bob Smith', email: 'bob@example.com', phone: '+995 234-567-890', age: 28 },
  { id: '3', fullName: 'Charlie Brown', email: 'charlie@example.com', phone: '+995 345-678-901', age: 22 },
  { id: '4', fullName: 'Diana Prince', email: 'diana@example.com', phone: '+995 456-789-012', age: 30 },
  { id: '5', fullName: 'Eve Adams', email: 'eve@example.com', phone: '+995 567-890-123', age: 27 },
  { id: '6', fullName: 'Frank Miller', email: 'frank@example.com', phone: '+995 678-901-234', age: 31 },
  { id: '7', fullName: 'Grace Lee', email: 'grace@example.com', phone: '+995 789-012-345', age: 24 },
  { id: '8', fullName: 'Henry Ford', email: 'henry@example.com', phone: '+995 890-123-456', age: 29 },
  { id: '9', fullName: 'Iris Wilson', email: 'iris@example.com', phone: '+995 901-234-567', age: 26 },
  { id: '10', fullName: 'Jack Davis', email: 'jack@example.com', phone: '+995 012-345-678', age: 33 },
  { id: '11', fullName: 'Kate Martinez', email: 'kate@example.com', phone: '+995 123-456-789', age: 23 },
  { id: '12', fullName: 'Liam Thompson', email: 'liam@example.com', phone: '+995 234-567-890', age: 35 }
])

const currentPage = ref(1)
const pageSize = ref(6)
const searchQuery = ref('')
const ageFilter = ref(null)

const ageOptions = [
  { label: 'All Ages', value: null },
  { label: '18-25', value: '18-25' },
  { label: '26-35', value: '26-35' },
  { label: '36+', value: '36+' }
]

const filteredMembers = computed(() => {
  // For now, we'll use mock data since we only have friend IDs from backend
  // In a real app, you'd fetch user details for each friend ID
  let filtered = friendsWithDetails.value

  if (searchQuery.value) {
    filtered = filtered.filter(member => 
      member.fullName.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      member.email.toLowerCase().includes(searchQuery.value.toLowerCase())
    )
  }

  if (ageFilter.value) {
    const [minAge, maxAge] = ageFilter.value.split('-').map(Number)
    if (maxAge) {
      filtered = filtered.filter(member => member.age >= minAge && member.age <= maxAge)
    } else {
      filtered = filtered.filter(member => member.age >= minAge)
    }
  }

  return filtered
})

const paginatedMembers = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filteredMembers.value.slice(start, end)
})

const handlePageChange = (page: number) => {
  currentPage.value = page
}

const handlePageSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
}

const loadFriends = async () => {
  try {
    await userStore.fetchCurrentUserFriends()
  } catch (error) {
    console.error('Failed to load friends:', error)
  }
}

onMounted(() => {
  loadFriends()
})
</script>

<style scoped>
.community-page {
  width: 90%;
  margin: 0 auto;
  padding: 2rem;
  min-height: 100vh;
}

.community-header {
  background: white;
  color: #1e293b;
  padding: 3rem 2rem;
  border-radius: 1rem;
  margin-bottom: 2rem;
  text-align: center;
  border: 1px solid #e2e8f0;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
}

.header-content {
  margin-bottom: 2rem;
}

.header-title {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 1rem;
  margin-bottom: 0.5rem;
}

.header-title h1 {
  font-size: 2.5rem;
  font-weight: 700;
  margin: 0;
}

.header-subtitle {
  font-size: 1.125rem;
  opacity: 0.9;
  margin: 0;
}

.stats-grid {
  display: flex;
  justify-content: center;
  max-width: 400px;
  margin: 0 auto;
}

.stat-card {
  background: white;
  padding: 1.5rem;
  border-radius: 1rem;
  display: flex;
  align-items: center;
  gap: 1rem;
  border: 1px solid #e2e8f0;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
}

.stat-icon {
  background: #f8fafc;
  padding: 0.75rem;
  border-radius: 0.75rem;
}

.stat-content {
  flex: 1;
}

.stat-number {
  font-size: 1.5rem;
  font-weight: 700;
  margin-bottom: 0.25rem;
}

.stat-label {
  font-size: 0.875rem;
  opacity: 0.9;
}

.search-section {
  background: white;
  padding: 1.5rem;
  border-radius: 1rem;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
  margin-bottom: 2rem;
  border: 1px solid #e2e8f0;
}

.search-container {
  display: flex;
  gap: 1rem;
  align-items: center;
}

.search-input {
  flex: 1;
}

.filter-select {
  width: 200px;
}

.loading-state {
  text-align: center;
  padding: 4rem 2rem;
  color: #64748b;
}

.loading-state p {
  margin-top: 1rem;
  font-size: 1.125rem;
}

.error-state {
  text-align: center;
  padding: 4rem 2rem;
  color: #64748b;
  background: white;
  border-radius: 1rem;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
}

.error-state h3 {
  margin: 1rem 0 0.5rem 0;
  color: #1e293b;
}

.error-state p {
  margin-bottom: 1.5rem;
}

.members-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.member-card {
  background: white;
  border-radius: 1rem;
  padding: 1.5rem;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
  border: 1px solid #e2e8f0;
  transition: all 0.3s ease;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-bottom: 1rem;
  min-width: 0;
}

.member-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 10px 25px -3px rgba(0, 0, 0, 0.1);
}

.member-avatar {
  width: 4rem;
  height: 4rem;
  background: linear-gradient(135deg, #f1f5f9 0%, #e2e8f0 100%);
  color: #64748b;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 1.25rem;
  flex-shrink: 0;
}

.member-info {
  flex: 1;
  min-width: 0;
}

.member-name {
  font-size: 1.125rem;
  font-weight: 600;
  color: #1e293b;
  margin: 0 0 0.75rem 0;
}

.member-details {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.detail-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.875rem;
  color: #64748b;
}

.member-actions {
  flex-shrink: 0;
  min-width: 120px;
}

.view-profile-btn {
  font-weight: 600;
  border-radius: 0.75rem;
  white-space: nowrap;
}

.empty-state {
  text-align: center;
  padding: 4rem 2rem;
  color: #64748b;
}

.empty-state h3 {
  font-size: 1.5rem;
  font-weight: 600;
  margin: 1rem 0 0.5rem;
  color: #1e293b;
}

.empty-state p {
  font-size: 1rem;
  color: #64748b;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 2rem;
}

@media (max-width: 768px) {
  .community-page {
    padding: 1rem;
  }
  
  .community-header {
    padding: 2rem 1rem;
  }
  
  .header-title h1 {
    font-size: 2rem;
  }
  
  .stats-grid {
    grid-template-columns: 1fr;
  }
  
  .search-container {
    flex-direction: column;
  }
  
  .filter-select {
    width: 100%;
  }
  
  .members-grid {
    grid-template-columns: 1fr;
    gap: 1rem;
  }
  
  .member-card {
    flex-direction: column;
    text-align: center;
    padding: 1rem;
    gap: 0.75rem;
    align-items: center;
  }
  
  .member-info {
    width: 100%;
    text-align: center;
  }
  
  .member-actions {
    width: 100%;
    display: flex;
    justify-content: center;
    margin-top: 0.5rem;
  }
  
  .view-profile-btn {
    width: 100%;
    max-width: 200px;
  }
}

@media (max-width: 480px) {
  .member-card {
    padding: 0.75rem;
  }
  
  .member-avatar {
    width: 3rem;
    height: 3rem;
    font-size: 1rem;
  }
  
  .member-name {
    font-size: 1rem;
  }
  
  .detail-item {
    font-size: 0.8rem;
  }
}
</style> 