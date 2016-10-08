<div class="panel panel-default">
	<div class="panel-heading">
		<i class="fa fa-user fa-fw"></i> Últimos usuarios registrados
	</div>
	<!-- /.panel-heading -->
	 <div class="panel-body">
		<div class="list-group">
			<%--<a href="#" class="list-group-item">
				<i class="fa fa-user fa-fw" style="color: green;"></i> Angelina Jolie
				<span class="pull-right text-muted small" style="color: green;"><em>conectado</em>
				</span>
			</a>
			<a href="#" class="list-group-item">
				<i class="fa fa-user fa-fw" style="color: blue;"></i> Espinete
				<span class="pull-right text-muted small" style="color: blue;"><em>10 minutes ago</em>
				</span>
			</a>
			<a href="#" class="list-group-item">
				<i class="fa fa-user fa-fw" style="color: blue;"></i> Don Pimpon
				<span class="pull-right text-muted small" style="color: blue;"><em>3 days ago</em>
				</span>
			</a>--%>
		</div>
		<g:each in="${lastUsersRegistered}" var="profileInstance">
			<g:link class="list-group-item" controller="user" action="show" params="[guid: "${profileInstance.guid}"]">
				<i class="fa fa-user fa-fw" style="color: green;"></i> ${profileInstance.display_name}
				<g:if test="${profileInstance.isOnline}">
					<span class="pull-right text-muted small" style="color: green;"><em>conectado</em>
					</span>
				</g:if>
				<g:else>
					<span class="pull-right text-muted small" style="color: blue;"><em>desconectado</em>
					</span>
				</g:else>
			</g:link>
		</g:each>
		<!-- /.list-group -->
		<%--<a href="#" class="btn btn-default btn-block">View All Alerts</a>--%>
	</div>
	<!-- /.panel-body -->
</div>