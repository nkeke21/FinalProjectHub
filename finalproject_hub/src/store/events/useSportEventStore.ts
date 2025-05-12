import { defineStore } from 'pinia'
import { SportEvent } from '@/models/sportEvent'
import { createSportEvent, getSportEventById, updateSportEvent } from '@/services/apis/SportEventService'

export const useSportEventStore = defineStore('sportEvent', {
  state: () => ({
    events: [] as SportEvent[],
    selectedEvent: null as SportEvent | null
  }),
  
  actions: {
    addEvent(event: SportEvent) {
      this.events.push(event)
      console.log('Event added to store:', event)
    },

    async updateEvent(id: string, updatedEvent: SportEvent) {
      try {
        const response = await updateSportEvent(id, updatedEvent)
    
        if (!response.ok) {
          const errorData = await response.json()
          console.error('Error updating event:', errorData)
        } else {
          const result = await response.json()
          console.log('✅ Event updated:', result)
    
          this.selectedEvent = {
            ...updatedEvent,
            id // Optional: preserve ID in selectedEvent
          }
        }
      } catch (error) {
        console.error('❌ Failed to update event:', error)
      }
    },    

    async createEvent(event: SportEvent) {
      try {
        const response = await createSportEvent(event)

        if(!response.ok){
          const errorData = await response.json()
          console.error('Error creating event: ', errorData)
        } else {
          const result = await response.json()
          console.log('Event created: ', result)
        }
      } catch (error) {
        console.error('Failed to send event: ', error)
      }
    },

    async fetchEventById(id: string) {
      try {
        const response = await getSportEventById(id)
        if (!response.ok) {
          const errorData = await response.json()
          console.error('Error fetching event:', errorData)
          return
        }
    
        const eventData = await response.json()
    
        this.selectedEvent = {
          id: eventData.eventId,
          ageRange: eventData.ageRange,
          description: eventData.description,
          date: new Date(eventData.eventTime), 
          locationLat: eventData.latitude,
          locationLng: eventData.longitude,
          total: eventData.numberOfParticipants,
          joined: 0, 
          sportType: eventData.sportType,
          location: 'Unknown', 
          host: 'Unknown', 
          participantsList: [] 
        }
      } catch (error) {
        console.error('Failed to fetch event:', error)
      }
    }    
  },

  
  
  getters: {
  }
})
