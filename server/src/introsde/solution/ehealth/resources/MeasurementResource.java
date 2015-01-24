package introsde.solution.ehealth.resources;

import introsde.solution.ehealth.dozer.MeasureHistoryBean;
import introsde.solution.ehealth.dozer.MeasureHistoryBeanDelegate;
import introsde.solution.ehealth.Wrappers.WrapperMeasureHistory;
import introsde.solution.ehealth.model.MeasureHistory;
import introsde.solution.ehealth.model.MeasureType;
import introsde.solution.ehealth.model.Person;


import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This class manages the get and post request for MeasureHistory objects given
 * a certain person.
 */
@Stateless
@LocalBean
public class MeasurementResource {
    @Context
    UriInfo uriInfo;
    @Context
    Request request;

    EntityManager entityManager;

    int id;
    String type;
    int mid;

    public MeasurementResource(UriInfo uriInfo, Request request, int id, EntityManager em, String type, int mid) {
        this.uriInfo = uriInfo;
        this.request = request;
        this.entityManager = em;
        this.id = id;
        this.type = type;
        this.mid = mid;
    }

    public MeasurementResource(UriInfo uriInfo, Request request, int id, String type, int mid) {
        this.uriInfo = uriInfo;
        this.request = request;
        this.id = id;
        this.type = type;
        this.mid = mid;
    }


    /**
     * This method returns the list of MeasureHistory object for a certain measure type
     * for a specific person. This method is made for the browser.
     * @return the WrapperMeasureHistory that contains a list of MeasureHistory objects
     */
    @GET
    @Produces(MediaType.TEXT_XML)
    public WrapperMeasureHistory getMeasurementsHTML() {
        List<MeasureHistory> measure = this.getHistoryById(id, type);
        if (measure == null)
            throw new RuntimeException("Get: Measurements for person with " + id + " not found");
        System.out.println("Returning ... " + measure.size() + " measurements");
        return new WrapperMeasureHistory(measure);
    }

    /**
     * This method returns the list of MeasureHistory object for a certain measure type
     * for a specific person.
     * @return the WrapperMeasureHistory that contains a list of MeasureHistory objects
     */
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public WrapperMeasureHistory getMeasurements() {
        MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
        List<MeasureHistory> measure = this.getHistoryById(id, type);

        if (measure == null)
            throw new RuntimeException("Get: Measurements for person with " + id + " not found");
        System.out.println("Returning ... " + measure.size() + " measurements");

        if (queryParams.size() != 0) {
            measure = this.getMeasurementsBetween(queryParams.getFirst("before"),
                    queryParams.getFirst("after"), measure);
        }


        return new WrapperMeasureHistory(measure);
    }

    /**
     * This method returns the list of MeasureHistory object between
     * a certain range of dates given some parameters.
     * @param before the maximum date. A Measure History object can be retrieved if and
     *               only if is minor than this date
     * @param after the minimum date. A Measure History object can be retrieved if and
     *               only if is major than this date
     * @param measures the list of MeasureHistory objects to check if they are
     *                 in the range given
     * @return the List of MeasureHistory object that are in the given range
     */
    public List<MeasureHistory> getMeasurementsBetween(String before, String after, List<MeasureHistory> measures) {
        Date beforeDate;
        Date afterDate;

        beforeDate = Person.fromStringToDate(before);
        afterDate = Person.fromStringToDate(after);

        List<MeasureHistory> measure = new ArrayList<MeasureHistory>();
        Date temp;
        for (MeasureHistory m : measures) {
            temp = Person.fromStringToDate(m.getCreated());
            if (temp.before(beforeDate) && temp.after(afterDate))
                measure.add(m);
        }

        if (measure == null)
            throw new RuntimeException("Get: Measurements for person with " + id + " not found");
        System.out.println("Returning ... " + measure.size() + " measurements");
        return measure;


    }

    /**
     * This method returns the List of MeasureHistory objects that belongs
     * to a ertain person and has a certain measure type
     * @param personId the identifier of the person that belongs the list of
     *                 MeasureHistory objects
     * @param type the measure type that we want to retrieve
     * @return a List of MeasureHistory object that belongs to the request person
     */
    public List<MeasureHistory> getHistoryById(int personId, String type) {
        System.out.println("Reading person from DB with id: " + personId);

        List<MeasureHistory> me = MeasureHistory.getMeasurementsById(personId, type);

        return me;
    }

    /**
     * Allows to add a new measurement, by sending a value through a form. The other
     * attributes of the MeasureHistory object are retrieved from the URL, for the
     * date is setted up the current timestamp.
     * @param value the value that has to be set up in the MeasureHistory object
     * @return the MeasureHistory object with the required value
     */
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response setMeasurement(@FormParam("value") String value) {
        System.out.println("Adding a new measure...");
        MeasureHistory me = createFromValue(value);

        if (MeasureHistory.saveHealthMeasureHistory(me) != null)
            return Response.ok().build();
        else
            return Response.serverError().build();
    }

    /**
     * This method creates from a given value a MeasureHistory object.
     * @param value the value is set up in the MeasureHistory object
     * @return the measureHistory object with the required
     */
    public MeasureHistory createFromValue(String value) {
        MeasureHistory me = new MeasureHistory();
        me.setMeasureType(MeasureType.getMeasureDefinitionByName(type));
        me.setValue(value);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        me.setCreated(dateFormat.format(cal.getTime()));
        me.setPerson(Person.getPersonById(id));
        return me;
    }

    /**
     * This method add a new measure in the database, by the given MeasureHistory object.
     * If the created attribute is not given is set up with the current timestamp
     * @param m is used to create a new Measurement.
     * @return the MeasureHistoryBean object created
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public MeasureHistoryBean setMeasurementFromValues(MeasureHistory m) {

        m.setPerson(Person.getPersonById(id));
        m.setMeasureType(MeasureType.getMeasureDefinitionByName(type));
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        if (m.getCreated() != null) {
            m.setCreated(m.getCreated());
        } else {
            m.setCreated(dateFormat.format(Calendar.getInstance().getTime()));
        }


        m = MeasureHistory.saveHealthMeasureHistory(m);


        if (m == null)
            throw new BadRequestException();

        return MeasureHistoryBeanDelegate.mapFromMeasure(m);
    }

}
