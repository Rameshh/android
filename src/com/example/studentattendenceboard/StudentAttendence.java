package com.example.studentattendenceboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class StudentAttendence extends Activity {
	TextView tv1;
	Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_attendence);
		tv1 = (TextView) findViewById(R.id.textView1);
		button = (Button) findViewById(R.id.attendence);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), SelectDetails.class);
				startActivity(intent);
			}
		});
	}
}
	
