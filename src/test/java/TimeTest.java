import common.Time;
import junit.framework.Assert;
import org.junit.Test;

public class TimeTest {

    @Test
    public void testParseStamps() throws Exception {
        Assert.assertEquals(Time.parseStamp("2014-02-17(09:16)").getTime(), 1392624960000L);
        Assert.assertEquals(Time.parseStamp("2214-02-17(09:16)").getTime(), 7703972160000L);
    }
}
