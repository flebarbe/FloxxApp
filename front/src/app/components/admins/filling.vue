<template>
  <div>
    <div class="d-flex justify-content-around separate-headfooter">
      <div>
        <button v-on:click="backAdminMenu" type="button" class="btn btn-secondary navbtn">
          <font-awesome-icon icon="arrow-circle-left" />
        </button>
      </div>
      <div class="screen-title">Rooms Filling</div>
      <div>
        <button v-on:click="refresh" type="button" class="btn btn-secondary navbtn">
          <font-awesome-icon icon="sync" />
        </button>
      </div>
    </div>
    <b-input-group size="sm">
      <b-form-input v-model="filter" type="search" id="filterInput" placeholder="Type to Search"></b-form-input>
      <b-input-group-append>
        <b-button :disabled="!filter" @click="filter = ''">Clear</b-button>
      </b-input-group-append>
    </b-input-group>

    <b-table
      head-variant="light"
      details-td-class="cell"
      responsive="true"
      dark
      striped
      hover
      :sort-by="sortBy"
      :sort-desc="sortDesc"
      :fields="fields"
      :items="items"
      :filter="filter"
    >
    <template v-slot:head(slotId)>Slot</template>
      <template v-slot:head(percentage)>Percentage</template>
      <template v-slot:cell(slotId)="data">{{ data.item.slotId }}</template>
      <template v-slot:cell(percentage)="data">{{ data.item.percentage }}</template>     
    </b-table>
  </div>
</template>



<script>
import shared from "../../shared";
export default {
  data() {
    return {
      items: [],
      sortBy: "slotId",
      sortDesc: false,
      fields: [
        { key: "slotId", sortable: true },
        { key: "percentage", sortable: true }
      ],
      filter: null,
      filterOn: ["slotId"]
    };
  },
  created: function() {
    shared.securityAccess(this.$router, p => {
      fetch("/api/stats/slots/_filling", {
          headers: shared.tokenHandle()
        })
        .then((response) => response.json())
        .then(p => {
          console.log(p)
          this.items = p;
        });
    });
  },
  methods: {
    backAdminMenu: function() {
      this.$router.push("/adminMenu");
    },
    refresh: function() {
      shared.securityAccess(this.$router, p => {
            fetch("/api/stats/slots/_filling", {
                headers: shared.tokenHandle()
              })
              .then((response) => response.json())
              .then(p => {
                this.items = p;
              });
          });
    }
  }
};
</script>