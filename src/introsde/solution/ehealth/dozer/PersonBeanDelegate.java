package introsde.solution.ehealth.dozer;

import introsde.solution.ehealth.model.Person;
import org.dozer.DozerBeanMapper;

import javax.xml.bind.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class has the role to map a Person entity object to its high-level
 * representation: PersonBean. This class contains the mapping functions
 * for the two classes
 */
public class PersonBeanDelegate {
    /**
     * This function maps a Person to PersonBeam.
     * @param sourceObject the Person object, from which the Bean is created
     * @return the PersonBean obtained from the sourceObject's mapping
     */
    public static PersonBean mapFromPerson(Person sourceObject) {
        List<String> myMappingFiles = new ArrayList<String>();
        myMappingFiles.add("dozerMappings.xml");

        DozerBeanMapper mapper = new DozerBeanMapper();
        mapper.setMappingFiles(myMappingFiles);

        System.out.println("Mapping Person to PersonBean...");
        return (PersonBean) mapper.map(sourceObject, PersonBean.class);
    }


}
