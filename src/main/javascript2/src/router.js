import Vue from 'vue';
import Router from 'vue-router';
import Login from './views/Login.vue';
import Dashboard from './views/Dashboard.vue';

Vue.use(Router);

export default new Router({
  routes: [
    {
      path: '/',
      name: 'dashboard',
      component: Dashboard,
    },
    {
      path: '/login',
      name: 'login',
      component: Login,
    },
    {
      path: '*',
      redirect: '/login',
    },
  ],
});
