<g:if test="${totalPages > 1}">
	<div align="right" style="margin-top: 5px; margin-bottom: 5px; margin-right: 15px;">
		<ul class="pagination">
			<g:if test="${totalPages <= 3}">
				<g:each in="${ (1..<(totalPages+1)) }" var="p">
					<g:if test="${p == page}">
						<li class="active"><a href="#">${p}</a></li>
					</g:if>
					<g:else>
						<li><g:remoteLink controller='slr' action='filtredReferencesByParam' update='searchresults' params="[guidSlr: "${guidSlr}", p: "${p}"]">${p}</g:remoteLink></li>
					</g:else>
   				</g:each>
  				</g:if>
  				<g:else>
  					<%-- Boton << y < --%>
  					<g:if test="${page == 1}">
					<li class="active"><a href="#">&laquo;</a></li>
					<li class="active"><a href="#">&lt;</a></li>
				</g:if>
				<g:else>
					<li><a href="#">&laquo;</a></li>
					<li><a href="#">&lt;</a></li>
				</g:else>
  				
  					<%-- Pages --%>
   				<g:if test="${page == 1}">
					<li class="active"><a href="#">${page}</a></li>
					<li><a href="#">${page+1}</a></li>
					<li><a href="#">${page+2}</a></li>
				</g:if>
				<g:elseif test="${ (page+1) == totalPages }">
					<li><a href="#">${page-1}</a></li>
					<li class="active"><a href="#">${page}</a></li>
					<li><a href="#">${page+1}</a></li>
				</g:elseif>
				<g:elseif test="${ page == totalPages }">
					<li><a href="#">${page-2}</a></li>
					<li><a href="#">${page-1}</a></li>
					<li class="active"><a href="#">${page}</a></li>
				</g:elseif>
				<g:else>
					<g:each in="${ ((page-1)..<(page+2)) }" var="p">
						<g:if test="${p == page}">
							<li class="active"><a href="#">${p}</a></li>
						</g:if>
						<g:else>
							<li><a href="#">${p}</a></li>
						</g:else>
					</g:each>
				</g:else>
			
				<%-- Boton >> y > --%>
				<g:if test="${page == totalPages}">
					<li class="active"><a href="#">&gt;</a></li>
					<li class="active"><a href="#">&raquo;</a></li>
				</g:if>
				<g:else>
					<li><a href="#">&gt;</a></li>
					<li><a href="#">&raquo;</a></li>
				</g:else>
			</g:else>
		</ul>
	</div>
</g:if>
