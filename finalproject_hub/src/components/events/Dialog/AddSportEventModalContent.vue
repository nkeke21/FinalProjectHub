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
        <div class="age-range-inputs">
            <n-input-number 
                v-model:value="formValue.minAge" 
                placeholder="Min Age" 
                :min="1" 
                :max="100"
                class="age-input"
            />
            <span class="age-separator">to</span>
            <n-input-number 
                v-model:value="formValue.maxAge" 
                placeholder="Max Age" 
                :min="1" 
                :max="100"
                class="age-input"
            />
        </div>
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
            <n-button type="primary" @click="handleSubmit" color="orange">{{ props.submitButtonText }}</n-button>
        </div>
    </n-form-item>
    </n-form>
</template>
  
<script setup lang="ts">
import { reactive, ref, watchEffect } from 'vue'
import {
  NForm, NFormItem, NInput, NInputNumber, NDatePicker, NSelect, NButton
} from 'naive-ui'
import LocationSearch from '../../GMaps/LocationSearch.vue'

const emit = defineEmits(['submit'])
const props = defineProps({
  initialData: {
    type: Object,
    default: null
  },
  submitButtonText: { type: String, default: 'Add Event' }
})

const locationError = ref('')

const formValue = reactive({
  location: '',
  locationLat: 0,
  locationLng: 0,
  participants: 1,
  minAge: null as null | number,
  maxAge: null as null | number,
  eventTime: null as null | number,
  sportType: null as null | string,
  description: ''
})

const sportOptions = [
  { label: 'Football', value: 'Football' },
  { label: 'Basketball', value: 'Basketball' },
  { label: 'Tennis', value: 'Tennis' },
  { label: 'Running', value: 'Running' }
]

watchEffect(() => {
  if (props.initialData) {
    formValue.location = props.initialData.location || ''
    formValue.participants = props.initialData.total || 1
    
    if (props.initialData.ageRange) {
      const ages = props.initialData.ageRange.split('-')
      if (ages.length === 2) {
        formValue.minAge = parseInt(ages[0])
        formValue.maxAge = parseInt(ages[1])
      }
    } else if (props.initialData.minAge && props.initialData.maxAge) {
      formValue.minAge = props.initialData.minAge
      formValue.maxAge = props.initialData.maxAge
    }
    
    formValue.eventTime = props.initialData.date ? new Date(props.initialData.date).getTime() : null
    formValue.sportType = props.initialData.sportType || null
    formValue.description = props.initialData.description || ''
  }
})

const onLocationSelected = ({ lat, lng, name }: { lat: number, lng: number, name: string }) => {
  formValue.locationLat = lat
  formValue.locationLng = lng
  formValue.location = name
  locationError.value = ''
}

const handleSubmit = () => {
  if (!formValue.locationLat || !formValue.locationLng) {
    locationError.value = 'Please select a valid location.'
    return
  }

  if (!formValue.minAge || !formValue.maxAge) {
    locationError.value = 'Please set both minimum and maximum age.'
    return
  }

  if (formValue.minAge > formValue.maxAge) {
    locationError.value = 'Minimum age cannot be greater than maximum age.'
    return
  }

  emit('submit', { ...formValue })
}
</script>


<style scoped>
.age-range-inputs {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.age-input {
  flex: 1;
}

.age-separator {
  color: #64748b;
  font-weight: 500;
}
</style>
