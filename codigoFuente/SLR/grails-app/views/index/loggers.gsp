<%@ page import="es.uca.pfc.User" %>
<%@ page import="es.uca.pfc.UserProfile" %>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>
   		SLR | Mis Loggers
    </title>
    
    <%-- CSS --%>
    <g:render template="css" contextPath="/"/>
    
</head>

<body>

    <div id="wrapper">

        <%-- Head --%>
        <g:render template="head" contextPath="/"/>
        
        <div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12">
		    		<h1 class="page-header">Mis Loggers</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            
            <div class="row">
                
                <div class="col-lg-12">
                
                	<div style="width: 100%;">
						<ul id="myTab" class="nav nav-tabs">
							<li class="${(currentOnglet == 'all' ? 'active' : '')}"><a href="#allLoggers" data-toggle="tab">Todos</a></li>
							<li class="${(currentOnglet == 'my' ? 'active' : '')}"><a href="#myLoggers" data-toggle="tab">Mis Loggers</a></li>
							<li class="${(currentOnglet == 'friend' ? 'active' : '')}"><a href="#friendLoggers" data-toggle="tab">Loggers Amigos</a></li>
						</ul>

						<div id="myTabContent" class="tab-content" style="margin-top: 20px;">
					
						<%-- TODOS LOS LOGGERS --%>
						<div class="tab-pane fade in active" id="allLoggers">
							<div class="chat-panel panel panel-default">
								<div class="panel-heading">
									<i class="fa fa-comments fa-fw"></i>
									Logger
								</div>
								<div class="panel-body" style="min-height: 500px;">
									<ul class="chat">
										<g:if test="${totalAllLoggers == 0}">
											<li class="left clearfix">
												No existen loggers disponibles.
											</li>
										</g:if>
										<g:else>
											<g:each in="${allLoggers}" var="loggerInstance">
												<li class="left clearfix">
													<span class="chat-img pull-left">
														<g:if test="${loggerInstance.tipo.contains('fr-')}">
															<img src="${loggerInstance.friendProfile.url_foto}" alt="User Avatar" class="img-circle" style="width: 50px; height: 50px;" />
														</g:if>
														<g:else>
															<img src="${loggerInstance.profile.url_foto}" alt="User Avatar" class="img-circle" style="width: 50px; height: 50px;" />
														</g:else>
													</span>
													<div class="chat-body clearfix">
														<div class="header">
															<%--<g:if test="${userProfileInstance.id != loggerInstance.profile.id}"> --%>
															<g:if test="${loggerInstance.tipo.contains('fr-')}">
																<strong class="primary-font">${loggerInstance.friendProfile.display_name}</strong>
															</g:if>
															<g:else>
																<strong class="primary-font">Tú</strong>
															</g:else>
															<small class="pull-right text-muted">
																<i class="fa fa-clock-o fa-fw"></i> ${loggerInstance.timeString}
															</small>
														</div>
														<p>
															<g:if test="${loggerInstance.tipo == 'bienvenida'}">
																<img src="${resource(dir:'images/logger',file:'start.png')}" alt="" width="20" height="20" /> ¡Bienvenido a Systematic Literature Review!
															</g:if>
															<g:elseif test="${loggerInstance.tipo == 'fr-bienvenida'}">
																<img src="${resource(dir:'images/logger',file:'start.png')}" alt="" width="20" height="20" /> Se ha registrado en Systematic Literature Review.
															</g:elseif>
															<g:elseif test="${loggerInstance.tipo == 'crear'}">
																<img src="${resource(dir:'images/logger',file:'libro.jpg')}" alt="" width="20" height="20" /> Has creado un nuevo SLR: <g:link controller="slr" action="myList">${loggerInstance.slr.title}</g:link>
															</g:elseif>
															<g:elseif test="${loggerInstance.tipo == 'fr-crear'}">
																<img src="${resource(dir:'images/logger',file:'libro.jpg')}" alt="" width="20" height="20" /> Ha creado un nuevo SLR: ${loggerInstance.slr.title}
															</g:elseif>
															<g:elseif test="${loggerInstance.tipo == 'buscar'}">
																<img src="${resource(dir:'images/logger',file:'lupa.jpg')}" alt="" width="20" height="20" /> Has realizado nuevas busquedas en el SLR: <g:link controller="slr" action="searchs" params="[guid: "${loggerInstance.slr.guid}"]">${loggerInstance.slr.title}</g:link>
															</g:elseif>
															<g:elseif test="${loggerInstance.tipo == 'fr-buscar'}">
																<img src="${resource(dir:'images/logger',file:'lupa.jpg')}" alt="" width="20" height="20" /> Ha realizado nuevas busquedas en el SLR: ${loggerInstance.slr.title}
															</g:elseif>
															<g:elseif test="${loggerInstance.tipo == 'seguir'}">
																<img src="${resource(dir:'images/logger',file:'friend.png')}" alt="" width="20" height="20" /> Has seguido a <g:link controller="user" action="show" params="[guid: "${loggerInstance.friendProfile.guid}"]">${loggerInstance.friendProfile.display_name}</g:link>
															</g:elseif>
															<g:elseif test="${loggerInstance.tipo == 'fr-seguir'}">
																<img src="${resource(dir:'images/logger',file:'friend.png')}" alt="" width="20" height="20" /> Ha seguido a ${loggerInstance.friendFriendProfile.display_name}
															</g:elseif>
															<g:else>
																No definido
															</g:else>
														</p>
													</div>
												</li>
											</g:each>
										</g:else>
									</ul>
								</div>
							</div>
							<g:render template="paginationAllLogger" contextPath="/index"/>
						</div>
						
						<%-- LOGGERS FRIENDS --%>
						<div class="tab-pane fade" id="friendLoggers">
							<div class="chat-panel panel panel-default">
								<div class="panel-heading">
									<i class="fa fa-comments fa-fw"></i>
									Logger de amigos
								</div>
								<div class="panel-body" style="min-height: 500px;">
									<ul class="chat">
										<g:if test="${totalFriendsLoggers == 0}">
											<li class="left clearfix">
												No existen loggers disponibles.
											</li>
										</g:if>
										<g:else>
											<g:each in="${friendsLogers}" var="loggerInstance">
												<li class="left clearfix">
													<span class="chat-img pull-left">
														<g:if test="${loggerInstance.tipo.contains('fr-')}">
															<img src="${loggerInstance.friendProfile.url_foto}" alt="User Avatar" class="img-circle" style="width: 50px; height: 50px;" />
														</g:if>
														<g:else>
															<img src="${loggerInstance.profile.url_foto}" alt="User Avatar" class="img-circle" style="width: 50px; height: 50px;" />
														</g:else>
													</span>
													<div class="chat-body clearfix">
														<div class="header">
															<%--<g:if test="${userProfileInstance.id != loggerInstance.profile.id}"> --%>
															<g:if test="${loggerInstance.tipo.contains('fr-')}">
																<strong class="primary-font">${loggerInstance.friendProfile.display_name}</strong>
															</g:if>
															<g:else>
																<strong class="primary-font">Tú</strong>
															</g:else>
															<small class="pull-right text-muted">
																<i class="fa fa-clock-o fa-fw"></i> ${loggerInstance.timeString}
															</small>
														</div>
														<p>
															<g:if test="${loggerInstance.tipo == 'bienvenida'}">
																<img src="${resource(dir:'images/logger',file:'start.png')}" alt="" width="20" height="20" /> ¡Bienvenido a Systematic Literature Review!
															</g:if>
															<g:elseif test="${loggerInstance.tipo == 'fr-bienvenida'}">
																<img src="${resource(dir:'images/logger',file:'start.png')}" alt="" width="20" height="20" /> Se ha registrado en Systematic Literature Review.
															</g:elseif>
															<g:elseif test="${loggerInstance.tipo == 'crear'}">
																<img src="${resource(dir:'images/logger',file:'libro.jpg')}" alt="" width="20" height="20" /> Has creado un nuevo SLR: <g:link controller="slr" action="myList">${loggerInstance.slr.title}</g:link>
															</g:elseif>
															<g:elseif test="${loggerInstance.tipo == 'fr-crear'}">
																<img src="${resource(dir:'images/logger',file:'libro.jpg')}" alt="" width="20" height="20" /> Ha creado un nuevo SLR: ${loggerInstance.slr.title}
															</g:elseif>
															<g:elseif test="${loggerInstance.tipo == 'buscar'}">
																<img src="${resource(dir:'images/logger',file:'lupa.jpg')}" alt="" width="20" height="20" /> Has realizado nuevas busquedas en el SLR: <g:link controller="slr" action="searchs" params="[guid: "${loggerInstance.slr.guid}"]">${loggerInstance.slr.title}</g:link>
															</g:elseif>
															<g:elseif test="${loggerInstance.tipo == 'fr-buscar'}">
																<img src="${resource(dir:'images/logger',file:'lupa.jpg')}" alt="" width="20" height="20" /> Ha realizado nuevas busquedas en el SLR: ${loggerInstance.slr.title}
															</g:elseif>
															<g:elseif test="${loggerInstance.tipo == 'seguir'}">
																<img src="${resource(dir:'images/logger',file:'friend.png')}" alt="" width="20" height="20" /> Has seguido a <g:link controller="user" action="show" params="[guid: "${loggerInstance.friendProfile.guid}"]">${loggerInstance.friendProfile.display_name}</g:link>
															</g:elseif>
															<g:elseif test="${loggerInstance.tipo == 'fr-seguir'}">
																<img src="${resource(dir:'images/logger',file:'friend.png')}" alt="" width="20" height="20" /> Ha seguido a ${loggerInstance.friendFriendProfile.display_name}
															</g:elseif>
															<g:else>
																No definido
															</g:else>
														</p>
													</div>
												</li>
											</g:each>
										</g:else>
									</ul>
								</div>
							</div>
							<g:render template="paginationFriendsLogger" contextPath="/index"/>
						</div>
						
						
						<%-- Mis loggers --%>
						<div class="tab-pane fade" id="myLoggers">
							<div class="chat-panel panel panel-default">
								<div class="panel-heading">
									<i class="fa fa-comments fa-fw"></i>
									Mis Loggers
								</div>
								<div class="panel-body" style="min-height: 500px;">
									<ul class="chat">
										<g:if test="${totalMyLoggers == 0}">
											<li class="left clearfix">
												No existen loggers disponibles.
											</li>
										</g:if>
										<g:else>
											<g:each in="${myLoggers}" var="loggerInstance">
												<li class="left clearfix">
													<span class="chat-img pull-left">
														<g:if test="${loggerInstance.tipo.contains('fr-')}">
															<img src="${loggerInstance.friendProfile.url_foto}" alt="User Avatar" class="img-circle" style="width: 50px; height: 50px;" />
														</g:if>
														<g:else>
															<img src="${loggerInstance.profile.url_foto}" alt="User Avatar" class="img-circle" style="width: 50px; height: 50px;" />
														</g:else>
													</span>
													<div class="chat-body clearfix">
														<div class="header">
															<%--<g:if test="${userProfileInstance.id != loggerInstance.profile.id}"> --%>
															<g:if test="${loggerInstance.tipo.contains('fr-')}">
																<strong class="primary-font">${loggerInstance.friendProfile.display_name}</strong>
															</g:if>
															<g:else>
																<strong class="primary-font">Tú</strong>
															</g:else>
															<small class="pull-right text-muted">
																<i class="fa fa-clock-o fa-fw"></i> ${loggerInstance.timeString}
															</small>
														</div>
														<p>
															<g:if test="${loggerInstance.tipo == 'bienvenida'}">
																<img src="${resource(dir:'images/logger',file:'start.png')}" alt="" width="20" height="20" /> ¡Bienvenido a Systematic Literature Review!
															</g:if>
															<g:elseif test="${loggerInstance.tipo == 'fr-bienvenida'}">
																<img src="${resource(dir:'images/logger',file:'start.png')}" alt="" width="20" height="20" /> Se ha registrado en Systematic Literature Review.
															</g:elseif>
															<g:elseif test="${loggerInstance.tipo == 'crear'}">
																<img src="${resource(dir:'images/logger',file:'libro.jpg')}" alt="" width="20" height="20" /> Has creado un nuevo SLR: <g:link controller="slr" action="myList">${loggerInstance.slr.title}</g:link>
															</g:elseif>
															<g:elseif test="${loggerInstance.tipo == 'fr-crear'}">
																<img src="${resource(dir:'images/logger',file:'libro.jpg')}" alt="" width="20" height="20" /> Ha creado un nuevo SLR: ${loggerInstance.slr.title}
															</g:elseif>
															<g:elseif test="${loggerInstance.tipo == 'buscar'}">
																<img src="${resource(dir:'images/logger',file:'lupa.jpg')}" alt="" width="20" height="20" /> Has realizado nuevas busquedas en el SLR: <g:link controller="slr" action="searchs" params="[guid: "${loggerInstance.slr.guid}"]">${loggerInstance.slr.title}</g:link>
															</g:elseif>
															<g:elseif test="${loggerInstance.tipo == 'fr-buscar'}">
																<img src="${resource(dir:'images/logger',file:'lupa.jpg')}" alt="" width="20" height="20" /> Ha realizado nuevas busquedas en el SLR: ${loggerInstance.slr.title}
															</g:elseif>
															<g:elseif test="${loggerInstance.tipo == 'seguir'}">
																<img src="${resource(dir:'images/logger',file:'friend.png')}" alt="" width="20" height="20" /> Has seguido a <g:link controller="user" action="show" params="[guid: "${loggerInstance.friendProfile.guid}"]">${loggerInstance.friendProfile.display_name}</g:link>
															</g:elseif>
															<g:elseif test="${loggerInstance.tipo == 'fr-seguir'}">
																<img src="${resource(dir:'images/logger',file:'friend.png')}" alt="" width="20" height="20" /> Ha seguido a ${loggerInstance.friendFriendProfile.display_name}
															</g:elseif>
															<g:else>
																No definido
															</g:else>
														</p>
													</div>
												</li>
											</g:each>
										</g:else>
									</ul>
								</div>
							</div>
							<g:render template="paginationMyLogger" contextPath="/index"/>
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

</body>

</html>
