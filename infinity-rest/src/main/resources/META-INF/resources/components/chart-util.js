
function createChart(labels, datasets){
    var ctx = document.getElementById('monitorChart');
    var myChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: labels,
            datasets: datasets,
        },
        options: {
          legend: { position: 'bottom' },
          responsive: true,
          scales: {
            y: { min: 10, max: 50 },
            yAxes: [{
              ticks: {
                beginAtZero: true
              }
            }]
          }
        }
    });
}
