<%@ page import="es.uca.pfc.ApiKeyEngine" %>
<!DOCTYPE html>
<html lang="en">

<head>

    <%-- Head Meta --%>
    <g:render template="headMeta" contextPath="/"/>

    <title>SLR | Api Key Engines</title>
    
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

<g:form class="form-horizontal" controller="apiKeyEngine" action="save" method="POST" name="myForm" id="myForm">

	<div id="wrapper">
	
		<div id="page-wrapper">
        	<div class="row" style="margin-bottom: 20px;">
				<div class="col-lg-12">
					<h1 class="page-header">API KEY ENGINES</h1>
					<!--<g:link type="button" class="btn btn-primary" controller="apiKeyEngine" action="save">Guardar cambios</g:link>-->
					<g:submitButton name="create" class="btn btn-primary" value="Guardar cambios"/>
					<p> </p>
				</div>
			</div>
			
			<div class="row">
				<div class="col-lg-12">
					<div id="divSuccess" class="alert alert-success" role="alert" style="display: none;"><i class="fa fa-check fa-fw"></i> Cambios guardados.</div>
					<g:each in="${apiKeyEngineListInstance}" var="apiKeyEngineInstance">
						<div class="form-group">
							<label for="${'input' + apiKeyEngineInstance.engine}" class="col-sm-3 control-label">${apiKeyEngineInstance.engine}</label>
							<div class="col-sm-8">
								<input type="text" class="form-control" id="${'input' + apiKeyEngineInstance.engine}" name="${'input' + apiKeyEngineInstance.engine}" 
									value="${apiKeyEngineInstance.apiKey}"  />
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