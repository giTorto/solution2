package introsde.solution.ehealth.dozer;

import introsde.solution.ehealth.model.MeasureType;
import org.dozer.DozerBeanMapper;

import javax.xml.bind.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class has the role to map a MeasureType entity object to its high-level
 * representation: MeasureTypeBean. This class contains the mapping functions
 * for the two classes
 */
public class TypesBeanDelegate {

    /**
     * This function maps a MeasureType to MeasureTypeBean.
     * @param sourceObject the MeasureType object, from which the Bean is created
     * @return the MeasureTypeBean obtained from the sourceObject's mapping
     */
    public static MeasureTypeBean mapFromMeasure(MeasureType sourceObject) {
        List<String> myMappingFiles = new ArrayList<String>();
        myMappingFiles.add("dozerMappings.xml");

        DozerBeanMapper mapper = new DozerBeanMapper();
        mapper.setMappingFiles(myMappingFiles);

        System.out.println("Mapping Person to PersonBean...");
        return (MeasureTypeBean) mapper.map(sourceObject, MeasureTypeBean.class);
    }


}
