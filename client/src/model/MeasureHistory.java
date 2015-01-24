package model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;


@XmlRootElement(name="measure")
public class MeasureHistory implements Serializable {
	private static final long serialVersionUID = 1L;

    //findByAgeOrderByLastnameDesc

	private int mid;

    private String created;

	private String value;

    private MeasureTypeBean measureType;

    // notice that we haven't included a reference to the history in Person
	// this means that we don't have to make this attribute XmlTransient

	private PersonBean person;

	public MeasureHistory() {
	}

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

	public MeasureTypeBean getMeasureType() {
	    return measureType;
	}
	public void setMeasureType(MeasureTypeBean param) {
	    this.measureType = param;
	}

    @XmlTransient
	public PersonBean getPerson() {
	    return person;
	}

	public void setPerson(PersonBean param) {
	    this.person = param;
	}


}
