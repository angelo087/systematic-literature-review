<%@ page import="es.uca.pfc.User" %>
<%@ page import="es.uca.pfc.Slr" %>
<%@ page import="es.uca.pfc.Criterion" %>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>SLR | Criterios</title>

	<%-- CSS --%>
    <g:render template="css" contextPath="/"/>

	<script type="text/javascript">
		function getIdCriterion(id)
		{
			document.getElementById("idCriterion").value = id.toString();
			document.getElementById('divErrorCriterion').style.display = "none";
		}	
		function loadModal()
		{
			if(${null != errorCriterion && errorCriterion != ""})
			{
				$('#myModalCriterion').modal('show');
			}
			else if (${successCriterion})
			{
				document.getElementById('divSuccessCriterion').style.display = "";
				//setTimeout(hideSuccess, 5000);
				setTimeout(function(){
					document.getElementById('divSuccessCriterion').style.display = "none";
				}, 5000);
			}
		}
		function getIdSlr(id)
		{
			document.getElementById("guidSlr").value = id.toString();
			document.getElementById('divErrorCriterion').style.display = "none";
		}
	</script>
</head>

<body onload="loadModal();">

	<%--Ventana modal crear criterio --%>
	<div class="modal fade" id="myModalCriterion" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    	<div class="modal-dialog">
	    	<div class="modal-content">
	    		<div class="modal-header">
      				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
      				<h4 class="modal-title" id="myModalLabel">Crear Criterio</h4>
			    </div>
		    	<g:form controller="criterion" action="save" method="POST" name="myFormCriterion" id="myFormCriterion">
		    		<g:hiddenField name="guidSlr" value="${slrInstance.guid}" />
		    		<div class="modal-body">
				    	<g:if test="${null != errorCriterion && !errorCriterion.equals("")}">
					    	<div id="divErrorCriterion" class="alert alert-danger" role="alert"><i class="fa fa-remove fa-fw"></i> ${errorCriterion}</div>
				    	</g:if>
				    	<div class="form-inline">
				    		<table>
				    			<tr>
				    				<td><b>Nombre:</b></td>
				    				<td><input id="nombre" type="text" name="nombre" class="form-control" value="${nombreCriterion}" /></td>
				    			</tr>
				    			<tr>
				    				<td><b>Descripción:</b></td>
				    				<td><textarea id="descripcion" class="form-control" name="descripcion" style="resize: none; width: 400px; height: 80px;">${descripcionCriterion}</textarea></td>
				    			</tr>
				    		</table>
				    	</div>
				    </div>
				    <div class="modal-footer">
						<button class="btn btn-default" data-dismiss="modal" type="button">Close</button>
						<g:submitButton name="create" class="btn btn-primary" value="Crear Criterio"/>
					</div>
				</g:form>
		    </div>
		</div>
    </div>

	<%-- Ventana modal para eliminar criterio --%>
    <div class="modal fade" id="myModalDrop" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="myModalLabel">Eliminar Criterio</h4>
				</div>
				<%--<g:form url="[action:'delete']" method="DELETE"> --%>
				<g:form controller="criterion" action="delete" id="myFormDelete" name="myFormDelete" method="DELETE">
					<g:hiddenField name="idCriterion" value="0" />
					<g:hiddenField name="guidSlr" value="${slrInstance.guid}" />
		    		<div class="modal-body">
						La eliminación de este criterio supondrá la asignación del criterio "included" en aquellas referencias que tengan el criterio que deseas eliminar.
						<p> </p>
						<p>¿Deseas eliminar este Criterio?</p>
					</div>
					<div class="modal-footer">
						<button class="btn btn-default" data-dismiss="modal" type="button">No</button>
						<g:submitButton id="boton" name="boton" class="btn btn-primary" value="Sí"/>
					</div>
				</g:form>
			</div>
		</div>
	</div>
	
    <div id="wrapper">

        <%-- Head --%>
        <g:render template="head" contextPath="/"/>
        
        <div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">Criterios en SLR: ${slrInstance.title}</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
                        
            <div class="row">
                <div id="divSuccessCriterion" class="alert alert-success" role="alert" style="display: none;"><i class="fa fa-check fa-fw"></i> Criterio creado correctamente.</div>
            	<div class="col-lg-12">
                  	<div style="margin-top: 5px; margin-bottom: 20px;">
                  		<button type="button" class="btn btn-success" data-toggle="modal" data-target="#myModalCriterion">Crear Criterio</button>
                  		<button type="button" class="btn btn-primary" disabled="disabled">Sincronizar (Mendeley)</button>
	            	</div>
					<table class="table table-striped table-bordered table-hover" id="dataTables-mycriterions">
                        <thead>
                            <tr>
                                <th>Nombre</th>
                                <th>Fecha creación:</th>
                                <th>Última moficiación:</th>
                                <th>Nomenclatura (Mendeley)</th>
                                <th>Referencias asignadas:</th>
                                <th>Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <g:each in="${criterionListInstance}" var="criterionInstance">
                            	<g:set var="contReference" value="0" />
                            	<tr class="gradeX">
                            		<td>${criterionInstance.name}</td>
                            		<td>${formatDate(format: 'HH:mm - dd/MMM/yyyy', date: criterionInstance.submitDate)}</td>
                            		<td>${formatDate(format: 'HH:mm - dd/MMM/yyyy', date: criterionInstance.modifyDate)}</td>
                            		<td>${criterionInstance.nomenclatura}</td>
                            		<td>
                            			<g:if test="${null == totalReferences.get(criterionInstance.name)}">
                            				0
                            			</g:if>
                            			<g:else>
                            				${totalReferences.get(criterionInstance.name).value}
                            			</g:else>
                            		</td>
                            		<td>
                            			<g:if test="${slrInstance.noDrop == false && criterionInstance.nomenclatura != "cr_included"}">
											<p><button type="button" class="btn btn-link" data-toggle="modal" data-target="#myModalDrop" onclick="getIdCriterion('${criterionInstance.id.toString()}')">Eliminar Criterio</button></p>
										</g:if>
                            		</td>
                            	</tr>
                            </g:each>
                        </tbody>
                    </table>	
                    
                </div>

            </div>
            <!-- /.row -->
        </div>
        <!-- /#page-wrapper -->

    </div>
    <!-- /#wrapper -->

    <%-- JavaScript --%>
    <g:render template="javascript" contextPath="/"/>
	
	<script src="${resource(dir: 'bower_components', file: 'datatables/media/js/jquery.dataTables.min.js')}"></script>
	<script src="${resource(dir: 'bower_components', file: 'datatables-plugins/integration/bootstrap/3/dataTables.bootstrap.min.js')}"></script>
	
	<script type="text/javascript">
	    $(document).ready(function() {
	        $('#dataTables-mycriterions').DataTable({
	                responsive: true
	        });
	    });
    </script>

</body>

</html>
