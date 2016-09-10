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

    <title>SLR | My List</title>

	<%-- CSS --%>
    <g:render template="css" contextPath="/"/>

	<script type="text/javascript">
		function loadModal()
		{
			if(${null != error && error != ""})
			{
				$('#myModal').modal('show');
			}
			else if(${null != errorCriterion && errorCriterion != ""})
			{
				$('#myModalCriterion').modal('show');
			}
			else if(${null != errorAttribute && errorAttribute != ""})
			{
				$('#myModalAttribute').modal('show');
				if (${tipoAttribute == 'list'})
				{
					document.getElementById("opciones").disabled = false;
				}
			}
			else if(${null != errorQuestion && errorQuestion != ""})
			{
				$('#myModalQuestion').modal('show');
			}
			else if (${success})
			{
				document.getElementById('divSuccess').style.display = "";
				//setTimeout(hideSuccess, 5000);
				setTimeout(function(){
					document.getElementById('divSuccess').style.display = "none";
				}, 5000);
			}
			else if (${successCriterion})
			{
				document.getElementById('divSuccessCriterion').style.display = "";
				//setTimeout(hideSuccess, 5000);
				setTimeout(function(){
					document.getElementById('divSuccessCriterion').style.display = "none";
				}, 5000);
			}
			else if (${successAttribute})
			{
				document.getElementById('divSuccessAttribute').style.display = "";
				//setTimeout(hideSuccess, 5000);
				setTimeout(function(){
					document.getElementById('divSuccessAttribute').style.display = "none";
				}, 5000);
			}
			else if (${successQuestion})
			{
				document.getElementById('divSuccessQuestion').style.display = "";
				//setTimeout(hideSuccess, 5000);
				setTimeout(function(){
					document.getElementById('divSuccessQuestion').style.display = "none";
				}, 5000);
			}
		}
		function getIdSlr(id)
		{
			document.getElementById("guidSlr").value = id.toString();
			document.getElementById("guidSlrCriterion").value = id.toString();
			document.getElementById("guidSlrAttribute").value = id.toString();
			document.getElementById("guidSlrQuestion").value = id.toString();
			document.getElementById('divError').style.display = "none";
		}
		function typeChange()
		{
			if(document.getElementById("tipo").value == "list")
			{
				document.getElementById("opciones").disabled = false;
			}
			else
			{
				document.getElementById("opciones").disabled = true;
			}
		}
	</script>
	
</head>

