package es.uca.pfc

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date;
import org.apache.commons.validator.routines.EmailValidator

import grails.transaction.Transactional

@Transactional
class ToolService {

	Set<String> authors = new TreeSet<String>()
	Set<String> criterions = new TreeSet<String>()
	//Set<String> years = new TreeSet<String>()
	Set<String> engines = new TreeSet<String>()
	Set<String> languages = new TreeSet<String>()
	Set<String> departaments = new TreeSet<String>()
	Set<String> types = new TreeSet<String>()
	String minYear = ""
	String maxYear = ""
	
	def sessionRegistry
	
    def serviceMethod() {

    }
	
	boolean isDigit(String strNumber)
	{
		boolean ok = true;
		for (char c : strNumber.toCharArray())
		{
			if (!Character.isDigit(c))
			{
				ok = false;
				break;
			}
		}
		return ok;
	}
	
	// Método para actualizar el estado de un SLR
	String updateStatus(Slr slrInstance)
	{
		String estado = slrInstance.state.toString();
		
		// Un SLR pasa a estado 2 si tiene como m�nimo 2 criterios y 1 pregunta
		// Un SLR pasa a estado 3 si tiene todo lo del estado 2 y una b�squeda realizada
		
		if(slrInstance.state.equals("fase1"))
		{
			if(slrInstance.criterions.size() >=2 && slrInstance.questions.size() >= 1 && slrInstance.searchs.size() >= 1)
			{
				estado = "fase3"
			}
			else if(slrInstance.criterions.size() >=2 && slrInstance.questions.size() >= 1)
			{
				estado = "fase2"
			}
		}
		else if(slrInstance.state.equals("fase2"))
		{
			if(slrInstance.criterions.size() >=2 && slrInstance.questions.size() >= 1 && slrInstance.searchs.size() >= 1)
			{
				estado = "fase3"
			}
			else if(!(slrInstance.criterions.size() >=2 && slrInstance.questions.size() >= 1))
			{
				estado = "fase1"
			}
		}
		else // fase 3
		{
			if(slrInstance.criterions.size() >=2 && slrInstance.questions.size() >= 1 && slrInstance.searchs.size() == 0)
			{
				estado = "fase2"
			}
			else if(!(slrInstance.criterions.size() >=2 && slrInstance.questions.size() >= 1))
			{
				estado = "fase1"
			}
		}
		
		return estado
	}
	
	public Set<String> getTypes() {
		return types;
	}

	public void setTypes(Set<String> types) {
		this.types = types;
	}

	public Set<String> getAuthors() {
		return authors;
	}

	public Set<String> getCriterions() {
		return criterions;
	}

	public String getMinYear() {
		return minYear;
	}
	
	public String getMaxYear() {
		return maxYear;
	}

	public Set<String> getEngines() {
		return engines;
	}

	public Set<String> getLanguages() {
		return languages;
	}

	public Set<String> getDepartaments() {
		return departaments;
	}
	
	// Método para obtener el �ltimo login de un usuario en formato cadena
	String getTimeString(Date date)
	{
		String timeToString = "";
		
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		
		cal1.setTime(date);
		cal2.setTime(new Date());
		
		def milis1 = cal1.getTimeInMillis();
		def milis2 = cal2.getTimeInMillis();
		
		def diff = milis2 - milis1;
		
		// Calculamos la diferencia en segundos
		def diffSeconds = diff / 1000;
		if(diffSeconds < 60)
		{
			timeToString = "Hace " + Math.round(diffSeconds) + " segundos.";
		}
		else
		{
			def diffMinutes = diff / (60 * 1000);
			if(diffMinutes < 60)
			{
				timeToString = "Hace " + Math.round(diffMinutes) + " minutos.";
			}
			else
			{
				def diffHours = diff / (60 * 60 * 1000);
				if(diffHours < 24)
				{
					timeToString = "Hace " + Math.round(diffHours) + " horas.";
				}
				else
				{
					def diffDays = diff / (24 * 60 * 60 * 1000);
					if(diffDays < 30)
					{
						timeToString = "Hace " + Math.round(diffDays) + " dias.";
					}
					else
					{
						def diffMonths = diff / (30 * 24 * 60 * 60 * 1000);
						if(diffMonths <= 6)
						{
							timeToString = "Hace " + Math.round(diffMonths) + " meses.";
						}
						else
						{
							timeToString = "Hace más de 6 meses";
						}
					}
				}
			}
		}
		
		return timeToString;
	}
	
