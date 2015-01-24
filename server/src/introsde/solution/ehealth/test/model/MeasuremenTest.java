package introsde.solution.ehealth.test.model;

import introsde.solution.ehealth.model.MeasureHistory;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by giulian on 01/11/14.
 */
public class MeasuremenTest {

    @Test
    public void readMeasurementListTest() {
        System.out.println("--> TEST: readPersonList");
        List<MeasureHistory> list = em.createNamedQuery("Measurement.findAll", MeasureHistory.class)
                .getResultList();
        for (MeasureHistory me : list) {
            System.out.println(me.getPerson().getLastname() +" " + me.getMeasureType() + " : " + me.getValue());
        }
        assertTrue(list.size()>0);
    }

    @Test
    public void readMeasurementsByID() {
        System.out.println("-->TEST: getMeasurementsByID");
        Integer personId=2;
        String stringa= "weight";
        List<MeasureHistory> list = em.createNamedQuery("Measurement.findByPIDAndByTypeOrderByTimestampDesc",MeasureHistory.class)
                .setParameter(1,personId)
                .setParameter(2,stringa)
                .getResultList();

        for (MeasureHistory me : list) {
            System.out.println(me.getPerson().getLastname() +" " + me.getMeasureType() + " : " + me.getValue());
        }
        assertTrue(list.size()>0);
    }

    @BeforeClass
    public static void beforeClass() {
        System.out.println("Testing JPA on lifecoach database using 'introsde-jpa' persistence unit");
        emf = Persistence.createEntityManagerFactory("introsde-jpa");
        em = emf.createEntityManager();
    }

    @AfterClass
    public static void afterClass() {
        em.close();
        emf.close();
    }

    @Before
    public void before() {
        tx = em.getTransaction();
    }


    private static EntityManagerFactory emf;
    private static EntityManager em;
    private EntityTransaction tx;
}
