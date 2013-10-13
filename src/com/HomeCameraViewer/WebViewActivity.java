package com.HomeCameraViewer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.support.v4.app.NavUtils;

public class WebViewActivity extends Activity {

	private WebView webView;
	private String month;
	private String day;
	private String hour;
	private String minute;
	private String htmlImages;
	private String htmlNumber;
	private int trackTaskCompletion;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view);
		// Show the Up button in the action bar.
		setupActionBar();

		Bundle extras = getIntent().getExtras();
		// set all the variables from the spinners in mainActivity
		month = extras.getString("month");
		day = extras.getString("day");
		hour = extras.getString("hour");
		minute = extras.getString("minute");
		String myhtml = "retrieving images...";

		// set up the WebView
		webView = (WebView) findViewById(R.id.webView01);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient());
		webView.loadData(myhtml, "text/html", null);
		
		//instantiate threads and run them
		trackTaskCompletion = 0;
		runUpdates();

	}

	// set up AsyncTask for getting images from server
	class GetImages extends AsyncTask<Long, Void, String> {
		private Exception exception_;
		private String url_ = "http://71.197.6.239/netapps/project1/getImages.php?month="
				+ month
				+ "&day="
				+ day
				+ "&hour="
				+ hour
				+ "&minute="
				+ minute
				+ "&what=imgs";
		private HttpResponse response_;

		@Override
		protected String doInBackground(Long... params) {
			try {
				HttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet(url_);
				response_ = client.execute(request);
			} catch (Exception e) {
				exception_ = e;
			}
			String htmlString = "<html><body>Setting htmlString to default value in images AsyncTask</body></html>";
			int responseCode = response_.getStatusLine().getStatusCode();
			switch (responseCode) {
			case 200:
				HttpEntity entity = response_.getEntity();
				if (entity != null) {
					try {
						htmlString = EntityUtils.toString(entity);						
					} catch (ParseException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				break;
			default:
				htmlString = "Error code: " + responseCode;
				break;
			}
			return htmlString;
		}

		public void onPostExecute(String htmlString) {
			htmlImages = htmlString;
			trackTaskCompletion ++;
			if (trackTaskCompletion == 2) {
				updateUI();
				trackTaskCompletion = 0;
			}
		}
	} // END AsyncTask to get images

	// set up AsyncTask for getting number of images that match query
	class GetNumber extends AsyncTask<Long, Void, String> {
		private Exception exception_;
		private String url_ = "http://71.197.6.239/netapps/project1/getImages.php?month="
				+ month
				+ "&day="
				+ day
				+ "&hour="
				+ hour
				+ "&minute="
				+ minute
				+ "&what=nmbr";
		private HttpResponse response_;

		@Override
		protected String doInBackground(Long... params) {
			try {
				HttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet(url_);
				response_ = client.execute(request);
			} catch (Exception e) {
				exception_ = e;
			}
			String htmlString = "<html><body>Setting htmlString to default value in number asyctask</body></html>";
			int responseCode = response_.getStatusLine().getStatusCode();
			switch (responseCode) {
			case 200:
				HttpEntity entity = response_.getEntity();
				if (entity != null) {
					try {
						htmlString = EntityUtils.toString(entity);
						htmlNumber = htmlString;
					} catch (ParseException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				break;
			default:
				htmlString = "Error code: " + responseCode;
				break;
			}
			return htmlString;
		}

		public void onPostExecute(String htmlString) {
			htmlNumber = htmlString;
			trackTaskCompletion ++;
			if (trackTaskCompletion == 2) {
				updateUI();
				trackTaskCompletion = 0;
			}
		}
	} // END AsyncTask to get number
	
	
	public String formatResponses() {
		String formattedResponse = null;
		String images = htmlImages;
		String number = htmlNumber;
		images = images.replace("<html>", "");
		images = images.replace("<body>", "");
		images = images.replace("<head>", "");
		images = images.replace("</head>", "");
		number = number.replace("</body>", "");
		number = number.replace("</html>", "");
		formattedResponse = number + "<br><br>" + images;
		return formattedResponse;
	}

	public void updateUI() {
		String htmlFinal = formatResponses();
		webView.loadData(htmlFinal, "text/html", null);
	}

	
	public void runUpdates() {
		final Handler handler = new Handler();
		Timer timer = new Timer();
		TimerTask doAsyncTasks = new TimerTask() {
			@Override
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						try {
							GetImages t1 = new GetImages();
							GetNumber t2 = new GetNumber();
							t1.execute();
							t2.execute();
						} catch (Exception e) {
							
						}
					}
				});
			}
		};
		timer.schedule(doAsyncTasks, 0, 15000); 
	}
	
	//*********************************************************************************************

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.web_view, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
