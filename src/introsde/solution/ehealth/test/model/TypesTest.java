package introsde.solution.ehealth.test.model;
import introsde.solution.ehealth.model.MeasureType;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.*;
import java.util.List;

import static org.junit.Assert.*;

public class TypesTest {

    @Test
    public void readMeasureTypeListTest() {
        System.out.println("--> TEST: readPersonList");
        List<MeasureType> list = em.createNamedQuery("MeasureType.findAll", MeasureType.class)
                .getResultList();
        for (MeasureType me : list) {
            System.out.println(me.getIdMeasureDef()+": " + me.getMeasureName());
        }
        assertTrue(list.size()>0);
    }

    @Test
    public void readMeasureByNameTest() {
        System.out.println("--> TEST: readWeight");
        MeasureType m = em.createNamedQuery("MeasureType.findByName", MeasureType.class).setParameter(1,"weight").getSingleResult();
        System.out.println(m.getIdMeasureDef()+": " + m.getMeasureName());

        assertTrue(m.getMeasureName()!=null);
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
