<a class="dropdown-toggle" data-toggle="dropdown" href="#">
   	<i class="fa fa-tasks fa-fw"></i>  <i class="fa fa-caret-down"></i>
</a>

<ul class="dropdown-menu dropdown-tasks">
	<g:if test="${taskList.size() == 0}">
		<li>
			<a class="text-center" href="#">
				<strong>No hay búsquedas</strong>
			</a>
		</li>
	</g:if>
	<g:else>
		<g:each in="${taskList}" var="taskInstance">
			<li>
				<a href="#">
					<div>
						<p>
							<strong>${taskInstance.slr.title}</strong>
							<span class="pull-right text-muted">${taskInstance.porcentaje}% Complete</span>
						</p>
						<div class="progress progress-striped active">
							<div class="progress-bar progress-bar-info" role="progressbar" aria-valuenow="${taskInstance.porcentaje}" aria-valuemin="0" aria-valuemax="100" style="width: ${taskInstance.porcentaje}%">
								<span class="sr-only">${taskInstance.porcentaje}% Complete</span>
							</div>
						</div>
					</div>
				</a>
			</li>
			<li class="divider"></li>
		</g:each>
		<li>
			<a class="text-center" href="#">
				<strong>See All Tasks</strong>
				<i class="fa fa-angle-right"></i>
			</a>
		</li>
	</g:else>
</ul>
