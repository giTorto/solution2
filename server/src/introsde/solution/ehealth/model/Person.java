package introsde.solution.ehealth.model;

import introsde.solution.ehealth.dao.LifeCoachDao;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.XmlElement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * The persistent class for the "Person" database table.
 * This class manages the communication between the application and the DB.
 */
@Entity
@Table(name = "Person")
@NamedQuery(name = "Person.findAll", query = "SELECT p FROM Person p")
@XmlRootElement(name = "person")
public class Person {
    private static final long serialVersionUID = 1L;

    @Id
    // For sqlite in particular, you need to use the following @GeneratedValue annotation
    // This holds also for the other tables
    // SQLITE implements auto increment ids through named sequences that are stored in a
    // special table named "sqlite_sequence"
    @GeneratedValue(generator = "sqlite_person")
    @TableGenerator(name = "sqlite_person", table = "Sequence",
            pkColumnName = "name", valueColumnName = "seq",
            pkColumnValue = "Person")
    @Column(name = "personID")
    private int id;

    @Column(name = "lastName")
    private String lastname;

    @Column(name = "firstName")
    private String firstname;

    @Column(name = "birthDate")
    private String birthdate;

    @Transient
    private HealthProfile healthProfile;


    // mappedBy must be equal to the name of the attribute in LifeStatus that maps this relation
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderBy("created DESC")
    private List<MeasureHistory> historyMeasureHistories;

    public Person() {
    }

    // Database operations
    // Notice that, for this example, we create and destroy and entityManager on each operation.

    /**
     * This method allows to retrieve a Person object from the database by its id
     * @param personId which is the unique identifier for the person in the database
     * @return the Person Object which contains all the person detail retrieved from the DB
     */
    public static Person getPersonById(int personId) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        Person p = em.find(Person.class, personId);

