package simpleweather.ockmore.will.simpleweather;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Weather app fragment, handles the display of all elements within the FrameLayout of the main
 * activity.
 */
public class WeatherFragment extends Fragment {

    public static final String TAG = "SimpleWeather Fragment";

    Typeface weatherFont;

    TextView cityField;
    TextView updatedField;
    TextView detailsField;
    TextView currentTemperatureField;
    TextView weatherIcon;
    TextView hourly0;
    TextView hourly1;
    TextView hourly2;
    TextView hourly3;
    TextView hourly4;
    TextView icon0;
    TextView icon1;
    TextView icon2;
    TextView icon3;
    TextView icon4;

    Handler handler;

    public WeatherFragment(){
        handler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weather, container, false);
        cityField = (TextView)rootView.findViewById(R.id.city_field);
        updatedField = (TextView)rootView.findViewById(R.id.updated_field);
        detailsField = (TextView)rootView.findViewById(R.id.details_field);
        currentTemperatureField = (TextView)rootView.findViewById(R.id.current_temperature_field);
        weatherIcon = (TextView)rootView.findViewById(R.id.weather_icon);

        hourly0 = (TextView)rootView.findViewById(R.id.hourly_0);
        hourly1 = (TextView)rootView.findViewById(R.id.hourly_1);
        hourly2 = (TextView)rootView.findViewById(R.id.hourly_2);
        hourly3 = (TextView)rootView.findViewById(R.id.hourly_3);
        hourly4 = (TextView)rootView.findViewById(R.id.hourly_4);

        icon0 = (TextView)rootView.findViewById(R.id.hourly_icon_0);
        icon1 = (TextView)rootView.findViewById(R.id.hourly_icon_1);
        icon2 = (TextView)rootView.findViewById(R.id.hourly_icon_2);
        icon3 = (TextView)rootView.findViewById(R.id.hourly_icon_3);
        icon4 = (TextView)rootView.findViewById(R.id.hourly_icon_4);

        icon0.setTypeface(weatherFont);
        icon1.setTypeface(weatherFont);
        icon2.setTypeface(weatherFont);
        icon3.setTypeface(weatherFont);
        icon4.setTypeface(weatherFont);

        weatherIcon.setTypeface(weatherFont);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherFont = Typeface.createFromAsset(getActivity().getAssets(),
                                               "fonts/weathericons-regular-webfont.ttf");
        updateWeatherData(new CityPreference(getActivity()).getCity());


    }

    private void updateWeatherData(final String city){
        new Thread(){
            public void run(){
                final JSONObject json = RemoteFetch.getJSON(
                        getActivity(), city, "weather", "");
                final JSONObject hourly = RemoteFetch.getJSON(
                        getActivity(), city, "forecast", "&cnt=5");

                final RelativeLayout layout = (RelativeLayout)getActivity()
                        .findViewById(R.id.fragment_weather);
                layout.animate().setDuration(700);

                if (json == null || hourly == null){
                    handler.post(new Runnable(){
                        public void run(){
                            Toast.makeText(getActivity(),
                                    getActivity().getString(R.string.place_not_found),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            renderWeather(json, hourly);
                            layout.animate().alpha(1);
                        }
                    });
                }
            }
        }.start();
    }

    private void renderWeather(JSONObject json, JSONObject hourly){
        try {
            cityField.setText(json.getString("name").toUpperCase(Locale.UK) +
                    "," +
                    json.getJSONObject("sys").getString("country"));

            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");
            detailsField.setText(
                    details.getString("description").toUpperCase(Locale.UK) +
                            "\n" + "Humidity: " + main.getString("humidity") + "%" /*+
                            "\n" + "Pressure: " + main.getString("pressure") + "hPa"*/);

            currentTemperatureField.setText(
                    String.format("%.1f", main.getDouble("temp")) + " ℃");

            DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
            String updatedOn = df.format(new Date(json.getLong("dt") * 1000));
            updatedField.setText("Last update: " + updatedOn);

            /* TODO local time field */
            long sunrise = json.getJSONObject("sys").getLong("sunrise") * 1000;
            long sunset =  json.getJSONObject("sys").getLong("sunset") * 1000;

            weatherIcon.setText(WeatherIcon.getWeatherIcon(details.getInt("id"),
                    sunrise, sunset, json.getLong("dt")*1000, getActivity()));

            String t = new TimeOfDay().getTimeOfDay(sunrise, sunset);

            ((WeatherActivity)getActivity()).changeBackground(t);

            JSONArray list = hourly.getJSONArray("list");
            DateFormat hourFormat = new SimpleDateFormat("h a");
            String[] time = new String[5];
            int[] weatherCodes = new int[5];
            long[] dt = new long[5];

            /*Pattern p = Pattern.compile("\\s(\\d{2})");*/

            for (int i=0; i<5; i++){ //This cycles through the json data and gets dt and weather
                JSONObject jo = list.getJSONObject(i);

                dt[i] = jo.getLong("dt")*1000;
                time[i] = hourFormat.format(new Date(dt[i]));

                /*Matcher m = p.matcher(jo.getString("dt_txt"));
                try {
                    String d = "";
                    while (m.find()){
                        d = m.group();
                    }
                    dt_txt[i] = d;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }*/
                JSONObject weather = jo.getJSONArray("weather").getJSONObject(0);
                weatherCodes[i] = weather.getInt("id");
            }

            hourly0.setText(time[0]);
            hourly1.setText(time[1]);
            hourly2.setText(time[2]);
            hourly3.setText(time[3]);
            hourly4.setText(time[4]);

            icon0.setText(WeatherIcon.getWeatherIcon(weatherCodes[0],
                                                     sunrise, sunset, dt[0], getActivity()));
            icon1.setText(WeatherIcon.getWeatherIcon(weatherCodes[1],
                                                     sunrise, sunset, dt[1], getActivity()));
            icon2.setText(WeatherIcon.getWeatherIcon(weatherCodes[2],
                                                     sunrise, sunset, dt[2], getActivity()));
            icon3.setText(WeatherIcon.getWeatherIcon(weatherCodes[3],
                                                     sunrise, sunset, dt[3], getActivity()));
            icon4.setText(WeatherIcon.getWeatherIcon(weatherCodes[4],
                                                     sunrise, sunset, dt[4], getActivity()));
        }catch (Exception e) {
            Log.e("SimpleWeather", "One or more fields not found in JSON data");
        }
    }

    public void changeCity(final String city){
        RelativeLayout layout = (RelativeLayout)getActivity()
                .findViewById(R.id.fragment_weather);
        layout.animate().setDuration(700);
        layout.animate().alpha(0);
        layout.animate().withEndAction(new Runnable() {
            @Override
            public void run() {
                updateWeatherData(city);
            }
        });
    }


}
