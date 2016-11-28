package me.leofontes.driversed;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Statistics.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Statistics#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Statistics extends Fragment {
    DriveDBadapter db;
    Map<String, Double> hours;
    int[] requiredHours;
    Cursor cursor;

    TextView dayHoursLabel;
    TextView nightHoursLabel;
    TextView residentialHoursLabel;
    TextView commercialHoursLabel;
    TextView highwayHoursLabel;
    TextView clearHoursLabel;
    TextView rainingHoursLabel;
    TextView snowIceHoursLabel;

    ProgressBar dayProgress;
    ProgressBar nightProgress;
    ProgressBar residentialProgress;
    ProgressBar commercialProgress;
    ProgressBar highwayProgress;
    ProgressBar clearProgress;
    ProgressBar rainingProgress;
    ProgressBar snowIceProgress;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Statistics() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Statistics.
     */
    // TODO: Rename and change types and number of parameters
    public static Statistics newInstance(String param1, String param2) {
        Statistics fragment = new Statistics();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        hours = new HashMap<String, Double>();
        hours.put("total", 0.0);
        hours.put("day", 0.0);
        hours.put("night", 0.0);
        hours.put("residential", 0.0);
        hours.put("commercial", 0.0);
        hours.put("highway", 0.0);
        hours.put("clear", 0.0);
        hours.put("raining", 0.0);
        hours.put("snowIce", 0.0);

        dayHoursLabel = (TextView) view.findViewById(R.id.hoursByDayTextView);
        nightHoursLabel = (TextView) view.findViewById(R.id.hoursByNightTextView);
        residentialHoursLabel = (TextView) view.findViewById(R.id.residentialHoursTextView);
        commercialHoursLabel = (TextView) view.findViewById(R.id.commercialHoursTextView);
        highwayHoursLabel = (TextView) view.findViewById(R.id.highwayHoursTextView);
        clearHoursLabel = (TextView) view.findViewById(R.id.clearWeatherTextView);
        rainingHoursLabel = (TextView) view.findViewById(R.id.rainyWeatherTextView);
        snowIceHoursLabel = (TextView) view.findViewById(R.id.snowTextView);

        dayProgress = (ProgressBar) view.findViewById(R.id.hoursByDayProgressBar);
        nightProgress = (ProgressBar) view.findViewById(R.id.hoursByNightProgressBar);
        residentialProgress = (ProgressBar) view.findViewById(R.id.residentialProgressBar);
        commercialProgress = (ProgressBar) view.findViewById(R.id.commercialProgressBar);
        highwayProgress = (ProgressBar) view.findViewById(R.id.highwayProgressBar);
        clearProgress = (ProgressBar) view.findViewById(R.id.clearProgressBar);
        rainingProgress = (ProgressBar) view.findViewById(R.id.rainyProgressBar);
        snowIceProgress = (ProgressBar) view.findViewById(R.id.snowyProgressBar);

        Context context = view.getContext();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();

        requiredHours = new int[9];
        if(settings.getInt(Settings.TOTAL_KEY, -1) == -1) {
            editor.putInt(Settings.TOTAL_KEY, 65);
            editor.putInt(Settings.DAY_KEY, 55);
            editor.putInt(Settings.NIGHT_KEY, 10);
            editor.putInt(Settings.RESIDENTIAL_KEY, 4);
            editor.putInt(Settings.COMMERCIAL_KEY, 2);
            editor.putInt(Settings.HIGHWAY_KEY, 4);
            editor.putInt(Settings.CLEAR_KEY, 55);
            editor.putInt(Settings.RAINY_KEY, 5);
            editor.putInt(Settings.SNOWY_KEY, 5);
            editor.commit();
            requiredHours[0] = 65;
            requiredHours[1] = 55;
            requiredHours[2] = 10;
            requiredHours[3] = 4;
            requiredHours[4] = 2;
            requiredHours[5] = 2;
            requiredHours[6] = 55;
            requiredHours[7] = 5;
            requiredHours[8] = 5;
        } else {
            requiredHours[0] = settings.getInt(Settings.TOTAL_KEY, 65);
            requiredHours[1] = settings.getInt(Settings.DAY_KEY, 55);
            requiredHours[2] = settings.getInt(Settings.NIGHT_KEY, 10);
            requiredHours[3] = settings.getInt(Settings.RESIDENTIAL_KEY, 4);
            requiredHours[4] = settings.getInt(Settings.COMMERCIAL_KEY, 2);
            requiredHours[5] = settings.getInt(Settings.HIGHWAY_KEY, 4);
            requiredHours[6] = settings.getInt(Settings.CLEAR_KEY, 55);
            requiredHours[7] = settings.getInt(Settings.RAINY_KEY, 5);
            requiredHours[8] = settings.getInt(Settings.SNOWY_KEY, 5);
        }

        db = db.getInstance(getActivity().getApplicationContext());
        db.open();

        calculateHours();
        Log.i("Hours", hours.toString());

        setUpTitles();
        setUpProgressBars();

        return view;
    }

    public void setUpTitles() {
        DecimalFormat df = new DecimalFormat("0.0");

        dayHoursLabel.setText("Total Hours By Day" + " (" + df.format(hours.get("day")) + "/" + requiredHours[1] + ")");
        nightHoursLabel.setText("Total Hours By Night" + " (" + df.format(hours.get("night")) + "/" + requiredHours[2] + ")");
        residentialHoursLabel.setText("Total Hours Residential" + " (" + df.format(hours.get("residential")) + "/" + requiredHours[3] + ")");
        commercialHoursLabel.setText("Total Hours Comercial" + " (" + df.format(hours.get("commercial")) + "/" + requiredHours[4] + ")");
        highwayHoursLabel.setText("Total Hours Highway" + " (" + df.format(hours.get("highway")) + "/" + requiredHours[5] + ")");
        clearHoursLabel.setText("Total Hours in Clear Weather" + " (" + df.format(hours.get("clear")) + "/" + requiredHours[6] + ")");
        rainingHoursLabel.setText("Total Hours in Rainy Weather" + " (" + df.format(hours.get("raining")) + "/" + requiredHours[7] + ")");
        snowIceHoursLabel.setText("Total Hours in Snowy/Icy Weather" + " (" + df.format(hours.get("snowIce")) + "/" + requiredHours[8] + ")");
    }

    public void setUpProgressBars() {
        dayProgress.setProgress((int) Math.round(hours.get("day")));
        nightProgress.setProgress((int) Math.round(hours.get("night")));
        residentialProgress.setProgress((int) Math.round(hours.get("residential")));
        commercialProgress.setProgress((int) Math.round(hours.get("commercial")));
        highwayProgress.setProgress((int) Math.round(hours.get("highway")));
        clearProgress.setProgress((int) Math.round(hours.get("clear")));
        rainingProgress.setProgress((int) Math.round(hours.get("raining")));
        snowIceProgress.setProgress((int) Math.round(hours.get("snowIce")));

        dayProgress.setMax(requiredHours[1]);
        nightProgress.setMax(requiredHours[2]);
        residentialProgress.setMax(requiredHours[3]);
        commercialProgress.setMax(requiredHours[4]);
        highwayProgress.setMax(requiredHours[5]);
        clearProgress.setMax(requiredHours[6]);
        rainingProgress.setMax(requiredHours[7]);
        snowIceProgress.setMax(requiredHours[8]);
    }

    public void calculateHours() {
        cursor = db.getAllItems();

        if(cursor.moveToFirst()) {
            do {
                DriveInfo result = new DriveInfo(cursor.getString(1), cursor.getString(6), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5));

                Log.i("Hours: ", result.getHours());

                Double toAdd = Double.valueOf(result.getHours());
                Double prev = 0.0;

                hours.put("total", toAdd);

                if(result.getCondition().equals("day")) {
                    prev = hours.get("day");
                    toAdd += prev;
                    hours.put("day", toAdd);
                } else {
                    prev = hours.get("night");
                    toAdd += prev;
                    hours.put("night", toAdd);
                }

                toAdd = Double.valueOf(result.getHours());

                if(result.getLesson().equals("Residential")) {
                    prev = hours.get("residential");
                    toAdd += prev;
                    hours.put("residential", toAdd);
                } else if(result.getLesson().equals("Commercial")) {
                    prev = hours.get("commercial");
                    toAdd += prev;
                    hours.put("commercial", toAdd);
                } else {
                    prev = hours.get("highway");
                    toAdd += prev;
                    hours.put("highway", toAdd);
                }

                toAdd = Double.valueOf(result.getHours());

                if(result.getWeather().equals("Clear")) {
                    prev = hours.get("clear");
                    toAdd += prev;
                    hours.put("clear", toAdd);
                } else if(result.getWeather().equals("Raining")) {
                    prev = hours.get("raining");
                    toAdd += prev;
                    hours.put("raining", toAdd);
                } else {
                    prev = hours.get("snowIce");
                    toAdd += prev;
                    hours.put("snowIce", toAdd);
                }

            } while(cursor.moveToNext());
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
