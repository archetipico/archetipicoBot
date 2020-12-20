package functions;

import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.CurrentWeather;

import java.util.Objects;

public class Weather {

    private CurrentWeather cwd;
    private final OWM owm;
    private String city;
    private final String msg;
    private StringBuilder sb;

    public Weather(String msg) {
        this.owm = new OWM(new Secret().getOWM());
        this.msg = msg;
    }

    public String getWeather() {
        try {
            city = msg.substring(8)
                    .replaceAll("(?=[_*,\\[\\]()~`>#+\\-=|{}.!])", "\\\\");
            cwd = owm.currentWeatherByCityName(city);
            sb = new StringBuilder("_" + Objects
                    .requireNonNull(cwd.getCityName()) + "_");

            try {
                cityLine();
            } catch (Exception e) {
                sb.append("\n");
            }

            firstBlock();

            try {
                windLine();
            } catch (Exception e) {
                sb.append("*Wind*:   No wind");
            }

            try {
                rainSnowLine(cwd.component2() != null, Objects
                        .requireNonNull(cwd.component2())
                        .getPrecipVol3h(), "\n*Rain*:   ");
            } catch (Exception e) {
                sb.append("\n*Rain*:   Not raining right now");
            }

            try {
                rainSnowLine(cwd.component3() != null, Objects
                        .requireNonNull(cwd.component3())
                        .getSnowVol3h(), "\n*Snow*:   ");
            } catch (Exception e) {
                sb.append("\n*Snow*:   Not snowing right now");
            }

            try {
                cloudLine();
            } catch (Exception e) {
                sb.append("\n*Clouds*:   0");
            }

            return sb.toString()
                    .replaceAll("(?=[\\[\\]()~`>#+\\-=|{}.!])", "\\\\");
        } catch (Exception e) {
            return "I couldn't find '" + city + "', the place requested might not be " +
                    "in my database or it can be mispelled\n\n`weather city[, state/zipcode]`";
        }
    }

    private void cloudLine() {
        if (cwd.component9() != null) {
            int n = (int) (Objects
                    .requireNonNull(cwd.component9())
                    .getCloud() == null? 0 : Objects
                    .requireNonNull(cwd.component9())
                    .getCloud());

            sb.append("\n*Clouds*:   ").append(n).append("% coverage");
        }
    }

    private void rainSnowLine(boolean b, Double precipVol3h, String s) {
        if (b) {
            double vol = precipVol3h == null ? 0 : precipVol3h;
            sb
                    .append(s)
                    .append(vol)
                    .append(" mm in the last 3h");
        }
    }

    private void windLine() {
        if (cwd.component13() != null) {
            String speed = Objects
                    .requireNonNull(cwd.component13())
                    .getSpeed() == null? "0" : toKmh(Objects
                    .requireNonNull(cwd.component13())
                    .getSpeed());

            double degree = Objects
                    .requireNonNull(cwd.component13())
                    .getDegree() == null? 0 : Objects
                    .requireNonNull(cwd.component13())
                    .getDegree();

            double gust = Objects
                    .requireNonNull(cwd.component13())
                    .getGust() == null? 0 : Objects
                    .requireNonNull(cwd.component13())
                    .getGust();

            sb
                    .append("\n*Wind*:   ")
                    .append(speed)
                    .append(" km/h, ")
                    .append(degree)
                    .append("°, ")
                    .append(gust)
                    .append(" kt");

        }
    }

    private void firstBlock() {
        sb.append("\n*Last data taken*:   ")
                .append(Objects.requireNonNull(cwd.getDateTime()).toString())
                .append("\n*Max/min*:   ")
                .append(toCelsius(Objects.requireNonNull(cwd.getMainData()).getTempMax() + 1))
                .append("/")
                .append(toCelsius(cwd.getMainData().getTempMin() - 1))
                .append(" °C")
                .append("\n*Humidity*:   ")
                .append(cwd.getMainData().getHumidity())
                .append("%")
                .append("\n*Pressure*:   ")
                .append(Math.round(cwd.getMainData().getPressure()))
                .append(" atm");
    }

    private void cityLine() {
        if (cwd.component5() != null) {
            int start = Objects
                    .requireNonNull(cwd.component5()).toString().indexOf("moreInfo=") + 9;

            int end = Objects
                    .requireNonNull(cwd.component5()).toString().indexOf("iconCode=") - 2;

            sb
                    .append(", ")
                    .append(Objects
                            .requireNonNull(cwd.component5())
                            .toString(), start, end)
                    .append("\n");
        }
    }

    private String toCelsius(double temp) {
        return String.format("%.1f", temp - 273.15);
    }

    private String toKmh(double ms) {
        return String.format("%.1f", ms * 3.6);
    }
}
