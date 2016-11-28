package me.leofontes.driversed;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Leo on 3/22/16.
 */
public class EditLogDialog extends DialogFragment {
    EditText dateInput;
    EditText hoursInput;
    RadioButton dayButton;
    RadioButton nightButton;
    Spinner lessonSpinner;
    Spinner weatherSpinner;

    Calendar myCalendar;

    DriveDBadapter db;
    Cursor cursor;
    ArrayList<DriveInfo> logs;
    AdapterLogs adapterLogs;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.edit_log, null);

        final long itemId = getArguments().getLong("driveId");

        db = DriveDBadapter.getInstance(view.getContext());
        db.open();

        DriveInfo driveInfo = new DriveInfo();

        try {
            driveInfo = db.getDriveItem(itemId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        dateInput = (EditText) view.findViewById(R.id.editDateInput);
        hoursInput = (EditText) view.findViewById(R.id.editHoursInput);
        dayButton = (RadioButton) view.findViewById(R.id.editDayRadioButton);
        nightButton = (RadioButton) view.findViewById(R.id.editNightRadioButton);

        lessonSpinner = (Spinner) view.findViewById(R.id.editLessonSpinner);
        ArrayAdapter<CharSequence> lessonAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.lessonTypeArray, android.R.layout.simple_spinner_item);
        lessonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lessonSpinner.setAdapter(lessonAdapter);

        weatherSpinner = (Spinner) view.findViewById(R.id.editWeatherSpinner);
        ArrayAdapter<CharSequence> weatherAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.weatherTypeArray, android.R.layout.simple_spinner_item);
        weatherAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weatherSpinner.setAdapter(weatherAdapter);

        dateInput.setText(driveInfo.getDate());
        hoursInput.setText(driveInfo.getHours());

        if(driveInfo.getCondition().equals("day")) {
            dayButton.setChecked(true);
            nightButton.setChecked(false);
        } else {
            nightButton.setChecked(true);
            dayButton.setChecked(false);
        }

        if(driveInfo.getLesson().equals("Residential")) {
            lessonSpinner.setSelection(0);
        } else if(driveInfo.getLesson().equals("Commercial")) {
            lessonSpinner.setSelection(1);
        } else {
            lessonSpinner.setSelection(2);
        }

        if(driveInfo.getWeather().equals("Clear")) {
            weatherSpinner.setSelection(0);
        } else if(driveInfo.getWeather().equals("Raining")) {
            weatherSpinner.setSelection(1);
        } else {
            weatherSpinner.setSelection(2);
        }

        myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        dateInput = (EditText) view.findViewById(R.id.editDateInput);
        dateInput.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new DatePickerDialog(v.getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view).setTitle(R.string.edit_log_title).setPositiveButton(R.string.dialog_set, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                db.updateField(itemId, fetchUpdatedData());
                Toast.makeText(getActivity().getApplicationContext(), "Record edited and stored on the Database.", Toast.LENGTH_SHORT).show();
            }
        }).setNeutralButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity().getApplicationContext(), "Nothing changed.", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton(R.string.dialog_delete, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {

                db.removeItem(itemId);
                Toast.makeText(getActivity().getApplicationContext(), "Record deleted.", Toast.LENGTH_SHORT).show();
            }
        });

        hoursInput = (EditText) view.findViewById(R.id.editHoursInput);

        hoursInput.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mCurrentTime = Calendar.getInstance();
                int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mCurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        hoursInput.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Hours");
                mTimePicker.show();

            }
        });

        return builder.create();
    }

    private void newAdapter() {
        cursor = db.getAllItems();
        logs.clear();
        if(cursor.moveToFirst()) {
            do {
                DriveInfo result = new DriveInfo(cursor.getString(1), cursor.getString(6), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5));
                logs.add(0, result);
            } while(cursor.moveToNext());
        }
        adapterLogs.notifyDataSetChanged();
    }

    private void updateLabel() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateInput.setText(sdf.format(myCalendar.getTime()));
    }

    private ContentValues fetchUpdatedData() {
        ContentValues cvalues = new ContentValues();

        String ins_date = dateInput.getText().toString();
        String ins_hour = hoursInput.getText().toString();
        String ins_condition;
        if(dayButton.isChecked()) {
            ins_condition = "day";
        } else {
            ins_condition = "night";
        }
        String ins_lesson = lessonSpinner.getSelectedItem().toString();
        String ins_weather = weatherSpinner.getSelectedItem().toString();
        String ins_day = NewLesson.getDayOfWeek(myCalendar.get(Calendar.DAY_OF_WEEK));

        cvalues.put(DriveDBadapter.DRIVE_DATE, ins_date);
        cvalues.put(DriveDBadapter.DRIVE_HOURS, ins_hour);
        cvalues.put(DriveDBadapter.DRIVE_CONDITION, ins_condition);
        cvalues.put(DriveDBadapter.DRIVE_LESSON, ins_lesson);
        cvalues.put(DriveDBadapter.DRIVE_WEATHER, ins_weather);
        cvalues.put(DriveDBadapter.DRIVE_DAY, ins_day);

        return cvalues;
    }

}
