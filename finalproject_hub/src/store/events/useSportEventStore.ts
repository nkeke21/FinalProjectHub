import { defineStore } from 'pinia'

export interface SportEvent {
  location: string
  participants: number
  ageRange: string
  eventTime: number | null
  sportType: string | null
}

export const useSportEventStore = defineStore('sportEvent', {
  state: () => ({
    events: [] as SportEvent[]
  }),
  actions: {
    addEvent(event: SportEvent) {
      this.events.push(event)
      console.log('Event added to store:', event)
    }
  },
  getters: {
    eventCount: (state) => state.events.length
  }
})
