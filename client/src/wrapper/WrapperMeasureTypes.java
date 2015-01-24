package wrapper;

import model.MeasureTypeBean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by gt on 11/12/14.
 */
@XmlRootElement(name="measureTypes")
public class WrapperMeasureTypes {

    @XmlElement(name="measureType")
    public List<String> list = new LinkedList<String>();

    public WrapperMeasureTypes() {

    }


    public WrapperMeasureTypes(List<MeasureTypeBean> list) {
        for (MeasureTypeBean m : list)
            this.list.add(m.getValue());

    }



}