	List<UserProfile> updateTimeStringUser(List<UserProfile> profiles)
	{
		List<UserProfile> updateProfiles = new ArrayList<UserProfile>()
		for(UserProfile userProfileInstance : profiles)
		{
			Date date = userProfileInstance.ultimaConexion
			String timeToString = getTimeStringFromNow(date);
			
			userProfileInstance.timeString = timeToString
			userProfileInstance.save(failOnError: true)
			updateProfiles.add(userProfileInstance)
		}
		
		return updateProfiles
	}
	
	List<Logger> updateTimeStringLogger(List<Logger> loggers)
	{
		List<Logger> updateLoggers = new ArrayList<Logger>()
		for(Logger loggerInstance : loggers)
		{
			Date date = loggerInstance.submitDate
			String timeToString = getTimeStringFromNow(date);
			
			loggerInstance.timeString = timeToString
			loggerInstance.save(failOnError: true)
			updateLoggers.add(loggerInstance)
		}
		
		return updateLoggers
	}
	
	List<Slr> updateTimeStringSlr(List<Slr> slrs)
	{
		List<Slr> updateSlrs = new ArrayList<Slr>()
		for(Slr slrInstance : slrs)
		{
			Date date = slrInstance.submitDate
			String timeToString = getTimeStringFromNow(date);
			
			slrInstance.timeString = timeToString
			slrInstance.save(failOnError: true)
			updateSlrs.add(slrInstance)
		}
		
		return updateSlrs
	}
	
	String getTimeStringFromNow(Date date)
	{
		String timeToString = "";
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
			
		cal1.setTime(date);
		cal2.setTime(new Date());
			
		def milis1 = cal1.getTimeInMillis();
		def milis2 = cal2.getTimeInMillis();
			
		def diff = milis2 - milis1;
			
		// Calculamos la diferencia en segundos
		def diffSeconds = diff / 1000;
		if(diffSeconds < 60)
		{
			timeToString = "Hace " + Math.round(diffSeconds) + " segundos.";
		}
		else
		{
			def diffMinutes = diff / (60 * 1000);
			if(diffMinutes < 60)
			{
				timeToString = "Hace " + Math.round(diffMinutes) + " minutos.";
			}
			else
			{
				def diffHours = diff / (60 * 60 * 1000);
				if(diffHours < 24)
				{
					timeToString = "Hace " + Math.round(diffHours) + " horas.";
				}
				else
				{
					DateFormat df = new SimpleDateFormat("dd/MM/yyyy")
					timeToString = df.format(date)
				}
			}
		}
		
		return timeToString
	}
	
	Set<Reference> getReferences(Slr slrInstance)
	{
		SortedSet<Reference> references = new TreeSet<Reference>(new Reference());
		
		for(Search search : slrInstance.searchs)
		{
			for (Reference reference : search.references)
			{
				references.add(reference)
			}
			//references.addAll(search.references)
		}
		
		return references;
	}	
	
	void getAllParamsFilter(Set<Reference> references)
	{
		authors = new TreeSet<String>()
		criterions = new TreeSet<String>()
		engines = new TreeSet<String>()
		languages = new TreeSet<String>()
		departaments = new TreeSet<String>()
		types = new TreeSet<String>()
		minYear = "9999"
		maxYear = "0"
		
		for(Reference reference : references)
		{
			// Criterio
			if(!criterions.contains(reference.criterion.name))
			{
				criterions.add(reference.criterion.name)
			}
			
			// Min Year
			if (Integer.parseInt(minYear) > Integer.parseInt(reference.year))
			{
				minYear = reference.year
			}
			
			// Max Year
			if (Integer.parseInt(maxYear) < Integer.parseInt(reference.year))
			{
				maxYear = reference.year
			}
			
			// Engine
			if (!engines.contains(reference.engine.name))
			{
				engines.add(reference.engine.name)
			}
			
			// Idioma
			if (!languages.contains(reference.language.name))
			{
				languages.add(reference.language.name)
			}
			
			// Departamento
			if (!departaments.contains(reference.department))
			{
				departaments.add(reference.department)
			}
			
			// Type
			if (!types.contains(reference.type.nombre))
			{
				types.add(reference.type.nombre)
			}
			
			// Autores
			for (AuthorReference ar : reference.authorsRefs)
			{
				if(!authors.contains(ar.author.display_name))
				{
					authors.add(ar.author.display_name)
				}
			}
		}
	}
	
