<g:if test="${totalFriendsLoggers > 1}">
	<div align="right" style="margin-top: 5px; margin-bottom: 5px; margin-right: 15px;">
		<ul class="pagination">
			<g:if test="${totalFriendsLoggers <= 3}">
				<g:each in="${ (1..<(totalFriendsLoggers+1)) }" var="p">
					<g:if test="${p == pageFriendsLoggers}">
						<li class="active"><a href="#">${p}</a></li>
					</g:if>
					<g:else>
						<li><a href="#">${p}</a></li>
					</g:else>
   				</g:each>
  				</g:if>
  				<g:else>
  					<%-- Boton << y < --%>
  					<g:if test="${pageFriendsLoggers == 1}">
					<li class="active"><a href="#">&laquo;</a></li>
					<li class="active"><a href="#">&lt;</a></li>
				</g:if>
				<g:else>
					<li><a href="#">&laquo;</a></li>
					<li><a href="#">&lt;</a></li>
				</g:else>
  				
  					<%-- Pages --%>
   				<g:if test="${pageFriendsLoggers == 1}">
					<li class="active"><a href="#">${pageFriendsLoggers}</a></li>
					<li><a href="#">${pageFriendsLoggers+1}</a></li>
					<li><a href="#">${pageFriendsLoggers+2}</a></li>
				</g:if>
				<g:elseif test="${ (pageFriendsLoggers+1) == totalFriendsLoggers }">
					<li><a href="#">${pageFriendsLoggers-1}</a></li>
					<li class="active"><a href="#">${pageFriendsLoggers}</a></li>
					<li><a href="#">${pageFriendsLoggers+1}</a></li>
				</g:elseif>
				<g:elseif test="${ pageFriendsLoggers == totalFriendsLoggers }">
					<li><a href="#">${pageFriendsLoggers-2}</a></li>
					<li><a href="#">${pageFriendsLoggers-1}</a></li>
					<li class="active"><a href="#">${pageFriendsLoggers}</a></li>
				</g:elseif>
				<g:else>
					<g:each in="${ ((pageFriendsLoggers-1)..<(pageFriendsLoggers+2)) }" var="p">
						<g:if test="${p == pageFriendsLoggers}">
							<li class="active"><a href="#">${p}</a></li>
						</g:if>
						<g:else>
							<li><a href="#">${p}</a></li>
						</g:else>
					</g:each>
				</g:else>
			
				<%-- Boton >> y > --%>
				<g:if test="${pageFriendsLoggers == totalFriendsLoggers}">
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
