package wrapper;

import dozer.MeasureHistoryBeanDelegate;
import model.MeasureHistory;
import model.MeasureHistoryBean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by gt on 11/18/14.
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