	SortedSet<Reference> getReferencesWithFilter(String guidSlr, String strSearch)
	{
		SortedSet<Reference> referencesFilter = new TreeSet<Reference>(new Reference())
		
		Slr slrInstance = Slr.findByGuid(guidSlr)
		
		String[] filters = strSearch.trim().split("AND")
		
		List<String> engines = new ArrayList<String>()
		List<String> criterions = new ArrayList<String>()
		List<String> languages = new ArrayList<String>()
		List<String> departments = new ArrayList<String>()
		List<String> types = new ArrayList<String>()
		List<String> authors = new ArrayList<String>()
		String min_year = "";
		String max_year = "";

		for(String filter : filters)
		{
			if (filter.trim().contains("engine="))
			{
				engines.add(filter.trim().replaceAll("engine=", ""))
			}
			else if (filter.trim().contains("criterion="))
			{
				criterions.add(filter.trim().replaceAll("criterion=", ""))
			}
			else if (filter.trim().contains("language="))
			{
				languages.add(filter.trim().replaceAll("language=", ""))
			}
			else if (filter.trim().contains("department="))
			{
				departments.add(filter.trim().replaceAll("department=", ""))
			}
			else if (filter.trim().contains("type="))
			{
				types.add(filter.trim().replaceAll("type=", ""))
			}
			else if (filter.trim().contains("author="))
			{
				authors.add(filter.trim().replaceAll("author=", ""))
			}
			else if (filter.trim().contains("min_year="))
			{
				min_year = filter.trim().replaceAll("min_year=")
			}
			else if (filter.trim().contains("max_year="))
			{
				max_year = filter.trim().replaceAll("max_year=")
			}
		}
		
		int yearMinInt = (min_year.equals("") ? 1800 : Integer.parseInt(min_year));
		int yearMaxInt = (min_year.equals("") ? 2015 : Integer.parseInt(max_year));
		
		for(Search search : slrInstance.searchs)
		{
			for(Reference reference : search.references)
			{
				boolean inserted = true;
				
				if (!(Integer.parseInt(reference.year.toString()) >= yearMinInt && Integer.parseInt(reference.year.toString()) <= yearMaxInt))
				{
					inserted = false;
				}
				else if (engines.size() > 0 && !engines.contains(reference.engine.name))
				{
					inserted = false;
				}
				else if (criterions.size() > 0 && !criterions.contains(reference.criterion.name))
				{
					inserted = false;
				}
				else if (languages.size() > 0 && !languages.contains(reference.language.name))
				{
					inserted = false;
				}
				else if (departments.size() > 0 && !departments.contains(reference.department))
				{
					inserted = false;
				}
				else if (types.size() > 0 && !types.contains(reference.type.nombre))
				{
					inserted = false;
				}
				else if (authors.size() > 0)
				{
					boolean isAuthor = false
					for(AuthorReference authorRef : reference.authorsRefs)
					{
						if (authors.contains(authorRef.author.display_name))
						{
							isAuthor = true
							break;
						}
					}
					
					inserted = inserted && isAuthor
				}
				
				if (inserted)
				{
					referencesFilter.add(reference)
				}
			}
		}
		
		return referencesFilter;
	}
	
	String formatSearchString(String strSearch, String filter)
	{
		String result = "";
		
		if (strSearch.indexOf(filter) != -1)
		{
			String[] strArray = strSearch.trim().split("AND")
			
			for(String str : strArray)
			{
				if (!str.trim().equals(filter.trim()) && !str.trim().equals(""))
				{
					if(result.equals(""))
					{
						result = str.trim()
					}
					else
					{
						result += " AND " + str.trim()
					}
				}
			}
		}
		else
		{
			if (strSearch.equals(""))
			{
				result = filter;
			}
			else
			{
				result = strSearch.trim() + " AND " + filter.trim()
			}
		}
		
		return result;
	}
	
