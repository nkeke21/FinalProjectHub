<template>
  <AddSportEventModal/>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue'
import AddSportEventModal from '../src/components/Dialog/AddSportEventModal.vue'

export default defineComponent({
  components: {
    AddSportEventModal
  },
})
</script>

<style scoped>

</style>
