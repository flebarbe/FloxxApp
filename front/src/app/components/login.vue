<template>
  <div class="container-fluid">
    <div id="errorlogin" class="errorMsg" :hidden="loginFailedMsg">
      Authentification Failed
    </div>
    <div id="errorUnthorized" class="errorMsg" :hidden="notAuthorized">
      Authentification required
    </div>
    <form id="signup-form" @submit.prevent="processForm">
      <div class="form-group">
        <label for="login">Login</label>
        <input
          id="login"
          type="text"
          v-model="login"
          class="form-control"
          placeholder="Enter login"
        />
      </div>
      <div class="form-group">
        <label for="mdp">Password</label>
        <input
          id="mdp"
          type="password"
          class="form-control"
          v-model="password"
          placeholder="Enter password"
        />
      </div>

      <button type="submit" class="btn btn-primary">
        Submit
        <font-awesome-icon icon="sign-in-alt" />
      </button>
    </form>
  </div>
</template>
<script>
import _ from "lodash";
import shared from "../shared";

export default {
  data: function () {
    return {
      email: "",
      login: "",
      password: "",
      loginFailedMsg: true,
      notAuthorized: true,
    };
  },
  created() {
    const a = this.$route.query.authenticate;
    if (a == "no") {
      this.notAuthorized = false;
    }
  },
  methods: {
    processForm: function () {
      Promise.all([
          fetch("/login", {
            body: JSON.stringify({
              login: this.login,
              mdp: this.password,
            }),
            method: "POST",
          })
            .then((response) => response.json())
            .then(
              (r) => {
                shared.storeToken(r.token, r.isAdmin, r.name);
                this.$store.commit("setUsername", r.name);
              },
              (r) => {
                this.loginFailedMsg = false;
                console.error(r);
              }
            ).then(e => fetch("/api/days", {
             method: "GET",
             headers: shared.tokenHandle() })
            .then((response) => response.json())
            .then(
              (r) => {
                this.$store.commit("setConfDays", r);
              },
              (r) => {
                this.loginFailedMsg = false;
                console.error(r);
              }
            )),
          
        ])
        .then((v) => this.$router.push("/menu"));
    },
  },
};
</script>

<style>
.errorMsg {
  background-color: red;
  font-size: 16px;
  color: whitesmoke;
  text-align: center;
  margin: 17px 13px 14px 10px;
  padding: 10px 0px 10px 0px;
}
</style>