	SortedSet<Reference> getPaginatedReferences(SortedSet<Reference> references, int maxPerPage, int offset)
	{
		SortedSet<Reference> referencesPaginated = new TreeSet<Reference>(new Reference())
		
		int cont = offset;
		
		while (cont < (offset+maxPerPage) && cont < references.size())
		{
			referencesPaginated.add(references.getAt(cont))
			cont++
		}
		
		return referencesPaginated
	}
	
	boolean validateAuthorString(String strAuthors)
	{
		boolean ok = true;
		
		String[] authors = strAuthors.toLowerCase().trim().split(",")
		
		if(authors.size() == 0)
		{
			ok = false;
		}
		else
		{
			for(String author : authors)
			{
				String[] datas = author.split(" ")
				if(datas.size() < 2)
				{
					ok = false;
					break;
				}
			}
		}
		
		return ok;
	}
	
	String getCitationKey(Reference referenceInstance, Set<Author> authors)
	{
		String citation_key = ""
		
		for(Author author : authors)
		{
			String[] apellidos = author.surname.toString().trim().split(" ")
			
			def referenceFilter = Reference.findBySearchAndCitation_keyLike(referenceInstance.search, apellidos[0]+referenceInstance.year)
			
			if (null == referenceFilter)
			{
				citation_key = apellidos[0]+referenceInstance.year
				break;
			}
		}
		
		// Si el citation_key sigue en blanco, insertamos mas de un apellido
		if (citation_key.equals(""))
		{
			Author firstAuthor = null
			
			for(Author author : authors)
			{
				firstAuthor = author
				String apellidos = author.surname.toString().trim().replaceAll(" ", "")				
				
				def referenceFilter = Reference.findBySearchAndCitation_keyLike(referenceInstance.search, apellidos+referenceInstance.year)
				
				if (null == referenceFilter)
				{
					citation_key = apellidos+referenceInstance.year
					break;
				}
			}
			
			// Si sigue en blanco, insertamos uno repetido
			if (citation_key.equals(""))
			{
				citation_key = firstAuthor.surname.toString().trim().split(" ")[0] + referenceInstance.year
			}
		}
		
		return citation_key;
	}
	
	void createLoggersBetweenUsers(UserProfile userLoginProfile, UserProfile userFriendProfile)
	{
		Logger log = null
		
		for(Logger loggerFriendInstance : userFriendProfile.loggers)
		{
			log = null
			if(!loggerFriendInstance.tipo.contains("fr-"))
			{
				if (loggerFriendInstance.tipo == 'bienvenida')
				{
					log = new LoggerFriend(friendProfile: userFriendProfile, tipo: 'fr-bienvenida', submitDate: loggerFriendInstance.submitDate)
				}
				else if (loggerFriendInstance.tipo == 'crear')
				{
					log = new FriendLoggerSlr(friendProfile: userFriendProfile, tipo: 'fr-crear', submitDate: loggerFriendInstance.submitDate,
												slr: loggerFriendInstance.slr)
				}
				else if (loggerFriendInstance.tipo == 'buscar')
				{
					log = new FriendLoggerSlr(friendProfile: userFriendProfile, tipo: 'fr-buscar', submitDate: loggerFriendInstance.submitDate,
						slr: loggerFriendInstance.slr, isSearch: true)
				}
				else if (loggerFriendInstance.tipo == 'seguir')
				{
					log = new FriendLoggerFriend(friendProfile: userFriendProfile, tipo: 'fr-seguir',
												 submitDate: loggerFriendInstance.submitDate,
												 friendFriendProfile: loggerFriendInstance.friendProfile)
				}
				userLoginProfile.addToLoggers(log)
			}
		}

		log = new LoggerFriend(friendProfile: userFriendProfile, tipo: 'seguir')
		userLoginProfile.addToLoggers(log)
		userLoginProfile.save(failOnError: true)
		
		for(Logger loggerInstance : userLoginProfile.loggers)
		{
			log = null
			if (!loggerInstance.tipo.contains("fr-"))
			{
				if (loggerInstance.tipo == 'bienvenida')
				{
					log = new LoggerFriend(friendProfile: userLoginProfile, tipo: 'fr-bienvenida', submitDate: loggerInstance.submitDate)
				}
				else if (loggerInstance.tipo == 'crear')
				{
					log = new FriendLoggerSlr(friendProfile: userLoginProfile, tipo: 'fr-crear', submitDate: loggerInstance.submitDate,
												slr: loggerInstance.slr)
				}
				else if (loggerInstance.tipo == 'buscar')
				{
					log = new FriendLoggerSlr(friendProfile: userLoginProfile, tipo: 'fr-buscar', submitDate: loggerInstance.submitDate,
												slr: loggerInstance.slr, isSearch: true)
				}
				else if (loggerInstance.tipo == 'seguir' && !loggerInstance.friendProfile.guid.equals(userFriendProfile.guid))
				{
					log = new FriendLoggerFriend(friendProfile: userLoginProfile, tipo: 'fr-seguir',
												 submitDate: loggerInstance.submitDate,
												 friendFriendProfile: loggerInstance.friendProfile)
				}
				
				if(log != null)
				{
					userFriendProfile.addToLoggers(log)
				}
			}
		}
		log = new LoggerFriend(friendProfile: userLoginProfile, tipo: 'seguir')
		userFriendProfile.addToLoggers(log)
		userFriendProfile.save(failOnError: true)
	}
	
