package introsde.solution.ehealth.Wrappers;

import introsde.solution.ehealth.dozer.PersonBean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.LinkedList;
import java.util.List;

/**
 * A wrapper for people
 */
@XmlRootElement(name="people")
public class WrapperPeople {

    @XmlElement(name="person")
    public List<PersonBean> list;

    public WrapperPeople() {

    }


    public WrapperPeople(List<PersonBean> list) {
        this.list = list;

    }
}
