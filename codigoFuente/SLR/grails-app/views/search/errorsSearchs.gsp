<%@ page import="es.uca.pfc.TaskSearch" %>
<!DOCTYPE html>
<html lang="en">

<head>

    <%-- Head Meta --%>
	<g:render template="headMeta" contextPath="/"/>

    <title>SLR | Errors in Searchs</title>

	<%-- CSS --%>
    <g:render template="css" contextPath="/"/>
	
	<script type="text/javascript">
		function showException(strException, code)
		{
			var modal = $('#myModal')
			modal.find('.modal-title').text('Exception with code ' + code.toString())
			modal.find('.modal-body').text(strException.toString())
			modal.modal('show')
		}
	</script>
	
</head>

<body>

	<%--Ventana modal para mostrar excepcion --%>
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    	<div class="modal-dialog">
	    	<div class="modal-content">
	    		<div class="modal-header">
      				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
      				<h4 class="modal-title" id="myModalLabel">Title</h4>
			    </div>
		    		<div class="modal-body" style="height: 250px; overflow-y: auto;">
				    	Texto
				    </div>

			    <div class="modal-footer">
					<button class="btn btn-default" data-dismiss="modal" type="button">Close</button>
				</div>
		    </div>
		</div>
    </div>


    <div id="wrapper">
        <%-- Head --%>
        <g:render template="head" contextPath="/"/>
                
        <div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">Errors in Searchs</h1>
                    
           			<ol class="breadcrumb">
					  <li><g:link controller="index" action="menu">Home</g:link></li>
					  <li class="active">Errors in Searchs</li>
					</ol>
					
                </div>
                <!-- /.col-lg-12 -->
            </div>

            <div class="row">
            	<div class="col-lg-12">
					<table class="table table-striped table-bordered table-hover" id="dataTables-mytaskWithErrors">
                        <thead>
                            <tr>
                                <th>Código</th>
                                <th>Fecha creación</th>
                                <th>Username</th>
                                <th>Error producido</th>
                            </tr>
                        </thead>
                        <tbody>
                            <g:each in="${taskWithErrors}" var="taskInstance">
                            	<tr class="gradeX">
                            		<td>${taskInstance.id}</td>
                            		<td>${formatDate(format: 'dd/MMM/yyyy HH:mm', date: taskInstance.submitDate)}</td>
                            		<td>${taskInstance.username}</td>
                            		<td><button onclick="showException('${taskInstance.strException}', '${taskInstance.id.toString()}');">Mostrar error</button></td>
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
	        $('#dataTables-mytaskWithErrors').DataTable({
	                responsive: true
	        });
	    });
    </script>

</body>

</html>
