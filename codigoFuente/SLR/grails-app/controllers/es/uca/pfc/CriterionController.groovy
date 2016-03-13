package es.uca.pfc

import grails.transaction.Transactional;

class CriterionController {
	
	static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
	
	def springSecurityService
	
	def index() 
	{ 
		redirect(controller: 'slr', action: 'myList')
	}
	
	@Transactional
	def save()
	{
		boolean isCriterionList = false
		def guidSlr = ""
		
		if (params.guidSlrCriterion.toString().equals("null") || params.guidSlrCriterion.toString().equals(""))
		{
			isCriterionList = true
			guidSlr = params.guidSlr.toString()
		}
		else
		{
			guidSlr = params.guidSlrCriterion.toString()
		}
		
		def slrInstance = Slr.findByGuid(guidSlr)
		
		if (null == slrInstance)
		{
			redirect(controller: 'index', action: 'index')
		}
		else
		{
			String nombreCriterion = params.nombre.toString()
			String descripcionCriterion = params.descripcion.toString()
			String errorCriterion = ""
			boolean successCriterion = false
			
			if(nombreCriterion.equals("null") || nombreCriterion.length() <= 5)
			{
				errorCriterion = "ERROR: El nombre debe contener mas de 5 caracteres."
			}
			else if (descripcionCriterion.equals("null") || descripcionCriterion.length() <= 5)
			{
				errorCriterion = "ERROR: La descripcion debe contener mas de 5 caracteres."
			}
			else
			{
				nombreCriterion = nombreCriterion.toLowerCase();
				def criterionInstance = Criterion.findBySlrAndNameLike(slrInstance, nombreCriterion)
				
				if (null != criterionInstance)
				{
					errorCriterion = "ERROR: Ya existe un criterio con ese nombre."
				}
				
				if (errorCriterion.equals(""))
				{
					criterionInstance = new Criterion(name: nombreCriterion, description: descripcionCriterion, nomenclatura: "cr_"+nombreCriterion)
					slrInstance.addToCriterions(criterionInstance)
					slrInstance.save(failOnError: true)
					successCriterion = true
				}
			}
			
			// Procede de la lista de criterios
			if (isCriterionList)
			{
				redirect(controller: 'slr', action: 'criterions', params: [errorCriterion: errorCriterion, successCriterion: successCriterion, 
																	   nombreCriterion: nombreCriterion, descripcionCriterion: descripcionCriterion,
																	   guid: guidSlr])
			}
			else // Procede de la lista de slrs
			{
				redirect(controller: 'slr', action: 'myList', params: [errorCriterion: errorCriterion, successCriterion: successCriterion,
																		nombreCriterion: nombreCriterion, descripcionCriterion: descripcionCriterion,
																		guidSlrError: guidSlr])
			}
		}
	}
	
	@Transactional
	def delete()
	{
		def idCriterion = params.idCriterion.toString()
		def guidSlr = params.guidSlr.toString()
		
		def criterionInstance = Criterion.get(Long.parseLong(idCriterion))
		def slrInstance = Slr.findByGuid(guidSlr)
		def criterionIncluded = Criterion.findByNomenclaturaLikeAndSlr("cr_included",slrInstance)
		
		if(criterionInstance != null)
		{
			// Actualizamos las referencias
			for(Search search : slrInstance.searchs)
			{
				for(Reference reference : search.references)
				{
					if (reference.criterion == criterionInstance)
					{
						reference.criterion = criterionIncluded
						reference.save(failOnError: true)
					}
				}
			}
			
			criterionInstance.delete flush: true
		}
		
		redirect(controller: 'slr', action: 'criterions', params: [guid: guidSlr])
	}
}
