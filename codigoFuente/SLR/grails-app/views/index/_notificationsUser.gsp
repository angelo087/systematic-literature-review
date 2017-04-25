<a class="dropdown-toggle" data-toggle="dropdown" href="#">
    <i class="fa fa-envelope fa-fw"></i> <g:if test="${notificationList.size() > 0}"><span class="badge">${notificationList.size()}</span></g:if> <i class="fa fa-caret-down"></i> 
</a>
<ul class="dropdown-menu dropdown-messages">
	<g:if test="${notificationList.size() == 0}">
		<li>
			<a class="text-center" href="#">
				<strong>No hay avisos</strong>
			</a>
		</li>
	</g:if>
	<g:else>
		<g:each var="notificationInstance" in="${notificationList}">
			<li>
				<g:if test="${notificationInstance.tipo == 'slr'}">
					<g:link controller="slr" action="myList" params="[guidNotif: "${notificationInstance.guid}"]">
						<div>
			                <strong>${notificationInstance.asunto}</strong>
			                <span class="pull-right text-muted">
			                    <em>${notificationInstance.fechaString}</em>
			                </span>
			            </div>
			            <div>${notificationInstance.texto}</div>
			         </g:link>
				</g:if>
				<g:elseif test="${notificationInstance.tipo == 'search'}">
					<g:link controller="slr" action="searchs" params="[guid: "${notificationInstance.slr.guid}", guidNotif: "${notificationInstance.guid}"]">
						<div>
			                <strong>${notificationInstance.asunto}</strong>
			                <small class="pull-right text-muted">
			                    <i class="fa fa-clock-o fa-fw"></i> <em>${notificationInstance.fechaString}</em>
			                </small>
			            </div>
			            <div>${notificationInstance.texto}</div>
			        </g:link>
				</g:elseif>
				<g:elseif test="${notificationInstance.tipo == 'friend'}">
					<g:link controller="user" action="show" params="[guid: "${notificationInstance.friendProfile.guid}", guidNotif: "${notificationInstance.guid}"]">
						<div>
			                <strong>${notificationInstance.asunto}</strong>
			                <small class="pull-right text-muted">
			                    <i class="fa fa-clock-o fa-fw"></i> <em>${notificationInstance.fechaString}</em>
			                </small>
			            </div>
			            <div>${notificationInstance.texto}</div>
			         </g:link>
				</g:elseif>
				<g:else>
					<a href="#">
						<div>
			                <strong>${notificationInstance.asunto}</strong>
			                <span class="pull-right text-muted">
			                    <em>${notificationInstance.fechaString}</em>
			                </span>
			            </div>
			            <div>${notificationInstance.texto}</div>
			         </a>
				</g:else>
		    </li>
		    <li class="divider"></li>
	    </g:each>
	    <li>
			<g:link class="text-center" controller="slr" action="myList" params="[guidNotif: 'all']">
		        <strong>Ver todos</strong>
		        <i class="fa fa-angle-right"></i>
		    </g:link>
		</li>
	</g:else>
</ul>
<!-- /.dropdown-messages -->    
 