<template>
    <n-form :model="formValue" label-placement="top">
    <n-form-item label="Location">
        <LocationSearch 
            v-model="formValue.location"
            @location-selected="onLocationSelected"
        />
        <div v-if="locationError" style="color: red; margin-top: 6px;">
            {{ locationError }}
        </div>
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
    <n-form-item label="Description">
    <n-input
        v-model:value="formValue.description"
        type="textarea"
        placeholder="e.g. Hosted by John, bring water, etc."
        :autosize="{ minRows: 3, maxRows: 6 }"
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
import { reactive, ref } from 'vue'
import {
  NForm,
  NFormItem,
  NInput,
  NInputNumber,
  NDatePicker,
  NSelect,
  NButton
} from 'naive-ui'
import LocationSearch from '../GMaps/LocationSearch.vue'

const emit = defineEmits(['submit'])
const locationError = ref('')

const formValue = reactive({
  locationLat: 0,
  locationLng: 0,
  participants: 1,
  ageRange: '',
  eventTime: null as null | number,
  sportType: null as null | string,
  description: ''
})

const sportOptions = [
  { label: 'Football', value: 'football' },
  { label: 'Basketball', value: 'basketball' },
  { label: 'Tennis', value: 'tennis' },
  { label: 'Running', value: 'running' }
]

const onLocationSelected = ({ lat, lng }: { lat: number, lng: number }) => {
  formValue.locationLat = lat
  formValue.locationLng = lng
  locationError.value = ''
}

const handleSubmit = () => {
  if (!formValue.locationLat || !formValue.locationLng) {
    locationError.value = 'Please select a valid location.'
    return
  }

  emit('submit', { ...formValue })
}

</script>


<style>

</style>
