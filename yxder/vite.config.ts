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
  base:  process.env.NODE_ENV === 'development' ? '' : '/sg/code/',
  
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
      }
    }
  },
})
