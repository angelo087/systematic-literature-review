package es.uca.pfc

import grails.transaction.Transactional;

class ResearchQuestionController {

	static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
	
	def springSecurityService
	def toolService
	def strSearch = ""
	int maxPerPage = 10
	
    def index() 
	{
		redirect(controller: 'index', action: 'index')
	}
	
	@Transactional
	def save()
	{
		boolean isQuestionList = false
		def guidSlr = ""
		
		if (params.guidSlrQuestion.toString().equals("null") || params.guidSlrQuestion.toString().equals(""))
		{
			isQuestionList = true
			guidSlr = params.guidSlr.toString()
		}
		else
		{
			guidSlr = params.guidSlrQuestion.toString()
		}
		
		def slrInstance = Slr.findByGuid(guidSlr)
		
		if (null == slrInstance)
		{
			redirect(controller: 'index', action: 'index')
		}
		else
		{
			String enunciadoQuestion = params.enunciado.toString()
			String errorQuestion = ""
			boolean successQuestion = false
			
			if(enunciadoQuestion.equals("null") || enunciadoQuestion.length() <= 5)
			{
				errorQuestion = "ERROR: El enunciado debe contener mas de 5 caracteres."
			}
			else
			{
				enunciadoQuestion = enunciadoQuestion.toLowerCase();
				def questionInstance = ResearchQuestion.findBySlrAndEnunciadoLike(slrInstance, enunciadoQuestion)
				
				if (null != questionInstance)
				{
					errorQuestion = "ERROR: Ya existe una pregunta con ese enunciado."
				}
				
				if (errorQuestion.equals(""))
				{
					questionInstance = new ResearchQuestion(enunciado: enunciadoQuestion)
					slrInstance.addToQuestions(questionInstance)
					slrInstance.save(failOnError: true)
					successQuestion = true
				}
			}
			
			// Procede de la lista de questions
			if (isQuestionList)
			{
				redirect(controller: 'slr', action: 'researchQuestions', params: [errorQuestion: errorQuestion, successQuestion: successQuestion,
																	   enunciadoQuestion: enunciadoQuestion, guid: guidSlr])
			}
			else // Procede de la lista de slrs
			{
				redirect(controller: 'slr', action: 'myList', params: [errorQuestion: errorQuestion, successQuestion: successQuestion,
																		enunciadoQuestion: enunciadoQuestion, guidSlrError: guidSlr])
			}
		}
	}
	
	@Transactional
	def delete()
	{
		def idQuestion = params.idQuestion.toString()
		def guidSlr = params.guidSlr.toString()
		
		def questionInstance = ResearchQuestion.get(Long.parseLong(idQuestion))
		def slrInstance = Slr.findByGuid(guidSlr)
		
		if(questionInstance != null)
		{			
			questionInstance.delete flush: true
		}
		
		redirect(controller: 'slr', action: 'researchQuestions', params: [guid: guidSlr])
	}
}
