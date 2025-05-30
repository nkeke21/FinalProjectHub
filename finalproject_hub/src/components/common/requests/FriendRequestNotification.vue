<template>
</template>

<script setup lang="ts">
import { onMounted, onBeforeUnmount, h } from 'vue'
import { useNotification } from 'naive-ui'
import SockJS from 'sockjs-client'
import { Stomp } from '@stomp/stompjs'

const notification = useNotification()

const loggedInUserId = '57ce9f60-5bcd-7b2d-9f04-edcba7654321'

let stompClient: any = null

onMounted(() => {
  const socket = new SockJS('http://localhost:8080/ws')
  stompClient = Stomp.over(socket)

  stompClient.connect({}, () => {
    stompClient.subscribe(`/topic/friend-requests/${loggedInUserId}`, (message: any) => {
      const data = JSON.parse(message.body)

      notification.create({
        title: '👥 New Friend Request',
        content: data.message,
        meta: new Date().toLocaleString(),
        duration: 10000,
        type: 'info'
      })
    })
  })
})

onBeforeUnmount(() => {
  if (stompClient) stompClient.disconnect()
})
</script>
