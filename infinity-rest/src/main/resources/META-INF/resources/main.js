import Vue from '/js/vue.esm.browser.min.js'
import { MainTemplate } from './templates/main-template.js'
import { Data } from './components/data.js'
import { Aggregations } from './components/aggregations.js'
import { Predictions } from './components/predictions.js'
import { Chart } from './components/chart.js'

// Router
Vue.use(VueRouter);
const router = new VueRouter({
  routes: [
    { path: '/', redirect: "/data" },
    { path: '/data', component: Data, name: "Data" },
    { path: '/aggregations', component: Aggregations, name: "Aggregations" },
    { path: '/predictions', component: Predictions, name: "Predictions" },
    { path: '/chart', component: Chart, name: "Chart" },
  ]
})

// Application
var client = new Vue({
  el: '#app',
  router,
  template: MainTemplate,
  mounted: function () {
  }
})
