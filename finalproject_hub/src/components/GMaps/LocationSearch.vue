<template>
  <div style="min-height: 60px; padding: 1rem 0;">
    <gmpx-place-picker id="place-picker" style="width: 100%;" />
  </div>
</template>
  
<script>
export default {
  props: {
    modelValue: String
  },
  emits: ['update:modelValue', 'location-selected'],
  mounted() {
    customElements.whenDefined('gmpx-place-picker').then(() => {
      const picker = document.getElementById('place-picker')
      picker.addEventListener('gmpx-placechange', () => {
        const place = picker.value
        if (place && place.formattedAddress) {
          this.$emit('update:modelValue', place.formattedAddress)
          if (place.location) {
            this.$emit('location-selected', {
              lat: place.location.lat(),
              lng: place.location.lng(),
              name: place.formattedAddress
            })
          }
        }
      })
    })
  }
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
