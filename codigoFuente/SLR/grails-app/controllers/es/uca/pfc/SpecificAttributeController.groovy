package es.uca.pfc

import grails.transaction.Transactional;

class SpecificAttributeController {

	static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
	
	def springSecurityService
	def toolService
	
    def index() 
	{ 
		redirect(controller: 'slr', action: 'myList')
	}
	
	@Transactional
	def save()
	{
		boolean isAttributeList = false
		def guidSlr = ""
		
		if (params.guidSlrAttribute.toString().equals("null") || params.guidSlrAttribute.toString().equals(""))
		{
			isAttributeList = true
			guidSlr = params.guidSlr.toString()
		}
		else
		{
			guidSlr = params.guidSlrAttribute.toString()
		}
		println "GUID: " + guidSlr
		
		def slrInstance = Slr.findByGuid(guidSlr)
		
		if (null == slrInstance)
		{
			redirect(controller: 'index', action: 'index')
		}
		else
		{
			String nombreAttribute = params.nombre.toString()
			String tipoAttribute = params.tipo.toString()
			String opcionesAttribute = params.opciones.toString()
			String errorAttribute = ""
			boolean successAttribute = false
			String[] opciones
			List<String> opcionesCorrect = new ArrayList<String>()

			if(nombreAttribute.equals("null") || nombreAttribute.length() < 5)
			{
				errorAttribute = "ERROR: El nombre debe contener mas de 5 caracteres."
			}
			else if(tipoAttribute.equals("list") && (opcionesAttribute.trim().equals("") || opcionesAttribute.trim().equals(" ") || (null == params.opciones)))
			{
				errorAttribute = "ERROR: El tipo lista debe contener al menos una opcion."
			}
			else
			{
				nombreAttribute = nombreAttribute.toLowerCase();
				def attributeInstance = SpecificAttribute.findBySlrAndNameLike(slrInstance, nombreAttribute)
				
				if (null != attributeInstance)
				{
					errorAttribute = "ERROR: Ya existe un atributo especifico con ese nombre."
				}
				else if (tipoAttribute.equals("list"))
				{
					opciones = opcionesAttribute.toString().trim().toLowerCase().split(";")
					
					for(int i=0; i<opciones.length; i++)
					{
						if(!opciones[i].equals(null) && !opciones[i].toString().trim().toLowerCase().equals("") && !opciones[i].toString().trim().toLowerCase().equals("null") && !opciones[i].toString().trim().toLowerCase().equals(" "))
						{
							opcionesCorrect.add(opciones[i])
						}
					}
					
					if (opcionesCorrect.size() == 0)
					{
						errorAttribute = "ERROR: Debes introducir al menos una opción para el tipo lista."
					}
				}
				
				if (errorAttribute.equals(""))
				{
					if(tipoAttribute == "list")
					{
						attributeInstance = new SpecificAttributeMultipleValue(name: nombreAttribute)
						for(String op : opcionesCorrect)
						{
							attributeInstance.addToOptions(op)
						}
						attributeInstance.optionDefault = opcionesCorrect.get(0)
					}
					else
					{
						attributeInstance = new SpecificAttribute(name: nombreAttribute, tipo: tipoAttribute)
					}

					slrInstance.addToSpecAttributes(attributeInstance)
					slrInstance.save(failOnError: true)
					successAttribute = true
					
					// Insertamos el nuevo atributo en cada una de las referencias
					for(Search search : slrInstance.searchs)
					{
						for(Reference reference : search.references)
						{
							if (tipoAttribute == "list")
							{
								reference.addToSpecificAttributes(attribute: attributeInstance, value: attributeInstance.optionDefault)
							}
							else
							{
								reference.addToSpecificAttributes(attribute: attributeInstance)
							}
							reference.save(failOnError: true)
						}
					}
				}
			}
			
			// Procede de la lista de atributos
			if (isAttributeList)
			{
				redirect(controller: 'slr', action: 'specAttributes', params: [errorAttribute: errorAttribute, successAttribute: successAttribute,
																	   nombreAttribute: nombreAttribute, opcionesAttribute: opcionesAttribute,
																	   tipoAttribute: tipoAttribute, guid: guidSlr])
			}
			else // Procede de la lista de slrs
			{
				redirect(controller: 'slr', action: 'myList', params: [errorAttribute: errorAttribute, successAttribute: successAttribute,
																		nombreAttribute: nombreAttribute, opcionesAttribute: opcionesAttribute,
																		tipoAttribute: tipoAttribute, guidSlrError: guidSlr])
			}
		}
	}
	
	@Transactional
	def delete()
	{
		def idAttribute = params.idAttribute.toString()
		def guidSlr = params.guidSlr.toString()
		
		def attributeInstance = SpecificAttribute.get(Long.parseLong(idAttribute))
		def slrInstance = Slr.findByGuid(guidSlr)
		
		if(attributeInstance != null)
		{
			// Actualizamos las referencias
			for(Search search : slrInstance.searchs)
			{
				for(Reference reference : search.references)
				{
					SpecificAttributeReference.deleteAll(SpecificAttributeReference.findAllByReferenceAndAttribute(reference, attributeInstance))
				}
			}
			
			attributeInstance.delete flush: true
		}
		
		redirect(controller: 'slr', action: 'specAttributes', params: [guid: guidSlr])
	}
}
