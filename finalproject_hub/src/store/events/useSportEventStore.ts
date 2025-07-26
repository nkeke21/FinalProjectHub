import { defineStore } from 'pinia'
import { SportEvent } from '@/models/sportEvent'
import { createSportEvent, getSportEventById, updateSportEvent, getAllSportEvents, joinEvent, checkParticipation, removeParticipant, deleteEvent as deleteEventAPI } from '@/services/apis/SportEventService'

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
              id
            }
          }
      } catch (error) {
        console.error('❌ Failed to update event:', error)
      }
    },    

    async createEvent(event: SportEvent): Promise<string | null> {
      try {
        const response = await createSportEvent(event)
    
        if (!response.ok) {
          const errorData = await response.json()
          console.error('Error creating event: ', errorData)
          return null
        } else {
          const result = await response.json()
          console.log('✅ Event created: ', result)
          return result.eventId
        }
      } catch (error) {
        console.error('❌ Failed to send event: ', error)
        return null
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
      
          this.selectedEvent = await response.json()
        } catch (error) {
          console.error('Failed to fetch event:', error)
        }
    },
    
    async fetchAllEvents() {
      try {
        const response = await getAllSportEvents()
    
        if (!response.ok) {
          const errorData = await response.json()
          console.error('Error fetching all events:', errorData)
          return
        }
    
        this.events = await response.json()
      } catch (error) {
        console.error('Failed to fetch all events:', error)
      }
    },

    async joinEvent(id: string) {
      try {
        const response = await joinEvent(id)
        
        if (!response.ok) {
          const errorData = await response.json()
          console.error('Error joining event:', errorData)
          if (errorData.code === 'USER_AGE_NOT_IN_RANGE') {
            throw new Error('You do not meet the age requirement for this event.')
          }
          throw new Error(errorData.message || 'Failed to join event')
        }
        
        const updatedEvent = await response.json()
        console.log('✅ Joined event:', updatedEvent)
        
        if (this.selectedEvent && this.selectedEvent.eventId === id) {
          this.selectedEvent = updatedEvent
        }
        
        const eventIndex = this.events.findIndex(e => e.eventId === id)
        if (eventIndex !== -1) {
          this.events[eventIndex] = updatedEvent
        }
        
        return updatedEvent
      } catch (error) {
        console.error('❌ Failed to join event:', error)
        throw error
      }
    },

    async removeParticipant(eventId: string, participantId: string) {
      try {
        const response = await removeParticipant(eventId, participantId)
        
        if (!response.ok) {
          const errorData = await response.json()
          console.error('Error removing participant:', errorData)
          throw new Error(errorData.message || 'Failed to remove participant')
        }
        
        const updatedEvent = await response.json()
        console.log('✅ Removed participant from event:', updatedEvent)
        
        if (this.selectedEvent && this.selectedEvent.eventId === eventId) {
          this.selectedEvent = updatedEvent
        }
        
        const eventIndex = this.events.findIndex(e => e.eventId === eventId)
        if (eventIndex !== -1) {
          this.events[eventIndex] = updatedEvent
        }
        
        return updatedEvent
      } catch (error) {
        console.error('❌ Failed to remove participant:', error)
        throw error
      }
    },

    async checkParticipation(id: string): Promise<boolean> {
      try {
        const response = await checkParticipation(id)
        
        if (!response.ok) {
          const errorData = await response.json()
          console.error('Error checking participation:', errorData)
          return false
        }
        
        return await response.json()
      } catch (error) {
        console.error('❌ Failed to check participation:', error)
        return false
      }
    },

    async deleteEvent(id: string) {
      try {
        const response = await deleteEventAPI(id)
        
        if (!response.ok) {
          const errorData = await response.json()
          console.error('Error deleting event:', errorData)
          throw new Error(errorData.message || 'Failed to delete event')
        }
        
        console.log('✅ Event deleted:', id)
        
        if (this.selectedEvent && this.selectedEvent.eventId === id) {
          this.selectedEvent = null
        }
        
        const eventIndex = this.events.findIndex(e => e.eventId === id)
        if (eventIndex !== -1) {
          this.events.splice(eventIndex, 1)
        }
        
        return true
      } catch (error) {
        console.error('❌ Failed to delete event:', error)
        throw error
      }
    }
  },

  
  
  getters: {
  }
})
