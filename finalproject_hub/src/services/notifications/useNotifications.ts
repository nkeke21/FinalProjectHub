import { ref, onUnmounted } from 'vue'
import { Client, IMessage } from '@stomp/stompjs'
import SockJS from 'sockjs-client'

export function useNotifications(userId: string) {
    const notification = ref<any>(null)

    const stompClient = new Client({
        webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
        reconnectDelay: 5000,
        debug: (str) => console.log('[STOMP]', str),
    })

    stompClient.onConnect = () => {
        console.log('✅ Connected to STOMP')

        stompClient.subscribe(`/topic/user/${userId}`, (message: IMessage) => {
            notification.value = JSON.parse(message.body)
        })
    }

    stompClient.activate()

    onUnmounted(() => {
        stompClient.deactivate()
    })

    return { notification }
}
