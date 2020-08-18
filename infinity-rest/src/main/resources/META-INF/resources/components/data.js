import { DataTemplate } from "../templates/data-template.js";
import Vue from '/js/vue.esm.browser.min.js'

const Data = Vue.component('Data', {
  data: function () {
    return {
      rows: [],
      showSpinner: false,
      file: null
    }
  },
  mounted: function () {
    this.getData();
  },
  methods: {
    getData: function (event) {
      axios.get('/events/Quebec/Trucks/2222-08-12T15:52:42.942Z').then(response => {
        this.rows = response.data;
        this.showSpinner = false;
      });
    },
    submitFile(){
        var formData = new FormData();
        formData.append('file', this.file);
        axios.post('/file',formData, {headers: {'Content-Type': 'multipart/form-data' }});
    },
    handleFileUpload(){
        this.file = this.$refs.file.files[0];
        console.log('FILE : ' + this.file);
    },
    analyze() {
      axios.post('/analytic', {
          eventGroup: 'Quebec',
          eventType: 'Trucks'
        });
    },
  },
  template: DataTemplate
})

export { Data }