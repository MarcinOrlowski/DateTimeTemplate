package com.marcinorlowski.datetimetemplate.demo;

/**
 * DateTime Template
 *
 * @author Marcin Orlowski <mail (#) marcinOrlowski (.) com>
 * @copyright 2013-2020 Marcin Orlowski
 * @license http://www.opensource.org/licenses/mit-license.php MIT
 * @link https://github.com/MarcinOrlowski/DateTimeTemplate
 */

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.marcinorlowski.datetimetemplate.DateTimeTemplate;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

	@BindView (R.id.root)
	View mRootContainer;

	@BindView (R.id.format)
	EditText mFormat;

	@BindView (R.id.text)
	TextView mText;

	@BindView (R.id.resultCard)
	View mResultCard;

	@BindView (R.id.time)
	Button mTime;

	@BindView (R.id.date)
	Button mDate;

	protected Unbinder mButterKnifeUnbinder;

	final static Calendar mCalendar = new GregorianCalendar(TimeZone.getDefault());

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mCalendar.setTime(new Date());

		mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
		mMinute = mCalendar.get(Calendar.MINUTE);
		mYear = mCalendar.get(Calendar.YEAR);
		mMonth = mCalendar.get(Calendar.MONTH);
		mDay = mCalendar.get(Calendar.DAY_OF_MONTH);

		mButterKnifeUnbinder = ButterKnife.bind(this);
	}

	@Override
	protected void onDestroy() {
		mButterKnifeUnbinder.unbind();
		super.onDestroy();
	}

	@OnClick (R.id.update)
	public void update() {
		IBinder windowToken = getWindow().getDecorView().getRootView().getWindowToken();
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(windowToken, 0);
		mRootContainer.clearFocus();

		mText.setText(DateTimeTemplate.format(mCalendar, mFormat.getText().toString()));

		mResultCard.setVisibility(View.VISIBLE);
	}

	protected static void updateCalendar() {
		mCalendar.set(mYear, mMonth, mDay, mHour, mMinute);
	}

	// --------------------------------------------------------------------------------------------------------------------------

	@OnClick (R.id.time)
	public void pickTime() {
		DialogFragment newFragment = new TimePickerFragment();
		newFragment.show(getSupportFragmentManager(), "timePicker");
	}

	protected static int mHour, mMinute;

	public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new TimePickerDialog(getActivity(), this, mHour, mMinute, DateFormat.is24HourFormat(getActivity()));
		}

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;

			updateCalendar();
		}
	}

	// --------------------------------------------------------------------------------------------------------------------------

	@OnClick (R.id.date)
	public void pickDate() {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getSupportFragmentManager(), "datePicker");
	}

	protected static int mYear, mMonth, mDay;

	public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new DatePickerDialog(getActivity(), this, mYear, mMonth, mDay);
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {
			mYear = year;
			mMonth = month;
			mDay = day;

			updateCalendar();
		}
	}


}
