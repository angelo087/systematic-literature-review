<g:if test="${totalAllLoggers > 1}">
	<div align="right" style="margin-top: 5px; margin-bottom: 5px; margin-right: 15px;">
		<ul class="pagination">
			<g:if test="${totalAllLoggers <= 3}">
				<g:each in="${ (1..<(totalAllLoggers+1)) }" var="p">
					<g:if test="${p == pageAllLoggers}">
						<li class="active"><a href="#">${p}</a></li>
					</g:if>
					<g:else>
						<li><a href="#">${p}</a></li>
					</g:else>
   				</g:each>
  				</g:if>
  				<g:else>
  					<%-- Boton << y < --%>
  					<g:if test="${pageAllLoggers == 1}">
					<li class="active"><a href="#">&laquo;</a></li>
					<li class="active"><a href="#">&lt;</a></li>
				</g:if>
				<g:else>
					<li><a href="#">&laquo;</a></li>
					<li><a href="#">&lt;</a></li>
				</g:else>
  				
  					<%-- Pages --%>
   				<g:if test="${pageAllLoggers == 1}">
					<li class="active"><a href="#">${pageAllLoggers}</a></li>
					<li><a href="#">${pageAllLoggers+1}</a></li>
					<li><a href="#">${pageAllLoggers+2}</a></li>
				</g:if>
				<g:elseif test="${ (pageAllLoggers+1) == totalAllLoggers }">
					<li><a href="#">${pageAllLoggers-1}</a></li>
					<li class="active"><a href="#">${pageAllLoggers}</a></li>
					<li><a href="#">${pageAllLoggers+1}</a></li>
				</g:elseif>
				<g:elseif test="${ pageAllLoggers == totalAllLoggers }">
					<li><a href="#">${pageAllLoggers-2}</a></li>
					<li><a href="#">${pageAllLoggers-1}</a></li>
					<li class="active"><a href="#">${pageAllLoggers}</a></li>
				</g:elseif>
				<g:else>
					<g:each in="${ ((pageAllLoggers-1)..<(pageAllLoggers+2)) }" var="p">
						<g:if test="${p == pageAllLoggers}">
							<li class="active"><a href="#">${p}</a></li>
						</g:if>
						<g:else>
							<li><a href="#">${p}</a></li>
						</g:else>
					</g:each>
				</g:else>
			
				<%-- Boton >> y > --%>
				<g:if test="${pageAllLoggers == totalAllLoggers}">
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
