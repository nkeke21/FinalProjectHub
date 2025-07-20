<template>
</template>

<script setup lang="ts">
import { onMounted, onBeforeUnmount } from 'vue'
import { useNotification, useMessage } from 'naive-ui'
import SockJS from 'sockjs-client'
import { Stomp } from '@stomp/stompjs'
import { EventInvitationService, type EventInvitationResponseDTO } from '@/services/apis/EventInvitationService'

const notification = useNotification()
const message = useMessage()

const getLoggedInUserId = (): string | null => {
  const userStr = localStorage.getItem('user')
  if (!userStr) return null
  try {
    return JSON.parse(userStr).id
  } catch {
    return null
  }
}

let stompClient: any = null

const handleEventInvitation = (data: any) => {
  notification.create({
    title: '🎉 Event Invitation',
    content: data.message,
    meta: new Date().toLocaleString(),
    duration: 15000,
    type: 'info',
    action: () => {
      window.location.href = `/events/${data.eventId}`
    },
    onClose: () => {
    }
  })
}

const connectWebSocket = () => {
  const userId = getLoggedInUserId()
  if (!userId) {
    console.warn('No logged in user found for event invitation notifications')
    return
  }

  const socket = new SockJS('http://localhost:8080/ws')
  stompClient = Stomp.over(socket)

  stompClient.connect({}, () => {
    console.log('Connected to event invitation WebSocket')
    
    stompClient.subscribe(`/topic/event-invitations/${userId}`, (message: any) => {
      try {
        const data = JSON.parse(message.body)
        handleEventInvitation(data)
      } catch (error) {
        console.error('Error parsing event invitation notification:', error)
      }
    })
  }, (error: any) => {
    console.error('WebSocket connection error:', error)
  })
}

const disconnectWebSocket = () => {
  if (stompClient) {
    stompClient.disconnect()
    console.log('Disconnected from event invitation WebSocket')
  }
}

onMounted(() => {
  connectWebSocket()
})

onBeforeUnmount(() => {
  disconnectWebSocket()
})
</script> 