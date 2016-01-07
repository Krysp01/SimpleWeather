package simpleweather.ockmore.will.simpleweather;

import java.util.Date;

/**
 * Calculates the time of day (sunset, sunrise, day or night) with 2 utc time inputs
 * for sunrise and sunset.
 */
public class TimeOfDay {

    public static final String DAY = "DAY";
    public static final String NIGHT = "NIGHT";
    public static final String DUSK = "DUSK";
    String timeOfDay;

    public String getTimeOfDay(long sunrise, long sunset) {
        long currentTime = new Date().getTime();

        if((currentTime>=sunrise && (currentTime%sunrise)<3600000)
                ||(currentTime<sunset && (sunset%currentTime)<3600000)) {
            timeOfDay = DUSK;
        } else if (currentTime>=sunrise && currentTime<sunset){
            timeOfDay = DAY;
        } else {
            timeOfDay = NIGHT;
        } return timeOfDay;
    }
}
