package model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This is the high-level representation class of the MeasureHistory class. Indeed this
 * class has just three parameters which are shown to the user when send requests
 * for measurements.
 */
@XmlRootElement(name="measure")
public class MeasureHistoryBean {

    private int mid;
    private String created;
    private String value;

    public int getMid() {
        return this.mid;
    }

    public void setMid(int idMeasureHistory) {
        this.mid = idMeasureHistory;
    }

    public String getCreated() {
        return this.created;
    }

    public void setCreated(String timestamp) {
        this.created = timestamp;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}


