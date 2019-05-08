<template>
  <div>
    <Highcharts :options="chartOptions"></Highcharts>
  </div>
</template>

<script>
import axios from 'axios';
import { Chart } from 'highcharts-vue';

export default {
  name: 'CalendarContracts',
  components: {
    Highcharts: Chart,
  },
  props: {
    contractId: String,
    contractStart: String,
    contractEnd: String,
    contractPensum: Number,
  },
  beforeMount() {
    this.restHeader = { headers: { Authorization: `Bearer ${localStorage.getItem('token')}` } };

    this.testing = this.getValues();

    this.chartOptions.series[0].data = this.testing;

    this.chartOptions.series[1].data = [
      { x: (new Date(this.contractStart)).getTime(), y: this.contractPensum },
      { x: (new Date(this.contractEnd)).getTime(), y: this.contractPensum },
    ];
  },
  data() {
    return {
      restHeader: null,
      testing: null,
      chartOptions: {
        title: { text: 'Contract Overview', align: 'left' },
        tooltip: {
          enabled: false,
        },
        chart: {
          type: 'area',
          spacingBottom: 30,
        },
        xAxis: {
          type: 'datetime',
          startOnTick: true,
          endOnTick: true,
        },
        yAxis: {
          title: {
            text: 'Absolute Workload Percentage',
            // align: 'center'
          },
        },
        plotOptions: {
          area: {
            stacking: 'normal',
            step: 'left',
          },
        },
        series: [{
          // data: [1,2,3] // sample data
          data: [1, 2, 3],
          name: 'Workload',
        }, {
          // data: [1,2,3] // sample data
          data: [1, 2, 3],
          name: 'Contract Pensum',
          type: 'line',
        }],
      },
    };
  },
  methods: {
    getValues() {
      const statistics = [];
      // eslint-disable-next-line
      axios.get(`${process.env.VUE_APP_API_SERVER}:${process.env.VUE_APP_API_PORT}/api/allocation`, this.restHeader)
        .then(response => response.data)
        .then((allocations) => {
          // eslint-disable-next-line
          allocations.sort(function (a, b) {
            return new Date(a.startDate) - new Date(b.startDate);
          });
          const sortedAllocations = [];
          allocations.forEach((entry) => {
            if (String(entry.contractId) === this.contractId) {
              sortedAllocations.push([entry.startDate, entry.pensumPercentage, 'first']);
              sortedAllocations.push([entry.endDate, entry.pensumPercentage, 'last']);
            }
          });
          // eslint-disable-next-line
          sortedAllocations.sort(function (a, b) {
            return new Date(a[0]) - new Date(b[0]);
          });
          let pensum = 0;
          let endDate;
          sortedAllocations.forEach((entry) => {
            if (entry[2] === 'first') pensum += entry[1];
            else pensum -= entry[1];
            if ((new Date(entry[0])).getTime() === endDate) statistics.pop();
            statistics.push({ x: (new Date(entry[0])).getTime(), y: pensum });
            if ((new Date(entry[0])).getTime() > endDate
              && (pensum - entry[2]) >= 0) {
              statistics.push({ x: (new Date(entry[0])).getTime() + 1, y: 0 });
            }
            endDate = (new Date(entry[0])).getTime();
          });
        });
      return statistics;
    },
  },
};
</script>

<style scoped>

</style>
