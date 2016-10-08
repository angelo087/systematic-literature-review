<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js" ></script>
<script type="text/javascript">
   google.charts.load("current", {packages:['corechart','treemap','bar']});
   google.charts.setOnLoadCallback(drawColumnChart);
   google.charts.setOnLoadCallback(drawPieChart);

   function drawPieChart() {

	   var data = google.visualization.arrayToDataTable([<%=queryCriterion1%>]);
	   
	   var options = {
		         title: 'Total Referencias por criterios',
		         width: 425,
		         height: 200,
		         pieHole: 0.4,
		         chartArea: {
		        	    left: 40,
		        	    top: 50,
		        	    width: 700
		        }
		       };

	   var chart = new google.visualization.PieChart(document.getElementById('chart_div_4'));
       chart.draw(data, options);
     }
      
   function drawColumnChart() {

       var data = google.visualization.arrayToDataTable([<%=queryCriterion1%>]);
   
       var view = new google.visualization.DataView(data);
	   
	   view.setColumns([0, 1,
                        { calc: "stringify",
                          sourceColumn: 1,
                          type: "string",
                          role: "annotation" },
                        2]);
 
	   var options = {
		         title: "Criterios",
		         width: 400,
		         height: 200,
		         chartArea: {
		        	    left: 40,
		        	    top: 50,
		        	    width: 600
		        },
		        bar: {groupWidth: "95%"}
		       };
	
       var chart = new google.visualization.ColumnChart(document.getElementById("chart_div"));
       chart.draw(view, options);       
   }
 </script>