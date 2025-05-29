import { defineStore } from 'pinia'
import { getUserDetails, UserDetailsResponse, updateUserDetails } from '@/services/apis/ProfileDetailsService'
import { searchUsers, type UserSearchResult } from '@/services/apis/UserSearchService'
import { UserUpdateDTO } from '@/models/UserUpdateDTO'

export const useUserStore = defineStore('user', {
    state: () => ({
        profile: null as UserDetailsResponse | null,
        searchResults: [] as UserSearchResult[],
        isLoading: false,
        error: null as string | null
    }),

    actions: {
        async fetchProfile(userId: string) {
            this.isLoading = true
            this.error = null

            try {
                const result = await getUserDetails(userId)
                this.profile = result
            } catch (err: any) {
                this.error = err.message
            } finally {
                this.isLoading = false
            }
        },

        async updateProfile(userId: string, updateData: UserUpdateDTO) {
            this.isLoading = true
            this.error = null
      
            try {
                const updated = await updateUserDetails(userId, updateData)
                this.profile = { ...this.profile, ...updated }
            } catch (err: any) {
                this.error = err.message
                throw err
            } finally {
                this.isLoading = false
            }
        },

        async searchUsers(query: string) {
            this.isLoading = true
            this.error = null

            try {
                this.searchResults = await searchUsers(query)
            } catch (err: any) {
                this.error = err.message
                this.searchResults = []
            } finally {
                this.isLoading = false
            }
        }
    }
})
