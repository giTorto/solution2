package introsde.solution.ehealth.resources;

import introsde.solution.ehealth.Wrappers.WrapperPeople;
import introsde.solution.ehealth.dozer.*;
import introsde.solution.ehealth.model.HealthProfile;
import introsde.solution.ehealth.model.MeasureHistory;
import introsde.solution.ehealth.model.MeasureType;
import introsde.solution.ehealth.model.Person;

import java.io.IOException;
import java.util.*;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceUnit;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.*;


/**
 * This class manages the biggest part of the request.
 */
@Stateless
@LocalBean//Will map the resource to the URL /introsde.solution.ehealth/v2
@Path("/person")
public class PersonCollectionResource {

    // Allows to insert contextual objects into the class,
    // e.g. ServletContext, Request, Response, UriInfo
    @Context
    UriInfo uriInfo;
    @Context
    Request request;

    // THIS IS NOT WORKING
    @PersistenceUnit(unitName = "introsde-jpa")
    EntityManager entityManager;

    // THIS IS NOT WORKING
    @PersistenceContext(unitName = "introsde-jpa", type = PersistenceContextType.TRANSACTION)
    private EntityManagerFactory entityManagerFactory;

    /**
     * Return the list of people to the user in the browser
     * @return a WrapperPeople object that contains the list of people
     */
    @GET
    @Produces({MediaType.TEXT_HTML, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public WrapperPeople getPersonsBrowser() {
        MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();

        System.out.println("Getting list of people...");
        List<Person> people = Person.getAll();

        if (queryParams.size() > 0)
            people = this.selectPeopleByValues(queryParams, people);

        List<PersonBean> personBeanList = new ArrayList<PersonBean>();

        for (Person p : people)
            personBeanList.add(PersonBeanDelegate.mapFromPerson(p));

        return new WrapperPeople(personBeanList);
    }


    /**
     * This method allows to create a new person, by sending paramters through a form
     * @param firstname  the firstname of the new created person
     * @param lastname the lastname of the new created person
     * @param servletResponse the response that gives the context
     * @return a response set up with a http ok code or a http error code
     * @throws IOException
     */
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response newPerson(
            @FormParam("firstname") String firstname,
            @FormParam("lastname") String lastname,
            @Context HttpServletResponse servletResponse) throws IOException {
        Person p = new Person();
        p.setFirstname(firstname);
        p.setLastname(lastname);
        Person.savePerson(p);
        if (p != null)
            return Response.ok().build();
        else
            return Response.serverError().build();
    }

    /**
     * This method creates a new person from the JSON/XML received in the request
     * @param person the object received from the client
     * @return the PersonBean stored in the DB
     * @throws IOException
     */
    @POST
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public PersonBean newPerson(Person person) throws IOException {
        System.out.println("Creating new person...");

        return PersonBeanDelegate.mapFromPerson(Person.savePerson(person));
    }

    /**
     * This method allows to obtain a person with a certain id
     * @param id the identifier of a person in the DB
     * @return PersonResource that "redirect" our request to the PersonResource class
     */
    @Path("{personId}")
    public PersonResource getPerson(@PathParam("personId") int id) {

        return new PersonResource(uriInfo, request, id);
    }

    /**
     * This method let the control to the MeasurementResource class when
     * the specific path written below is requested.
     * @param id the identifier of a person in the DB
     * @param type the measure type tells us which type of measure we want to retrieve
     * @return
     */
    @Path("{personId}/{measure}")
    public MeasurementResource getMeasurementsByIDandMeasure(@PathParam("personId") int id,
                                                             @PathParam("measure") String type) {

        return new MeasurementResource(uriInfo, request, id, type, -1);
    }

    /**
     * This method allows to retrieve a specific measure
     * @param id the identifier of a person in the DB
     * @param type the measure type tells us which type of measure we want to retrieve
     * @param mid the mid is the identifier for the measure in the DB
     * @return the MeasureHistoryBean object related to the given parameters
     */
    @GET
    @Path("{personId}/{measure}/{mid}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public MeasureHistoryBean getMeasurementsByMid(@PathParam("personId") int id,
                                                   @PathParam("measure") String type,
                                                   @PathParam("mid") Integer mid) {

        MeasureHistory measure = MeasureHistory.getMeasurementsByMid(mid);
        if (measure == null)
            throw new RuntimeException("Get: Measurements for person with " + id + " not found");
        System.out.println("Returning ... " + measure.getMeasureType() + " measurements");
        return MeasureHistoryBeanDelegate.mapFromMeasure(measure);
    }

    /**
     * This method allows us to update a measure with a specific identifier
     * @param id the identifier of a person in the DB
     * @param type the measure type tells us which type of measure we want to retrieve
     * @param mid the mid is the identifier for the measure in the DB
     * @param me the object that contains the update information to insert in the DB
     * @return the MeasureHistoryBean object updated
     */
    @PUT
    @Path("{personId}/{measure}/{mid}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public MeasureHistoryBean setMeasurementPut(@PathParam("personId") int id,
                                                @PathParam("measure") String type,
                                                @PathParam("mid") Integer mid,
                                                MeasureHistory me) {
        System.out.println("Adding a new measure...");
        // MeasureHistory me = createFromValue(value);
        me.setMid(mid);
        me.setPerson(Person.getPersonById(id));
        me.setMeasureType(MeasureType.getMeasureDefinitionByName(type));

        if (MeasureHistory.updateHealthMeasureHistory(me) == null)
            throw new NotFoundException();

        return MeasureHistoryBeanDelegate.mapFromMeasure(me);
    }

    /**
     * This method allows to obtain a list of Person object that has the value
     * for a certain measure type between a certain range
     * @param queryParams contains the range of the values accepted(the max and the min)
     *                    and the type of measure we want to select
     * @param persons the list of all people to check
     * @return the List of Person object that respect the given parameters
     */
    public List<Person> selectPeopleByValues(MultivaluedMap<String, String> queryParams, List<Person> persons) {
        String type = queryParams.getFirst("measureType");
        String max = queryParams.getFirst("max");
        String min = queryParams.getFirst("min");
        Double maxVal = null;
        Double minVal = null ;
        HealthProfile hp;
        Double value = null;
        List<Person> personList = new ArrayList<Person>();

        for (Person p : persons) {

            hp = p.getHealthProfile();

            if (type.equals("weight")) {
                if (hp.getWeight() != null && !hp.getWeight().equals(""))
                    value = Double.parseDouble(hp.getWeight());

            } else if (type.equals("height")) {
                if (hp.getHeight() != null && !hp.getHeight().equals(""))
                    value = Double.parseDouble(hp.getHeight());
            } else if (type.equals("bloodPressure")) {
                if (hp.getBloodPressure() != null && !hp.getBloodPressure().equals(""))
                    value = Double.parseDouble(hp.getBloodPressure());
            }

            if (value!=null) {
                if ( (min != null && max != null) ) {
                    maxVal = Double.parseDouble(max);
                    minVal = Double.parseDouble(min);
                    if (value > minVal && value < maxVal)
                        personList.add(p);
                } else if (min != null && max == null) {
                    minVal = Double.parseDouble(min);
                    if (value>minVal)
                        personList.add(p);
                } else if (max != null && min==null) {
                    maxVal = Double.parseDouble(max);
                    if (value<maxVal)
                        personList.add(p);
                }
            }

        }


        return personList;

    }

}
