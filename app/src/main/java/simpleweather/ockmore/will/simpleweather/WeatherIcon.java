package simpleweather.ockmore.will.simpleweather;

import android.content.Context;

import java.util.Date;

/**
 *Class for defining which weather icons to use with overloaded functions for the two fragments;
 * the weather fragment passes the sunrise and sunset times for different icons for night and day
 * whilst the forecast fragment only uses daytime icons.
 */
public class WeatherIcon {

    public static String getWeatherIcon(int id, long sunrise, long sunset,
                                        long currentTime, Context context){
        int shortId = id / 100;
        //long currentTime = new Date().getTime();
        String icon = "";

        /*TODO fix drizzle*/

        if((shortId==8 && (id%800 < 4))|| (shortId==5 && (id%500>19)) || shortId==3){
            if (currentTime>=sunrise && currentTime<sunset) {
                if(shortId==3){
                    icon = context.getString(R.string.weather_day_showers);
                } else if (shortId==8){
                    switch (id) {
                        case 800 : icon = context.getString(R.string.weather_sunny);
                            break;
                        case 801 : icon = context.getString(R.string.weather_day_few_clouds);
                            break;
                        case 802: icon = context.getString(R.string.weather_day_overcast);
                            break;
                        case 803: icon = context.getString(R.string.weather_day_overcast);
                            break;
                    }
                } else {
                    switch(id) {
                        case 520 : icon = context.getString(R.string.weather_day_showers);
                            break;
                        case 521 : icon = context.getString(R.string.weather_day_showers);
                            break;
                        default : icon = context.getString(R.string.weather_showers);
                            break;
                    }
                }

            } else {
                if (shortId==3){
                    icon = context.getString(R.string.weather_night_showers);
                } else if (shortId==8){
                    switch (id) {
                        case 800: icon = context.getString(R.string.weather_clear_night);
                            break;
                        case 801: icon = context.getString(R.string.weather_night_few_clouds);
                            break;
                        case 802: icon = context.getString(R.string.weather_night_overcast);
                            break;
                        case 803: icon = context.getString(R.string.weather_night_overcast);
                            break;
                    }
                } else {
                    switch (id){
                        case 520 : icon = context.getString(R.string.weather_night_showers);
                            break;
                        case 521 : icon = context.getString(R.string.weather_night_showers);
                            break;
                        default : icon = context.getString(R.string.weather_showers);
                            break;
                    }
                }
            }
        } else {
            switch (shortId) {
                case 2 : icon = context.getString(R.string.weather_thunder);
                    break;
                case 7 : icon = context.getString(R.string.weather_foggy);
                    break;
                case 8 : icon = context.getString(R.string.weather_cloudy);
                    break;
                case 6 : icon = context.getString(R.string.weather_snowy);
                    break;
                case 5 : icon = context.getString(R.string.weather_rainy);
                    break;
            }
        } return icon;
    }

    public static String getWeatherIcon(int id, Context context){
        int shortId = id / 100;
        String icon = "";

        /*TODO fix drizzle*/

        if((shortId==8 && (id%800 < 4)) || (shortId==5 && (id%500>19)) || shortId==3 || id == 500){
            if(shortId==3){
                icon = context.getString(R.string.weather_day_showers);
            } else if (shortId==8){
                switch (id) {
                    case 800 : icon = context.getString(R.string.weather_sunny);
                        break;
                    case 801 : icon = context.getString(R.string.weather_day_few_clouds);
                        break;
                    case 802: icon = context.getString(R.string.weather_day_overcast);
                        break;
                    case 803: icon = context.getString(R.string.weather_day_overcast);
                        break;
                }
            } else {
                switch (id) {
                    case 520: icon = context.getString(R.string.weather_day_showers);
                        break;
                    case 521: icon = context.getString(R.string.weather_day_showers);
                        break;
                    default: icon = context.getString(R.string.weather_showers);
                        break;
                }
            }
        } else {
            switch (shortId) {
                case 2 : icon = context.getString(R.string.weather_thunder);
                    break;
                case 7 : icon = context.getString(R.string.weather_foggy);
                    break;
                case 8 : icon = context.getString(R.string.weather_cloudy);
                    break;
                case 6 : icon = context.getString(R.string.weather_snowy);
                    break;
                case 5 : icon = context.getString(R.string.weather_rainy);
                    break;
            }
        } return icon;
    }
}
