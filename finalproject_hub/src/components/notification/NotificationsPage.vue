<template>
    <div class="notifications-page">
        <h1>📬 Notifications</h1>

        <div v-if="notification" class="notification-card">
            <p><strong>Message:</strong> {{ notification.message }}</p>
            <p><strong>Event ID:</strong> {{ notification.eventId }}</p>
            <p><strong>Sender:</strong> {{ notification.senderUserId }}</p>
        </div>

        <div v-else class="no-notifications">
            <p>No notifications yet. Waiting...</p>
        </div>
    </div>
</template>
  
<script setup lang="ts">
import { useNotifications } from '@/services/notifications/useNotifications'

const { notification } = useNotifications('456')
</script>
  
<style scoped>
.notifications-page {
    max-width: 600px;
    margin: 60px auto;
    padding: 24px;
    background: #fff;
    box-shadow: 0 2px 10px rgba(0,0,0,0.05);
    border-radius: 8px;
    font-family: sans-serif;
}

.notification-card {
    background: #f9f9f9;
    padding: 16px;
    border: 1px solid #ddd;
    border-radius: 6px;
}

.no-notifications {
    font-style: italic;
    color: #999;
    text-align: center;
}
</style>
