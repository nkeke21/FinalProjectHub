<template>
  <div class="location-search-container">
    <div v-if="isLoading" class="loading-state">
      <n-spin size="small" />
      <span>Loading location picker...</span>
    </div>
    
    <div v-else-if="!hasError" class="place-picker-wrapper">
      <gmpx-place-picker id="place-picker" style="width: 100%;" />
    </div>
    
    <div v-else class="fallback-input">
      <n-input
        v-model:value="fallbackValue"
        placeholder="Enter location manually"
        clearable
        @update:value="onFallbackInput"
      />
      <div class="fallback-note">
        <n-icon name="warning" color="#f0a020" />
        <span>Google Maps not available. Please enter location manually.</span>
      </div>
    </div>
  </div>
</template>
  
<script setup>
import { ref, onMounted } from 'vue'
import { NInput, NSpin, NIcon } from 'naive-ui'
import { config } from '../../utils/config'

const props = defineProps({
  modelValue: String
})

const emit = defineEmits(['update:modelValue', 'location-selected'])

const isLoading = ref(true)
const hasError = ref(false)
const fallbackValue = ref(props.modelValue || '')

onMounted(async () => {
  try {
    // Initialize Google Maps API if not already loaded
    if (!window.google) {
      await loadGoogleMapsScript()
    }
    
    // Wait for the custom element to be defined
    await customElements.whenDefined('gmpx-place-picker')
    
    const picker = document.getElementById('place-picker')
    if (picker) {
      picker.addEventListener('gmpx-placechange', () => {
        const place = picker.value
        if (place && place.formattedAddress) {
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
      
      isLoading.value = false
    } else {
      throw new Error('Place picker element not found')
    }
  } catch (error) {
    console.error('Failed to load Google Maps:', error)
    hasError.value = true
    isLoading.value = false
  }
})

const loadGoogleMapsScript = () => {
  return new Promise((resolve, reject) => {
    if (window.google) {
      return resolve(window.google)
    }

    const script = document.createElement('script')
    script.src = `https://maps.googleapis.com/maps/api/js?key=${config.googleMapsApiKey}&libraries=places`
    script.async = true
    script.onload = () => resolve(window.google)
    script.onerror = (error) => {
      console.error('Google Maps script failed to load:', error)
      reject(new Error('Failed to load Google Maps API'))
    }
    document.head.appendChild(script)
  })
}

const onFallbackInput = (value) => {
  fallbackValue.value = value
  emit('update:modelValue', value)
  // Emit a basic location object for consistency
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

.loading-state {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #666;
  font-size: 14px;
}

.place-picker-wrapper {
  width: 100%;
}

.fallback-input {
  width: 100%;
}

.fallback-note {
  display: flex;
  align-items: center;
  gap: 4px;
  margin-top: 4px;
  font-size: 12px;
  color: #f0a020;
}

gmpx-place-picker {
  z-index: 9999;
  position: relative;
  display: block;
}

.location-search-input {
  width: 100%;
  padding: 0.5rem 0;
}

.result-preview {
  margin-top: 0.5rem;
  font-size: 14px;
  color: #666;
}
</style>
