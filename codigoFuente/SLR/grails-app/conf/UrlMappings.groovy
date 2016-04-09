class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        //"/"(view:"/index")
        "/"(controller:"index", action: "index")
        "500"(view:'/error')
		
		"/menu"(controller: "index", action: "menu")
		
		"/loggers"(controller: "index", action: "loggers")
		
		"/faqs"(controller: "index", action: "faqs")
		
		"/user/" {
			controller="user"
			action = "index"
		}
		
		"/user/$guid" {
			controller="user"
			action = "show"
		}
		
		"/$guid/searchs" {
			controller="slr"
			action="searchs"
		}
		
		"/$guid/references" {
			controller="slr"
			action="references"
		}
		
		"/$guid/criterions" {
			controller="slr"
			action="criterions"
		}
		
		"/$guid/specAttributes" {
			controller="slr"
			action="specAttributes"
		}
		
		"/$guid/researchQuestions" {
			controller="slr"
			action="researchQuestions"
		}
		
		"/reference/$idmend" {
			controller="reference"
			action="show"
		}
		
		"/$guidSlr/search/create" {
			controller="search"
			action="create"
		}
		
		"/$guidSlr/show" {
			controller="slr"
			action="show"
		}
	}
}
