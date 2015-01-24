package introsde.solution.ehealth.model;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.jaxrs.json.annotation.JSONP;
import introsde.solution.ehealth.dao.LifeCoachDao;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;


/**
 * The persistent class for the "MeasureType" database table.
 * This class manages the communication with the MeasureType table in the DB.
 * For now are allowed only read function, due to the assignment.
 */
@Entity
@Table(name="MeasureType")
@NamedQueries({
        @NamedQuery(name = "MeasureType.findAll", query = "SELECT m FROM MeasureType m"),
        @NamedQuery(name = "MeasureType.findByName", query = "SELECT m FROM MeasureType m WHERE m.measureName like ?1")
})
@XmlRootElement(name="Measures")
public class MeasureType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="sqlite_measuredef")
	@TableGenerator(name="sqlite_measuredef", table="Sequence",
	    pkColumnName="name", valueColumnName="seq",
	    pkColumnValue="MeasureDefinition")
	@Column(name="typeID")
	private int idMeasureDef;

	@Column(name="name")
	private String measureName;

	public MeasureType() {
	}

    @XmlTransient
	public int getIdMeasureDef() {
		return this.idMeasureDef;
	}

	public void setIdMeasureDef(int idMeasureDef) {
		this.idMeasureDef = idMeasureDef;
	}


    @XmlValue()
	public String getMeasureName() {
		return this.measureName;
	}

	public void setMeasureName(String measureName) {
		this.measureName = measureName;
	}

	// database operations

    /**
     * Given a specific type name, this method allows to retrieve the associated
     * measure type. This function is useful to know the id associated to a
     * certain measure type.
     * @param name the name of the type that we want to obtain
     * @return the MeasureType object related to the given type name
     */
	public static MeasureType getMeasureDefinitionByName(String name) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		MeasureType m = em.createNamedQuery("MeasureType.findByName", MeasureType.class).setParameter(1,name).getSingleResult();
		LifeCoachDao.instance.closeConnections(em);
		return m;
	}

    /**
     * This method allows to retrieve all the types contained in the DB
     * @return a List of Measure Type object containing all the type information
     */
	public static List<MeasureType> getAll() {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
	    List<MeasureType> list = em.createNamedQuery("MeasureType.findAll", MeasureType.class).getResultList();
	    LifeCoachDao.instance.closeConnections(em);
	    return list;
	}
	

}
