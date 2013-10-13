package com.HomeCameraViewer;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends Activity {
	
	private Button button;
	private Spinner monthspinner;
	private Spinner dayspinner;
	private Spinner hourspinner;
	private Spinner minutespinner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		final Context context = this;
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		button = (Button) findViewById(R.id.button1);
		monthspinner  = (Spinner)findViewById(R.id.month_spinner);
		dayspinner    = (Spinner)findViewById(R.id.day_spinner);
		hourspinner   = (Spinner)findViewById(R.id.hour_spinner);
		minutespinner = (Spinner)findViewById(R.id.minute_spinner);
		
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//get the current values from the spinners
				String month  = monthspinner.getSelectedItem().toString();
				String day    = dayspinner.getSelectedItem().toString();
				String hour   = hourspinner.getSelectedItem().toString();
				String minute = minutespinner.getSelectedItem().toString();
				
				//create intent and send variables set above
				Intent toWebView = new Intent(context, WebViewActivity.class);
				toWebView.putExtra("month", month);
				toWebView.putExtra("day", day);
				toWebView.putExtra("hour", hour);
				toWebView.putExtra("minute", minute);
				startActivity(toWebView);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
