package introsde.solution.ehealth.model;

import introsde.solution.ehealth.dao.LifeCoachDao;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;


/**
 * The persistent class for the "Measurement" database table.
 * This class manages the communication between the application and the DB.
 */
@Entity
@Table(name="Measurement")
@NamedQueries( {
        @NamedQuery(name="Measurement.findAll", query="SELECT h FROM MeasureHistory h"),
        @NamedQuery(name="Measurement.findByPIDAndByTypeOrderByTimestampDesc",
                query="SELECT h FROM MeasureHistory h inner join MeasureType t ON h.measureType.idMeasureDef=t.idMeasureDef " +
                        "WHERE h.person.id=?1 and t.measureName like ?2 ORDER BY h.created ASC"),
        @NamedQuery(name="Measurement.findByPIDAndByTypAndByMid",
            query="SELECT h FROM MeasureHistory h inner join MeasureType t ON h.measureType.idMeasureDef=t.idMeasureDef " +
                    "WHERE h.mid=?1")

        })
@XmlRootElement(name="measure")
public class MeasureHistory implements Serializable {
	private static final long serialVersionUID = 1L;

    //findByAgeOrderByLastnameDesc
	@Id
    @GeneratedValue(generator="sqlite_mhistory")
    @TableGenerator(name="sqlite_mhistory", table="Sequence",
            pkColumnName="name", valueColumnName="seq",
            pkColumnValue="MeasureHistory")
	@Column(name="measureID")
	private int mid;


	@Column(name="timestamp")
    private String created;

	@Column(name="value")
	private String value;


	@ManyToOne
	@JoinColumn(name = "tID", referencedColumnName = "typeID")
    private MeasureType measureType;


    // notice that we haven't included a reference to the history in Person
	// this means that we don't have to make this attribute XmlTransient
	@ManyToOne
	@JoinColumn(name = "pID", referencedColumnName = "personID")
	private Person person;

	public MeasureHistory() {
	}

	public int getMid() {
		return this.mid;
	}

	public void setMid(int idMeasureHistory) {
		this.mid = idMeasureHistory;
	}

	public String getCreated() {
		return this.created;
	}

	public void setCreated(String timestamp) {
		this.created = timestamp;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public MeasureType getMeasureType() {
	    return measureType;
	}
	public void setMeasureType(MeasureType param) {
	    this.measureType = param;
	}

    @XmlTransient
	public Person getPerson() {
	    return person;
	}

	public void setPerson(Person param) {
	    this.person = param;
	}

    /**
     * This method given the id of the measurement return the related MeasureHistory.
     * @param id the id of the measure
     * @return the object that contains all the detail about it
     */
	public static MeasureHistory getHealthMeasureHistoryById(int id) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		MeasureHistory p = em.find(MeasureHistory.class, id);
		LifeCoachDao.instance.closeConnections(em);
		return p;
	}

    /**
     * This method returns all the measures stored in the database
     * @return the list of measures stored in the database
     */
	public static List<MeasureHistory> getAll() {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
	    List<MeasureHistory> list = em.createNamedQuery("Measurement.findAll", MeasureHistory.class).getResultList();
	    LifeCoachDao.instance.closeConnections(em);
	    return list;
	}

    /**
     * This method allow to store a new Measure in the DB
     * @param p is the Measure object to store
     * @return the object stored in the DB
     */
	public static MeasureHistory saveHealthMeasureHistory(MeasureHistory p) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(p);
		tx.commit();
	    LifeCoachDao.instance.closeConnections(em);

	    return p;
	}

    /**
     * This method allow to update a Measure already existent in the DB
     * @param p is the Measure object to store
     * @return the object updated from the DB
     */
	public static MeasureHistory updateHealthMeasureHistory(MeasureHistory p) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		p=em.merge(p);
		tx.commit();
	    LifeCoachDao.instance.closeConnections(em);
	    return p;
	}

    /**
     * This method remove a Measure  existent in the DB
     * @param p is the Measure object to delete
     */
	public static void removeHealthMeasureHistory(MeasureHistory p) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
	    p=em.merge(p);
	    em.remove(p);
	    tx.commit();
	    LifeCoachDao.instance.closeConnections(em);
	}

    /**
     * This method allows to retrieve all the measurement for a certain type
     * related to a certain person in the database
     * @param personId the id of the person stored in the DB
     * @param type the type of the measures that we want to retrieve
     * @return the list of MeasureHistory objects for the required type and for the required person
     */
    public static List<MeasureHistory> getMeasurementsById(int personId, String type) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        List<MeasureHistory> list = em.createNamedQuery("Measurement.findByPIDAndByTypeOrderByTimestampDesc",MeasureHistory.class)
                .setParameter(1, personId)
                .setParameter(2, type)
                .getResultList();
        LifeCoachDao.instance.closeConnections(em);

        return list;
    }

    /**
     * This method allows to retrieve a specific measurement by its id in the database
     * @param mid is the id related to a specific MeasureHistory object in the DB
     * @return the MeasureHistory object, given that id
     */
    public static MeasureHistory getMeasurementsByMid(int mid) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        MeasureHistory list = em.createNamedQuery("Measurement.findByPIDAndByTypAndByMid", MeasureHistory.class)
                .setParameter(1, mid)
                .getSingleResult();
        LifeCoachDao.instance.closeConnections(em);

        return list;
    }


}
