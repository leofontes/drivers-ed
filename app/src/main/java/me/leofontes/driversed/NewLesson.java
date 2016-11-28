package me.leofontes.driversed;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewLesson.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewLesson#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewLesson extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Calendar myCalendar;
    EditText dateInput;
    EditText hoursInput;
    View view;
    Calendar startDateTime;
    Calendar endDateTime;

    RadioButton dayRadio;
    RadioButton nightRadio;
    Spinner weatherSpinner;
    Spinner lessonSpinner;
    Button startButton;
    Button stopButton;
    Button clearButton;
    Button saveButton;
    DatePickerDialog.OnDateSetListener date;

    DriveDBadapter db;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public NewLesson() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewLesson.
     */
    // TODO: Rename and change types and number of parameters
    public static NewLesson newInstance(String param1, String param2) {
        NewLesson fragment = new NewLesson();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_new_lesson, container, false);

        lessonSpinner = (Spinner) view.findViewById(R.id.editLessonSpinner);
        ArrayAdapter<CharSequence> lessonAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.lessonTypeArray, android.R.layout.simple_spinner_item);
        lessonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lessonSpinner.setAdapter(lessonAdapter);

        weatherSpinner = (Spinner) view.findViewById(R.id.editWeatherSpinner);
        ArrayAdapter<CharSequence> weatherAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.weatherTypeArray, android.R.layout.simple_spinner_item);
        weatherAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weatherSpinner.setAdapter(weatherAdapter);

        myCalendar = Calendar.getInstance();

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        dateInput = (EditText) view.findViewById(R.id.editDateInput);
        hoursInput = (EditText) view.findViewById(R.id.editHoursInput);
        startButton = (Button) view.findViewById(R.id.startButton);
        stopButton = (Button) view.findViewById(R.id.stopButton);
        clearButton = (Button) view.findViewById(R.id.clearButton);
        saveButton = (Button) view.findViewById(R.id.saveButton);
        dayRadio = (RadioButton) view.findViewById(R.id.dayRadioButton);
        nightRadio = (RadioButton) view.findViewById(R.id.nightRadioButton);

        setListeners();

        db = db.getInstance(getActivity().getApplicationContext());
        db.open();

        return view;
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

    private void updateLabel() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        startDateTime = myCalendar.getInstance();
        dateInput.setText(sdf.format(myCalendar.getTime()));
    }

    public static String getDayOfWeek(int n) {
        switch (n) {
            case 1:
                return "Sunday";
            case 2:
                return "Monday";
            case 3:
                return "Tuesday";
            case 4:
                return "Wednesday";
            case 5:
                return "Thursday";
            case 6:
                return "Friday";
            case 7:
                return "Saturday";
            default:
                return "";
        }
    }

    /*********************** LISTENERS ***************************************/

    public void setListeners() {
        startButton.setOnClickListener(startDriveListener);
        stopButton.setOnClickListener(stopDriveListener);
        clearButton.setOnClickListener(clearInputs);
        saveButton.setOnClickListener(saveDrive);
        dateInput.setOnClickListener(dateClick);
        hoursInput.setOnClickListener(hoursClick);
    }

    View.OnClickListener startDriveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startDateTime = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            String startDate = dateFormat.format(startDateTime.getTime());
            dateInput.setText(startDate);
        }
    };

    View.OnClickListener stopDriveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            endDateTime = Calendar.getInstance();
            long difference = endDateTime.getTime().getTime() - startDateTime.getTime().getTime();
            long hours = TimeUnit.MILLISECONDS.toHours(difference);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(difference);

            hoursInput.setText(hours + "." + minutes);
        }
    };

    View.OnClickListener clearInputs = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dateInput.setText("");
            hoursInput.setText("");
        }
    };

    View.OnClickListener saveDrive = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //First, validate to make sure the input is appropriate
            if(hoursInput.getText().toString().equals("") || dateInput.getText().toString().equals("")) {
                Toast.makeText(v.getContext(), "Can't save unless you fill Date and Hour", Toast.LENGTH_LONG).show();
            } else if(!dayRadio.isChecked() && !nightRadio.isChecked()) {
                Toast.makeText(v.getContext(), "Can't save unless you check Day or Night", Toast.LENGTH_LONG).show();
            } else if(hoursInput.getText().toString().equals("0.0") || hoursInput.getText().toString().equals("0:0")) {
                Toast.makeText(v.getContext(), "What's the point of saving 0 minutes of Driving? Drive some more!", Toast.LENGTH_LONG).show();
            } else {
                //Fetch all the information for our DriveInfo object that gets passed to the DriveDBadapter
                String tmpHour = hoursInput.getText().toString();
                String ins_hour = "";
                for(int i = 0; i < tmpHour.length(); i++) {
                    if(tmpHour.charAt(i) == ':') {
                        ins_hour += ".";
                    } else {
                        ins_hour += tmpHour.charAt(i);
                    }
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
                String ins_day = getDayOfWeek(startDateTime.get(Calendar.DAY_OF_WEEK));
                DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                String ins_date = dateFormat.format(startDateTime.getTime());
                String ins_condition;
                if(dayRadio.isChecked()) {
                    ins_condition = "day";
                } else {
                    ins_condition = "night";
                }
                String ins_lesson = lessonSpinner.getSelectedItem().toString();
                String ins_weather = weatherSpinner.getSelectedItem().toString();

                DriveInfo driveInfo = new DriveInfo(ins_date, ins_day, ins_hour, ins_condition, ins_lesson, ins_weather);
                db.insertItem(driveInfo);
                Toast.makeText(v.getContext(), "Drive has been saved to the Database", Toast.LENGTH_SHORT).show();
            }


        }
    };

    View.OnClickListener dateClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new DatePickerDialog(v.getContext(), date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    };


    View.OnClickListener hoursClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            Calendar mCurrentTime = Calendar.getInstance();
            int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mCurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    hoursInput.setText(selectedHour + ":" + selectedMinute);
                }
            }, hour, minute, true);//Yes 24 hour time
            mTimePicker.setTitle("Hours");
            mTimePicker.show();
        }
    };




}
