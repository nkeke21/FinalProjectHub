<template>
  <div class="community-page">
    <h2>👥 Community</h2>
    <n-data-table
      :columns="columns"
      :data="paginatedFriends"
      size="large"
      :row-props="rowProps"
    />
    <n-pagination
      v-model:page="currentPage"
      v-model:page-size="pageSize"
      :item-count="friends.length"
      show-size-picker
      :page-sizes="[5, 10, 20]"
      @update:page="handlePageChange"
      @update:page-size="handlePageSizeChange"
      style="margin-top: 16px; display: flex; justify-content: center;"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { NDataTable, NPagination } from 'naive-ui'
import { useRouter } from 'vue-router'

const router = useRouter()

const columns = [
  { title: 'Full Name', key: 'fullName' },
  { title: 'Email', key: 'email' },
  { title: 'Phone Number', key: 'phone' },
  { title: 'Age', key: 'age' }
]

const friends = ref([
  { id: '1', fullName: 'Alice Johnson', email: 'alice@example.com', phone: '123-456-7890', age: 25 },
  { id: '2', fullName: 'Bob Smith', email: 'bob@example.com', phone: '234-567-8901', age: 28 },
  { id: '3', fullName: 'Charlie Brown', email: 'charlie@example.com', phone: '345-678-9012', age: 22 },
  { id: '4', fullName: 'Diana Prince', email: 'diana@example.com', phone: '456-789-0123', age: 30 },
  { id: '5', fullName: 'Eve Adams', email: 'eve@example.com', phone: '567-890-1234', age: 27 },
  { id: '6', fullName: 'Frank Miller', email: 'frank@example.com', phone: '678-901-2345', age: 31 },
  { id: '7', fullName: 'Grace Lee', email: 'grace@example.com', phone: '789-012-3456', age: 24 },
  { id: '8', fullName: 'Henry Ford', email: 'henry@example.com', phone: '890-123-4567', age: 29 }
])

const currentPage = ref(1)
const pageSize = ref(5)

const paginatedFriends = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return friends.value.slice(start, end)
})

const rowProps = (row: any) => ({
  style: 'cursor: pointer;',
  onClick: () => {
    router.push(`/profile/${row.id}`)
  }
})

const handlePageChange = (page: number) => {
  currentPage.value = page
}

const handlePageSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
}
</script>

<style scoped>
.community-page {
  width: 80%;
  margin: 40px auto;
  padding: 24px;
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.06);
}
h2 {
  margin-bottom: 24px;
  color: #333;
}
</style> 