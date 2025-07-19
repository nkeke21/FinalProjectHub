<template>
</template>

<script setup lang="ts">
import { onMounted, onBeforeUnmount, ref } from 'vue'
import { useNotification } from 'naive-ui'
import SockJS from 'sockjs-client'
import { Stomp } from '@stomp/stompjs'

const notification = useNotification()

const getCurrentUserId = async (): Promise<string> => {
  try {
    const response = await fetch('http://localhost:8080/api/team-requests/current-user', {
      credentials: 'include'
    })
    if (response.ok) {
      const userId = await response.text()
      console.log('Got current user ID from backend:', userId)
      return userId
    }
  } catch (error) {
    console.error('Failed to get current user ID from backend:', error)
  }
  
  const userStr = localStorage.getItem('user')
  if (userStr) {
    try {
      const user = JSON.parse(userStr)
      console.log('Using user ID from localStorage:', user.id)
      return user.id || 'default-user-id'
    } catch (e) {
      console.error('Failed to parse user from localStorage:', e)
    }
  }
  return 'default-user-id'
}

let stompClient: any = null

onMounted(async () => {
  const socket = new SockJS('http://localhost:8080/ws')
  stompClient = Stomp.over(socket)

  stompClient.connect({}, async () => {
    console.log('Connected to WebSocket for team join requests')
    
    const userId = await getCurrentUserId()
    console.log('Subscribing to team join requests for user:', userId)
    
    stompClient.subscribe(`/topic/team-requests/${userId}`, (message: any) => {
      const data = JSON.parse(message.body)
      console.log('Received team join request notification:', data)

      notification.create({
        title: '🏈 New Team Join Request',
        content: data.message,
        meta: new Date().toLocaleString(),
        duration: 10000,
        type: 'info'
      })
    })
  }, (error: any) => {
    console.error('WebSocket connection error:', error)
  })
})

onBeforeUnmount(() => {
  if (stompClient) {
    stompClient.disconnect()
  }
})
</script> 