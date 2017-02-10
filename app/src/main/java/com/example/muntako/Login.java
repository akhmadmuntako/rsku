package com.example.muntako;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;


public class Login extends ActionBarActivity  {

	Intent a;
	EditText email, password;
	String url, success;
	SessionManager session;
	Button daftar, login;
	private static final int DIALOG_ERROR_CONNECTION = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		overridePendingTransition(R.layout.trans1, R.layout.trans2);
		session = new SessionManager(getApplicationContext());
		//Toast.makeText(getApplicationContext(),"User Login Status: " + session.isLoggedIn(), Toast.LENGTH_SHORT).show();

		daftar = (Button) findViewById(R.id.daftar);
		login = (Button) findViewById(R.id.login);
		email = (EditText) findViewById(R.id.email);
		password = (EditText) findViewById(R.id.password);
		CheckBox cb2 = (CheckBox) findViewById(R.id.Cb2);
		cb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (!isChecked) {
					password.setTransformationMethod(PasswordTransformationMethod.getInstance());
				} else {
					password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				}
			}
		});
		daftar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				if (!isOnline(Login.this)) {
					showDialog(DIALOG_ERROR_CONNECTION); //displaying the created dialog.
				} else {
					Intent daftar = new Intent(Login.this, Register.class);
					startActivity(daftar);
				}

			}
		});

		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isOnline(Login.this)) {
					showDialog(DIALOG_ERROR_CONNECTION); //displaying the created dialog.
				} else {
					url = Config.LOGIN + "?email="
							+ email.getText().toString() + "&password="
							+ password.getText().toString();

					if (email.getText().toString().trim().length() > 0
							&& password.getText().toString().trim().length() > 0) {
						new Masuk().execute();
					} else {
						Toast.makeText(getApplicationContext(), R.string.ep_kosong, Toast.LENGTH_SHORT).show();
					}
				}
			}
		});

	}

	public boolean isOnline(Context c) {
		ConnectivityManager cm = (ConnectivityManager) c
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();

		if (ni != null && ni.isConnected())
			return true;
		else
			return false;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch (id) {
			case DIALOG_ERROR_CONNECTION:
				AlertDialog.Builder errorDialog = new AlertDialog.Builder(this);
				errorDialog.setTitle("Error");
				errorDialog.setMessage(R.string.error_internet);
				errorDialog.setNeutralButton("OK",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						});

				AlertDialog errorAlert = errorDialog.create();
				return errorAlert;

			default:
				break;
		}
		return dialog;
	}
	public class InternetConnection extends Activity {
		static final int DIALOG_ERROR_CONNECTION = 1;
	}

	public class Masuk extends AsyncTask<String, String, String> 
	{
		ProgressDialog pDialog;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(Login.this);
			pDialog.setMessage(getResources().getString(R.string.tunggu_msg));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		@Override
		protected String doInBackground(String... arg0) {
			JSONParser jParser = new JSONParser();

			JSONObject json = jParser.getJSONFromUrl(url);

			try {
				success = json.getString("success");

				Log.e("error", "nilai sukses=" + success);

				JSONArray hasil = json.getJSONArray("login");

				if (success.equals("1")) {

					for (int i = 0; i < hasil.length(); i++) {

						JSONObject c = hasil.getJSONObject(i);

						String nama = c.getString("nama").trim();
						String email = c.getString("email").trim();
						String password = c.getString("password").trim();
						String ttl = c.getString("ttl").trim();
						String notel = c.getString("notel").trim();
						String id = c.getString("id").trim();
						String ackey = c.getString("ackey").trim();
						String jekel = c.getString("jekel");
						session.createLoginSession(email, password, nama, ttl, notel, id, ackey,jekel);
						Log.e("ok", " ambil data");

					}
				} else {
					Log.e("erro", "tidak bisa ambil data 0");
				}

			} catch (Exception e) {
				// TODO: handle exception
				Log.e("erro", "tidak bisa ambil data 1");
			}

			return null;
			
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if (success.equals("1")) {
				session = new SessionManager(getApplicationContext());
				//Toast.makeText(getApplicationContext(),"User Login Status: " + session.isLoggedIn(), Toast.LENGTH_SHORT).show();
				finish();
			} else {
				Toast.makeText(getApplicationContext(), R.string.ep_valid, Toast.LENGTH_LONG).show();
			}

		}
		
	}

}
