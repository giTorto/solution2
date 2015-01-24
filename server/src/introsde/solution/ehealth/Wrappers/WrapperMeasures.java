package introsde.solution.ehealth.Wrappers;

import introsde.solution.ehealth.dozer.MeasureTypeBean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.LinkedList;
import java.util.List;

/**
 * A wrrapper for measureTypes
 */
@XmlRootElement(name="measureTypes")
public class WrapperMeasures {

    @XmlElement(name="measureType")
    public List<String> list = new LinkedList<String>();

    public WrapperMeasures() {

    }


    public WrapperMeasures(List<MeasureTypeBean> list) {
        for (MeasureTypeBean m : list)
            this.list.add(m.getMeasureType());

    }



}
