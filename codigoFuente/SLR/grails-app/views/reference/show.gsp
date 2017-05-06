<%@ page import="es.uca.pfc.User" %>
<%@ page import="es.uca.pfc.Reference" %>
<%@ page import="es.uca.pfc.TypeDocument" %>
<!DOCTYPE html>
<html lang="en">

<head>

    <%-- Head Meta --%>
	<g:render template="headMeta" contextPath="/"/>

    <title>SLR | Reference ${referenceInstance.title}</title>

	<%-- CSS --%>
    <g:render template="css" contextPath="/"/>
	
	<script type="text/javascript">
		function loadModal()
		{
			if(${null != error && error != ""})
			{
				document.getElementById('divError').style.display = "";
			}
			else if (${success})
			{
				document.getElementById('divSuccess').style.display = "";
				setTimeout(function(){
					document.getElementById('divSuccess').style.display = "none";
				}, 5000);
			}
		}
	</script>

</head>

<body onload="loadModal();">

	<%-- Ventana modal para eliminar slr --%>
    <div class="modal fade" id="myModalDrop" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="myModalLabel">Eliminar Referencia</h4>
				</div>
				<g:form controller="reference" action="delete" id="myFormDelete" name="myFormDelete" method="DELETE">
					<g:hiddenField name="idmendReference" value="${referenceInstance.idmend}" />
					<div class="modal-body">
						¿Deseas eliminar esta referencia?
					</div>
					<div class="modal-footer">
						<button class="btn btn-default" data-dismiss="modal" type="button">No</button>
						<g:submitButton id="boton" name="boton" class="btn btn-primary" value="Sí" onclick="loading('Eliminando...');" />
					</div>
				</g:form>
			</div>
		</div>
	</div>

	<g:form class="form-horizontal" controller="reference" action="save" method="POST" name="myForm" id="myForm">
	
		<g:hiddenField name="idmend" value="${referenceInstance.idmend}"/>
	
	    <div id="wrapper">
	
	        <%-- Head --%>
	        <g:render template="head" contextPath="/"/>
	        
	        <div id="page-wrapper">	
				<div class="row" style="margin-bottom: 20px;">
					<div class="col-lg-12">
						<h1 class="page-header">${referenceInstance.title}</h1>
						
						<ol class="breadcrumb">
						  <li><g:link controller="index" action="menu">Inicio</g:link></li>
						  <li><g:link controller="slr" action="myList">Mis SLR's</g:link></li>
						  <li><g:link controller="slr" action="show" params="[guidSlr: "${slrInstance.guid}"]">${slrBreadCrumb}</g:link></li>
						  <li><g:link controller="slr" action="references" params="[guid: "${slrInstance.guid}"]">Referencias</g:link></li>
						  <li class="active">${refBreadCrumb}</li>
						</ol>
						
						<g:if test="${userOwnerInstance.id == User.get(sec.loggedInUserInfo(field:"id").toString().toLong()).id}">
							<g:submitButton class="btn btn-success" name="create" value="Guardar cambios" onclick="loading('Aplicando cambios...');" />
							<g:if test="${!noDrop}">
								<button type="button" class="btn btn-danger" data-toggle="modal" data-target="#myModalDrop">Eliminar Referencia</button>
							</g:if>
							<g:else>
								<button type="button" class="btn btn-danger" data-toggle="modal" data-target="#myModalDrop" disabled="disabled">Eliminar Referencia</button>
							</g:else>
							<g:link type="button" class="btn btn-primary" controller="reference" action="exportReferenceToBibTex" params="[idmend: "${referenceInstance.idmend}"]">Exportar BibTex</g:link>
							<g:link type="button" class="btn btn-primary" controller="reference" action="sychronizeReferenceMend" onclick="loading('Sincronizando con Mendeley...');" params="[idmend: "${referenceInstance.idmend}"]">Sincronizar (Mendeley)</g:link>
							<p> </p>
						</g:if>
					</div>
				</div>
				
				<g:if test="${userOwnerInstance.id == User.get(sec.loggedInUserInfo(field:"id").toString().toLong()).id}">
					<g:set var="isEditable" value="" />
					<g:set var="isEditableSelect" value="" />
				</g:if>
				<g:else>
					<g:set var="isEditable" value="readonly=\'readonly\'" />
					<g:set var="isEditableSelect" value="disabled=\'disabled\'" />
				</g:else>
				
				<g:if test="${isEditable == ""}">
					<div class="row">
						<div id="divSuccess" class="alert alert-success" role="alert" style="display: none;"><i class="fa fa-check fa-fw"></i> Cambios guardados correctamente.</div>
						<div id="divError" class="alert alert-danger" role="alert" style="display: none;"><i class="fa fa-remove fa-fw"></i> ${error}</div>
					</div>
				</g:if>
				
				<h4>Criterio</h4>
				<div class="row">
					<div class="col-lg-6">
						<div class="form-group">
							<label for="selectCriterion" class="col-sm-3 control-label">Criterion</label>
							<div class="col-sm-8">
								<select id="selectCriterion" name="selectCriterion" class="form-control" ${isEditableSelect}>
									<g:each in="${criterionListInstance}" var="criterionInstance">
										<g:if test="${referenceInstance.criterion.name.equals(criterionInstance.name)}">
											<option value="${criterionInstance.nomenclatura}" selected="selected">${criterionInstance.name}</option>
										</g:if>
										<g:else>
											<option value="${criterionInstance.nomenclatura}">${criterionInstance.name}</option>
										</g:else>
									</g:each>
								</select>
							</div>
						</div>
					</div>
					<div class="col-lg-6"></div>
				</div>
				
				<g:if test="${referenceInstance.specificAttributes.size() != 0}">
					<h4>Atributos específicos</h4>
					<g:set var="cont" value="${0}" />
					<div class="row">
						<g:each in="${referenceInstance.specificAttributes}" var="attributeRefInstance">
							<div class="col-lg-6">
								<div class="form-group">
									<label for="att${attributeRefInstance.attribute.id}" class="col-sm-3 control-label">${attributeRefInstance.attribute.name}:</label>
									<div class="col-sm-8">
										<g:if test="${attributeRefInstance.attribute.tipo == "list"}">
											<select id="att${attributeRefInstance.attribute.id}" name="att${attributeRefInstance.attribute.id}" class="form-control" ${isEditableSelect}>
												<g:each in="${attributeRefInstance.attribute.options}" var="optionInstance">
													<g:if test="${optionInstance.equals(attributeRefInstance.value)}">
														<option value="${optionInstance}" selected="selected">${optionInstance}</option>
													</g:if>
													<g:else>
														<option value="${optionInstance}">${optionInstance}</option>
													</g:else>
												</g:each>
											</select>
										</g:if>
										<g:else>
											<input type="text" class="form-control" id="att${attributeRefInstance.attribute.id}" name="att${attributeRefInstance.attribute.id}" value="${attributeRefInstance.value}" ${isEditable} />
										</g:else>
									</div>
								</div>
							</div>
						</g:each>
						
						<g:if test="${cont % 2 != 0}">
							<div class="col-lg-6"></div>
						</g:if>
						
					</div>
				</g:if>
				
				<h4>Atributos generales</h4>
				<div class="row">
					<div class="col-lg-6">
						<div class="form-group">
							<label for="inputTitle" class="col-sm-3 control-label">Title</label>
							<div class="col-sm-8">
								<input type="text" class="form-control" id="inputTitle" name="inputTitle" value="${referenceInstance.title}" ${isEditable} />
							</div>
						</div>
						<div class="form-group">
							<label for="selectType" class="col-sm-3 control-label">Type</label>
							<div class="col-sm-8">
								<select id="selectType" name="selectType" class="form-control" ${isEditableSelect}>
									<g:each in="${typeListInstance}" var="typeInstance">
										<g:if test="${referenceInstance.type.nombre.equals(typeInstance.nombre)}">
											<option value="${typeInstance.nomenclatura}" selected="selected">${typeInstance.nombre}</option>
										</g:if>
										<g:else>
											<option value="${typeInstance.nomenclatura}">${typeInstance.nombre}</option>
										</g:else>
									</g:each>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label for="inputISBN" class="col-sm-3 control-label">ISBN</label>
							<div class="col-sm-8">
								<input type="text" class="form-control" name="inputISBN" id="inputISBN" value="${referenceInstance.isbn}" readonly="readonly" />
							</div>
						</div>
						<div class="form-group">
							<label for="inputDateCreated" class="col-sm-3 control-label">Created:</label>
							<div class="col-sm-8">
								<input type="text" class="form-control" id="inputDateCreated" name="inputDateCreated" value="${formatDate(format: 'HH:mm - dd/MMM/yyyy', date: referenceInstance.created)}" readonly="readonly" />
							</div>
						</div>
						<div class="form-group">
							<label for="inputSource" class="col-sm-3 control-label">Source</label>
							<div class="col-sm-8">
								<input type="text" class="form-control" name="inputSource" id="inputSource" value="${referenceInstance.source}" ${isEditable} />
							</div>
						</div>
						<div class="form-group">
							<label for="inputSourceType" class="col-sm-3 control-label">Source Type:</label>
							<div class="col-sm-8">
								<input type="text" class="form-control" name="inputSourceType" id="inputSourceType" value="${referenceInstance.source_type}" ${isEditable} />
							</div>
						</div>
						<div class="form-group">
							<label for="inputPages" class="col-sm-3 control-label">Pages</label>
							<div class="col-sm-8">
								<input type="text" class="form-control" id="inputPages" name="inputPages" value="${referenceInstance.pages}" ${isEditable} />
							</div>
						</div>
						<div class="form-group">
							<label for="inputVolume" class="col-sm-3 control-label">Volume</label>
							<div class="col-sm-8">
								<input type="text" class="form-control" id="inputVolume" name="inputVolume" value="${referenceInstance.volume}" ${isEditable}/>
							</div>
						</div>
						<div class="form-group">
							<label for="inputIssue" class="col-sm-3 control-label">Issue</label>
							<div class="col-sm-8">
								<input type="text" class="form-control" id="inputIssue" name="inputIssue" value="${referenceInstance.issue}" ${isEditable} />
							</div>
						</div>
						<div class="form-group">
							<label for="inputCountry" class="col-sm-3 control-label">Country:</label>
							<div class="col-sm-8">
								<input type="text" class="form-control" id="inputCountry" name="inputCountry" value="${referenceInstance.country}" ${isEditable} />
							</div>
						</div>
					<!-- 	<div class="form-group">
							<label for="inputDepartment" class="col-sm-3 control-label">Department:</label>
							<div class="col-sm-8">
								<input type="text" class="form-control" id="inputDepartment" name="inputDepartment" value="${referenceInstance.department}" ${isEditable} />
							</div>
						</div>  -->
						<div class="form-group">
							<label for="inputAbstract" class="col-sm-3 control-label">Abstract</label>
							<div class="col-sm-8">
								<textarea rows="5" cols="10" class="form-control" id="inputAbstract" name="inputAbstract" style="resize: none;" ${isEditable}>${referenceInstance.docAbstract}</textarea>
							</div>
						</div>
						<div class="form-group">
							<label for="inputKeys" class="col-sm-3 control-label">Keywords:<br /><small>(separados por 'intro')</small></label>
							<div class="col-sm-8">
								<textarea rows="5" cols="10" class="form-control token-example-field" id="inputKeys" name="inputKeys" style="resize: none;" ${isEditable}>${listKeywordsString}</textarea>
							</div>
						</div>
						<div class="form-group">
							<label for="inputWebs" class="col-sm-3 control-label">Websites:<br /><small>(separados por 'intro')</small></label>
							<div class="col-sm-8">
								<textarea rows="5" cols="10" class="form-control token-example-field" id="inputWebs" name="inputWebs" style="resize: none;" ${isEditable}>${listWebsString}</textarea>
							</div>
						</div>
					</div>
					<div class="col-lg-6">
						
					<!-- <div class="form-group">
							<label for="selectCriterion" class="col-sm-3 control-label">Criterion</label>
							<div class="col-sm-8">
								<select id="selectCriterion" name="selectCriterion" class="form-control" ${isEditableSelect}>
									<g:each in="${criterionListInstance}" var="criterionInstance">
										<g:if test="${referenceInstance.criterion.name.equals(criterionInstance.name)}">
											<option value="${criterionInstance.nomenclatura}" selected="selected">${criterionInstance.name}</option>
										</g:if>
										<g:else>
											<option value="${criterionInstance.nomenclatura}">${criterionInstance.name}</option>
										</g:else>
									</g:each>
								</select>
							</div>
						</div>  -->
						
						<div class="form-group">
							<label for="inputYear" class="col-sm-3 control-label">Year</label>
							<div class="col-sm-8">
								<input type="text" class="form-control" id="inputYear" name="inputYear" value="${referenceInstance.year}" readonly="readonly" />
							</div>
						</div>
						<div class="form-group">
							<label for="inputCitationKey" class="col-sm-3 control-label">Citation Key:</label>
							<div class="col-sm-8">
								<input type="text" class="form-control" id="inputCitationKey" name="inputCitationKey" value="${referenceInstance.citation_key}" readonly="readonly" />
							</div>
						</div>
						<div class="form-group">
							<label for="inputDateModified" class="col-sm-3 control-label">Modified:</label>
							<div class="col-sm-8">
								<input type="text" class="form-control" id="inputDateModified" name="inputDateModified" value="${formatDate(format: 'HH:mm - dd/MMM/yyyy', date: referenceInstance.last_modified)}" readonly="readonly" />
							</div>
						</div>
						<div class="form-group">
							<label for="inputPublisher" class="col-sm-3 control-label">Publisher:</label>
							<div class="col-sm-8">
								<input type="text" class="form-control" id="inputPublisher" name="inputPublisher" value="${referenceInstance.publisher}" ${isEditable} />
							</div>
						</div>
						<div class="form-group">
							<label for="selectLanguage" class="col-sm-3 control-label">Language</label>
							<div class="col-sm-8">
								<select id="selectLanguage" name="selectLanguage" class="form-control" ${isEditableSelect}>
									<g:each in="${languageListInstance}" var="languageInstance">
										<g:if test="${referenceInstance.language.name.equals(languageInstance.name)}">
											<option value="${languageInstance.code}" selected="selected">${languageInstance.name}</option>
										</g:if>
										<g:else>
											<option value="${languageInstance.code}">${languageInstance.name}</option>
										</g:else>
									</g:each>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label for="inputCity" class="col-sm-3 control-label">City:</label>
							<div class="col-sm-8">
								<input type="text" class="form-control" id="inputCity" name="inputCity" value="${referenceInstance.city}" ${isEditable} />
							</div>
						</div>
						<div class="form-group">
							<label for="inputInstitution" class="col-sm-3 control-label">Institution:</label>
							<div class="col-sm-8">
								<input type="text" class="form-control" id="inputInstitution" name="inputInstitution" value="${referenceInstance.institution}" ${isEditable} />
							</div>
						</div>
						<div class="form-group">
							<label for="inputSeries" class="col-sm-3 control-label">Series:</label>
							<div class="col-sm-8">
								<input type="text" class="form-control" id="inputSeries" name="inputSeries" value="${referenceInstance.series}" ${isEditable} />
							</div>
						</div>
						<div class="form-group">
							<label for="inputChapter" class="col-sm-3 control-label">Chapter:</label>
							<div class="col-sm-8">
								<input type="text" class="form-control" id="inputChapter" name="inputChapter" value="${referenceInstance.chapter}" ${isEditable} />
							</div>
						</div>
						<div class="form-group">
							<label for="inputGenre" class="col-sm-3 control-label">Genre:</label>
							<div class="col-sm-8">
								<input type="text" class="form-control" id="inputGenre" name="inputGenre" value="${referenceInstance.genre}" ${isEditable} />
							</div>
						</div>
						<div class="form-group">
							<label for="inputNotes" class="col-sm-3 control-label">Notes:</label>
							<div class="col-sm-8">
								<textarea rows="5" cols="10" class="form-control" id="inputNotes" name="inputNotes" style="resize: none;" ${isEditableSelect}>${referenceInstance.notes}</textarea>
							</div>
						</div>
						<div class="form-group">
							<label for="inputAuthors" class="col-sm-3 control-label">Authors:<br /><small>('Nombre Apellido' y después intro)</small></label>
							<div class="col-sm-8">
								<textarea rows="5" cols="10" class="form-control token-example-field" id="inputAuthors" name="inputAuthors" style="resize: none;" ${isEditableSelect}>${listAuthorsString}</textarea>
							</div>
						</div>
						<div class="form-group">
							<label for="inputTags" class="col-sm-3 control-label">Tags:<br /><small>(separados por 'intro')</small></label>
							<div class="col-sm-8">
								<textarea rows="5" cols="10" class="form-control token-example-field" id="inputTags" name="inputTags" style="resize: none;" ${isEditableSelect}>${listTagsString}</textarea>
							</div>
						</div>
					</div>
				</div>
					
				
			</div>
			<!-- /#page-wrapper -->
	
	    </div>
	    <!-- /#wrapper -->
    </g:form>    
    
	<%-- JavaScript --%>
    <g:render template="javascript" contextPath="/"/>

</body>

</html>
