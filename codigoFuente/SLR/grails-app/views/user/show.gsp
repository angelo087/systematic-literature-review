<%@ page import="es.uca.pfc.User" %>
<%@ page import="es.uca.pfc.UserProfile" %>
<!DOCTYPE html>
<html lang="es">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>
    	<g:if test="${isMyProfile}">
    		SLR | Mi Perfil
    	</g:if>
    	<g:else>
    		SLR | ${profileInstance.display_name}
    	</g:else>
    </title>
    
    <%-- CSS --%>
    <g:render template="css" contextPath="/"/>
    <link href="${resource(dir: 'bower_components', file: 'datatables-plugins/integration/bootstrap/3/dataTables.bootstrap.css')}" rel="stylesheet">
    <link href="${resource(dir: 'bower_components', file: 'datatables-responsive/css/dataTables.responsive.css')}" rel="stylesheet">
    
    <script type="text/javascript">
    function changeColourFriend()
	{
    	if(document.getElementById('btn_friend').className == "btn btn-success btn-lg btn-block")
		{
    		document.getElementById('btn_friend').className = "btn btn-danger btn-lg btn-block";
    		document.getElementById('btn_friend').innerHTML = "<i class='glyphicon glyphicon-remove'></i> Eliminar amigo</button>";
		}
    	else
    	{
    		document.getElementById('btn_friend').className = "btn btn-success btn-lg btn-block";
    		document.getElementById('btn_friend').innerHTML = "<i class='glyphicon glyphicon-ok'></i> Sois amigos";
    	}
	}

    function changeColourFriend2()
	{
    	if(document.getElementById('btn_friend_2').className == "btn btn-default btn-lg btn-block")
    	{
    		document.getElementById('btn_friend_2').className = "btn btn-success btn-lg btn-block";
    		document.getElementById('btn_friend_2').innerHTML = "<i class='glyphicon glyphicon-ok'></i> Agregar a mis amigos";
    	}
    	else
	    {
    		document.getElementById('btn_friend_2').className = "btn btn-default btn-lg btn-block";
    		document.getElementById('btn_friend_2').innerHTML = "<i class='glyphicon glyphicon-thumbs-up'></i> Agregar a mis amigos";
		}
	}
    </script>
    
</head>

