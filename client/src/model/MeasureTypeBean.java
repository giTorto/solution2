package model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * This class is used to have an high-level representation of the MeasureType class that
 * is the entity bound to the DB
 */
@XmlRootElement
public class MeasureTypeBean {

    private String value;

    @JsonProperty("value")
    @XmlValue
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
