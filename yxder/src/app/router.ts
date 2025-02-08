import { createRouter, createWebHistory } from 'vue-router'
import EntityCoder from '../views/EntityCoder.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'coder',
      component: EntityCoder,
    },
  ],
})

export default router
