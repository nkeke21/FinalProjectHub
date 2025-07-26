import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'
import { nodePolyfills } from 'vite-plugin-node-polyfills'

export default defineConfig({
  plugins: [
    vue({
      template: {
        compilerOptions: {
          isCustomElement: (tag) =>
            ['gmpx-api-loader', 'gmpx-place-picker', 'gmp-map', 'gmp-advanced-marker'].includes(tag)
        }
      }
    }),
    vueDevTools(),
    nodePolyfills(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  define: {
    global: 'globalThis',
    __VUE_PROD_DEVTOOLS__: false
  },
  server: {
    // proxy: {
    //   '/api': {
    //     target: 'http://13.60.247.145:8080',
    //     changeOrigin: true
    //   }
    // }
  }
})
