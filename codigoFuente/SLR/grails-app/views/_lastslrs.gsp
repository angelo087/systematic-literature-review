<g:if test="${lastSlrCreated.size() > 0}">
	<div class="panel panel-default">
		<div class="panel-heading">
			<i class="fa fa-book fa-fw"></i> Últimos SLR's creados
		</div>
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
</g:if>