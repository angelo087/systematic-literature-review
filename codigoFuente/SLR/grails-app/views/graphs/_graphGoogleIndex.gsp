<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js" ></script>
<script type="text/javascript">

google.charts.load("current", {packages:['corechart','treemap','bar']});
google.charts.setOnLoadCallback(drawColumnChart);

function drawColumnChart() {

	//var data = google.visualization.arrayToDataTable([["Fruta", "Total", { role: "style" }], ["Diciembre 2016", 5, ""], ["Enero 2017", 10, ""], ["Febrero 2017", 4, ""]]);
	var data = google.visualization.arrayToDataTable([<%=queryChartIndex%>]);
	var view = new google.visualization.DataView(data);

	view.setColumns([0, 1,
                     { calc: 'stringify',
                       sourceColumn: 1,
                       type: 'string',
                       role: 'annotation' },
                     2]);

	var options = {
	         title: "SLR's creados en los ultimos 5 meses",
	         width: 800,
	         height: 400,
	         chartArea: {
	        	    left: 40,
	        	    top: 50,
	        	    width: 500
	        },
	        bar: {groupWidth: "95%"}
	       };

	var chart = new google.visualization.ColumnChart(document.getElementById("chart_div"));
    chart.draw(view, options);  	
}

</script>