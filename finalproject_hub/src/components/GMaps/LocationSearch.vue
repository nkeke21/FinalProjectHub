<template>
  <div style="min-height: 60px; padding: 1rem 0;">
    <gmpx-place-picker id="place-picker" style="width: 100%;" />
  </div>
</template>
  
<script setup>
import { onMounted } from 'vue'
import { config } from '../../utils/config'

const props = defineProps({
  modelValue: String
})

const emit = defineEmits(['update:modelValue', 'location-selected'])

onMounted(async () => {
  // Initialize Google Maps API if not already loaded
  if (!window.google) {
    await loadGoogleMapsScript()
  }
  
  customElements.whenDefined('gmpx-place-picker').then(() => {
    const picker = document.getElementById('place-picker')
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
  })
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
    script.onerror = reject
    document.head.appendChild(script)
  })
}
</script>
  
  
<style scoped>
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
