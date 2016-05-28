<script type="text/javascript">	
$(function() {    
    Morris.Bar({
	    element: 'morris-bar-chart',
	    data: [<%= strGraph01%>],
	    xkey: 'l',
	    ykeys: [<%=ykeys01%>],
	    labels: [<%=labels01%>],
	    hideHover: 'auto',
	    resize: true
	});

    Morris.Donut({
  	  element: 'morris-donut-chart',
  	  data: [<%=strGraph02%>]
  	});

});
</script>