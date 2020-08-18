import { AggregationsTemplate } from "../templates/aggregations-template.js";
import Vue from '/js/vue.esm.browser.min.js'

const Aggregations = Vue.component('Aggregations', {
  data: function () {
    return {
      rows: [],
      showSpinner: false
    }
  },
  mounted: function () {
    this.getData();
  },
  methods: {
    getData: function (event) {
      axios.get('/analytic/aggregation/Quebec/Trucks/YEARS/2020').then(response => {
        this.rows = response.data;
        this.showSpinner = false;
      });
    },
  },
  template: AggregationsTemplate
})

export { Aggregations }