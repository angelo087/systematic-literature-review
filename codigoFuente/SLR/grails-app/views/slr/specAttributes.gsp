<%@ page import="es.uca.pfc.User" %>
<%@ page import="es.uca.pfc.Slr" %>
<%@ page import="es.uca.pfc.SpecificAttribute" %>
<%@ page import="es.uca.pfc.SpecificAttributeMultipleValue" %>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>SLR | Specific Attributes</title>

	<%-- CSS --%>
    <g:render template="css" contextPath="/"/>
    
    <script type="text/javascript">
		function getIdAttribute(id)
		{
			document.getElementById("idAttribute").value = id.toString();
			document.getElementById('divErrorAttribute').style.display = "none";
		}
		function loadModal()
		{
			if(${null != errorAttribute && errorAttribute != ""})
			{
				$('#myModalAttribute').modal('show');
				if (${tipoAttribute == "list"})
				{
					document.getElementById("opciones").disabled = false;
				}
			}
			else if (${successAttribute})
			{
				document.getElementById('divSuccessAttribute').style.display = "";
				//setTimeout(hideSuccess, 5000);
				setTimeout(function(){
					document.getElementById('divSuccessAttribute').style.display = "none";
				}, 5000);
			}
		}
		function getIdSlr(id)
		{
			document.getElementById("guidSlr").value = id.toString();
			document.getElementById('divErrorAttribute').style.display = "none";
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

	<%--Ventana modal crear atributo --%>
	<div class="modal fade" id="myModalAttribute" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    	<div class="modal-dialog">
	    	<div class="modal-content">
	    		<div class="modal-header">
      				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
      				<h4 class="modal-title" id="myModalLabel">Crear Atributo Específico</h4>
			    </div>
		    	<g:form controller="specificAttribute" action="save" method="POST" name="myFormAttribute" id="myFormAttribute">
		    		<g:hiddenField name="guidSlr" value="${slrInstance.guid}" />
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
    
    <%-- Ventana modal para eliminar atributo --%>
    <div class="modal fade" id="myModalDrop" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="myModalLabel">Eliminar Atributo</h4>
				</div>
				<%--<g:form url="[action:'delete']" method="DELETE"> --%>
				<g:form controller="specificAttribute" action="delete" id="myFormDelete" name="myFormDelete" method="DELETE">
					<g:hiddenField name="idAttribute" value="0" />
					<g:hiddenField name="guidSlr" value="${slrInstance.guid}" />
		    		<div class="modal-body">
						La eliminación de este atributo supondrá la eliminación de dichos atributos en cada una de las referencias.
						<p> </p>
						<p>¿Deseas eliminar este Atributo?</p>
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
                    <h1 class="page-header">Atrib. Especificos en SLR: ${slrInstance.title}</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
                        
            <div class="row">
	            <div id="divSuccessAttribute" class="alert alert-success" role="alert" style="display: none;"><i class="fa fa-check fa-fw"></i> Atributo creado correctamente.</div>        	
            	<div class="col-lg-12">
                  	<div style="margin-top: 5px; margin-bottom: 20px;">
                  		<button type="button" class="btn btn-success" data-toggle="modal" data-target="#myModalAttribute">Crear Atributo</button>
                  		<button type="button" class="btn btn-primary" disabled="disabled">Sincronizar (Mendeley)</button>
	            	</div>
					<table class="table table-striped table-bordered table-hover" id="dataTables-myattributes">
                        <thead>
                            <tr>
                                <th>Nombre</th>
                                <th>Tipo:</th>
                                <th>Fecha creación:</th>
                                <th>Última moficiación:</th>
                                <th>Opciones</th>
                                <th>Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <g:each in="${specAttributesListInstance}" var="attributeInstance">
                            	<tr class="gradeX">
                            		<td>${attributeInstance.name}</td>
                            		<td>${attributeInstance.tipo}</td>
                            		<td>${formatDate(format: 'HH:mm - dd/MMM/yyyy', date: attributeInstance.submitDate)}</td>
                            		<td>${formatDate(format: 'HH:mm - dd/MMM/yyyy', date: attributeInstance.modifyDate)}</td>
                            		<td>
                            			<g:if test="${attributeInstance.tipo != "list"}">
                            				--
                            			</g:if>
                            			<g:else>
                            				<g:each in="${attributeInstance.options}" var="optionInstance">
                            					<p>${optionInstance}</p>
                            				</g:each>
                            			</g:else>
                            		</td>
                            		<td>
                            			<g:if test="${slrInstance.noDrop == false}">
											<p><button type="button" class="btn btn-link" data-toggle="modal" data-target="#myModalDrop" onclick="getIdAttribute('${attributeInstance.id.toString()}')">Eliminar Atributo</button></p>
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
	        $('#dataTables-myattributes').DataTable({
	                responsive: true
	        });
	    });
    </script>

</body>

</html>
