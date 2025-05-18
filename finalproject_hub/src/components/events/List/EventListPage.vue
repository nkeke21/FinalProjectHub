<template>
    <EventListTable :events="events" />
</template>

<script setup lang="ts">
import EventListTable from '@/components/events/List/EventListTable.vue'
import events from '@/components/events/List/events.json'
</script>