package functions;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

public class UrbanDictionary {

    private final String s;
    private String ending;
    private final List<String> specials = Arrays.asList("&quot;", "&apos;", "&frasl;", "&lt;", "&gt;", "&sbquo;",
            "&bdquo;", "&lsaquo;", "&lsquo;", "&rsquo;", "&ldquo;", "&rdquo;", "&trade;", "&rsaquo;", "&nbsp;", "&cent;",
            "&pound;", "&copy;", "&shy;", "&reg;", "&amp;", "&deg;");
    private final List<String> specials_corrector = Arrays.asList("\"", "'", "/", "<", ">", "‚",
            "„", "‹", "‘", "’", "“", "”", "™", "›", " ", "¢",
            "£", "©", "", "®", "&", "°");

    public UrbanDictionary(String s) {
        String[] words = s.split("\\s+", 2);
        this.s = words[1];
    }

    @Override
    public String toString() {
        try {
            String ending = this.s.replaceAll("\\s+", "%20");
            String webPage = "https://www.urbandictionary.com/define.php?term=" + ending;
            URL url = new URL(webPage);
            URLConnection urlConnection = url.openConnection();

            InputStream is = urlConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);

            int numCharsRead;
            char[] charArray = new char[1024];
            StringBuilder sb = new StringBuilder();
            while ((numCharsRead = isr.read(charArray)) > 0) {
                sb.append(charArray, 0, numCharsRead);
            }

            String result = sb.toString();
            String s;
            for (int i = 0; i < specials.size(); i++) {
                s = specials.get(i);
                if (result.contains(s)) {
                    result = result.replaceAll(s, specials_corrector.get(i));
                }
            }

            int start = result.indexOf("<div class=\"meaning\">");
            int end = result.indexOf("<div class=\"example\">");

            String content = result.substring(start + 21, end);

            return getData(content);
        } catch (IOException e) {
            return "Nothing found for " + this.s;
        }
    }

    private String getData(String content) {
        StringBuilder sb = new StringBuilder();
        content = content.replaceAll("<br/>", "\n");

        int is_min = 0;
        char c;
        for (int i = 0; i < content.length(); i++) {
            c = content.charAt(i);
            if (c == '<') {
                is_min = 1;
            } else if (c == '>') {
                is_min = 0;
            }

            if (is_min == 0) {
                sb.append(c);
            }
        }

        return sb.toString().replaceAll(">", "");
    }
}