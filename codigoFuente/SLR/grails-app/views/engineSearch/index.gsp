<%@ page import="es.uca.pfc.EngineSearch" %>
<!DOCTYPE html>
<html lang="en">

<head>

    <%-- Head Meta --%>
    <g:render template="headMeta" contextPath="/"/>

    <title>SLR | Engine Searchs</title>
    
    <%-- CSS --%>
    <g:render template="css" contextPath="/"/>

	<script type="text/javascript">
	function loadMessageSucess()
	{
		if(${null != updateOk && updateOk})
		{
			document.getElementById('divSuccess').style.display = "";
			//setTimeout(hideSuccess, 5000);
			setTimeout(function(){
				document.getElementById('divSuccess').style.display = "none";
			}, 5000);
		}
	}
	</script>

</head>

<body onload="loadMessageSucess();">

<g:form class="form-horizontal" controller="engineSearch" action="save" method="POST" name="myForm" id="myForm">

	<div id="wrapper">
		<%-- Head --%>
        <g:render template="head" contextPath="/"/>
        	
		<div id="page-wrapper">
        	<div class="row" style="margin-bottom: 20px;">
				<div class="col-lg-12">
					<h1 class="page-header">ENGINE SEARCHS</h1>
					
					<ol class="breadcrumb">
					  <li><g:link controller="index" action="menu">Inicio</g:link></li>
					  <li class="active">Engine Searchs</li>
					</ol>
					
					<g:submitButton name="create" class="btn btn-primary" value="Guardar cambios"/>
					<p> </p>
				</div>
			</div>
			
			<div class="row">
				<div class="col-lg-12">
					<div id="divSuccess" class="alert alert-success" role="alert" style="display: none;"><i class="fa fa-check fa-fw"></i> Cambios guardados.</div>
					<g:each in="${engineSearchListInstance}" var="engineSearchInstance">
						<%--<div class="form-group">
							<label for="${'input' + engineSearchInstance.name}" class="col-sm-3 control-label">${engineSearchInstance.display_name}</label>
							<div class="col-sm-8">
								<input type="text" class="form-control" id="${'input' + engineSearchInstance.name}" name="${'input' + engineSearchInstance.name}" 
									value="${engineSearchInstance.apiKey}"  />
							</div>
						</div> --%>
						
						<h3>${engineSearchInstance.display_name}</h3>
						
						<div class="form-group">
							<label for="${'input' + engineSearchInstance.name}" class="col-sm-3 control-label">api key</label>
							<div class="col-sm-8">
								<input type="text" class="form-control" id="${'input' + engineSearchInstance.name}" name="${'input' + engineSearchInstance.name}" 
									value="${engineSearchInstance.apiKey}"  />
							</div>						
						</div>
						<div class="form-group">
							<div class="col-sm-3"></div>
						    <div class="col-sm-8">
						      <div class="checkbox">
						        <label>
						        	<g:if test="${engineSearchInstance.status == true}">
						        		<input id="${'cbox' + engineSearchInstance.name}" name="${'cbox' + engineSearchInstance.name}" type="checkbox" checked="checked" /> Mostrar en búsquedas
						        	</g:if>
						        	<g:else>
						        		<input id="${'cbox' + engineSearchInstance.name}" name="${'cbox' + engineSearchInstance.name}" type="checkbox" /> Mostrar en búsquedas
						        	</g:else>
						        </label>
						      </div>
						    </div>
						</div>
					</g:each>
				</div>
			</div>
        </div>
        
	</div>

</g:form>

<%-- JavaScript --%>
<g:render template="javascript" contextPath="/"/>

</body>

</html>