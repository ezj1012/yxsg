import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  server: {
    proxy: {
      '/sg': {
        target: 'http://localhost:80/sg',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/sg/, '')
      },
      '/rsm': {
        target: 'http://localhost:80/sg/rsm',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/rsm/, '')
      }
    }
  },
})
