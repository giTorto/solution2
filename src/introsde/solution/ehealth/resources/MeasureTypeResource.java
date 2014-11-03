package introsde.solution.ehealth.resources;

import introsde.solution.ehealth.dozer.MeasureTypeBean;
import introsde.solution.ehealth.dozer.TypesBeanDelegate;
import introsde.solution.ehealth.Wrappers.WrapperMeasures;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.*;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

/**
 * Created by giulian on 02/11/14.
 */
@Stateless
@LocalBean
@Path("/measureTypes")
public class MeasureTypeResource {

    @Context
    UriInfo uriInfo;
    @Context
    Request request;

    @PersistenceUnit(unitName = "introsde-jpa")
    EntityManager entityManager;

    // THIS IS NOT WORKING
    @PersistenceContext(unitName = "introsde-jpa", type = PersistenceContextType.TRANSACTION)
    private EntityManagerFactory entityManagerFactory;


    //for brows
    @GET
    @Produces(MediaType.TEXT_HTML)
    public List<MeasureTypeBean> getMeasurementsHTML() {
        List<introsde.solution.ehealth.model.MeasureType> types = introsde.solution.ehealth.model.MeasureType.getAll();
        List<MeasureTypeBean> beanList = new Vector<MeasureTypeBean>();
        for (introsde.solution.ehealth.model.MeasureType type : types)
            beanList.add(TypesBeanDelegate.mapFromMeasure(type));

        if (types == null)
            throw new RuntimeException("Get: Types not found");
        System.out.println("Returning ... " + types.size() + " types");

        return beanList;
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public WrapperMeasures getMeasurements() {
        List<introsde.solution.ehealth.model.MeasureType> types = introsde.solution.ehealth.model.MeasureType.getAll();
        List<MeasureTypeBean> beanList = new LinkedList<MeasureTypeBean>();
        for (introsde.solution.ehealth.model.MeasureType type : types)
            beanList.add(TypesBeanDelegate.mapFromMeasure(type));

        if (types == null)
            throw new RuntimeException("Get: Types not found");
        System.out.println("Returning ... " + types.size() + " types");

        return new WrapperMeasures(beanList);
    }

}
