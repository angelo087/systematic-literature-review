<div class="panel panel-default">
	<div class="panel-heading">
		<i class="fa fa-book fa-fw"></i> Últimos SLR's creados
	</div>
	<!-- 
	<div class="panel-body">
		<div class="list-group">
			<a href="#" class="list-group-item">
				<i class="fa fa-book fa-fw"></i> Jack Sparrow
				<span class="pull-right text-muted small"><em>1 minute ago</em>
				</span>
				<p><em><em>"La vida de las ballenas".</em></em></p>
			</a>
			<a href="#" class="list-group-item">
				<i class="fa fa-book fa-fw"></i> Vin Diessel
				<span class="pull-right text-muted small"><em>3 minutes ago</em>
				</span>
				<p><em>"Los crustáceos en tierra".</em></p>
			</a>
			<a href="#" class="list-group-item">
				<i class="fa fa-book fa-fw"></i> Morgan Freeman
				<span class="pull-right text-muted small"><em>4 minutes ago</em>
				</span>
				<p><em>"Las focas con bigotes cortos".</em></p>
			</a>
		</div>
		
		<%--<a href="#" class="btn btn-default btn-block">View All Alerts</a>--%>
	</div> -->
	<div class="panel-body">
		<div class="list-group">
			
			<g:each var="slrInstance" in="${lastSlrCreated}">
				<g:link class="list-group-item" controller="slr" action="show" params="[guidSlr: "${slrInstance.guid}"]">
					<i class="fa fa-book fa-fw"></i> ${slrInstance.userProfile.display_name}
					<span class="pull-right text-muted small"><em>${slrInstance.timeString}</em>
					</span>
					<p><em><em>"${slrInstance.title}".</em></em></p>
				</g:link>
			</g:each>
			
		</div>	
	</div>
	<!-- /.panel-body -->
</div>