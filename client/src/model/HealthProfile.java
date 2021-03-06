package model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class is an high-level representation of the HistoryMeasurement.
 * Indeed in the DB there isn't a specific table for the HealthProfile, but is
 * generated automatically, by taking the most updated values.
 */
@XmlRootElement(name="HealthProfile")
@XmlAccessorType(XmlAccessType.FIELD)
public class HealthProfile {

    private String weight;
    private String height;
    private String bloodPressure;
    private String todaySteps;


    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public String getTodaySteps() {
        return todaySteps;
    }

    public void setTodaySteps(String todaySteps) {
        this.todaySteps = todaySteps;
    }
}

