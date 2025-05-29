<template>
    <n-auto-complete
      v-model:value="searchQuery"
      :options="userOptions"
      placeholder="Search users"
      clearable
      @update:value="onSearch"
      @select="onSelect"
    />
  </template>
  
<script setup lang="ts">
import { ref, computed, defineEmits } from 'vue'
import { NAutoComplete } from 'naive-ui'
import { useUserStore } from '@/store/profile/userStore'

const searchQuery = ref('')
const userStore = useUserStore()
const emit = defineEmits(['user-selected'])

const userOptions = computed(() =>
    userStore.searchResults.map(user => ({
        label: `${user.name} (${user.email})`,
        value: user.email
    }))
)

const onSearch = async (query: string) => {
    if (!query.trim()) {
        userStore.searchResults = []
        return
    }

    await userStore.searchUsers(query)
}

const onSelect = (selectedValue: string) => {
    emit('user-selected', selectedValue)
}
</script>
  