package functions;

public class Maps {

    private final String s;

    public Maps(String s) {
        String[] words = s.split("\\s+", 2);
        this.s = words[1].replaceAll("\\s+", "+");
    }

    public String getDirections() {
        String[] stops = this.s.split("-");
        StringBuilder result = new StringBuilder("https://www.google.com/maps/dir/");

        for (String s : stops) {
            result.append(normalizeLink(s)).append("/");
        }

        return result.toString();
    }

    public String normalizeLink(String s) {
        StringBuilder sb = new StringBuilder(s);
        for (int i = 0; i < sb.length(); i++) {
            if (sb.charAt(i) == '+') {
                sb.replace(i, i + 1, "");
            } else {
                break;
            }
        }

        for (int i = sb.length() - 1; i >= 0; i--) {
            if (sb.charAt(i) == '+') {
                sb.replace(i, i + 1, "");
            } else {
                break;
            }
        }

        return sb.toString();
    }
}