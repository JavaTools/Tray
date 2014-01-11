package module.wallpaper.markers;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Formatter;

public class Markers {
    private HashMap<String, ColorSet> weeks = new HashMap<String, ColorSet>();
    private HashMap<String, ColorSet> days = new HashMap<String, ColorSet>();

    public Markers() {
    }

    public Markers(File markersFile) {
        try {
            InputStream is = new FileInputStream(markersFile);
            Reader reader = new InputStreamReader(is, "UTF-8");
            InputSource source = new InputSource(reader);
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(source);
            //Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(markersFile);

            NodeList list = doc.getElementsByTagName("entry");
            for (int index = 0; index < list.getLength(); index++) {
                Node node = list.item(index);
                Entry entry = new Entry();
                Node nn = node.getAttributes().getNamedItem("day");
                if (nn != null)
                    entry.setDay(nn.getNodeValue());
                nn = node.getAttributes().getNamedItem("week");
                if (nn != null)
                    entry.setWeek(nn.getNodeValue());
                nn = node.getAttributes().getNamedItem("back");
                if (nn != null)
                    entry.setBack(nn.getNodeValue());
                nn = node.getAttributes().getNamedItem("front");
                if (nn != null)
                    entry.setFront(nn.getNodeValue());

                ColorSet cs = new ColorSet();
                cs.setFront(new Color(Integer.parseInt(entry.getFront().substring(1), 16)));
                cs.setBack(new Color(Integer.parseInt(entry.getBack().substring(1), 16)));
                if (entry.getWeek() != null)
                    weeks.put(entry.getWeek(), cs);
                else
                    days.put(entry.getDay(), cs);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ColorSet getColor(Calendar calendar) {
        ColorSet result = null;

        int w = calendar.get(Calendar.WEEK_OF_YEAR);
        int d = calendar.get(Calendar.DATE);
        int m = calendar.get(Calendar.MONTH) + 1;
        String ww = new Formatter().format("%02d", w).toString();
        String dd = new Formatter().format("%02d/%02d", d, m).toString();
        result = weeks.get(ww);
        ColorSet cs = days.get(dd);
        if (cs != null)
            result = cs;

        return result;
    }
}
