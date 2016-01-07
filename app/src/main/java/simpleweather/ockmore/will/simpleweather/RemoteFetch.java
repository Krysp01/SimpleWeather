package simpleweather.ockmore.will.simpleweather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class RemoteFetch {


    public static JSONObject getJSON(Context context, String city, String dataType, String days){
        try {
            URL url = new URL("http://api.openweathermap.org/data/2.5/"+dataType+"?q="+city+"&units=metric"+days);
            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();

            connection.setRequestProperty("x-api-key",
                    context.getString(R.string.open_weather_maps_app_id));

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuilder json = new StringBuilder(1024);
            String tmp=" ";
            while ((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            //This value will be 404 if the request was not
            //successful
            if (data.getInt("cod")!=200){
                return null;
            }
            return data;

        }catch(Exception e) {
            Log.e("MyActivity", "Exception", e.fillInStackTrace());
            return null;
        }
    }
}