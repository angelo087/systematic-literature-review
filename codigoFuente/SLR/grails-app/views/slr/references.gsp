<%@ page import="es.uca.pfc.User" %>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>SLR | References</title>

	<%-- CSS --%>
    <g:render template="css" contextPath="/"/>

</head>

<body>

    <div id="wrapper">

        <%-- Head --%>
        <g:render template="head" contextPath="/"/>
        
        <div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">References <small>${slrInstance.title}</small></h1>
                </div>
            </div>
            
            <div id="searchresults" class="row">
            	<g:render template="referencesSearchResult" contextPath="/slr"/>
            </div>

        </div>
        <!-- /#page-wrapper -->

    </div>
    <!-- /#wrapper -->

    <%-- JavaScript --%>
    <g:render template="javascript" contextPath="/"/>
    
    <script type="text/javascript">
		$(document).ready(function(){
	        $('.range-slider').jRange({
	            from: ${minYear},
	            to: ${maxYear},
	            step: 1,
	            scale: [${minYear},${maxYear}],
	            format: '%s',
	            width: 200,
	            showLabels: true,
	            isRange : true
	        });
	    });
	</script>

</body>

</html>