<body>

    <div id="wrapper">

        <%-- Head --%>
        <g:render template="head" contextPath="/"/>
        
        <div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <g:if test="${isMyProfile}">
			    		<h1 class="page-header">Mi Perfil</h1>
			    	</g:if>
			    	<g:else>
			    		<h1 class="page-header">Perfil de ${profileInstance.display_name}</h1>
			    	</g:else>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            
            <div class="row">
                <div class="col-lg-3">
                
                	<div style="margin-bottom: 10px;"><img src="${profileInstance.url_foto}" alt="" width="220" height="220" style="border: black solid thin;" /></div>
					<%-- Code Mendeley --%>
					<p><a target="parent" href="${profileInstance.link}"><img border="0" src="http://www.mendeley.com/embed/icon/1/blue/big" alt=""/></a></p>
					
					<g:if test="${!isMyProfile}">
						<g:if test="${isMyFriend == 'S'}">
							<%--<button id="btn_friend" type="button" class="btn btn-success btn-lg btn-block" onmouseover="changeColourFriend();" onmouseout="changeColourFriend();"><i class="glyphicon glyphicon-ok"></i> Sois amigos</button>--%>
							<g:link elementId="btn_friend" id="btn_friend" type="button" class="btn btn-success btn-lg btn-block" onmouseover="changeColourFriend();" onmouseout="changeColourFriend();" controller="user" action="removeRequestFriends" params="[guid: "${profileInstance.guid}"]"><i class="glyphicon glyphicon-ok"></i> Sois amigos</g:link>
						</g:if>
						<g:elseif test="${isMyFriend == 'P'}">
							<button type="button" class="btn btn-primary btn-lg btn-block disabled"><i class="glyphicon glyphicon-time"></i> Esperando respuesta</button>
						</g:elseif>
						<g:else>
							<g:link elementId="btn_friend_2" id="btn_friend_2" type="button" class="btn btn-default btn-lg btn-block" onmouseover="changeColourFriend2();" onmouseout="changeColourFriend2();" controller="user" action="addRequestFriends" params="[guid: "${profileInstance.guid}"]"><i class="glyphicon glyphicon-thumbs-up"></i> Agregar a mis amigos</g:link>
						</g:else>
					</g:if>
					<g:else>
						<g:link type="buton" class="btn btn-primary" controller="user" action="synchronizeUserProfile" onclick="loading('Sincronizando Perfil con Mendeley...');" params="[guid: "${profileInstance.guid}"]">Sincronizar (Mendeley)</g:link>
					</g:else>
					
                </div>
                
                <div class="col-lg-9">
                
                	<div class="bs-example bs-example-tabs" style="width: 100%;">
						<ul id="myTab" class="nav nav-tabs">
							<li class="active"><a href="#datospersonales" data-toggle="tab">Datos Personales</a></li>								
							<g:if test="${profileInstance.user.id != User.get(sec.loggedInUserInfo(field:"id").toString().toLong()).id}">
								<li><a href="#misslrs" data-toggle="tab">SLR's creados (${profileInstance.slrs.size()})</a></li>
								<li><a href="#educations" data-toggle="tab">Estudios (${profileInstance.educations.size()})</a></li>
								<li><a href="#myfriends" data-toggle="tab"><i class="fa fa-users"></i> Amigos (${profileInstance.friends.size()})</a></li>
							</g:if>
							<g:else>
								<li><a href="#misslrs" data-toggle="tab">Mis SLR's (${profileInstance.slrs.size()})</a></li>
								<li><a href="#educations" data-toggle="tab">Mis Estudios (${profileInstance.educations.size()})</a></li>
								<li><a href="#myfriends" data-toggle="tab"><i class="fa fa-users"></i> Mis Amigos (${profileInstance.friends.size()})</a></li>
							</g:else>
						</ul>
					</div>
					<div id="myTabContent" class="tab-content">
					
						<%-- DATOS PERSONALES --%>
						<div class="tab-pane fade in active" id="datospersonales">
							<div style="float: left; margin-left: 20px; margin-top:10px; clear: both;">
								<div style="min-width: 50px; float: left;">
									<p><b>Nombre: </b><g:fieldValue bean="${profileInstance}" field="first_name"/></p>
									<p><b>Apellidos: </b><g:fieldValue bean="${profileInstance}" field="last_name"/></p>
									<p><b>Email: </b><g:fieldValue bean="${profileInstance.user}" field="username"/></p>
									<g:if test="${profileInstance.link.toString().contains("http://")}">	
										<p><b>Web: </b><a target="parent" href="${profileInstance.link.toString()}">${profileInstance.link}</a></p>
									</g:if>
									<g:else>
										<p><b>Web: </b><a target="parent" href="http://${profileInstance.link.toString()}">${profileInstance.link}</a></p>
									</g:else>
									<p><b>Fecha Registro: </b>${formatDate(format: 'dd MMM, yyyy - HH:mm', date: profileInstance.fechaRegistro)}</p>
									<p><b>Última Conexión: </b>${lastTime}</p>
								</div>
								<div style="min-width: 50px; float: left;">
									<p><b>Intereses: </b>${profileInstance.research_interests}</p>
									<p><b>Estado académico: </b>${profileInstance.academic_status}</p>
									<p><b>Localización: </b>${profileInstance.locationName}</p>
									<p><b>Localización: </b>${profileInstance.discipline}</p>
									
								</div>
								<div style="float: left; margin-top:10px; clear: both;">
									<p><b>Información Biográfica: </b></p>
									<g:if test="${profileInstance.biography.equals("")}">
										Nothing
									</g:if>
									<g:else>
										<g:fieldValue bean="${profileInstance}" field="biography"/>
									</g:else>
								</div>
							</div>
						</div>
						
						<%-- Educations --%>
						<div class="tab-pane fade" id="educations">
							
							<div class="panel panel-default">
		                        <div class="panel-heading">
		                            Estudios
		                        </div>
		                        <!-- /.panel-heading -->
		                        <div class="panel-body">
		                            <div class="dataTable_wrapper">
		                                <table class="table table-striped table-bordered table-hover" id="dataTables-educations">
		                                    <thead>
		                                        <tr>
		                                            <th>Degree</th>
		                                            <th>Institution</th>
		                                            <th>WebSite</th>
		                                            <th>Start Date</th>
		                                            <th>End Date</th>
		                                        </tr>
		                                    </thead>
		                                    <tbody>
		                                        <g:each in="${profileInstance.educations}" var="educationInstance">
		                                        	<tr class="gradeX">
		                                        		<td>${educationInstance.degree}</td>
		                                        		<td>${educationInstance.institution}</td>
		                                        		<td>${educationInstance.website}</td>
		                                        		<td>${formatDate(format: 'dd/MMM/yyyy', date: educationInstance.start_date)}</td>
		                                        		<td>${formatDate(format: 'dd/MMM/yyyy', date: educationInstance.end_date)}</td>
		                                        	</tr>
		                                        </g:each>
		                                    </tbody>
		                                </table>
		                            </div>
		                            
		                        </div>
		                        <!-- /.panel-body -->
		                    </div>
							
						</div>
						
						
						<%-- SLR's --%>
						<div class="tab-pane fade" id="misslrs">
							
							<div class="panel panel-default">
		                        <div class="panel-heading">
		                            Systematic Literature Review
		                        </div>
		                        <!-- /.panel-heading -->
		                        <div class="panel-body">
		                            <div class="dataTable_wrapper">
		                                <table class="table table-striped table-bordered table-hover" id="dataTables-slrs">
		                                    <thead>
		                                        <tr>
		                                            <th>Título</th>
		                                            <th>Fecha Creación</th>
		                                            <th>Última Modificación</th>
		                                            <th>Referencias incluidas</th>
		                                            <th>Total Referencias</th>
		                                        </tr>
		                                    </thead>
		                                    <tbody>
		                                        <g:each in="${profileInstance.slrs}" var="slrInstance">
		                                        	<tr class="gradeX">
		                                        		<td>${slrInstance.title}</td>
		                                        		<td>${formatDate(format: 'dd/MMM/yyyy HH:mm', date: slrInstance.submitDate)}</td>
		                                        		<td>${formatDate(format: 'dd/MMM/yyyy HH:mm', date: slrInstance.lastModified)}</td>
		                                        		<td>--</td>
		                                        		<td>--</td>
		                                        	</tr>
		                                        </g:each>
		                                    </tbody>
		                                </table>
		                            </div>
		                            
		                        </div>
		                        <!-- /.panel-body -->
		                    </div>
							
						</div>
						
						
						<%-- SLR's --%>
						<div class="tab-pane fade" id="myfriends">
							
							<div class="panel panel-default">
		                        <div class="panel-heading">
		                            Amigos
		                        </div>
		                        <!-- /.panel-heading -->
		                        <div class="panel-body">
		                            <div class="dataTable_wrapper">
		                                <table class="table table-striped table-bordered table-hover" id="dataTables-friends">
		                                    <thead>
		                                        <tr>
		                                            <th>Nombre</th>
		                                            <th>Email</th>
		                                            <th>Disciplina</th>
		                                            <th>Fecha Registro</th>
		                                            <th>Última conexión</th>
		                                        </tr>
		                                    </thead>
		                                    <tbody>
		                                        <g:each in="${profileInstance.friends}" var="friendInstance">
		                                        	<tr class="gradeX">
		                                        		<td><g:link controller="user" action="show" params="[guid: "${friendInstance.guid}"]">${friendInstance.display_name}</g:link></td>
		                                        		<td>${friendInstance.user.username}</td>
		                                        		<td>${friendInstance.discipline}</td>
		                                        		<td>${formatDate(format: 'dd/MMM/yyyy HH:mm', date: friendInstance.fechaRegistro)}</td>
		                                        		<td>${formatDate(format: 'dd/MMM/yyyy HH:mm', date: friendInstance.ultimaConexion)}</td>
		                                        	</tr>
		                                        </g:each>
		                                    </tbody>
		                                </table>
		                            </div>
		                            
		                        </div>
		                        <!-- /.panel-body -->
		                    </div>
							
						</div>
						
						
					</div>
                
                </div>
            </div>
                        
        </div>
        <!-- /#page-wrapper -->

    </div>
    <!-- /#wrapper -->

    <%-- JavaScript --%>
    <g:render template="javascript" contextPath="/" />
	
	<script src="${resource(dir: 'bower_components', file: 'datatables/media/js/jquery.dataTables.min.js')}"></script>
	<script src="${resource(dir: 'bower_components', file: 'datatables-plugins/integration/bootstrap/3/dataTables.bootstrap.min.js')}"></script>
	
	<script type="text/javascript">
	    $(document).ready(function() {
	        $('#dataTables-educations').DataTable({
	                responsive: true
	        });
	        $('#dataTables-slrs').DataTable({
                responsive: true
        	});
	        $('#dataTables-friends').DataTable({
                responsive: true
        	});
	    });
    </script>

</body>

</html>
