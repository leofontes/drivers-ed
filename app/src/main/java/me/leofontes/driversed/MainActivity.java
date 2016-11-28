package me.leofontes.driversed;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NewLesson.OnFragmentInteractionListener, Statistics.OnFragmentInteractionListener, DrivingLogs.OnFragmentInteractionListener {

    FragmentManager fragmentManager;
    Fragment fragment;
    DriveDBadapter db;
    TextView drawerCurrentLabel;
    TextView drawerMaxLabel;
    ProgressBar drawerProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.getHeaderView(0);

        drawerCurrentLabel = (TextView) headerLayout.findViewById(R.id.drawerCurrTotalLabel);
        drawerMaxLabel = (TextView) headerLayout.findViewById(R.id.drawerMaxTotalLabel);
        drawerProgress = (ProgressBar) headerLayout.findViewById(R.id.drawerProgress);

        db = db.getInstance(getApplicationContext());
        db.open();

        configDrawerHours();

        fragment = new NewLesson();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.relativeLayout, fragment).commit();
    }

    public void configDrawerHours() {
        int max = 0;
        int current = calcTotalHours();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();

        if(prefs.getInt(Settings.TOTAL_KEY, -1) == -1) {
            editor.putInt(Settings.TOTAL_KEY, 65);
            max = 65;
        } else {
            max = prefs.getInt(Settings.TOTAL_KEY, 65);
        }

        drawerCurrentLabel.setText("" + current);
        drawerMaxLabel.setText("" + max);
        drawerProgress.setMax(max);
        drawerProgress.setProgress(current);
    }

    public int calcTotalHours() {
        Cursor cursor = db.getAllItems();
        double total = 0.0;
        if(cursor.moveToFirst()) {
            do {
                DriveInfo result = new DriveInfo(cursor.getString(1), cursor.getString(6), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5));
                total += Double.valueOf(result.getHours());
            } while (cursor.moveToNext());
        }

        int roundTotal = (int) Math.round(total);
        return roundTotal;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
        } else if(id == R.id.addLesson) {
            fragment = new NewLesson();
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.relativeLayout, fragment).commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        configDrawerHours();

        int id = item.getItemId();

        if (id == R.id.nav_new_lesson) {
            fragment = new NewLesson();
        } else if (id == R.id.nav_statistics) {
            fragment = new Statistics();
        } else if (id == R.id.nav_driving_logs) {
            fragment = new DrivingLogs();
        }

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.relativeLayout, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
