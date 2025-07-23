<template>
  <div class="location-search-container">
    <n-input
      ref="locationInput"
      v-model:value="locationValue"
      placeholder="Enter location (e.g., Tbilisi, Georgia)"
      clearable
      @update:value="onLocationInput"
    />
    
    <div v-if="statusMessage" class="status-message" :class="statusType">
      <n-icon :name="statusIcon" :color="statusColor" />
      <span>{{ statusMessage }}</span>
    </div>
  </div>
</template>
  
<script setup>
import { ref, onMounted, watch, nextTick } from 'vue'
import { NInput, NIcon } from 'naive-ui'
import { config } from '../../utils/config'

const props = defineProps({
  modelValue: String
})

const emit = defineEmits(['update:modelValue', 'location-selected'])

const locationValue = ref(props.modelValue || '')
const locationInput = ref(null)
const statusMessage = ref('')
const statusType = ref('info')
const statusIcon = ref('info-circle')
const statusColor = ref('#1890ff')
let autocomplete = null

watch(() => props.modelValue, (newValue) => {
  if (newValue !== locationValue.value) {
    locationValue.value = newValue || ''
  }
})

onMounted(async () => {
  try {
    if (!window.google && config.googleMapsApiKey) {
      statusMessage.value = 'Loading Google Maps...'
      statusType.value = 'info'
      statusIcon.value = 'loading'
      statusColor.value = '#1890ff'
      
      await loadGoogleMapsScript()
      await setupAutocomplete()
    } else if (window.google) {
      await setupAutocomplete()
    }
  } catch (error) {
    console.warn('Google Maps not available, using manual input:', error)
    statusMessage.value = 'Using manual location input'
    statusType.value = 'warning'
    statusIcon.value = 'warning'
    statusColor.value = '#faad14'
  }
})

const loadGoogleMapsScript = () => {
  return new Promise((resolve, reject) => {
    if (window.google) {
      return resolve(window.google)
    }

    if (!config.googleMapsApiKey) {
      reject(new Error('Google Maps API key not configured'))
      return
    }

    const script = document.createElement('script')
    script.src = `https://maps.googleapis.com/maps/api/js?key=${config.googleMapsApiKey}&libraries=places`
    script.async = true
    script.onload = () => resolve(window.google)
    script.onerror = () => reject(new Error('Failed to load Google Maps API'))
    document.head.appendChild(script)
  })
}

const setupAutocomplete = async () => {
  try {
    await nextTick()
    
    if (!locationInput.value || !window.google) {
      throw new Error('Input element or Google Maps not available')
    }

    const inputElement = locationInput.value.$el.querySelector('input')
    if (!inputElement) {
      throw new Error('Input element not found')
    }

    autocomplete = new window.google.maps.places.Autocomplete(inputElement, {
      types: ['geocode', 'establishment'],
      componentRestrictions: { country: 'ge' }
    })

    autocomplete.addListener('place_changed', () => {
      const place = autocomplete.getPlace()
      
      if (place.geometry) {
        const location = {
          lat: place.geometry.location.lat(),
          lng: place.geometry.location.lng(),
          name: place.formatted_address || place.name
        }
        
        locationValue.value = location.name
        emit('update:modelValue', location.name)
        emit('location-selected', location)
        
        statusMessage.value = 'Location selected from Google Maps'
        statusType.value = 'success'
        statusIcon.value = 'check-circle'
        statusColor.value = '#52c41a'
        
        setTimeout(() => {
          if (statusMessage.value === 'Location selected from Google Maps') {
            statusMessage.value = ''
          }
        }, 3000)
      }
    })
    
    statusMessage.value = 'Google Maps autocomplete ready'
    statusType.value = 'success'
    statusIcon.value = 'check-circle'
    statusColor.value = '#52c41a'
    
    setTimeout(() => {
      if (statusMessage.value === 'Google Maps autocomplete ready') {
        statusMessage.value = ''
      }
    }, 3000)
    
  } catch (error) {
    console.warn('Could not setup Google Maps autocomplete:', error)
    statusMessage.value = 'Using manual location input'
    statusType.value = 'warning'
    statusIcon.value = 'warning'
    statusColor.value = '#faad14'
  }
}

const onLocationInput = (value) => {
  locationValue.value = value
  emit('update:modelValue', value)
  emit('location-selected', {
    lat: null,
    lng: null,
    name: value
  })
}
</script>
  
  
<style scoped>
.location-search-container {
  min-height: 60px;
  padding: 1rem 0;
}

.status-message {
  display: flex;
  align-items: center;
  gap: 4px;
  margin-top: 4px;
  font-size: 12px;
}

.status-message.info {
  color: #1890ff;
}

.status-message.warning {
  color: #faad14;
}

.status-message.success {
  color: #52c41a;
}

.status-message.error {
  color: #ff4d4f;
}

:deep(.pac-container) {
  z-index: 9999;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}
</style>
