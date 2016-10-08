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

				<h4><u>Datos principales</u></h4>
				<div class="row">
					<div class="col-lg-1">

					</div>
					<div class="col-lg-5">
						<p><b>Título: </b>${slrInstance.title}</p>
						<p><b>Creado por: </b><g:link controller="user" action="show" params="[guid: "${slrInstance.userProfile.guid}"]">${slrInstance.userProfile.display_name}</g:link></p>
						<p><b>Total Búsquedas: </b><g:link controller="slr" action="searchs" params="[guid: "${slrInstance.guid}"]">${slrInstance.searchs.size()} búsqueda(s)</g:link></p>
						<p><b>Total Referencias: </b><g:link controller="slr" action="references" params="[guid: "${slrInstance.guid}"]">${slrInstance.totalReferences} referencia(s)</g:link></p>
						<p><b>Total visitas: </b>${slrInstance.numVisits} visita(s)</p>
						<p><b>Justificación:</b></p>${slrInstance.justification}
					</div>
					<div class="col-lg-5">
						<p><b>Preguntas Investigación</b></p>
						<ul>
							<g:each in="${slrInstance.questions}" var="questionInstance">
								<li>${questionInstance.enunciado}</li>
							</g:each>
						</ul>
					</div>
				</div>
				
				<div class="row" style="margin-top: 20px;">
					<div class="col-lg-6">
						<div class="panel panel-default">
	                        <div class="panel-heading">
	                            <b>Criterios</b>
	                        </div>
	                        <div class="panel-body" style="overflow: hidden;">
	                            <div id="chart_div" align="center"></div>
	                        </div>
	                    </div>
					</div>
					<div class="col-lg-6">
						<div class="panel panel-default">
	                        <div class="panel-heading">
	                            <b>Criterios</b>
	                        </div>
	                        <div class="panel-body" style="overflow: hidden;">
	                            <div id="chart_div_4" align="center"></div>
	                        </div>
	                    </div>
					</div>
				</div>
			</div>
			<!-- /#page-wrapper -->
	
	    </div>
	    <!-- /#wrapper -->
    </g:form>

    <%-- JavaScript --%>
    <g:render template="javascript" contextPath="/"/>
	<g:render template="graphsGoogleSlrView" contextPath="/graphs"/>
</body>

</html>
