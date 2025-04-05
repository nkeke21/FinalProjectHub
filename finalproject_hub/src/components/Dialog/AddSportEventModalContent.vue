<template>
    <n-form :model="formValue" label-placement="top">
    <n-form-item label="Location">
        <n-input v-model:value="formValue.location" placeholder="e.g. Central Park" />
    </n-form-item>

    <n-form-item label="Number of Participants">
        <n-input-number v-model:value="formValue.participants" :min="1" />
    </n-form-item>

    <n-form-item label="Age Range">
        <n-input v-model:value="formValue.ageRange" placeholder="e.g. 18-30" />
    </n-form-item>

    <n-form-item label="Event Time">
        <n-date-picker v-model:value="formValue.eventTime" type="datetime" />
    </n-form-item>

    <n-form-item label="Sport Type">
        <n-select
            v-model:value="formValue.sportType"
            :options="sportOptions"
            placeholder="Select sport"
        />
    </n-form-item>
    <n-form-item>
        <div style="width: 100%; text-align: right;">
            <n-button type="primary" @click="handleSubmit" color="orange">Add Event</n-button>
        </div>
    </n-form-item>
    </n-form>
</template>
  
<script setup lang="ts">
import { reactive } from 'vue'
import {
  NForm,
  NFormItem,
  NInput,
  NInputNumber,
  NDatePicker,
  NSelect,
  NButton
} from 'naive-ui'

const emit = defineEmits(['submit'])

const formValue = reactive({
  location: '',
  participants: 1,
  ageRange: '',
  eventTime: null as null | number,
  sportType: null as null | string
})

const sportOptions = [
  { label: 'Football', value: 'football' },
  { label: 'Basketball', value: 'basketball' },
  { label: 'Tennis', value: 'tennis' },
  { label: 'Running', value: 'running' }
]

const handleSubmit = () => {
    emit('submit', { ...formValue })
}
</script>


<style>

</style>