<body onload="loadModal();">

	<%--Ventana modal crear slr --%>
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    	<div class="modal-dialog">
	    	<div class="modal-content">
	    		<div class="modal-header">
      				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
      				<h4 class="modal-title" id="myModalLabel">Crear SLR</h4>
			    </div>
		    	<g:form controller="slr" action="save" method="POST" name="myForm" id="myForm">
		    		<div class="modal-body">
				    	<g:if test="${null != error && !error.equals("")}">
					    	<div id="divError" class="alert alert-danger" role="alert"><i class="fa fa-remove fa-fw"></i> ${error}</div>
				    	</g:if>
				    	<div class="form-inline">
				    		<table>
				    			<tr>
				    				<td><b>Titulo:</b></td>
				    				<td><input id="titulo" type="text" name="titulo" class="form-control" value="${tituloSlr}" /></td>
				    			</tr>
				    			<tr>
				    				<td><b>Justificación:</b></td>
				    				<td><textarea id="justificacion" class="form-control" name="justificacion" style="resize: none; width: 400px; height: 80px;">${justificacionSlr}</textarea></td>
				    			</tr>
				    		</table>
				    	</div>
				    </div>
				    <div class="modal-footer">
						<button class="btn btn-default" data-dismiss="modal" type="button">Close</button>
						<g:submitButton name="create" class="btn btn-primary" value="Crear Slr"/>
					</div>
				</g:form>
		    </div>
		</div>
    </div>
    
    <%--Ventana modal crear criterio --%>
	<div class="modal fade" id="myModalCriterion" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    	<div class="modal-dialog">
	    	<div class="modal-content">
	    		<div class="modal-header">
      				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
      				<h4 class="modal-title" id="myModalLabel">Crear Criterio</h4>
			    </div>
		    	<g:form controller="criterion" action="save" method="POST" name="myFormCriterion" id="myFormCriterion">
		    		<g:hiddenField name="guidSlrCriterion" value="${guidSlrError}" />
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
    
    <%--Ventana modal crear atributo --%>
	<div class="modal fade" id="myModalAttribute" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    	<div class="modal-dialog">
	    	<div class="modal-content">
	    		<div class="modal-header">
      				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
      				<h4 class="modal-title" id="myModalLabel">Crear Atributo Específico</h4>
			    </div>
		    	<g:form controller="specificAttribute" action="save" method="POST" name="myFormAttribute" id="myFormAttribute">
		    		<g:hiddenField name="guidSlrAttribute" value="${guidSlrError}" />
		    		<div class="modal-body">
				    	<g:if test="${null != errorAttribute && !errorAttribute.equals("")}">
					    	<div id="divErrorAttribute" class="alert alert-danger" role="alert"><i class="fa fa-remove fa-fw"></i> ${errorAttribute}</div>
				    	</g:if>
				    	<div class="form-inline">
				    		<table>
				    			<tr>
				    				<td><b>Nombre:</b></td>
				    				<td><input id="nombre" type="text" name="nombre" class="form-control" value="${nombreAttribute}" /></td>
				    			</tr>
				    			<tr>
				    				<td><b>Tipo:</b></td>
				    				<td>
				    					<select id="tipo" name="tipo" onchange="typeChange();">
				    						<g:if test="${tipoAttribute == 'string'}">
					    						<option value="string" selected="selected">String</option>
					    					</g:if>
					    					<g:else>
					    						<option value="string">String</option>
					    					</g:else>
					    					<g:if test="${tipoAttribute == 'number'}">
					    						<option value="number" selected="selected">Number</option>
					    					</g:if>
					    					<g:else>
					    						<option value="number">Number</option>
					    					</g:else>
					    					<g:if test="${tipoAttribute == 'list'}">
					    						<option value="list" selected="selected">List</option>
					    					</g:if>
					    					<g:else>
					    						<option value="list">List</option>
					    					</g:else>
				    					</select>
				    				</td>
				    			</tr>
				    			<tr>
				    				<td><b>Opciones:</b></td>
				    				<td>
				    					<g:if test="${opcionesAttribute == null || opcionesAttribute == 'null'}">
				    						<textarea id="opciones" class="form-control" name="opciones" style="resize: none; width: 400px; height: 80px;" disabled="disabled"></textarea>
				    					</g:if>
				    					<g:else>
				    						<textarea id="opciones" class="form-control" name="opciones" style="resize: none; width: 400px; height: 80px;" disabled="disabled">${opcionesAttribute}</textarea>
				    					</g:else>
				    				</td>
				    			</tr>
				    		</table>
				    	</div>
				    </div>
				    <div class="modal-footer">
						<button class="btn btn-default" data-dismiss="modal" type="button">Close</button>
						<g:submitButton name="create" class="btn btn-primary" value="Crear Atributo"/>
					</div>
				</g:form>
		    </div>
		</div>
    </div>
    
    <%-- Ventana modal para eliminar slr --%>
    <div class="modal fade" id="myModalDrop" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="myModalLabel">Eliminar SLR</h4>
				</div>
				<%--<g:form url="[action:'delete']" method="DELETE"> --%>
				<g:form controller="slr" action="delete" id="myFormDelete" name="myFormDelete" method="DELETE">
					<g:hiddenField name="guidSlr" value="0" />
					<div class="modal-body">
						¿Deseas eliminar este SLR?
					</div>
					<div class="modal-footer">
						<button class="btn btn-default" data-dismiss="modal" type="button">No</button>
						<g:submitButton id="boton" name="boton" class="btn btn-primary" value="Sí"/>
					</div>
				</g:form>
			</div>
		</div>
	</div>


	<%--Ventana modal crear question --%>
	<div class="modal fade" id="myModalQuestion" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    	<div class="modal-dialog">
	    	<div class="modal-content">
	    		<div class="modal-header">
      				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
      				<h4 class="modal-title" id="myModalLabel">Crear Pregunta de Investigación</h4>
			    </div>
		    	<g:form controller="researchQuestion" action="save" method="POST" name="myFormQuestion" id="myFormQuestion">
		    		<g:hiddenField name="guidSlrQuestion" value="${guidSlrError}" />
		    		<div class="modal-body">
				    	<g:if test="${null != errorQuestion && !errorQuestion.equals("")}">
					    	<div id="divErrorQuestion" class="alert alert-danger" role="alert"><i class="fa fa-remove fa-fw"></i> ${errorQuestion}</div>
				    	</g:if>
				    	<div class="form-inline">
				    		<table>
				    			<tr>
				    				<td><b>Enunciado:</b></td>
				    				<td><input id="enunciado" type="text" name="enunciado" class="form-control" value="${enunciadoQuestion}" /></td>
				    			</tr>
				    		</table>
				    	</div>
				    </div>
				    <div class="modal-footer">
						<button class="btn btn-default" data-dismiss="modal" type="button">Close</button>
						<g:submitButton name="create" class="btn btn-primary" value="Crear Pregunta Investigacion"/>
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
                    <h1 class="page-header">Mis SLR's</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>

            <div class="row">
		    	<div id="divSuccess" class="alert alert-success" role="alert" style="display: none;"><i class="fa fa-check fa-fw"></i> SLR creado correctamente.</div>
            	<div id="divSuccessCriterion" class="alert alert-success" role="alert" style="display: none;"><i class="fa fa-check fa-fw"></i> Criterio creado correctamente.</div>
            	<div id="divSuccessAttribute" class="alert alert-success" role="alert" style="display: none;"><i class="fa fa-check fa-fw"></i> Atributo creado correctamente.</div>
            	<div id="divSuccessQuestion" class="alert alert-success" role="alert" style="display: none;"><i class="fa fa-check fa-fw"></i> Pregunta creada correctamente.</div>
            	<div class="col-lg-12">
                	<div style="margin-top: 5px; margin-bottom: 20px;">
	                	<button type="button" class="btn btn-success" data-toggle="modal" data-target="#myModal">Crear SLR</button>
	                	<g:link type="button" class="btn btn-primary" controller="slr" action="syncronizeListSlrMendeley"  onclick="loading('Sincronizando con Mendeley...');">Sincronizar (Mendeley)</g:link>
	            	</div>
					<table class="table table-striped table-bordered table-hover" id="dataTables-myslrs">
                        <thead>
                            <tr>
                                <th>Titulo</th>
                                <th>Estado</th>
                                <th>Nº Visitas</th>
                                <th>Fecha creación</th>
                                <th>Nº Búsquedas</th>
                                <th>Nº Referencias</th>
                                <th>Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <g:each in="${slrListInstance}" var="slrInstance">
                            	<tr class="gradeX">
                            		<td><g:link controller="slr" action="show" params="[guidSlr: "${slrInstance.guid}"]">${slrInstance.title}</g:link></td>
                            		<td>${slrInstance.state}</td>
                            		<td>${slrInstance.numVisits}</td>
                            		<td>${formatDate(format: 'MMM/yyyy', date: slrInstance.submitDate)}</td>
                            		<td>
                            			<g:if test="${slrInstance.searchs.size() > 0}">
                            				<g:link controller="slr" action="searchs" params="[guid: "${slrInstance.guid}"]">${slrInstance.searchs.size()}</g:link>
                            			</g:if>
                            			<g:else>
                            				${slrInstance.searchs.size()}
                            			</g:else>
                            		</td>
                            		<td>
                            			<g:if test="${slrInstance.totalReferences > 0}">
                            				<g:link controller="slr" action="references" params="[guid: "${slrInstance.guid}"]">${slrInstance.totalReferences}</g:link>
                            			</g:if>
                            			<g:else>
                            				${slrInstance.totalReferences}
                            			</g:else>
                            		</td>
                            		<td>
										<g:link title="Búsquedas" type="button" class="btn btn-outline btn-primary btn-circle" controller="slr" action="searchs" params="[guid: "${slrInstance.guid}"]"><i class="glyphicon glyphicon-search"></i></g:link>
										<g:link title="Criterios" class="btn btn-outline btn-primary btn-circle" controller="slr" action="criterions" params="[guid: "${slrInstance.guid}"]"><i class="fa fa-bookmark"></i></g:link>
										<g:link title="Atributos Especificos" class="btn btn-outline btn-primary btn-circle" controller="slr" action="specAttributes" params="[guid: "${slrInstance.guid}"]"><i class="glyphicon glyphicon-tags"></i></g:link>
										<g:link title="Listar preguntas" type="button" class="btn btn-outline btn-primary btn-circle" controller="slr" action="researchQuestions" params="[guid: "${slrInstance.guid}"]"><i class="fa fa-question"></i></g:link>
										<g:if test="${slrInstance.noDrop == false}">
											<button title="Eliminar SLR" type="button" class="btn btn-outline btn-danger btn-circle" data-toggle="modal" data-target="#myModalDrop" onclick="getIdSlr('${slrInstance.guid}')"><i class="fa fa-times"></i></button>
										</g:if>
										<p> </p>
										<g:link title="Exportar a Excel" type="button" class="btn btn-outline btn-success btn-circle" controller="slr" action="exportToExcel" params="[guid: "${slrInstance.guid}"]"><i class="fa fa-file-excel-o"></i></g:link>
										<g:link title="Exportar a PDF" type="button" class="btn btn-outline btn-success btn-circle" controller="slr" action="exportToPdf" params="[guid: "${slrInstance.guid}"]"><i class="fa fa-file-pdf-o"></i></g:link>
										<g:link title="Exportar a Bibtex" type="button" class="btn btn-outline btn-success btn-circle" controller="slr" action="exportToBibTex" params="[guid: "${slrInstance.guid}"]"><i class="fa fa-file-code-o"></i></g:link>
										<g:link title="Gráficos" type="button" class="btn btn-outline btn-success btn-circle" controller="slr" action="graphs" params="[guid: "${slrInstance.guid}"]"><i class="glyphicon glyphicon-stats"></i></g:link>
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
	        $('#dataTables-myslrs').DataTable({
	                responsive: true
	        });
	    });
    </script>

</body>

</html>
