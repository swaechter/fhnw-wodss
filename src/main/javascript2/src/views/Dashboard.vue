<template>
  <div>
    <Navbar :employeeName="this.loggedInEmployeeName" class="marg-bot-nav" />
    <Employees :loggedInRole="loggedInRole" :loggedInId="loggedInId" />
    <div class="marg-line">
      <b-tabs>
      </b-tabs>
    </div>
    <Projects  :loggedInRole="loggedInRole" :loggedInId="loggedInId" />
    <div class="marg-line">
      <b-tabs>
      </b-tabs>
    </div>
    <Contracts :loggedInRole="loggedInRole" />
    <div class="marg-line">
      <b-tabs>
      </b-tabs>
    </div>
    <Allocations :loggedInRole="loggedInRole" :loggedInId="loggedInId" />
    <Footer />
  </div>
</template>

<script>
import Navbar from '@/components/Navbar.vue';
import Employees from '@/components/Employees.vue';
import Projects from '@/components/Projects.vue';
import Contracts from '@/components/Contracts.vue';
import Allocations from '@/components/Allocations.vue';
import Footer from '@/components/Footer.vue';

const jwtDecode = require('jwt-decode');

export default {
  name: 'dashboard',
  data() {
    return {
      loggedInEmployee: '',
      loggedInEmployeeName: '',
      loggedInRole: '',
      loggedInId: '',
    };
  },
  components: {
    Navbar,
    Employees,
    Projects,
    Contracts,
    Allocations,
    Footer,
  },
  methods: {
    checkToken() {
      if (localStorage.getItem('token') === null) {
        this.$router.push({ name: 'login' });
      } else {
        try {
          const decoded = jwtDecode(localStorage.getItem('token'));
          this.loggedInEmployee = decoded;
          if (decoded.exp < (new Date().getTime() / 1000)) {
            localStorage.removeItem('token');
            this.$router.push({ name: 'login' });
          }
          this.loggedInEmployeeName = `${this.loggedInEmployee.firstName} ${this.loggedInEmployee.lastName}`;
          this.loggedInRole = this.loggedInEmployee.role;
          this.loggedInId = String(this.loggedInEmployee.id);
        } catch (error) {
          localStorage.removeItem('token');
          this.$router.push({ name: 'login' });
        }
      }
    },
  },
  beforeMount() {
    this.checkToken();
  },
};
</script>

<style scoped>
.marg-bot-nav {
  margin-bottom: 10px;
}

.marg-line {
  margin-bottom: 25px;
  margin-top: 25px;
}
</style>
