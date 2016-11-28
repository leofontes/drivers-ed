package me.leofontes.driversed;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class Settings extends AppCompatActivity {

    public static final String PREFS_NAME = "SettingsSharedPrefs";
    public static final String TOTAL_KEY = "total";
    public static final String DAY_KEY = "day";
    public static final String NIGHT_KEY = "night";
    public static final String RESIDENTIAL_KEY = "residential";
    public static final String COMMERCIAL_KEY = "commercial";
    public static final String HIGHWAY_KEY = "highway";
    public static final String CLEAR_KEY = "clear";
    public static final String RAINY_KEY = "rainy";
    public static final String SNOWY_KEY = "snowy";

    EditText totalHours;
    EditText dayHours;
    EditText nightHours;
    EditText resHours;
    EditText comHours;
    EditText hwHours;
    EditText clearHours;
    EditText rainyHours;
    EditText snowyHours;

    private SharedPreferences myPrefs;
    private SharedPreferences.Editor pEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.title_activity_settings);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        totalHours = (EditText) findViewById(R.id.totalHoursInput);
        dayHours = (EditText) findViewById(R.id.dayHoursInput);
        nightHours = (EditText) findViewById(R.id.nightHoursInput);
        resHours = (EditText) findViewById(R.id.resHoursInput);
        comHours = (EditText) findViewById(R.id.comHoursInput);
        hwHours = (EditText) findViewById(R.id.hwHoursInput);
        clearHours = (EditText) findViewById(R.id.clearHoursInput);
        rainyHours = (EditText) findViewById(R.id.rainyHoursInput);
        snowyHours = (EditText) findViewById(R.id.snowyHoursInput);


        //Manage Shared Preferences
        Context context = getApplicationContext();  // app level storage
        myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        pEditor = myPrefs.edit();

        if(myPrefs.getInt(TOTAL_KEY, -1) == -1) {
            pEditor.putInt(TOTAL_KEY, Integer.parseInt(totalHours.getText().toString()));
            pEditor.putInt(DAY_KEY, Integer.parseInt(dayHours.getText().toString()));
            pEditor.putInt(NIGHT_KEY, Integer.parseInt(nightHours.getText().toString()));
            pEditor.putInt(RESIDENTIAL_KEY, Integer.parseInt(resHours.getText().toString()));
            pEditor.putInt(COMMERCIAL_KEY, Integer.parseInt(comHours.getText().toString()));
            pEditor.putInt(HIGHWAY_KEY, Integer.parseInt(hwHours.getText().toString()));
            pEditor.putInt(CLEAR_KEY, Integer.parseInt(clearHours.getText().toString()));
            pEditor.putInt(RAINY_KEY, Integer.parseInt(rainyHours.getText().toString()));
            pEditor.putInt(SNOWY_KEY, Integer.parseInt(snowyHours.getText().toString()));
            pEditor.commit();
        } else {
            totalHours.setText(String.valueOf(myPrefs.getInt(TOTAL_KEY, 65)));
            dayHours.setText(String.valueOf(myPrefs.getInt(DAY_KEY, 55)));
            nightHours.setText(String.valueOf(myPrefs.getInt(NIGHT_KEY, 10)));
            resHours.setText(String.valueOf(myPrefs.getInt(RESIDENTIAL_KEY, 4)));
            comHours.setText(String.valueOf(myPrefs.getInt(COMMERCIAL_KEY, 2)));
            hwHours.setText(String.valueOf(myPrefs.getInt(HIGHWAY_KEY, 4)));
            clearHours.setText(String.valueOf(myPrefs.getInt(CLEAR_KEY, 55)));
            rainyHours.setText(String.valueOf(myPrefs.getInt(RAINY_KEY, 5)));
            snowyHours.setText(String.valueOf(myPrefs.getInt(SNOWY_KEY, 5)));
        }
    }

    @Override
    public void onPause() {

        pEditor.putInt(TOTAL_KEY, Integer.parseInt(totalHours.getText().toString()));
        pEditor.putInt(DAY_KEY, Integer.parseInt(dayHours.getText().toString()));
        pEditor.putInt(NIGHT_KEY, Integer.parseInt(nightHours.getText().toString()));
        pEditor.putInt(RESIDENTIAL_KEY, Integer.parseInt(resHours.getText().toString()));
        pEditor.putInt(COMMERCIAL_KEY, Integer.parseInt(comHours.getText().toString()));
        pEditor.putInt(HIGHWAY_KEY, Integer.parseInt(hwHours.getText().toString()));
        pEditor.putInt(CLEAR_KEY, Integer.parseInt(clearHours.getText().toString()));
        pEditor.putInt(RAINY_KEY, Integer.parseInt(rainyHours.getText().toString()));
        pEditor.putInt(SNOWY_KEY, Integer.parseInt(snowyHours.getText().toString()));

        pEditor.commit();
        super.onPause();
    }

}
