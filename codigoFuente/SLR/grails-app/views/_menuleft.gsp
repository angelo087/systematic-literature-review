<%@ page import="es.uca.pfc.User" %>
<div class="navbar-default sidebar" role="navigation">
	<div class="sidebar-nav navbar-collapse">
	
	<g:if test="${!request.forwardURI.contains("references")}">
		<ul class="nav" id="side-menu">
			<li class="sidebar-search">
				<g:form class="form-horizontal" controller="searcher" action="search" method="POST" name="myForm" id="myForm">
					<div class="input-group custom-search-form">
						<input id="query" name="query" type="text" class="form-control" placeholder="Search...">
						<span class="input-group-btn">
							<button class="btn btn-default" type="submit">
								<i class="fa fa-search"></i>
							</button>
						</span>
					</div>
					<!-- /input-group -->
				</g:form>
			</li>
			<li>
				<g:link controller="index" action="menu"><i class="fa fa-dashboard fa-fw"></i> Menu Principal</g:link>
			</li>
			<li>
			    <g:link controller="user" action="show" params="[guid: "${User.get(sec.loggedInUserInfo(field:"id").toString().toLong()).userProfile.guid.toString()}"]"><i class="fa fa-user fa-fw"></i> Perfil</g:link>
			</li>
			<li>
		    	<g:link controller="user" action="list"><i class="fa fa-group fa-fw"></i> Usuarios</g:link>
			</li>
			<li>
				<g:link controller="slr" action="myList"><i class="fa fa-folder-open"></i> SLR's</g:link>
			</li>
			<li>
			    <g:link controller="index" action="loggers"><i class="fa fa-history fa-fw"></i> Logger</g:link>
			</li>
			<li>
			    <g:link controller="index" action="faqs"><i class="fa fa-question fa-fw"></i> FAQS</g:link>
			</li>
			<g:if test="${!User.get(sec.loggedInUserInfo(field:"id").toString().toLong()).getAuthorities().toString().contains("USER")}">
				<li>
					<g:link controller="engineSearch" actions="index"><i class="fa fa-university fa-fw"></i> Engines</g:link>
				</li>
			</g:if>
			<g:if test="${!User.get(sec.loggedInUserInfo(field:"id").toString().toLong()).getAuthorities().toString().contains("USER")}">
				<li>
					<g:link controller="mendeleyApi" actions="index"><i class="fa fa-key  fa-fw"></i> Mendeley API</g:link>
				</li>
			</g:if>
			<li>
			    <a href="index.html"><i class="fa fa-dashboard fa-fw"></i> Notificaciones</a>
			</li>
			<li>
			    <a href="index.html"><i class="fa fa-dashboard fa-fw"></i> FAQ's</a>
			</li>
			<li>
			    <a href="index.html"><i class="fa fa-dashboard fa-fw"></i> Amigos</a>
			</li>

			<li>
			    <a href="#"><i class="fa fa-wrench fa-fw"></i> UI Elements<span class="fa arrow"></span></a>
			    <ul class="nav nav-second-level">
			        <li>
			            <a href="panels-wells.html">Panels and Wells</a>
			        </li>
			        <li>
			            <a href="buttons.html">Buttons</a>
			        </li>
			        <li>
			            <a href="notifications.html">Notifications</a>
			        </li>
			        <li>
			            <a href="typography.html">Typography</a>
			        </li>
			        <li>
			            <a href="icons.html"> Icons</a>
			        </li>
			        <li>
			            <a href="grid.html">Grid</a>
			        </li>
			    </ul>
			    <!-- /.nav-second-level -->
	        </li>
	    </ul>
    </g:if>
    <g:else>
    	<g:render template="menuLeftSearch" contextPath="/slr"/>
    </g:else>
	</div>
<!-- /.sidebar-collapse -->
</div>