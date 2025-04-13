import { defineStore } from 'pinia'

export interface SportEvent {
  locationLat: number | null
  locationLng: number | null
  participants: number
  ageRange: string
  eventTime: number | null
  sportType: string | null
  description: string | null
}

export const useSportEventStore = defineStore('sportEvent', {
  state: () => ({
    events: [] as SportEvent[]
  }),
  
  actions: {
    addEvent(event: SportEvent) {
      this.events.push(event)
      console.log('Event added to store:', event)
    },

    updateEvent(updatedEvent: Partial<SportEvent>) {
      if (updatedEvent) {
        console.log('Event updated:', updatedEvent)
      } else {
        console.warn(`Event with id ${updatedEvent} not found`)
      }
    }
  },
  
  getters: {
  }
})
