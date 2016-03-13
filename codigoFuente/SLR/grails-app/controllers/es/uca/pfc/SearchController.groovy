package es.uca.pfc

import grails.transaction.Transactional;

class SearchController {

	static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
	
	def springSecurityService
	def toolService
	def mendeleyService
    
	def index() 
	{
		redirect(controller: 'index', action: 'index')
	}
	
	def create()
	{
		def isLogin = springSecurityService.loggedIn
		
		String guidSlr = (null == params.guidSlr ? "" : params.guidSlr.toString())
		
		if(!isLogin || guidSlr.equals(""))
		{
			redirect(controller: 'index', action: 'index')
		}
		else
		{
			def slrInstance = Slr.findByGuid(guidSlr)
			def error = (null == params.error ? "" : params.error.toString())
			
			if (null == slrInstance)
			{
				redirect(controller: 'index', action: 'index')
			}
			else
			{				
				def terminosSearch = (null == params.terminosSearch ? "" : params.terminosSearch.toString())
				def operatorSearch = (null == params.operatorSearch ? "all" : params.operatorSearch.toString())
				
				def opACMSearch
				def opIEEESearch
				def opSCIENCESearch
				def opSPRINGERSearch
				
				if (null == params.opACMSearch && null == params.opIEEESearch && null == params.opSCIENCESearch 
					&& null == params.opSPRINGERSearch)
				{
					opACMSearch = EngineSearch.findByNameIlike("acm").checkDefault
					opIEEESearch = EngineSearch.findByNameIlike("ieee").checkDefault
					opSCIENCESearch = EngineSearch.findByNameIlike("science").checkDefault
					opSPRINGERSearch = EngineSearch.findByNameIlike("springer").checkDefault
				}
				else
				{
					opACMSearch = params.opACMSearch
					opIEEESearch = params.opIEEESearch
					opSCIENCESearch = params.opSCIENCESearch
					opSPRINGERSearch = params.opSPRINGERSearch
				}
				
				def componentSearch = params.componentSearch
				def minYearSearch = (null == params.minYearSearch ? 1980 : Integer.parseInt(params.minYearSearch.toString()))
				def maxYearSearch = (null == params.maxYearSearch ? Calendar.getInstance().get(Calendar.YEAR) : Integer.parseInt(params.maxYearSearch.toString()))
				def maxTotalSearch = params.maxTotalSearch
				
				def engineListInstance = EngineSearch.findAllByStatus(true)
				def operatorListInstance = SearchOperator.list()
				def componentListInstance = SearchComponent.list()
				def minYear = 1980
				def maxYear = Calendar.getInstance().get(Calendar.YEAR);
				
				[
					slrInstance: slrInstance, error: error, engineListInstance: engineListInstance,
					operatorListInstance: operatorListInstance, componentListInstance: componentListInstance,
					minYear: minYear, maxYear: maxYear,
					terminosSearch: terminosSearch,
					operatorSearch: operatorSearch,
					opACMSearch: opACMSearch,
					opIEEESearch: opIEEESearch,
					opSCIENCESearch: opSCIENCESearch,
					opSPRINGERSearch: opSPRINGERSearch,
					componentSearch: componentSearch,
					minYearSearch: minYearSearch,
					maxYearSearch: maxYearSearch,
					maxTotalSearch: maxTotalSearch
				]
			}
		}
		
		
	}
	
	@Transactional
	def save()
	{
		def isLogin = springSecurityService.loggedIn
		String guidSlr = (null == params.guidSlr ? "" : params.guidSlr.toString())

		if(!isLogin || guidSlr.equals(""))
		{
			redirect(controller: 'index', action: 'index')
		}
		else
		{
			String terminos = (null == params.inputTerminos ? "" : params.inputTerminos.toString().trim())
			String operator = (null == params.inputOperator ? "" : params.inputOperator.toString().trim())
			boolean opACM = (null == params.engineACM ? false : true)
			boolean opIEEE = (null == params.engineIEEE ? false : true)
			boolean opSCIENCE = (null == params.engineSCIENCE ? false : true)
			boolean opSPRINGER = (null == params.engineSPRINGER ? false : true)
			String component = (null == params.selectComponent ? "" : params.selectComponent.toString())
			String[] years = params.inputYears.toString().split(",")
			String minYear = years[0].trim()
			String maxYear = years[1].trim()
			String maxTotal = (null == params.inputTotalMax ? "" : params.inputTotalMax.toString().trim())
			String error = ""
			
			if(terminos.equals("") || terminos.length() < 5)
			{
				error = "ERROR: Los terminos deben contener mas de 5 caracteres o no puede estar vacio."
			}
			else if (!(opACM || opIEEE || opSCIENCE || opSPRINGER))
			{
				error = "ERROR: Debes seleccionar al menos un motor de busqueda."
			}
			else if(maxTotal.equals("") || !toolService.isDigit(maxTotal))
			{
				error = "ERROR: Debes introducir un numero valido en el maximo total."
			}
			
			if(error != "")
			{				
				redirect(controller: 'search', action: 'create',
				 params: [
							 error: error,
							 guidSlr: guidSlr,
							 terminosSearch: terminos,
							 operatorSearch: operator,
							 opACMSearch: opACM,
							 opIEEESearch: opIEEE,
							 opSCIENCESearch: opSCIENCE,
							 opSPRINGERSearch: opSPRINGER,
							 componentSearch: component,
							 minYearSearch: minYear,
							 maxYearSearch: maxYear,
							 maxTotalSearch: maxTotal
						 ])
			}
			else
			{
				// Creamos las busquedas
				List<EngineSearch> engines = new ArrayList<EngineSearch>()
				if (opACM)
				{
					engines.add(EngineSearch.findByName("ACM"));
				}
				if (opIEEE)
				{
					engines.add(EngineSearch.findByName("IEEE"));
				}
				if (opSCIENCE)
				{
					engines.add(EngineSearch.findByName("SCIENCE"));
				}
				if (opSPRINGER)
				{
					engines.add(EngineSearch.findByName("SPRINGER"));
				}
				
				mendeleyService.insertSearchsBackground(guidSlr, terminos, operator, engines, component, minYear, maxYear, maxTotal)
				
				redirect(controller: 'slr', action: 'searchs', params: [guid: guidSlr])
			}
		}
	}
	
	@Transactional
	def delete()
	{
		String guid = (null == params.guidSearch ? "" : params.guidSearch.toString())
		
		if (guid.equals(""))
		{
			redirect(controller: 'index', action: 'index')
		}
		else
		{
			def searchInstance = Search.findByGuid(guid)
			
			if (null == searchInstance)
			{
				redirect(controller: 'index', action: 'index')
			}
			else
			{
				String guidSlr = searchInstance.slr.guid
				
				Reference.deleteAll(searchInstance.references)
				
				searchInstance.delete flush: true
				
				redirect(controller: 'slr', action: 'searchs', params: [guid: guidSlr])
			}
		}
	}
}
