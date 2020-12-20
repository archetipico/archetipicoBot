package functions;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class Horoscope {

    private final String s;
    private final String day;

    public Horoscope(String s) {
        String[] words = s.split("\\s+");
        this.s = words[1].replaceAll("(?=[_*,\\[\\]()~`>#+\\-=|{}.!])", "\\\\");

        if (words.length == 3){
            this.day = "tomorrow/";
        } else {
            this.day = "";
        }
    }

    @Override
    public String toString() {
        try {
            String webPage = "https://www.astrology.com/horoscope/daily/" + this.day + this.s + ".html";

            Document doc = Jsoup.connect(webPage).get();
            Element elem = doc.select("p").first();

            String dateText = elem.getElementsByClass("date").text();
            String date = "_" + dateText.substring(0, dateText.length() - 1)  + "_\n\n";
            String text = elem.text().replaceAll("^([^:]+):\\s", "");

            return date + text.replaceAll("(?=[\\[\\]()~`>#+\\-=|{}.!])", "\\\\");
        } catch (IOException e) {
            return "I don't think " + this.s + " is a sign";
        }
    }
}
