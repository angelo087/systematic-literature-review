package es.uca.pfc



import grails.test.mixin.*
import spock.lang.*

@TestFor(EngineSearchController)
@Mock(EngineSearch)
class EngineSearchControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            !model.engineSearchInstanceList
            model.engineSearchInstanceCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.engineSearchInstance!= null
    }

    void "Test the save action correctly persists an instance"() {

        when:"The save action is executed with an invalid instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'POST'
            def engineSearch = new EngineSearch()
            engineSearch.validate()
            controller.save(engineSearch)

        then:"The create view is rendered again with the correct model"
            model.engineSearchInstance!= null
            view == 'create'

        when:"The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            engineSearch = new EngineSearch(params)

            controller.save(engineSearch)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/engineSearch/show/1'
            controller.flash.message != null
            EngineSearch.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            def engineSearch = new EngineSearch(params)
            controller.show(engineSearch)

        then:"A model is populated containing the domain instance"
            model.engineSearchInstance == engineSearch
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def engineSearch = new EngineSearch(params)
            controller.edit(engineSearch)

        then:"A model is populated containing the domain instance"
            model.engineSearchInstance == engineSearch
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            controller.update(null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/engineSearch/index'
            flash.message != null


        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def engineSearch = new EngineSearch()
            engineSearch.validate()
            controller.update(engineSearch)

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.engineSearchInstance == engineSearch

        when:"A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            engineSearch = new EngineSearch(params).save(flush: true)
            controller.update(engineSearch)

        then:"A redirect is issues to the show action"
            response.redirectedUrl == "/engineSearch/show/$engineSearch.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(null)

        then:"A 404 is returned"
            response.redirectedUrl == '/engineSearch/index'
            flash.message != null

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def engineSearch = new EngineSearch(params).save(flush: true)

        then:"It exists"
            EngineSearch.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(engineSearch)

        then:"The instance is deleted"
            EngineSearch.count() == 0
            response.redirectedUrl == '/engineSearch/index'
            flash.message != null
    }
}
