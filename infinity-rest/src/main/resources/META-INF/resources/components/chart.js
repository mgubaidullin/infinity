import { ChartTemplate } from '../templates/chart-template.js'
import Vue from '/js/vue.esm.browser.min.js'

const Chart = Vue.component('Chart', {
  data: function () {
      return {
        bc: 'rgb(255, 255, 255, 0.0)',
        bd: [5, 3],
        doit: null,
      }
    },
  mounted: function () {
    this.$nextTick(function() {
      window.addEventListener('resize', this.doChart());
      //Init
      this.drawChart()
    })
  },
  methods: {
      doChart(event) {
        clearTimeout(this.doit);
        this.doit = setTimeout(this.drawChart(), 100);
      },
      drawChart(event) {
        axios.all([
            axios.get('/analytic/aggregation/Quebec/Trucks/YEARS/2020'),
            axios.get('/analytic/prediction/Quebec/Trucks/ARIMA/YEARS/2025')
          ])
          .then(responseArr => {
            var aggregations = responseArr[0].data.reverse();
            var aggPeriods = aggregations.map(x => x.period);
            var aggAvg = aggregations.map(x => x.avgValue);
            var aggMin = aggregations.map(x => x.minValue);
            var aggMax = aggregations.map(x => x.maxValue);
            var aggSum = aggregations.map(x => x.sumValue);
            var aggMean = aggregations.map(x => x.meanValue);

            var predictions = responseArr[1].data.reverse();
            var pPeriods = predictions.map(x => x.period);
            var pAvg = new Array(aggAvg.length-1).fill(null).concat([aggAvg.slice(-1).pop()]).concat(predictions.map(x => x.avgValue));
            var pMin = new Array(aggMin.length-1).fill(null).concat([aggMin.slice(-1).pop()]).concat(predictions.map(x => x.minValue));
            var pMax = new Array(aggMax.length-1).fill(null).concat([aggMax.slice(-1).pop()]).concat(predictions.map(x => x.maxValue));
            var pMean = new Array(aggMean.length-1).fill(null).concat([aggMean.slice(-1).pop()]).concat(predictions.map(x => x.meanValue));
            var pSum = new Array(aggSum.length-1).fill(null).concat([aggSum.slice(-1).pop()]).concat(predictions.map(x => x.sumValue));

            var labels = aggPeriods.concat(pPeriods);
            console.log(aggregations);
            console.log(labels);
            console.log(aggAvg);

            var datasets = [
                {label: "AVG", data: aggAvg, backgroundColor: this.bc, borderColor: '#06c', borderWidth: 2,pointRadius: 1 },
                {label: "FORECAST AVG", data: pAvg, backgroundColor: this.bc, borderColor: '#06c', borderWidth: 2, pointRadius: 1, borderDash: this.bd },
                {label: "MIN", data: aggMin, backgroundColor: this.bc, borderColor: '#f4c145', borderWidth: 2,pointRadius: 1 },
                {label: "FORECAST MIN", data: pMin, backgroundColor: this.bc, borderColor: '#f4c145', borderWidth: 2, pointRadius: 1, borderDash: this.bd },
                {label: "MAX", data: aggMax, backgroundColor: this.bc, borderColor: '#4cb140', borderWidth: 2,pointRadius: 1 },
                {label: "FORECAST MAX", data: pMax, backgroundColor: this.bc, borderColor: '#4cb140', borderWidth: 2, pointRadius: 1, borderDash: this.bd },
                {label: "MEAN", data: aggMean, backgroundColor: this.bc, borderColor: '#764abc', borderWidth: 2,pointRadius: 1 },
                {label: "FORECAST MEAN", data: pMean, backgroundColor: this.bc, borderColor: '#764abc', borderWidth: 2, pointRadius: 1, borderDash: this.bd },
                {label: "SUM", data: aggSum, backgroundColor: this.bc, borderColor: '#f27173', borderWidth: 2,pointRadius: 1 },
                {label: "FORECAST SUM", data: pSum, backgroundColor: this.bc, borderColor: '#f27173', borderWidth: 2, pointRadius: 1, borderDash: this.bd }
            ]
            createChart(labels, datasets);
          });
      },
  },
  beforeDestroy() {
      window.removeEventListener('resize', this.doChart);
    },
  template: ChartTemplate
})

export { Chart }
