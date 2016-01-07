package simpleweather.ockmore.will.simpleweather;


import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Displays the weather data for the next 5 days on a separate tab.
 */
public class ForecastFragment extends Fragment {

    Typeface weatherFont;

    TextView dayField0;
    TextView dayField1;
    TextView dayField2;
    TextView dayField3;
    TextView dayField4;

    TextView forecastIcon0;
    TextView forecastIcon1;
    TextView forecastIcon2;
    TextView forecastIcon3;
    TextView forecastIcon4;

    TextView tempField0;
    TextView tempField1;
    TextView tempField2;
    TextView tempField3;
    TextView tempField4;

    Handler handler;

    public static final String TAG = "Forecast Fragment";

    public ForecastFragment(){
        handler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_forecast, container, false);

        dayField0 = (TextView)rootView.findViewById(R.id.day_0);
        dayField1 = (TextView)rootView.findViewById(R.id.day_1);
        dayField2 = (TextView)rootView.findViewById(R.id.day_2);
        dayField3 = (TextView)rootView.findViewById(R.id.day_3);
        dayField4 = (TextView)rootView.findViewById(R.id.day_4);

        forecastIcon0 = (TextView)rootView.findViewById(R.id.forecast_icon_0);
        forecastIcon1 = (TextView)rootView.findViewById(R.id.forecast_icon_1);
        forecastIcon2 = (TextView)rootView.findViewById(R.id.forecast_icon_2);
        forecastIcon3 = (TextView)rootView.findViewById(R.id.forecast_icon_3);
        forecastIcon4 = (TextView)rootView.findViewById(R.id.forecast_icon_4);

        tempField0 = (TextView)rootView.findViewById(R.id.day_0_temperature);
        tempField1 = (TextView)rootView.findViewById(R.id.day_1_temperature);
        tempField2 = (TextView)rootView.findViewById(R.id.day_2_temperature);
        tempField3 = (TextView)rootView.findViewById(R.id.day_3_temperature);
        tempField4 = (TextView)rootView.findViewById(R.id.day_4_temperature);

        forecastIcon0.setTypeface(weatherFont);
        forecastIcon1.setTypeface(weatherFont);
        forecastIcon2.setTypeface(weatherFont);
        forecastIcon3.setTypeface(weatherFont);
        forecastIcon4.setTypeface(weatherFont);

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/weathericons-regular-webfont.ttf");
        updateForecastData(new CityPreference(getActivity()).getCity());
    }

    private void updateForecastData(final String city){
        new Thread(){
            public void run(){
                final JSONObject json = RemoteFetch.getJSON(
                        getActivity(), city, "forecast/daily", "&cnt=6");

                final LinearLayout layout = (LinearLayout)getActivity()
                        .findViewById(R.id.fragment_forecast);
                layout.animate().setDuration(700);

                if (json == null){
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
                            renderForecast(json);
                            layout.animate().alpha(1);
                        }
                    });
                }
            }
        }.start();
    }

    private void renderForecast(JSONObject json) { //render the forecast data
        try{
            JSONArray list = json.getJSONArray("list");
            DateFormat df = new SimpleDateFormat("EEE");
            String[] days = new String[5];
            String[] temps = new String[5];
            int[] ids = new int[5];

            for (int i=0; i<5; i++){
                int j = i+1;
                JSONObject jo = list.getJSONObject(j);

                days[i] = df.format(new Date(jo.getLong("dt")*1000));

                JSONObject t = jo.getJSONObject("temp");
                temps[i] = String.format("%d", (int)t.getDouble("day"));

                JSONObject weather = jo.getJSONArray("weather").getJSONObject(0);
                ids[i] = weather.getInt("id");
            }

            dayField0.setText(days[0]);
            dayField1.setText(days[1]);
            dayField2.setText(days[2]);
            dayField3.setText(days[3]);
            dayField4.setText(days[4]);

            tempField0.setText(temps[0] + " ℃");
            tempField1.setText(temps[1] + " ℃");
            tempField2.setText(temps[2] + " ℃");
            tempField3.setText(temps[3] + " ℃");
            tempField4.setText(temps[4] + " ℃");

            forecastIcon0.setText(WeatherIcon.getWeatherIcon(ids[0], getActivity()));
            forecastIcon1.setText(WeatherIcon.getWeatherIcon(ids[1], getActivity()));
            forecastIcon2.setText(WeatherIcon.getWeatherIcon(ids[2], getActivity()));
            forecastIcon3.setText(WeatherIcon.getWeatherIcon(ids[3], getActivity()));
            forecastIcon4.setText(WeatherIcon.getWeatherIcon(ids[4], getActivity()));
        }catch (Exception e) {
            Log.e(TAG, "One or more fields missing from JSON data");
        }
    }

    public void changeCity(final String city){
        LinearLayout layout = (LinearLayout)getActivity()
                .findViewById(R.id.fragment_forecast);
        layout.animate().setDuration(700);
        layout.animate().alpha(0);
        layout.animate().withEndAction(new Runnable() {
            @Override
            public void run() {
                updateForecastData(city);
            }
        });
    }


}
