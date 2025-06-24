import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'
import autoprefixer from 'autoprefixer'
import path from 'path'

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
  css: {
    postcss: {
      plugins: [
        autoprefixer
      ]
    },
    preprocessorOptions: {
      less: {
        javascriptEnabled: true,
        additionalData: `@import "${path.resolve(__dirname, 'src/assets/base.less')}";`
      }
    }
  },
  base: process.env.NODE_ENV === 'development' ? '' : '/sg',
  server: {
    proxy: {
      '/sg-api': {
        target: 'http://localhost:80/sg-api',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/sg-api/, '')
      },
      '/rsm': {
        target: 'http://localhost:80/rsm',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/rsm/, '')
      }
    }
  },
})
