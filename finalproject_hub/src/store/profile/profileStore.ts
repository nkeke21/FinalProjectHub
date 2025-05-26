import { defineStore } from 'pinia'
import { getUserDetails, UserDetailsResponse, updateUserDetails } from '@/services/apis/ProfileDetailsService'
import { UserUpdateDTO } from '@/models/UserUpdateDTO'

export const useProfileStore = defineStore('profile', {
    state: () => ({
        profile: null as UserDetailsResponse | null,
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
        }
    }
})
