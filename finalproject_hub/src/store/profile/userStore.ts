import { defineStore } from 'pinia'
import { getUserDetails, UserDetailsResponse, updateUserDetails, getCurrentUserDetails, updateCurrentUserDetails } from '@/services/apis/ProfileDetailsService'
import { searchUsers, type UserSearchResult } from '@/services/apis/UserSearchService'
import { 
    sendFriendRequest, 
    FriendRequestPayload, 
    getPendingFriendRequests,
    getCurrentUserPendingFriendRequests,
    respondToFriendRequest,
    getFriends,
    getCurrentUserFriends,
    deleteFriend,
    checkFriendRequestAPI,
    type FriendRequest
} from '@/services/apis/FriendRequestService'
import { UserUpdateDTO } from '@/models/UserUpdateDTO'

export const useUserStore = defineStore('user', {
    state: () => ({
        profile: null as UserDetailsResponse | null,
        searchResults: [] as UserSearchResult[],
        pendingFriendRequests: [] as FriendRequest[],
        friends: [] as string[],
        existingFriendRequest: null as FriendRequest | null,
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

        async fetchCurrentUserProfile() {
            this.isLoading = true
            this.error = null

            try {
                const result = await getCurrentUserDetails()
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

        async updateCurrentUserProfile(updateData: UserUpdateDTO) {
            this.isLoading = true
            this.error = null
      
            try {
                const updated = await updateCurrentUserDetails(updateData)
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
        },

        async sendFriendRequest(payload: FriendRequestPayload) {
            this.isLoading = true
            this.error = null
            try {
                await sendFriendRequest(payload)
            } catch (err: any) {
                this.error = err.message
                throw err
            } finally {
                this.isLoading = false
            }
        },

        async fetchPendingFriendRequests(userId: string) {
            this.isLoading = true
            this.error = null

            try {
                this.pendingFriendRequests = await getPendingFriendRequests(userId)
            } catch (err: any) {
                this.error = err.message
                this.pendingFriendRequests = []
            } finally {
                this.isLoading = false
            }
        },

        async fetchCurrentUserPendingFriendRequests() {
            this.isLoading = true
            this.error = null

            try {
                console.log('Fetching current user pending friend requests...')
                this.pendingFriendRequests = await getCurrentUserPendingFriendRequests()
                console.log('Received pending friend requests:', this.pendingFriendRequests)
            } catch (err: any) {
                console.error('Error fetching pending friend requests:', err)
                this.error = err.message
                this.pendingFriendRequests = []
            } finally {
                this.isLoading = false
            }
        },

        async respondToFriendRequest(requestId: string, status: 'ACCEPTED' | 'REJECTED') {
            this.isLoading = true
            this.error = null

            try {
                const updatedRequest = await respondToFriendRequest(requestId, status)
                
                const requestIndex = this.pendingFriendRequests.findIndex(
                    (request: FriendRequest) => request.requestId === requestId
                )
                
                if (requestIndex !== -1) {
                    this.pendingFriendRequests[requestIndex] = updatedRequest
                }
                
                if (status === 'ACCEPTED') {
                    await this.fetchCurrentUserFriends()
                }
                
                return updatedRequest
            } catch (err: any) {
                this.error = err.message
                throw err
            } finally {
                this.isLoading = false
            }
        },

        async fetchFriends(userId: string) {
            this.isLoading = true
            this.error = null

            try {
                this.friends = await getFriends(userId)
            } catch (err: any) {
                this.error = err.message
                this.friends = []
            } finally {
                this.isLoading = false
            }
        },

        async fetchCurrentUserFriends() {
            this.isLoading = true
            this.error = null

            try {
                this.friends = await getCurrentUserFriends()
            } catch (err: any) {
                this.error = err.message
                this.friends = []
            } finally {
                this.isLoading = false
            }
        },

        async removeFriend(userId: string, friendId: string) {
            this.isLoading = true
            this.error = null

            try {
                await deleteFriend(userId, friendId)
                this.friends = this.friends.filter(id => id !== friendId)
            } catch (err: any) {
                this.error = err.message
                throw err
            } finally {
                this.isLoading = false
            }
        },

        async checkFriendRequest(senderId: string, receiverId: string) {
            this.isLoading = true
            this.error = null

            try {
                this.existingFriendRequest = await checkFriendRequestAPI(senderId, receiverId)
                return this.existingFriendRequest
            } catch (err: any) {
                this.error = err.message
                this.existingFriendRequest = null
                return null
            } finally {
                this.isLoading = false
            }
        }
    }
})
