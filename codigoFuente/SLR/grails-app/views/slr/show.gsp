<%@ page import="es.uca.pfc.User" %>
<%@ page import="es.uca.pfc.Slr" %>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>SLR | ${slrInstance.title}</title>

	<%-- CSS --%>
    <g:render template="css" contextPath="/"/>

</head>

<body>
	<g:form>
	
	    <div id="wrapper">
	
	        <%-- Head --%>
	        <g:render template="head" contextPath="/"/>
	        
	        <div id="page-wrapper">	
				<div class="row" style="margin-bottom: 20px;">
					<div class="col-lg-12">
						<h1 class="page-header">SLR: ${slrInstance.title}</h1>
						<button type="button" class="btn btn-danger" data-toggle="modal" data-target="#myModalDrop">Eliminar Referencia</button>
					</div>
				</div>
			</div>
			<!-- /#page-wrapper -->
	
	    </div>
	    <!-- /#wrapper -->
    </g:form>

    <%-- JavaScript --%>
    <g:render template="javascript" contextPath="/"/>

</body>

</html>
