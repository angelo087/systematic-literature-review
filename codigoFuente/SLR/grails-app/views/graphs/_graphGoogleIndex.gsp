<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
  google.charts.load('current', {'packages':['bar']});
  google.charts.setOnLoadCallback(drawChart);

  function drawChart() {
    var data = google.visualization.arrayToDataTable([<%=queryChartIndex%>]);

    var options = {
      chart: {
        title: 'SLR creados',
        subtitle: '5 últimos meses'
      }
    };

    var chart = new google.charts.Bar(document.getElementById('chart_div'));

    chart.draw(data, google.charts.Bar.convertOptions(options));
  }
</script>