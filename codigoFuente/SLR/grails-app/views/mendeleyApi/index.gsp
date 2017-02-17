<%@ page import="es.uca.pfc.MendeleyApi" %>

<!DOCTYPE html>
<html lang="en">

<head>

    <%-- Head Meta --%>
	<g:render template="headMeta" contextPath="/"/>

    <title>SLR | Api Key Mendeley</title>
    
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

<g:form class="form-horizontal" controller="mendeleyApi" action="save" method="POST" name="myForm" id="myForm">

	<div id="wrapper">
	
		<div id="page-wrapper">
        	<div class="row" style="margin-bottom: 20px;">
				<div class="col-lg-12">
					<h1 class="page-header">API KEY Mendeley</h1>
					<!--<g:link type="button" class="btn btn-primary" controller="apiKeyEngine" action="save">Guardar cambios</g:link>-->
					<g:submitButton name="create" class="btn btn-primary" value="Guardar cambios"/>
					<p> </p>
				</div>
			</div>
			
			<div class="row">
				<div class="col-lg-12">
					<div id="divSuccess" class="alert alert-success" role="alert" style="display: none;"><i class="fa fa-check fa-fw"></i> Cambios guardados.</div>
					<div class="form-group">
						<label for="inputClientId" class="col-sm-3 control-label">Client Id: </label>
						<div class="col-sm-8">
							<input type="text" class="form-control" id="inputClientId" name="inputClientId" value="${mendeleyApiInstance.clientId}"  />
						</div>
					</div>
					<div class="form-group">
						<label for="inputClientSecret" class="col-sm-3 control-label">Client Secret: </label>
						<div class="col-sm-8">
							<input type="text" class="form-control" id="inputClientSecret" name="inputClientSecret" value="${mendeleyApiInstance.clientSecret}"  />
						</div>
					</div>
					<div class="form-group">
						<label for="inputRedirectUri" class="col-sm-3 control-label">Redirect Uri: </label>
						<div class="col-sm-8">
							<input type="text" class="form-control" id="inputRedirectUri" name="inputRedirectUri" value="${mendeleyApiInstance.redirectUri}" />
						</div>
					</div>
					<div class="form-group">
						<label for="inputTotalHilos" class="col-sm-3 control-label">Total Hilos: </label>
						<div class="col-sm-8">
							<select class="form-control" id="inputTotalHilos" name="inputTotalHilos">
								<g:each in="${2..8}" var="op">
									<g:if test="${ op == mendeleyApiInstance.totalHilos }">
										<option value="${op}" selected="selected">${op}</option>
									</g:if>
									<g:else>
										<option value="${op}">${op}</option>
									</g:else>									
								</g:each>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label for="inputTotalTries" class="col-sm-3 control-label">Total Intentos: </label>
						<div class="col-sm-8">
							<select class="form-control" id="inputTotalTries" name="inputTotalTries">
								<g:each in="${1..5}" var="op">
									<g:if test="${ op == mendeleyApiInstance.totalTries }">
										<option value="${op}" selected="selected">${op}</option>
									</g:if>
									<g:else>
										<option value="${op}">${op}</option>
									</g:else>
								</g:each>
							</select>
						</div>
					</div>
				</div>
			</div>
        </div>
        
	</div>

</g:form>

<%-- JavaScript --%>
<g:render template="javascript" contextPath="/"/>

</body>

</html>