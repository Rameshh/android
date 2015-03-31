package com.example.studentattendenceboard;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SelectDetails extends Activity {
	TextView tilte;
	Spinner staffSpin;
	Spinner subjectSpin;
	Spinner semSpin;
	Button submit;
	String subname;
	String sem;
	String name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details);
		submit = (Button) findViewById(R.id.button1);

		new task().execute();
		// addItemsToSubject(subjectSpin);
		// addItemsToSem(semSpin);
		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getBaseContext(), "Validation is Done here",
						Toast.LENGTH_LONG).show();
   		//	 Intent intent = new Intent(getApplicationContext(), TakeAttendance.class);
 		//		 startActivity(intent);
				Intent intent = new Intent(SelectDetails.this, TakeAttendance.class);
		        String name = getIntent().getStringExtra("name");
		        intent.putExtra("subname",subname);
		        intent.putExtra("sem",sem);
		        startActivity(intent);
			}
		});
	}

	class task extends AsyncTask<String, String, Void> {

		InputStream is1 = null;
		InputStream is2 = null;
		String result = "";
		String result2 = "";

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(String... params) {

			String url_select = "http://192.168.1.14/attendance/teachers.php";
			String url_select2 = "http://192.168.1.14/attendance/subjects.php";

			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url_select);

			ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

			try {
				httpPost.setEntity(new UrlEncodedFormEntity(param));

				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();

				// read content
				is1 = httpEntity.getContent();

			} catch (Exception e) {

				Log.e("log_tag1", "Error in http connection " + e.toString());
			}
			try {
				BufferedReader br = new BufferedReader(
						new InputStreamReader(is1));
				StringBuilder sb = new StringBuilder();
				String line = "";
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				is1.close();
				result = sb.toString();

			} catch (Exception e) {
				// TODO: handle exception
				Log.e("log_tag2", "Error converting result " + e.toString());
			}

			HttpClient httpClient2 = new DefaultHttpClient();
			HttpPost httpPost2 = new HttpPost(url_select2);

			ArrayList<NameValuePair> param2 = new ArrayList<NameValuePair>();

			try {
				httpPost2.setEntity(new UrlEncodedFormEntity(param2));

				HttpResponse httpResponse2 = httpClient2.execute(httpPost2);
				HttpEntity httpEntity2 = httpResponse2.getEntity();

				// read content
				is2 = httpEntity2.getContent();

			} catch (Exception e) {

				Log.e("log_tag3", "Error in http connection " + e.toString());
			}
			try {
				BufferedReader br2 = new BufferedReader(new InputStreamReader(
						is2));
				StringBuilder sb2 = new StringBuilder();
				String line = "";
				while ((line = br2.readLine()) != null) {
					sb2.append(line + "\n");
				}
				is2.close();
				result2 = sb2.toString();

			} catch (Exception e) {
				// TODO: handle exception
				Log.e("log_tag4", "Error converting result " + e.toString());
			}

			return null;

		}

		protected void onPostExecute(Void v) {

			try {
				JSONArray Jarray = new JSONArray(result);

				String arr[] = new String[Jarray.length()];

				// String arr2[] = new String[Jarray.length()];
				for (int i = 0; i < Jarray.length(); i++) {
					JSONObject Jasonobject = null;

					Jasonobject = Jarray.getJSONObject(i);

					// get an output on the screen
					// String no =
					// Jasonobject.getString("no// this should be same in the table field name");
					String name = Jasonobject.getString("fname");

					arr[i] = name;
					Log.e("Data1", arr[i]);
				}
				addItemsOnStaffSpinner(arr);
				
				JSONArray Jarray2 = new JSONArray(result2);
				String arr2[] = new String[Jarray2.length()];
				for (int j = 0; j < Jarray2.length(); j++) {

					JSONObject Jasonobject = null;

					Jasonobject = Jarray2.getJSONObject(j);

					// get an output on the screen
					// String no =
					// Jasonobject.getString("no// this should be same in the table field name");
					String name = Jasonobject.getString("subname");

					arr2[j] = name;
					Log.e("Data2", arr2[j]);

				}
				addItemsOnSubSpinner(arr2);
				addItemsOnSemSpinner();

				// this.progressDialog.dismiss();

			} catch (Exception e) {
				// TODO: handle exception
				Log.e("log_tag", "Error parsing data " + e.toString());
			}
		}
	}

	public void addItemsOnStaffSpinner(String[] res) {
		Log.e("Data", "Parsed ".toString());
		staffSpin = (Spinner) findViewById(R.id.spinner1);
		List<String> list = new ArrayList<String>();
		list.add("Select Staff Name");
		for (int i = 0; i < res.length; i++) {
			list.add(res[i]);
		}

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);

		dataAdapter
				.setDropDownViewResource(android.R.layout.select_dialog_item);

		staffSpin.setAdapter(dataAdapter);
		
	}

	public void addItemsOnSemSpinner() {
		
	    semSpin = (Spinner) findViewById(R.id.spinner3);
		final List<String> list = new ArrayList<String>();
		list.add("sem");
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");
		list.add("6");
		list.add("7");
		list.add("8");
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.select_dialog_item);
		semSpin.setAdapter(dataAdapter);
		semSpin.setPrompt("select Semester");
		
		sem = semSpin.getSelectedItem().toString();
//		semSpin.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
//					long id) {
//				// TODO Auto-generated method stub
//				sem = list[position];
//			}
//		});
	}
	

	public void addItemsOnSubSpinner(final String[] s) {
		Log.e("Data", "Parsed ".toString());
		subjectSpin = (Spinner) findViewById(R.id.spinner2);
		List<String> list = new ArrayList<String>();
		list.add("Select Subject");
		for (int i = 0; i < s.length; i++) {
			list.add(s[i]);
		}

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);

		dataAdapter
				.setDropDownViewResource(android.R.layout.select_dialog_item);

		subjectSpin.setAdapter(dataAdapter);
		subjectSpin.setPrompt("Select Subject");
		subname = subjectSpin.getSelectedItem().toString();
//		subjectSpin.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
//					long id) {
//				// TODO Auto-generated method stub
//				subname = s[position];
//			}
//		});
	}
}
