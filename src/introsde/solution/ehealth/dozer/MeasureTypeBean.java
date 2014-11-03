package introsde.solution.ehealth.dozer;

import org.dozer.Mapping;

import javax.xml.bind.annotation.*;

/**
 * This class is used to have an high-level representation of the MeasureType class that
 * is the entity bound to the DB
 */
@XmlRootElement(name="MeasureType")
public class MeasureTypeBean {

    private String measureType;

    @XmlValue
    public String getMeasureType() {
        return measureType;
    }

    public void setMeasureType(String measureType) {
        this.measureType = measureType;
    }
}
