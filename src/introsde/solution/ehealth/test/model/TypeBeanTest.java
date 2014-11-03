package introsde.solution.ehealth.test.model;

import introsde.solution.ehealth.dozer.MeasureTypeBean;
import introsde.solution.ehealth.dozer.TypesBeanDelegate;
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
 * Created by giulian on 03/11/14.
 */
public class TypeBeanTest {

    @Test
    public void TestBean () {
        System.out.println("--> TEST: readPersonList");
        List<introsde.solution.ehealth.model.MeasureType> list = em.createNamedQuery("MeasureType.findAll", introsde.solution.ehealth.model.MeasureType.class)
                .getResultList();
        for (introsde.solution.ehealth.model.MeasureType measure : list) {
            MeasureTypeBean tb = TypesBeanDelegate.mapFromMeasure(measure);
            System.out.println("--> Type = "+tb.getMeasureType() );
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