	String converterToStrOptions(List<Object> elements)
	{
		String strOptions = "";

		for(Object element : elements)
		{
			String value = "";
			String name = "";
			if(element instanceof SearchOperator)
			{
				value = ((SearchOperator)element).value
				name = ((SearchOperator)element).name
			}
			else if(element instanceof SearchComponent)
			{
				value = ((SearchComponent)element).value
				name = ((SearchComponent)element).name
			}
			
			strOptions += "<option value='" + value + "'>" + name + "</option>"
		}
		
		return strOptions;
	}
	
	void deleteSlr(Slr slrInstance)
	{
		if(slrInstance != null)
		{
			// Borramos rreferencias con autores y atributos especificos
			for(Search search : slrInstance.searchs)
			{
				for (Reference reference : search.references)
				{
					// Borramos las referencias con los autores
					AuthorReference.deleteAll(AuthorReference.findAllByReference(reference))
					
					// Borramos las referencias con los atributos especificos
					SpecificAttributeReference.deleteAll(SpecificAttributeReference.findAllByReference(reference))
				
					reference.criterion = null // criterio a nulo
				}
			}
			
			// Borramos loggers
			LoggerSlr.deleteAll(LoggerSlr.findAllBySlr(slrInstance))
			FriendLoggerSlr.deleteAll(FriendLoggerSlr.findAllBySlr(slrInstance))
			
			slrInstance.delete flush: true
		}
	}
	
	boolean isValidEmail(String email)
	{
		return EmailValidator.getInstance(true).isValid(email);
	}
	
	boolean canEnabledDisabled(User userLogin, User userInstance)
	{
		boolean success = false;
		
		if((userLogin.authorities.any { it.authority == "ROLE_SUPER" } && userInstance.authorities.any { it.authority != "ROLE_SUPER" }) || 
		   (userLogin.authorities.any { it.authority == "ROLE_ADMIN" } && userInstance.authorities.any { it.authority == "ROLE_USER" }))
		{
			success = true;
		}
		
		return success;
	}
	
	boolean canChangeRole(User userLogin, User userInstance)
	{
		return canEnabledDisabled(userLogin, userInstance)
	}
	
	List<UserProfile> checkStatusOnline(List<UserProfile> usersProfiles, List<User> usersOnline)
	{		
		if(usersProfiles.size() > 0)
		{		
			List<UserProfile> userProfilesOnline = new ArrayList<UserProfile>()

			for(User user : usersOnline)
			{
				userProfilesOnline.add(user.userProfile)
			}
			
			for(UserProfile userProfile : usersProfiles)
			{
				if(userProfilesOnline.contains(userProfile))
				{
					userProfile.isOnline = true;
					userProfile.save(failOnError: true, flush: true)
				}
			}
		}
		
		return usersProfiles
	}
	
	List<User> getUsersOnline()
	{
		// Obtenemos los usuarios que se encuentran conectados y posteriormente hacemos una 'conversion' para
		// poder obtener toda su informacion
		def usersOnlineAux = new ArrayList<User>(sessionRegistry.getAllPrincipals())
		List<User> usersOnline = new ArrayList<User>()
		for(int i = 0; i<usersOnlineAux.size(); i++)
		{
			usersOnline.add(User.get(Long.parseLong(usersOnlineAux.get(i).id.toString())))
		}
		
		return usersOnline
	}
}
