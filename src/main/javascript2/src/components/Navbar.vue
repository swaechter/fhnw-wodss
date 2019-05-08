<template>
  <b-navbar type="dark" variant="dark">
    <b-navbar-brand tag="h1" class="mb-0">Job Scheduler</b-navbar-brand>
    <b-navbar-nav class="ml-auto">
      <b-nav-text>{{employeeName}}</b-nav-text>
      <b-nav-item @click="logout">Logout</b-nav-item>
    </b-navbar-nav>
  </b-navbar>
</template>

<script>
export default {
  name: 'Navbar',
  props: {
    employeeName: String,
  },
  methods: {
    logout() {
      localStorage.removeItem('token');
      this.$router.push({ name: 'login' });
    },
  },
};
</script>

<style scoped>
</style>
