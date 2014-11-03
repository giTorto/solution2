package introsde.solution.ehealth.test.model;

import introsde.solution.ehealth.dozer.PersonBean;
import introsde.solution.ehealth.dozer.PersonBeanDelegate;
import introsde.solution.ehealth.model.Person;
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
public class PersonBeanTest {


    @Test
    public void TestBean () {
        System.out.println("--> TEST: readPersonList");
        List<Person> list = em.createNamedQuery("Person.findAll", Person.class)
                .getResultList();
        for (Person person : list) {
            PersonBean pb = PersonBeanDelegate.mapFromPerson(person);
            System.out.println("--> Person = "+pb.getFirstname());
            System.out.println("--> ---> LastName = "+pb.getLastname());
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
