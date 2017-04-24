<a class="dropdown-toggle" data-toggle="dropdown" href="#">
    <i class="fa fa-tasks fa-fw"></i>  <i class="fa fa-caret-down"></i>
</a>
<ul class="dropdown-menu dropdown-tasks">
	<g:if test="${taskSearchListHead.size() == 0}">
		<li>
			<a class="text-center" href="#">
				<strong>No hay busquedas en proceso</strong>
			</a>
		</li>
	</g:if>
	<g:else>
		<g:each var="taskSearchInstance" in="${taskSearchListHead}">
			<li>
				<g:link controller="index" action="taskSearchs">
					<div>
						<p>
							<strong>${taskSearchInstance.titleSlr}</strong>
							<g:if test="${taskSearchInstance.hasErrors}">
								<span class="pull-right text-muted">Cancelado</span>
							</g:if>
							<g:else>
								<span class="pull-right text-muted">${taskSearchInstance.percentage}% Complete</span>
							</g:else>
						</p>
						<div class="progress progress-striped">
							<g:set var="cssBar" value="progress-bar progress-bar-striped progress-bar-primary" />
							<g:if test="${taskSearchInstance.percentage == 0 && taskSearchInstance.hasErrors}">
								<g:set var="cssBar" value="progress-bar progress-bar-danger" />
							</g:if>
							<g:elseif test="${taskSearchInstance.percentage != 100}">
								<g:set var="cssBar" value="progress-bar progress-bar-striped progress-bar-primary active" />
							</g:elseif>
							
							<g:if test="${taskSearchInstance.hasErrors}">
								<div class="${cssBar}" role="progressbar" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width: 100%">
									<span class="sr-only">${taskSearchInstance}% Complete (success)</span>
								</div>
							</g:if>
							<g:else>
								<div class="${cssBar}" role="progressbar" aria-valuenow="${taskSearchInstance.percentage}" aria-valuemin="0" aria-valuemax="100" style="width: ${taskSearchInstance.percentage}%">
									<span class="sr-only">${taskSearchInstance}% Complete (success)</span>
								</div>
							</g:else>
						</div>
					</div>
				</g:link>
			</li>
			<li class="divider"></li>
		</g:each>
	</g:else>
</ul>
