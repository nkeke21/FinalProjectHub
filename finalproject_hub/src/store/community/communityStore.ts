import { defineStore } from 'pinia'
import { 
  getCommunityMembers, 
  searchCommunityMembers, 
  getCommunityStats,
  type CommunityMember,
  type CommunityStats
} from '../../services/apis/CommunityService'

export const useCommunityStore = defineStore('community', {
  state: () => ({
    members: [] as CommunityMember[],
    searchResults: [] as CommunityMember[],
    stats: null as CommunityStats | null,
    isLoading: false,
    error: null as string | null,
    searchQuery: '',
    ageFilter: null as string | null
  }),

  getters: {
    filteredMembers: (state) => {
      let filtered = state.members

      if (state.searchQuery) {
        filtered = filtered.filter((member: CommunityMember) => 
          member.fullName.toLowerCase().includes(state.searchQuery.toLowerCase()) ||
          member.email.toLowerCase().includes(state.searchQuery.toLowerCase())
        )
      }

      if (state.ageFilter) {
        const [minAge, maxAge] = state.ageFilter.split('-').map(Number)
        if (maxAge) {
          filtered = filtered.filter((member: CommunityMember) => member.age >= minAge && member.age <= maxAge)
        } else {
          filtered = filtered.filter((member: CommunityMember) => member.age >= minAge)
        }
      }

      return filtered
    },

    totalMembers: (state) => state.members.length,
    totalFriends: (state) => state.stats?.totalFriends || 0
  },

  actions: {
    async fetchCommunityMembers() {
      this.isLoading = true
      this.error = null

      try {
        this.members = await getCommunityMembers()
      } catch (err: any) {
        this.error = err.message
        this.members = []
      } finally {
        this.isLoading = false
      }
    },

    async searchMembers(query: string) {
      this.isLoading = true
      this.error = null

      try {
        this.searchResults = await searchCommunityMembers(query)
      } catch (err: any) {
        this.error = err.message
        this.searchResults = []
      } finally {
        this.isLoading = false
      }
    },

    async fetchCommunityStats() {
      this.isLoading = true
      this.error = null

      try {
        this.stats = await getCommunityStats()
      } catch (err: any) {
        this.error = err.message
        this.stats = null
      } finally {
        this.isLoading = false
      }
    },

    setSearchQuery(query: string) {
      this.searchQuery = query
    },

    setAgeFilter(filter: string | null) {
      this.ageFilter = filter
    },

    clearFilters() {
      this.searchQuery = ''
      this.ageFilter = null
    }
  }
}) 