        LifeCoachDao.instance.closeConnections(em);
        return p;
    }

    /**
     * This method allows to retrieve the people in the DB
     * @return the list of person object contained in the DB
     */
    public static List<Person> getAll() {

        System.out.println("--> Initializing Entity manager...");
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        System.out.println("--> Querying the database for all the people...");
        List<Person> list = em.createNamedQuery("Person.findAll", Person.class).getResultList();
        System.out.println("--> Closing connections of entity manager...");
        LifeCoachDao.instance.closeConnections(em);
        return list;
    }

    /**
     * This method allow to store a new Person in the DB
     * @param p is the Person object to store
     * @return the object stored in the DB
     */
    public static Person savePerson(Person p) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(p);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
        return p;
    }

    /**
     * This method allow to update a Person already existent in the DB
     * @param p is the Person object to store
     * @return the object updated from the DB
     */
    public static Person updatePerson(Person p) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        p = em.merge(p);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
        return p;
    }

    /**
     * This method remove a Person  existent in the DB
     * @param p is the Person object to delete
     */
    public static void removePerson(Person p) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        p = em.merge(p);
        em.remove(p);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
    }

    public String getFirstname() {
        return this.firstname;
    }

    public void setFirstname(String name) {
        this.firstname = name;
    }

    public String getLastname() {
        return this.lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getBirthdate() {
        return this.birthdate;
    }

    public void setBirthdate(String birthDate) {
        this.birthdate = birthDate;
    }


    /**
     * This method retrieve from the database the measures related to this person.
     * After that sets up the Health Profile with the most recent values.
     * @return the HealthProfile related to the Person, filled with the last measure values
     */
    @XmlElement()
    public HealthProfile getHealthProfile() {
        HealthProfile hp = new HealthProfile();
        Date dataWeight = null;
        Date dataHeight = null;
        Date dataBlood = null;
        MeasureHistory lastWeight = null;
        MeasureHistory lastHeight = null;
        MeasureHistory lastBloodPresure = null;


        lastWeight = lastMeasure("weight");
        lastHeight = lastMeasure("height");
        lastBloodPresure = lastMeasure("bloodPressure");


        if (lastHeight != null)
            hp.setHeight(lastHeight.getValue());
        if (lastWeight != null)
            hp.setWeight(lastWeight.getValue());
        if (lastBloodPresure != null)
            hp.setBloodPressure(lastBloodPresure.getValue());


        return hp;
    }

    /**
     * This method allows to add the HealthProfile information to the Person.
     * Keeping update the DB, based on the measurement table.
     * @param hp the HealthProfile from which the History of Measurement is updated
     */
    public void setHealthProfile(HealthProfile hp) {
        this.fromHpToMeasurement(hp);
        this.healthProfile = hp;
    }

    @XmlTransient
    public List<MeasureHistory> getMeasurements() {
        return historyMeasureHistories;
    }

    public void setHistoryMeasureHistories(List<MeasureHistory> param) {
        this.historyMeasureHistories = param;
    }

    public int getId() {
        return id;
    }

    public void setId(int idPerson) {
        this.id = idPerson;
    }

    /**
     * This method takes the values from the HealthProfile and insert them
     * into the historyMeasureHistories which manage the persistence of the measures
     * in the Person class with the Measurement table in the DB.
     * @param hp the HealthProfile object from which the History of Measurement is updated
     */
    private void fromHpToMeasurement(HealthProfile hp) {
        List<MeasureHistory> measures = new Vector<MeasureHistory>();
        MeasureHistory weight = fromValueToMeasure(hp.getWeight(),"weight");
        MeasureHistory height = fromValueToMeasure(hp.getHeight(),"height");
        MeasureHistory blood = fromValueToMeasure(hp.getBloodPressure(),"bloodPressure");

        if (height!=null)
            measures.add(height);

        if (weight!=null)
            measures.add(weight);

        if (blood!=null)
            measures.add(blood);

        this.setHistoryMeasureHistories(measures);
    }

    /**
     * This method converts a text in a Date. It's really useful because it accept two
     * different date representation.
     * @param text the string date to convert in a date
     * @return the converted date
     */
    public static Date fromStringToDate(String text) {
        Date temp = null;
        try {
            if (text.matches("[0-9]{4}(-)[0-9]{2}(-)[0-9]{2}"))
                temp = new SimpleDateFormat("yyyy-MM-dd", Locale.ITALY).parse(text);
            else
                temp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ITALY).parse(text);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return temp;
    }

    /**
     * This method retrieve the last measure for a given type for the person id
     * of the object.
     * @param type is the measure type from which we want to extract the last measure
     * @return the last MeasureHistory object containing the last measure
     */
    private MeasureHistory lastMeasure(String type) {
        Date dataWeight = null;
        MeasureHistory lastWeight = null;

        for (MeasureHistory m : MeasureHistory.getMeasurementsById(this.id, type)) {

            if (dataWeight == null) {
                dataWeight = fromStringToDate(m.getCreated());

                lastWeight = m;
            } else {
                Date temp = fromStringToDate(m.getCreated());

                if (temp.after(dataWeight)) {
                    dataWeight = temp;
                    lastWeight = m;
                }
            }
        }

        return lastWeight;
    }

    /**
     * This method creates for a value a new MeasureHistory object, by using the current
     * date and some information available in the person object. The remaining values
     * are given as parameters.
     * @param value this string contains the value to set up in the MeasureHistory object
     * @param type this string contains the measure type to set up in the MeasureHistory object
     * @return the MeasureHistory object set up with the given parameter and other information
     *          available in the object Person
     */
    private MeasureHistory fromValueToMeasure(String value, String type) {
        MeasureHistory measureHistory = new MeasureHistory();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();

        measureHistory.setCreated(dateFormat.format(cal.getTime()));
        measureHistory.setPerson(this);
        measureHistory.setValue(value);

        measureHistory.setMeasureType(MeasureType.getMeasureDefinitionByName(type));

        return measureHistory;
    }
}
