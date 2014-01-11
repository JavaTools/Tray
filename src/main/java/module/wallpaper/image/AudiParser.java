package module.wallpaper.image;

import module.wallpaper.utilities.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AudiParser {
    public String getCurrentUrl() {
        String result = Config.getInstance().URL_ART_DEFAULT;
        String body = readStreamToString(Config.getInstance().URL_AUDI_IMAGES);

        Pattern p = Pattern.compile(Config.getInstance().URL_AUDI_REGEX, Pattern.DOTALL | Pattern.MULTILINE);
        Matcher m = p.matcher(body);
        if (m.find()) {
            result = m.group(1);
        }

        return result;
    }

    private String readStreamToString(String surl) {
        BufferedReader reader;
        StringBuilder sb = new StringBuilder();
        InputStream is = null;

        String line;
        try {
            URL url = new URL(surl);
            is = url.openStream();
            reader = new BufferedReader(new InputStreamReader(is));
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }
}
