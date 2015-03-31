package com.example.studentattendenceboard;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import android.content.Intent;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.example.studentattendenceboard.SelectDetails.task;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ListView;

public class TakeAttendance extends Activity {
	ListView listview;
	Button submit;
    String subname;
	String sem;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.attendance_list);

		submit = (Button) findViewById(R.id.button1);

		new task().execute();
		// addItemsToSubject(subjectSpin);
		// addItemsToSem(semSpin);
		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getBaseContext(), "result", Toast.LENGTH_LONG)
						.show();
				Intent i = getIntent();
				subname = i.getStringExtra("subname");
				sem = i.getStringExtra("sem");
				Log.e("subname", subname+sem);
				Intent intent = new Intent(TakeAttendance.this, StudentDetails.class);
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

			String url_select = "http://192.168.1.14/attendance/name.php";
			// String url_select2 = "http://192.168.1.14/attendance/users.php";

			ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("subname", subname));
			param.add(new BasicNameValuePair("sem", sem));

			try {
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url_select);
				
				httpPost.setEntity(new UrlEncodedFormEntity(param));

				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();

				// read content
				is1 = httpEntity.getContent();

			} catch (Exception e) {

				Log.e("log_tag", "Error in http connection " + e.toString());
			}
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						is1));
				StringBuilder sb = new StringBuilder();
				String line = "";
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				is1.close();
				result = sb.toString();

			} catch (Exception e) {
				// TODO: handle exception
				Log.e("log_tag", "Error converting result " + e.toString());
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
					String usn = Jasonobject.getString("usn");
					String name = Jasonobject.getString("fname");
					arr[i] = usn + "\t\t\t\t\t\t\t\t\t\t\t\t" + name;
					Log.e("Data", arr[i]);
				}
				
				listSelection(arr);

			} catch (Exception e) {
				// TODO: handle exception
				Log.e("log_tag", "Error parsing data " + e.toString());
			}
		}
	}

	public void listSelection(String[] res) {
		Log.e("Data", "Parsed ".toString());
		listview = (ListView) findViewById(R.id.listview1);
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_checked, res);
		
		listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listview.setTextFilterEnabled(true);
		listview.setAdapter(dataAdapter);

	}

}
