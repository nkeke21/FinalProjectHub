<template>
  <div class="location-search-container">
    <n-input
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
import { ref, onMounted, watch } from 'vue'
import { NInput, NIcon } from 'naive-ui'
import { config } from '../../utils/config'

const props = defineProps({
  modelValue: String
})

const emit = defineEmits(['update:modelValue', 'location-selected'])

const locationValue = ref(props.modelValue || '')
const statusMessage = ref('')
const statusType = ref('info')
const statusIcon = ref('info-circle')
const statusColor = ref('#1890ff')

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
      
      await setupPlacePicker()
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

const setupPlacePicker = async () => {
  try {
    await waitForCustomElement('gmpx-place-picker', 5000)
    
    const picker = document.createElement('gmpx-place-picker')
    picker.id = 'place-picker'
    picker.style.width = '100%'
    
    const container = document.querySelector('.location-search-container')
    const input = container.querySelector('.n-input')
    if (input && input.parentNode) {
      input.parentNode.replaceChild(picker, input)
    }
    
    picker.addEventListener('gmpx-placechange', () => {
      const place = picker.value
      if (place && place.formattedAddress) {
        locationValue.value = place.formattedAddress
        emit('update:modelValue', place.formattedAddress)
        if (place.location) {
          emit('location-selected', {
            lat: place.location.lat(),
            lng: place.location.lng(),
            name: place.formattedAddress
          })
        }
      }
    })
    
    statusMessage.value = 'Google Maps location picker ready'
    statusType.value = 'success'
    statusIcon.value = 'check-circle'
    statusColor.value = '#52c41a'
    
  } catch (error) {
    console.warn('Could not setup Google Maps picker:', error)
    statusMessage.value = 'Using manual location input'
    statusType.value = 'warning'
    statusIcon.value = 'warning'
    statusColor.value = '#faad14'
  }
}

const waitForCustomElement = (tagName, timeout = 5000) => {
  return new Promise((resolve, reject) => {
    if (customElements.get(tagName)) {
      return resolve()
    }
    
    const timeoutId = setTimeout(() => {
      reject(new Error(`Custom element ${tagName} not defined within ${timeout}ms`))
    }, timeout)
    
    customElements.whenDefined(tagName).then(() => {
      clearTimeout(timeoutId)
      resolve()
    }).catch((error) => {
      clearTimeout(timeoutId)
      reject(error)
    })
  })
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

gmpx-place-picker {
  z-index: 9999;
  position: relative;
  display: block;
}
</style>
