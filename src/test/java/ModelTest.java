import junit.framework.Assert;
import org.junit.Test;
import tray.model.Week;

import java.util.ArrayList;

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
