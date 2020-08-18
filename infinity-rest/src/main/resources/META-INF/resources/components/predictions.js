import { PredictionsTemplate } from "../templates/predictions-template.js";
import Vue from '/js/vue.esm.browser.min.js'

const Predictions = Vue.component('Predictions', {
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
      axios.get('/analytic/prediction/Quebec/Trucks/ARIMA/YEARS/2025').then(response => {
        this.rows = response.data;
        this.showSpinner = false;
      });
    },
  },
  template: PredictionsTemplate
})

export { Predictions }