<%@ page import="es.uca.pfc.User" %>
<%@ page import="es.uca.pfc.Slr" %>
<!DOCTYPE html>
<html lang="en">

<head>

    <%-- Head Meta --%>
	<g:render template="headMeta" contextPath="/"/>

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
						<h1 class="page-header">${slrInstance.title}</h1>
						
						<ol class="breadcrumb">
						  <li><g:link controller="index" action="menu">Home</g:link></li>
						  <li><g:link controller="slr" action="myList">Mis SLR's</g:link></li>
						  <li class="active">${slrBreadCrumb}</li>
						</ol>
					
						<!-- <button type="button" class="btn btn-danger" data-toggle="modal" data-target="#myModalDrop">Eliminar SLR</button> -->
					</div>
				</div>

				<h4><u>Datos principales</u></h4>
				<div class="row">
					<div class="col-lg-1">

					</div>
					<div class="col-lg-5">
						<p><b>Título: </b>${slrInstance.title}</p>
						<p><b>Creado por: </b><g:link controller="user" action="show" params="[guid: "${slrInstance.userProfile.guid}"]">${slrInstance.userProfile.display_name}</g:link></p>
						<p><b>Total Búsquedas: </b>
						<g:if test="${slrInstance.searchs.size() > 0}">
							<g:link controller="slr" action="searchs" params="[guid: "${slrInstance.guid}"]">${slrInstance.searchs.size()} búsqueda(s)</g:link>
						</g:if>
						<g:else>
							${slrInstance.searchs.size()} busquedas
						</g:else>
						</p>
						<p><b>Total Referencias: </b>
						<g:if test="${slrInstance.totalReferences > 0}">
							<g:link controller="slr" action="references" params="[guid: "${slrInstance.guid}"]">${slrInstance.totalReferences} referencia(s)</g:link>
						</g:if>
						<g:else>
							${slrInstance.totalReferences} referencias
						</g:else>
						</p>
						<p><b>Justificación:</b></p>${slrInstance.justification}
					</div>
					<div class="col-lg-5">
						<g:if test="${slrInstance.questions.size() > 0}">
						<p><b>Preguntas Investigación</b></p>
						<ul>
							<g:each in="${slrInstance.questions}" var="questionInstance">
								<li>${questionInstance.enunciado}</li>
							</g:each>
						</ul>
						</g:if>
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
