<%@ page import="es.uca.pfc.User" %>
<%@ page import="es.uca.pfc.Search" %>
<%@ page import="es.uca.pfc.Slr" %>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>SLR | Crear Búsqueda</title>

	<%-- CSS --%>
    <g:render template="css" contextPath="/"/>
    
    <script type="text/javascript">
	    function loadError()
		{
			if(${null != error && error != ""})
			{
				document.getElementById('divError').style.display = "";
			}
		}
	</script>

</head>

<body onload="loadError();">

    <div id="wrapper">

        <%-- Head --%>
        <g:render template="head" contextPath="/"/>
        
        <div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">Crear búsqueda <small>${slrInstance.title}</small></h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            
            <div class="row">
				<div id="divError" class="alert alert-danger" role="alert" style="display: none;"><i class="fa fa-remove fa-fw"></i> ${error}</div>
			</div>
				
            <g:form class="form-horizontal" controller="search" action="save" method="POST" name="myForm" id="myForm">
            	
            	<g:hiddenField name="guidSlr" value="${slrInstance.guid}" />
            	
	            <div class="row">
	                <div class="col-lg-6">
						<div class="form-group">
							<label for="inputTerminos" class="col-sm-3 control-label">Terminos: </label>
							<div class="col-sm-8">
								<input type="text" class="form-control" id="inputTerminos" name="inputTerminos" value="${terminosSearch}" />
							</div>
						</div>
	                </div>
	                <div class="col-lg-6">
	                	<div class="form-group">
							<label for="inputOperator" class="col-sm-3 control-label">Operator: </label>
							<div class="col-sm-8">
								<g:each in="${operatorListInstance}" var="operatorInstance" status="p">
									<label class="radio-inline">
										<g:if test="${operatorSearch != ""}">
											<g:if test="${operatorSearch == operatorInstance.value}">
												<input type="radio" id="inputOperator" name="inputOperator" value="${operatorInstance.value}" checked="checked"> ${operatorInstance.name}
											</g:if>
											<g:else>
												<input type="radio" id="inputOperator" name="inputOperator" value="${operatorInstance.value}"> ${operatorInstance.name}
											</g:else>
										</g:if>
										<g:else>
											<g:if test="${p==0}">
												<input type="radio" id="inputOperator" name="inputOperator" value="${operatorInstance.value}" checked="checked"> ${operatorInstance.name}
											</g:if>
											<g:else>
												<input type="radio" id="inputOperator" name="inputOperator" value="${operatorInstance.value}"> ${operatorInstance.name}
											</g:else>
										</g:else>
									</label>
								</g:each>
							</div>
						</div>
	                </div>
	            </div>
	            <div class="row" style="margin-top: 20px;">
	            	<div class="col-lg-6">
	                	<div class="form-group">
							<label for="inputEngine" class="col-sm-3 control-label">Engine</label>
							<div class="col-sm-8">
								<g:each in="${engineListInstance}" var="engineInstance">
									<label class="checkbox-inline">
										<g:if test="${engineInstance.name.toLowerCase().equals("acm") && opACMSearch.toString().equals('true')}">
											<input type="checkbox" id="engine${engineInstance.name}" name="engine${engineInstance.name}" value="${engineInstance.name}" checked="checked"> <img src="${resource(dir:'images/logos_engines', file: engineInstance.image)}" width="40" height="40" />
										</g:if>
										<g:elseif test="${engineInstance.name.toLowerCase().equals("ieee") && opIEEESearch.toString().equals('true')}">
											<input type="checkbox" id="engine${engineInstance.name}" name="engine${engineInstance.name}" value="${engineInstance.name}" checked="checked"> <img src="${resource(dir:'images/logos_engines', file: engineInstance.image)}" width="40" height="40" />
										</g:elseif>
										<g:elseif test="${engineInstance.name.toLowerCase().equals("science") && opSCIENCESearch.toString().equals('true')}">
											<input type="checkbox" id="engine${engineInstance.name}" name="engine${engineInstance.name}" value="${engineInstance.name}" checked="checked"> <img src="${resource(dir:'images/logos_engines', file: engineInstance.image)}" width="40" height="40" />
										</g:elseif>
										<g:elseif test="${engineInstance.name.toLowerCase().equals("springer") && opSPRINGERSearch.toString().equals('true')}">
											<input type="checkbox" id="engine${engineInstance.name}" name="engine${engineInstance.name}" value="${engineInstance.name}" checked="checked"> <img src="${resource(dir:'images/logos_engines', file: engineInstance.image)}" width="40" height="40" />
										</g:elseif>
										<g:else>
											<input type="checkbox" id="engine${engineInstance.name}" name="engine${engineInstance.name}" value="${engineInstance.name}"> <img src="${resource(dir:'images/logos_engines', file: engineInstance.image)}" width="40" height="40" />
										</g:else>
									</label>
								</g:each>
							</div>
						</div>
	                </div>
	                <div class="col-lg-6">
	                	<div class="form-group">
							<label for="selectComponent" class="col-sm-3 control-label">Component</label>
							<div class="col-sm-8">
								<select class="form-control" id="selectComponent" name="selectComponent">
									<g:each in="${componentListInstance}" var="componentInstance">
										<g:if test="${componentSearch != "" && componentInstance.value == componentSearch}">
											<option value="${componentInstance.value}" selected="selected">${componentInstance.name}</option>
										</g:if>
										<g:else>
											<option value="${componentInstance.value}">${componentInstance.name}</option>
										</g:else>
									</g:each>
								</select>
							</div>
						</div>
	                </div>
	            </div>
	            <div class="row" style="margin-top: 50px;">
	            	<div class="col-lg-6">
	                	<div class="form-group">
	                		<label for="inputYears" class="col-sm-3 control-label">Years</label>
							<div class="col-sm-8">
				            	<input id="inputYears" name="inputYears" type="hidden" class="range-slider" value="${minYearSearch},${maxYearSearch}" />
				            </div>
			            </div>
			        </div>
			        <div class="col-lg-6">
						<div class="form-group">
							<label for="inputTotalMax" class="col-sm-3 control-label">Total Máximo: </label>
							<div class="col-sm-8">
								<input type="text" class="form-control" id="inputTotalMax" name="inputTotalMax" value="${maxTotalSearch}" placeholder="" />
							</div>
						</div>
	                </div>
	            </div>
            
	            <div class="row" style="margin-top: 50px;">
	            	<div class="col-lg-12">
	            		<g:submitButton id="boton" name="boton" class="btn btn-success" value="Crear Búsqueda" />
	            	</div>
	            </div>
	    	</g:form>
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
	            width: 300,
	            showLabels: true,
	            isRange : true
	        });
	    });
	</script>

</body>

</html>
