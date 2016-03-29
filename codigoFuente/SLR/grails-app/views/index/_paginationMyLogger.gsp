<g:if test="${totalMyLoggers > 1}">
	<div align="right" style="margin-top: 5px; margin-bottom: 5px; margin-right: 15px;">
		<ul class="pagination">
			<g:if test="${totalMyLoggers <= 3}">
				<g:each in="${ (1..<(totalMyLoggers+1)) }" var="p">
					<g:if test="${p == pageMyLoggers}">
						<li class="active"><a href="#">${p}</a></li>
					</g:if>
					<g:else>
						<li><a href="#">${p}</a></li>
					</g:else>
   				</g:each>
  				</g:if>
  				<g:else>
  					<%-- Boton << y < --%>
  					<g:if test="${pageMyLoggers == 1}">
					<li class="active"><a href="#">&laquo;</a></li>
					<li class="active"><a href="#">&lt;</a></li>
				</g:if>
				<g:else>
					<li><a href="#">&laquo;</a></li>
					<li><a href="#">&lt;</a></li>
				</g:else>
  				
  					<%-- Pages --%>
   				<g:if test="${pageMyLoggers == 1}">
					<li class="active"><a href="#">${pageMyLoggers}</a></li>
					<li><a href="#">${pageMyLoggers+1}</a></li>
					<li><a href="#">${pageMyLoggers+2}</a></li>
				</g:if>
				<g:elseif test="${ (pageMyLoggers+1) == totalMyLoggers }">
					<li><a href="#">${pageMyLoggers-1}</a></li>
					<li class="active"><a href="#">${pageMyLoggers}</a></li>
					<li><a href="#">${pageMyLoggers+1}</a></li>
				</g:elseif>
				<g:elseif test="${ pageMyLoggers == totalMyLoggers }">
					<li><a href="#">${pageMyLoggers-2}</a></li>
					<li><a href="#">${pageMyLoggers-1}</a></li>
					<li class="active"><a href="#">${pageMyLoggers}</a></li>
				</g:elseif>
				<g:else>
					<g:each in="${ ((pageMyLoggers-1)..<(pageMyLoggers+2)) }" var="p">
						<g:if test="${p == pageMyLoggers}">
							<li class="active"><a href="#">${p}</a></li>
						</g:if>
						<g:else>
							<li><a href="#">${p}</a></li>
						</g:else>
					</g:each>
				</g:else>
			
				<%-- Boton >> y > --%>
				<g:if test="${pageMyLoggers == totalMyLoggers}">
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
