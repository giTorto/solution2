package introsde.solution.ehealth.Wrappers;

import introsde.solution.ehealth.dozer.MeasureHistoryBean;
import introsde.solution.ehealth.dozer.MeasureHistoryBeanDelegate;
import introsde.solution.ehealth.model.MeasureHistory;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.LinkedList;
import java.util.List;

/**
 * A wrapper for measures
 */
@XmlRootElement(name="measureHistory")
public class WrapperMeasureHistory {


        @XmlElement(name="measure")
        public List<MeasureHistoryBean> list;

        public WrapperMeasureHistory() {

        }


        public WrapperMeasureHistory(List<MeasureHistory> list) {
            List<MeasureHistoryBean> historyBeanList = new LinkedList<MeasureHistoryBean>();

            for(MeasureHistory m : list)
                historyBeanList.add(MeasureHistoryBeanDelegate.mapFromMeasure(m));

            this.list = historyBeanList;

        }


}
