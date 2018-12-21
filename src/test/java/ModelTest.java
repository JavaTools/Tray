import junit.framework.Assert;
import org.junit.Test;
import tray.model.Week;

import java.util.ArrayList;

/**
 * Tray - ERROR
 * #######################
 * start;2018-05-16(09:18)
 * stop;2018-05-16(17:17)
 * stop;2018-05-16(17:19)
 * #######################
 * start;2016-05-30(09:47)
 * stop;2016-05-30(16:54)
 * start;2016-05-31(09:37)
 * stop;2016-05-31(18:00)
 * stop;2016-05-31(18:01)
 * start;2016-06-01(08:57)
 * start;2016-06-01(12:07)
 * start;2016-06-01(16:21)
 * stop;2016-06-01(17:09)
 * start;2016-06-01(21:35)
 * start;2016-06-02(08:07)
 * stop;2016-06-02(12:36)
 * start;2016-06-02(12:52)
 * stop;2016-06-02(12:58)
 * start;2016-06-03(11:51)
 * #######################
 * start;2017-05-08(09:01)
 * stop;2017-05-08(17:17)
 * stop;2017-05-08(17:18)
 * stop;2017-05-08(17:18)
 * start;2017-05-08(20:21)
 * start;2017-05-09(08:34)
 * stop;2017-05-09(17:38)
 * start;2017-05-09(19:51)
 * stop;2017-05-09(21:49)
 * start;2017-05-10(08:45)
 * stop;2017-05-10(16:01)
 * start;2017-05-11(08:47)
 * #######################
 * start;2017-05-22(09:02)
 * stop;2017-05-22(09:03)
 * stop;2017-05-22(15:42)
 * start;2017-05-23(08:38)
 * #######################
 *
 * nedenstående 27,68!?!?
 *
 * start;2018-03-12(07:15)
 * start;2018-03-12(16:44)
 * stop;2018-03-12(17:28)
 * stop;2018-03-12(17:28)
 */
public class ModelTest {

    @Test
    public void testSingleNormalSpan() throws Exception {
        ArrayList<String> stamps = new ArrayList<String>();
        stamps.add("start;2014-01-13(09:00)");
        stamps.add("stop;2014-01-13(17:00)");
        Week week = new Week("2014-03",stamps);
        week.calculateValues();
        Assert.assertEquals(week.getActualSpan(),8*60);
    }

    @Test
    public void testSingleSpecialStampWithNoStart() throws Exception {
        ArrayList<String> stamps = new ArrayList<String>();
        stamps.add("stop;2014-01-13(02:00)");
        Week week = new Week("2014-03",stamps);
        week.calculateValues();
        Assert.assertEquals(
            "When first stamp is a stop stamp, time should be calculated from midnight (implicit start)",
            week.getActualSpan(),2*60
        );
    }

    @Test
    public void testSingleSpecialStampWithNoStop() throws Exception {
        ArrayList<String> stamps = new ArrayList<String>();
        stamps.add("start;2014-01-13(22:00)");
        Week week = new Week("2014-03",stamps);
        week.calculateValues();
        Assert.assertEquals(
            "When there is no stop-stamp, time should be calculated to midnight (implicit stop)",
            week.getActualSpan(),2*60
        );
    }
}
