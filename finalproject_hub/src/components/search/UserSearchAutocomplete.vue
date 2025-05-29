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
import { ref, defineEmits } from 'vue'
import { NAutoComplete } from 'naive-ui'

interface User {
    id: string
    name: string
    email: string
}

const searchQuery = ref('')
const userOptions = ref<{ label: string; value: string }[]>([])
const emit = defineEmits(['user-selected'])

const onSearch = async (query: string) => {
if (!query.trim()) {
    userOptions.value = []
    return
}

try {
    const response = await fetch(`/api/users/search?query=${encodeURIComponent(query)}`)
    if (response.ok) {
        const users: User[] = await response.json()

        userOptions.value = users.map(user => ({
            label: `${user.name} (${user.email})`,
            value: user.email
        }))
    } else {
        userOptions.value = []
    }
    } catch (error) {
        console.error('Error fetching users:', error)
        userOptions.value = []
    }
}

const onSelect = (selectedValue: string) => {
    emit('user-selected', selectedValue)
}
</script>
  