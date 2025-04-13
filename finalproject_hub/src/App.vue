<template>
  <SportEventDetail/>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue'
import SportEventDetail from './components/events/Details/SportEventDetail.vue';

export default defineComponent({
  components: {
    SportEventDetail
  },
})
</script>

<style scoped>

</style>
