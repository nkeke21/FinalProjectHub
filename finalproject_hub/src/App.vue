<template>
  <EventListTable/>
</template>

<script setup lang="ts">
import { defineComponent, ref } from 'vue'
import AddSportEventModal from "./components/events/Dialog/AddSportEventModal.vue";
import SportEventDetail from './components/events/Details/SportEventDetail.vue';
import EventListTable from './components/events/List/EventListTable.vue';
</script>

<style scoped>

</style>
