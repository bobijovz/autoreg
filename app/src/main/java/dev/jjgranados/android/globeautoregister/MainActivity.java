package dev.jjgranados.android.globeautoregister;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import dev.jjgranados.android.globeautoregister.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    private ActivityMainBinding binder;
    private SharedPreferences pref;
    private TimePickerDialog timePicker;
    private DatePickerDialog datePicker;
    private Calendar calendarDate;
    private Calendar calendarTime;
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat timeFormat;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this,R.layout.activity_main);
        calendarDate = Calendar.getInstance();
        calendarTime = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MMMM dd,yyyy", Locale.getDefault());
        timeFormat = new SimpleDateFormat("hh:mm aa", Locale.getDefault());

        binder.buttonSave.setOnClickListener(this);
        binder.imgbuttonSetSchedDay.setOnClickListener(this);
        binder.imgbuttonSetSchedTime.setOnClickListener(this);
        pref = getPreferences(Context.MODE_PRIVATE);
        if (pref.getBoolean(Constant.PREF_SETTINGS_IS_SET,false)) {
            long dateString = pref.getLong(Constant.PREF_SCHEDULE_DATE, calendarDate.getTimeInMillis());
            long timeString = pref.getLong(Constant.PREF_SCHEDULE_TIME, calendarTime.getTimeInMillis());

            calendarDate.setTimeInMillis(dateString);
            calendarTime.setTimeInMillis(timeString);
        }
        binder.edittextPromoBox.setText(pref.getString(Constant.PREF_MESSAGE_CONTENT, "GOCOMBOAHAF204"));
        binder.textviewPromoSched.setText(dateFormat.format( pref.getLong(Constant.PREF_SCHEDULE_DATE, calendarDate.getTimeInMillis())));
        binder.textviewPromoSchedTime.setText(timeFormat.format( pref.getLong(Constant.PREF_SCHEDULE_TIME, calendarTime.getTimeInMillis())));


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_save:
                SharedPreferences.Editor editor = pref.edit();
                editor.putString(Constant.PREF_MESSAGE_CONTENT,binder.edittextPromoBox.getText().toString());
                editor.putLong(Constant.PREF_SCHEDULE_DATE, calendarDate.getTimeInMillis());
                editor.putLong(Constant.PREF_SCHEDULE_TIME, calendarTime.getTimeInMillis());
                editor.apply();
                Toast.makeText(this,"Settings saved!",Toast.LENGTH_LONG).show();
                break;
            case R.id.imgbutton_set_sched_day:
                datePicker = new DatePickerDialog(this,this,calendarDate.get(Calendar.YEAR),calendarDate.get(Calendar.MONTH),calendarDate.get(Calendar.DAY_OF_MONTH));
                datePicker.show();
                break;
            case R.id.imgbutton_set_sched_time:
                timePicker = new TimePickerDialog(this,this,calendarTime.get(Calendar.HOUR_OF_DAY),calendarTime.get(Calendar.MINUTE),false);
                timePicker.show();
                break;
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        calendarTime.set(Calendar.HOUR_OF_DAY,hourOfDay);
        calendarTime.set(Calendar.MINUTE,minute);
        binder.textviewPromoSchedTime.setText(timeFormat.format(calendarTime.getTime()));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendarDate.set(Calendar.YEAR,year);
        calendarDate.set(Calendar.MONTH,month);
        calendarDate.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        binder.textviewPromoSched.setText(dateFormat.format(calendarDate.getTime()));
    }


}
