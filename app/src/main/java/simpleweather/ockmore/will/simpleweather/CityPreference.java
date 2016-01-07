package simpleweather.ockmore.will.simpleweather;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for interfacing with the shared preferences. Formats string input for city variable.
 */
public class CityPreference {

    SharedPreferences prefs;

    public CityPreference(Activity activity){
        prefs = PreferenceManager.getDefaultSharedPreferences(activity);
    }

    //If the user has not chosen a city yet, return
    //London as the default city

    String getCity(){
        String city = prefs.getString("city", "London");
        return city;
    }

    void setCity(String city){
        String noSpaceCity = city.replaceAll("\\s", "");  //removes whitespace in input
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("city", noSpaceCity);
        editor.commit();
    }
}
