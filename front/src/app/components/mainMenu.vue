<template>
  <div>
    <div class="d-flex justify-content-center separate-headfooter">
      <div>
        <button
          v-on:click="backDisconnect"
          type="button"
          class="btn btn-secondary navbtn"
        >
          <font-awesome-icon icon="sign-out-alt" />
        </button>
      </div>
    </div>
    <div>
      <br />
    </div>
    <div>
      <button
        type="button"
        class="btn btn-secondary btn-lg block"
        v-on:click="navToMySlots"
      >
        My slots
      </button>
    </div>
    <div>
      <button
        type="button"
        class="btn btn-secondary btn-lg block"
        v-on:click="navToAllActSlots"
      >
        All actives' slots
      </button>
    </div>
    <div>
      <button
        type="button"
        class="btn btn-secondary btn-lg block"
        v-on:click="navToOverf"
      >
        Overflow
      </button>
    </div>
    <div>
      <button
        type="button"
        class="btn btn-secondary btn-lg block"
        v-on:click="informations"
      >
        Informations
        <span :class="{'text-bg-danger': this.isUnreadMessage}" class="badge text-bg-secondary" >{{ this.nbUnreadMessage }}</span
        >
      </button>
    </div>
    <div v-if="adminState">
      <button
        type="button"
        class="btn btn-secondary btn-lg block"
        v-on:click="navToStat"
      >
        Admin.
      </button>
    </div>
  </div>
</template>
<script>
import shared from "../shared";
import _ from "lodash";
export default {
  data() {
    return {
      adminState: true,
      isUnreadMessage: false,
      nbUnreadMessage: 0,
    };
  },
  mounted() {
    this.adminState = shared.readAdminEtat();
  },
  created() {
    fetch("/api/informations/_unread", {
      method: "GET",
      headers: shared.tokenHandle(),
    })
      .then((response) => response.json())
      .then((p) => {
        this.nbUnreadMessage = _.size(p);
        this.isUnreadMessage = this.nbUnreadMessage > 0;
      });

    shared.securityAccess(this.$router, (p) => {});
  },
  methods: {
    navToOverf: function () {
      this.$router.push("/overflow");
    },
    navToMySlots: function () {
      this.$router.push("/mySlots");
    },
    navToStat: function () {
      this.$router.push("/adminMenu");
    },
    navToAllActSlots: function () {
      this.$router.push("/allactivesslots");
    },
    informations: function () {
      this.$router.push("/informations");
    },
    backDisconnect: function () {
      shared.cleanToken();
      this.$store.commit("setUsername", "");
      this.$router.push("/");
    },
  },
};
</script>


<style  scoped>
button:hover{
  
  background-color: #3b8a4b;
}
.block {
  width: 100%;
  border: 1px solid #f6f2c9;
  background-color: #265830;
  padding: 14px 28px;
  font-size: 16px;
  cursor: pointer;
  text-align: center;
}
</style>



