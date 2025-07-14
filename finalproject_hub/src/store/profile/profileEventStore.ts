import { defineStore } from 'pinia'
import { SportEvent } from '@/models/sportEvent'
import { getUserRegisteredEvents, getUserHostedEvents, getCurrentUserRegisteredEvents, getCurrentUserHostedEvents } from '@/services/apis/ProfileEventService'

export const useProfileEventStore = defineStore('profileEvent', {
    state: () => ({
        registeredEvents: [] as SportEvent[],
        hostedEvents: [] as SportEvent[],
        isLoading: false,
        error: null as string | null
    }),

    actions: {
        async fetchRegisteredEvents(userId: string) {
            this.isLoading = true;
            this.error = null;

            try {
                this.registeredEvents = await getUserRegisteredEvents(userId);
            } catch (err: any) {
                this.error = err.message;
            } finally {
                this.isLoading = false;
            }
        },

        async fetchCurrentUserRegisteredEvents() {
            this.isLoading = true;
            this.error = null;

            try {
                this.registeredEvents = await getCurrentUserRegisteredEvents();
            } catch (err: any) {
                this.error = err.message;
            } finally {
                this.isLoading = false;
            }
        },

        async fetchHostedEvents(userId: string) {
            this.isLoading = true;
            this.error = null;
    
            try {
                this.hostedEvents = await getUserHostedEvents(userId);
            } catch (err: any) {
                this.error = err.message;
            } finally {
                this.isLoading = false;
            }
        },

        async fetchCurrentUserHostedEvents() {
            this.isLoading = true;
            this.error = null;
    
            try {
                this.hostedEvents = await getCurrentUserHostedEvents();
            } catch (err: any) {
                this.error = err.message;
            } finally {
                this.isLoading = false;
            }
        }
    }
})
