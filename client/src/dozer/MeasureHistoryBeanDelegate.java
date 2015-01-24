package dozer;

import model.MeasureHistory;
import model.MeasureHistoryBean;
import org.dozer.DozerBeanMapper;

import javax.xml.bind.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class has the role to map a MeasureHistory object to its high-level
 * representation: MeasureHistoryBean. This class contains the mapping functions
 * for the two classes
 */
public class MeasureHistoryBeanDelegate {
    /**
     * This function maps a MeasureHistory to MeasureHistoryBean.
     * @param sourceObject the MeasureHistory object, from which the Bean is created
     * @return the MeasureHistoryBean obtained from the sourceObject's mapping
     */
    public static MeasureHistoryBean mapFromMeasure(MeasureHistory sourceObject) {
        List<String> myMappingFiles = new ArrayList<String>();
        myMappingFiles.add("dozerMappings.xml");

        DozerBeanMapper mapper = new DozerBeanMapper();
        mapper.setMappingFiles(myMappingFiles);

        System.out.println("Mapping Measurement to MeasureBean...");
        return (MeasureHistoryBean) mapper.map(sourceObject, MeasureHistoryBean.class);
    }

}
