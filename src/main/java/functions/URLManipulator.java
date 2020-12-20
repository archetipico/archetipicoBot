package functions;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class URLManipulator {

    private final URL url;

    public URLManipulator(URL url) {
        this.url = url;
    }

    public URL getURL(String token) throws IOException {
        String cleared = this
                .urlToString()
                .replace("\"}}", "");

        //making the image request
        return new URL("https://api.telegram.org/file/bot"
                + new Secret().getToken()
                + "/"
                + cleared
                        .substring(cleared
                                .indexOf(token))
        );
    }

    public void urlGET() throws IOException {
        //making the request for the path
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
    }

    public String urlToString() throws IOException {
        URLConnection urlConnection = url.openConnection();
        InputStream is = urlConnection.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);

        int numCharsRead;
        char[] charArray = new char[1024];
        StringBuilder sb = new StringBuilder();
        while ((numCharsRead = isr.read(charArray)) > 0) {
            sb.append(charArray, 0, numCharsRead);
        }

        return sb.toString();
    }
}
