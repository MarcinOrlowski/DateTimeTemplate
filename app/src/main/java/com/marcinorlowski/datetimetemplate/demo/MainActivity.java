package com.marcinorlowski.datetimetemplate.demo;

/*
 *********************************************************************************
 *
 * @author Marcin Orlowski <mail (@) marcinorlowski (.) com>
 *
 *********************************************************************************
*/

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

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

	protected Unbinder mButterKnifeUnbinder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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


		Calendar c = new GregorianCalendar(TimeZone.getDefault());
		c.setTime(new Date());

		mText.setText(DateTimeTemplate.format(c, mFormat.getText().toString()));

		mResultCard.setVisibility(View.VISIBLE);
	}

}
