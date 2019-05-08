<template>
  <div>
    <b-alert v-model="showFailLoginAlert" variant="danger" class="alert-center"
     fade dismissible>
      Login failed. Please try again.
    </b-alert>
    <b-form @submit="sendLogin" class="form-signin">
      <h1 class="h3 mb-3">Please sign in</h1>
      <b-form-input v-model="email" type="email" id="inputMail" class="form-control marg-bot"
      placeholder="E-Mail" required autofocus />
      <b-form-input v-model="pw" type="password" id="inputPass" class="form-control marg-bot"
      placeholder="Password" required />
      <b-button type="submit" block variant="primary" size="lg"
       class="marg-bot">Sign in</b-button>
    </b-form>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'login',
  data() {
    return {
      apiServer: process.env.VUE_APP_API_SERVER,
      apiPort: process.env.VUE_APP_API_PORT,
      showFailLoginAlert: false,
      email: '',
      pw: '',
    };
  },
  methods: {
    sendLogin(evt) {
      evt.preventDefault();
      axios.post(`${this.apiServer}:${this.apiPort}/api/token`, { emailAddress: this.email, rawPassword: this.pw })
        .then((response) => {
          if (response.status === 201) {
            localStorage.setItem('token', response.data.token);
            this.$router.push({ name: 'dashboard' });
          } else {
            this.showFailLoginAlert = true;
          }
        })
        .catch((error) => {
          console.log(error);
          this.showFailLoginAlert = true;
        });
    },
  },
};
</script>

<style scoped>
body {
  display: flex;
  align-items: center;
  padding-top: 40px;
  padding-bottom: 40px;
  text-align: center !important;
}

.form-signin {
  width: 100%;
  max-width: 330px;
  padding: 15px;
  margin: auto;
  padding-top: 20%;
  text-align: center;
}

.form-signin .form-control {
  box-sizing: border-box;
  height: auto;
  padding: 10px;
  font-size: 16px;
}

.marg-bot {
  margin-bottom: 10px;
}

.alert-center {
  position: absolute;
  margin-left: auto;
  margin-right: auto;
  left: 0;
  right: 0;
  width: 330px;
  margin-top: 20px;
}
</style>
