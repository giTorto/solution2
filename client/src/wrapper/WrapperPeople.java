package wrapper;

import model.PersonBean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by gt on 11/17/14.
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
