package simpleweather.ockmore.will.simpleweather;

import android.app.AlertDialog;
import android.support.v4.app.FragmentManager;
import android.content.DialogInterface;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class WeatherActivity extends AppCompatActivity {

    public static final String TAG = "Weather Activity";
    private Menu myMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Current Weather"));
        tabLayout.addTab(tabLayout.newTab().setText("Forecast"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_weather, menu);
        myMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.change_city) {
            showInputDialog();
        }
        else if (id == R.id.action_refresh) {
            refreshCity();
        }
        return false;
    }


    private void showInputDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change city");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Go", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                changeCity(input.getText().toString());
            }
        });
        builder.show();
    }

    private static String makeFragmentName(int viewPagerId, int index) {
        return "android:switcher:" + viewPagerId + ":" + index;
    }

    public void changeCity(String city){
        FragmentManager fm = getSupportFragmentManager();
        WeatherFragment wf = (WeatherFragment)fm
                .findFragmentByTag(makeFragmentName(R.id.pager, 0));
        ForecastFragment ff = (ForecastFragment)fm
                .findFragmentByTag(makeFragmentName(R.id.pager, 1));
        CityPreference cf = new CityPreference(this);
        cf.setCity(city);
        wf.changeCity(cf.getCity());
        ff.changeCity(cf.getCity());
    }

    public void refreshCity(){
        FragmentManager fm = getSupportFragmentManager();
        WeatherFragment wf = (WeatherFragment)fm
                .findFragmentByTag(makeFragmentName(R.id.pager, 0));
        ForecastFragment ff = (ForecastFragment)fm
                .findFragmentByTag(makeFragmentName(R.id.pager, 1));
        CityPreference cf = new CityPreference(this);
        wf.changeCity(cf.getCity());
        ff.changeCity(cf.getCity());
    }

    public void changeBackground(String timeOfDay) {
        String day = "DAY";
        String dusk = "DUSK";
        String night = "NIGHT";
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.container);
        if (timeOfDay.equals(day)) {
            layout.setBackgroundColor(getResources().getColor(R.color.background_day));
            //setTheme(R.style.CustomAppTheme_NoActionBarTitle_Day);
        } else if(timeOfDay.equals(dusk)){
            layout.setBackgroundColor(getResources().getColor(R.color.background_dusk));
            //setTheme(R.style.CustomAppTheme_NoActionBarTitle_Dusk);
        } else if(timeOfDay.equals(night)){
            layout.setBackgroundColor(getResources().getColor(R.color.background_night));
            //setTheme(R.style.CustomAppTheme_NoActionBarTitle_Night);
        }
    }
}

