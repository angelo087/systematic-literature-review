<g:set var="guidSlr" value="${guidSlr}" />
<div style="margin-left: 20px; font-size: 11px;">
	<g:if test="${criterionsListInstance.size() > 0}">
		<div id="critery_search" style="margin-top: 20px;">
			<b>Criterios</b>
			<g:each in="${criterionsListInstance}" var="criterion">
				<div class="checkbox">
				  <label>
				    <input type="checkbox" value="${criterion}" 
				    	onchange="${remoteFunction(controller:'slr', action:'filtredReferencesByParam', update:'searchresults',params:[guidSlr: guidSlr, filter: "criterion="+criterion])}"
				    />
			    	${criterion}
				  </label>
				</div>
			</g:each>
		</div>
	</g:if>
	<div id="critery_search" style="margin-top: 20px;">
		<b>Años</b>
		<div style="margin-bottom: 35px; margin-top: 35px;">
	   		<input type="hidden" class="range-slider" value="${minYear},${maxYear}" />
   		</div>
	</div>
	<div id="critery_search" style="margin-top: 20px;">
		<g:if test="${enginesListInstance.size() > 0}">
			<b>Motor búsqueda</b>
			<g:each in="${enginesListInstance}" var="engine">
				<div class="checkbox">
				  <label>
				    <input name="check${engine}" type="checkbox" value="${engine}"
			    		onchange="${remoteFunction(controller:'slr', action:'filtredReferencesByParam', update:'searchresults',params:[guidSlr: guidSlr, filter: "engine="+engine])}"
		    		/>
				    ${engine}
				  </label>
				</div>
			</g:each>
		</g:if>
	</div>
	<div id="critery_search" style="margin-top: 20px;">
		<g:if test="${languagesListInstance.size() > 0}">
			<b>Idioma</b>
			<g:each in="${languagesListInstance}" var="language">
				<div class="checkbox">
				  <label>
				    <input type="checkbox" value="${language}" 
				    	onchange="${remoteFunction(controller:'slr', action:'filtredReferencesByParam', update:'searchresults',params:[guidSlr: guidSlr, filter: "language="+language])}"
				    />
				    ${language}
				  </label>
				</div>
			</g:each>
		</g:if>
	</div>
	<div id="critery_search" style="margin-top: 20px;">
		<g:if test="${departmentsListInstance.size() > 0}">
			<b>Departamento</b>
			<g:each in="${departmentsListInstance}" var="department">
				<div class="checkbox">
				  <label>
				    <input type="checkbox" value="${department}"
				       	onchange="${remoteFunction(controller:'slr', action:'filtredReferencesByParam', update:'searchresults',params:[guidSlr: guidSlr, filter: "department="+department])}" 
				    />
				    ${department}
				  </label>
				</div>
			</g:each>
		</g:if>
	</div>
	<div id="critery_search" style="margin-top: 20px;">
		<g:if test="${typesListInstance.size() > 0}">
			<b>Tipo</b>
			<g:each in="${typesListInstance}" var="type">
				<div class="checkbox">
				  <label>
				    <input type="checkbox" value="${type}" 
				    	onchange="${remoteFunction(controller:'slr', action:'filtredReferencesByParam', update:'searchresults',params:[guidSlr: guidSlr, filter: "type="+type])}" 
				    />
				    ${type}
				  </label>
				</div>
			</g:each>
		</g:if>
	</div>
	<div id="critery_search" style="margin-top: 20px;">
		<g:if test="${authorsListInstance.size() > 0}">
			<b>Autor</b>
			<g:each in="${authorsListInstance}" var="author">
				<div class="checkbox">
				  <label>
				    <input type="checkbox" value="${author}" 
				    	onchange="${remoteFunction(controller:'slr', action:'filtredReferencesByParam', update:'searchresults',params:[guidSlr: guidSlr, filter: "author="+author])}" 
				    />
				    ${author}
				  </label>
				</div>
			</g:each>
		</g:if>
	</div>
</div>
