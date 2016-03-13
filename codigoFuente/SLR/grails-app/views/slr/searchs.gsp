<%@ page import="es.uca.pfc.User" %>
<%@ page import="es.uca.pfc.Slr" %>
<%@ page import="es.uca.pfc.Search" %>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>SLR | Searchs</title>

	<%-- CSS --%>
    <g:render template="css" contextPath="/"/>

	<script type="text/javascript">
	function getIdSearch(id)
	{
		document.getElementById("guidSearch").value = id.toString();
		document.getElementById('divError').style.display = "none";
	}
	</script>
</head>

<body>

	<%-- Ventana modal para eliminar slr --%>
    <div class="modal fade" id="myModalDrop" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="myModalLabel">Eliminar Búqueda</h4>
				</div>
				<%--<g:form url="[action:'delete']" method="DELETE"> --%>
				<g:form controller="search" action="delete" id="myFormDelete" name="myFormDelete" method="DELETE">
					<g:hiddenField name="guidSearch" value="0" />
					<div class="modal-body">
						La eliminación de esta búsqueda borrará todas las referencias asignadas tanto al SLR como a la propia búsqueda.
						<p> </p>
						<p>¿Deseas eliminar esta Búsqueda?</p>
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
                    <h1 class="page-header">Búsquedas en SLR: ${slrInstance.title}</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
                        
            <div class="row">
                <div class="col-lg-12">
                  	<div style="margin-top: 5px; margin-bottom: 20px;">
                  		<g:link controller="search" action="create" class="btn btn-success" params="[guidSlr:"${slrInstance.guid}"]">Crear búsqueda</g:link>
	                	<button type="button" class="btn btn-primary" disabled="disabled">Sincronizar (Mendeley)</button>
	            	</div>
					<table class="table table-striped table-bordered table-hover" id="dataTables-mysearchs">
                        <thead>
                            <tr>
                                <th>Términos</th>
                                <th>Realizada en:</th>
                                <th>Operador</th>
                                <th>Año Compienzo</th>
                                <th>Año finalización</th>
                                <th>Buscar en: </th>
                                <th>Total Máximo</th>
                                <th>Referencias encontradas:</th>
                                <th>Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <g:each in="${searchListInstance}" var="searchInstance">
                            	<tr class="gradeX">
                            		<td>${searchInstance.terminos}</td>
                            		<td>${searchInstance.fecha}</td>
                            		<td>${searchInstance.operator.name}</td>
                            		<td>${searchInstance.startYear}</td>
                            		<td>${searchInstance.endYear}</td>
                            		<td>${searchInstance.component.name}</td>
                            		<td>${searchInstance.maxTotal}</td>
                            		<td><g:link controller="slr" action="references" params="[guid: "${searchInstance.slr.guid}"]">${searchInstance.references.size()}</g:link></td>
                            		<td>
                            			<g:if test="${slrInstance.noDrop == false}">
											<p><button type="button" class="btn btn-link" data-toggle="modal" data-target="#myModalDrop" onclick="getIdSearch('${searchInstance.guid}')">Eliminar Búsqueda</button></p>
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
	        $('#dataTables-mysearchs').DataTable({
	                responsive: true
	        });
	    });
    </script>

</body>

</html>
