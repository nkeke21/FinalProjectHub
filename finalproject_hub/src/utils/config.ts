export const config = {
  googleMapsApiKey: import.meta.env.VITE_GOOGLE_MAPS_API_KEY as string,
  appTitle: import.meta.env.VITE_APP_TITLE || 'Join2Play',
  apiBaseUrl: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'
}

export function validateEnvironment() {
  const requiredVars = {
    'VITE_GOOGLE_MAPS_API_KEY': config.googleMapsApiKey
  }

  const missingVars = Object.entries(requiredVars)
    .filter(([_, value]) => !value)
    .map(([key]) => key)

  if (missingVars.length > 0) {
    console.error('Missing required environment variables:', missingVars)
    console.error('Please check your .env file and ensure all required variables are set.')
  }
} 