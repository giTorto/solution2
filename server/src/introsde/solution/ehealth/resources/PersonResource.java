package introsde.solution.ehealth.resources;

import introsde.solution.ehealth.model.Person;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * This class manages the get,delete and put request for the Person objects.
 */
@Stateless
@LocalBean
public class PersonResource {
    @Context
    UriInfo uriInfo;
    @Context
    Request request;

    EntityManager entityManager;

    int id;

    public PersonResource(UriInfo uriInfo, Request request, int id, EntityManager em) {
        this.uriInfo = uriInfo;
        this.request = request;
        this.id = id;
        this.entityManager = em;
    }

    public PersonResource(UriInfo uriInfo, Request request, int id) {
        this.uriInfo = uriInfo;
        this.request = request;
        this.id = id;
    }


    /**
     * This method allows to retrieve a person given its identifier
     * @return a person given the id set up in the constructor
     */
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Person getPerson() {
        Person person = this.getPersonById(id);
        if (person == null)
            throw new RuntimeException("Get: Person with " + id + " not found");
        return person;
    }

    /**
     *  This method allows to retrieve a person given its identifier. For the browser
     * @return a person given the id set up in the constructor
     */
    @GET
    @Produces(MediaType.TEXT_XML)
    public Person getPersonHTML() {
        Person person = this.getPersonById(id);
        if (person == null)
            throw new RuntimeException("Get: Person with " + id + " not found");
        System.out.println("Returning person... " + person.getId());
        return person;
    }

    /**
     * This method allows to update the person details in the DB, given an
     * object in the request.
     * @param person the person object that contains the new details
     * @return the updated person
     */
    @PUT
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Person putPerson(Person person) {

        System.out.println("--> Updating Person...  " + this.id);
        System.out.println("--> " + person.toString());


        Person existing = getPersonById(this.id);

        if (existing == null) {
            throw new BadRequestException();
        } else {
            if (person.getFirstname()!=null && person.getFirstname()!="")
                existing.setFirstname(person.getFirstname());
            if (person.getLastname()!=null && person.getLastname()!="")
                existing.setLastname(person.getLastname());
            if (person.getBirthdate()!=null && person.getBirthdate()!="")
                existing.setBirthdate(person.getBirthdate());

            existing =Person.updatePerson(existing);
        }

        return existing;
    }

    /**
     * This method allows to delete a person with a certain id
     */
    @DELETE
    public void deletePerson() {
        Person c = getPersonById(id);
        if (c == null)
            throw new RuntimeException("Delete: Person with " + id
                    + " not found");

        Person.removePerson(c);
    }

    /**
     * This method allows to retrieve a person given a certain ID
     * @param personId the person identifier in the DB
     * @return the person
     */
    public Person getPersonById(int personId) {
        System.out.println("Reading person from DB with id: " + personId);
        //Person person = entityManager.find(Person.class, personId);

        Person person = Person.getPersonById(personId);

        if (person==null)
            throw new NotFoundException();

        return person;
    }


}
