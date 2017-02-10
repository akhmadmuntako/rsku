package com.example.muntako;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class Daftar extends AppCompatActivity {
    private static final int DIALOG_ERROR_CONNECTION = 3;
    private TextView txtDate, txtTime;
    private int year, year2, month, day, hour, minute, sec, tl;
    JSONParser jsonParser = new JSONParser();
    private static String url = Config.DAFTAR;
    static final int DATE_PICKER_ID = 1;
    static final int TIME_DIALOG_ID = 0;
    String hasil = "gagal", dok, pol, jm, gab, email, nama, pass, id, ackey, ttl, notel, hari;
    EditText nm, not, peny, um;
    RadioGroup jk;
    String[] items = {"Poliklinik Gigi", "Poliklinik Anak", "Poliklinik Umum"};//ambil dari mysql
    Spinner s1, s2;
    TextView selection;
    ProgressDialog pDialog, sDialog;
    SessionManager session;
    Button klik;
    private Handler handler;
    String type;
    InputStream is = null;
    String result = null;
    String line = null;
    String jenis;
    String strnama, strnotel, strpeny, strumur, strjenis, strdok;
    ArrayAdapter<String> adapter, adapter2;
    ArrayList<String> listItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_daftar);
        overridePendingTransition(R.layout.trans1, R.layout.trans2);
        txtDate = (TextView) findViewById(R.id.tanggal);
        txtDate.setFocusable(false);
        txtTime = (TextView) findViewById(R.id.waktu);
        txtTime.setFocusable(false);
        session = new SessionManager(getApplicationContext());
        if (!isOnline(Daftar.this)) {
            showDialog(DIALOG_ERROR_CONNECTION);
        } else {

        }
        s1 = (Spinner) findViewById(R.id.spinner);
        s2 = (Spinner) findViewById(R.id.spinner2);
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        sec = c.get(Calendar.SECOND);

        txtDate.setOnClickListener(new View.OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDialog(DATE_PICKER_ID);
            }
        });

        txtTime.setOnClickListener(new View.OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDialog(TIME_DIALOG_ID);
            }
        });

        handler = new Handler();
        nm = (EditText) findViewById(R.id.nama);
        not = (EditText) findViewById(R.id.notel);
        peny = (EditText) findViewById(R.id.penyakit);
        um = (EditText) findViewById(R.id.umur);
        jk = (RadioGroup) findViewById(R.id.jekel);
        klik = (Button) findViewById(R.id.daftar);//button
        klik.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                try {
                    if (!isOnline(Daftar.this)) {
                        showDialog(DIALOG_ERROR_CONNECTION);
                    } else {
                        if (nm.getText().toString().length() == 0 || not.getText().toString().length() == 0 || peny.getText().toString().length() == 0
                                || um.getText().toString().length() == 0 || txtDate.getText().toString().length() == 0 || txtTime.getText().toString().length() == 0
                                || dok.length() == 0) {
                            Toast.makeText(Daftar.this, getResources().getString(R.string.p_akun), Toast.LENGTH_SHORT).show();
                        } else {
                            new daftarPasien().execute();
                        }
                    }

                } catch (NullPointerException e) {
                    Toast.makeText(Daftar.this, getResources().getString(R.string.p_akun), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (session.isLoggedIn() == true) {
            HashMap<String, String> user = session.getUserDetails();
            nama = user.get(SessionManager.KEY_NAME);
            id = user.get(SessionManager.KEY_ID);
            ackey = user.get(SessionManager.KEY_ACKEY);
            email = user.get(SessionManager.KEY_EMAIL);
            ttl = user.get(SessionManager.KEY_TTL);
            notel = user.get(SessionManager.KEY_NOTEL);
            pass = user.get(SessionManager.KEY_PASS);
            nm.setText(nama);
            not.setText(notel);
            final Calendar d = Calendar.getInstance();
            tl = Integer.parseInt(ttl.substring(0, 4));
            year2 = d.get(Calendar.YEAR);
            um.setText("" + (year2 - tl));
            jenis = user.get(SessionManager.KEY_JEKEL);
            if (jenis.equals("Laki-Laki")) {
                RadioButton rb1 = (RadioButton) findViewById(R.id.pria);
                rb1.setChecked(true);
            } else if (jenis.equals("Perempuan")) {
                RadioButton rb2 = (RadioButton) findViewById(R.id.perempuan);
                rb2.setChecked(true);
            }
        }
    }

    public class daftarPasien extends AsyncTask<String, String, String> {

        String success;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Daftar.this);
            pDialog.setMessage(getResources().getString(R.string.tunggu_msg));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            switch (jk.getCheckedRadioButtonId()) {
                case R.id.pria:
                    type = "Laki-Laki";
                    break;
                case R.id.perempuan:
                    type = "Perempuan";
                    break;
            }
            dok = s1.getSelectedItem().toString();
            strnama = nm.getText().toString();
            strnotel = not.getText().toString();
            strpeny = peny.getText().toString();
            strumur = um.getText().toString();
            strjenis = type;
            strdok = dok.toString();
            gab = txtDate.getText() + " " + txtTime.getText();
            String strjam = gab.toString();


            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("nama", strnama));
            nvp.add(new BasicNameValuePair("notel", strnotel));
            nvp.add(new BasicNameValuePair("penyakit", strpeny));
            nvp.add(new BasicNameValuePair("umur", strumur));
            nvp.add(new BasicNameValuePair("jekel", strjenis));
            nvp.add(new BasicNameValuePair("dokter", strdok));
            nvp.add(new BasicNameValuePair("jam", strjam));

            JSONObject json = jsonParser.makeHttpRequest(url, "POST", nvp);
            try {
                success = json.getString("success");
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error",
                        Toast.LENGTH_LONG).show();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, sec);
            calendar.set(Calendar.AM_PM, Calendar.PM);
            long milis = calendar.getTimeInMillis();//untuk panggilan 1 10 menit dalam milis
            //Toast.makeText(getApplicationContext(),""+calendar.getTimeInMillis(),Toast.LENGTH_SHORT).show();
            if (success.equals("1")) {
                Toast.makeText(getApplicationContext(), R.string.regsuk,
                        Toast.LENGTH_LONG).show();
                //scheduleNotification(getNotification(getResources().getString(R.string.cek_antrian)), milis-600000);//20 menit sebelum masuk ruangan
                finish();
                Intent a = new Intent(Daftar.this, Antrian.class);
                startActivity(a);
            } else {
                Toast.makeText(getApplicationContext(), R.string.reggag,
                        Toast.LENGTH_LONG).show();
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.daftar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//        if (id == R.id.lihat) {
//            Intent antr = new Intent(Daftar.this,Antrian.class);
//            startActivity(antr);
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:
//                return new DatePickerDialog(this, pickerListener, year, month,day);
                Date today = new Date();
                Calendar c = Calendar.getInstance();
                c.setTime(today);
                c.set(Calendar.HOUR_OF_DAY, 23);
                c.set(Calendar.MINUTE, 59);
                c.set(Calendar.SECOND, 59);
                c.add( Calendar.DAY_OF_MONTH, 7 );// tambah 7 hari
                long maxDate = c.getTimeInMillis(); // Twice!

                DatePickerDialog dialog = new DatePickerDialog(this, pickerListener, year, month, day);
                dialog.getDatePicker().setMinDate(today.getTime());
                dialog.getDatePicker().setMaxDate(maxDate);
                return dialog;
            //case TIME_DIALOG_ID:
            //    return new TimePickerDialog(this, timepicker, hour, minute, true);
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
        return null;
    }

    //private TimePickerDialog.OnTimeSetListener timepicker = new TimePickerDialog.OnTimeSetListener() {
    //    public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
    //        // TODO Auto-generated method stub
    //        hour = hourOfDay;
    //        minute = minuteOfHour;
    // Show selected date
    //        String jam = LPad(""+hour, "0", 2)+":"+LPad(""+minute,"0", 2)+":"+LPad(""+sec,"0",2);
    //        if(hour>=20||hour<=8&&minute<=59){
    //            Toast.makeText(getApplicationContext(),getResources().getString(R.string.rs_tutup),Toast.LENGTH_SHORT).show();
    //            txtTime.setText("");
    //        }else if(hour>=9&&minute>=0){
    //            txtTime.setText(jam);
    //        }else{
    //        txtTime.setText(jam);
    //        }
    //buat if untuk jika jam 9 pagi dan 9 malam dipilih maka keluar toast dan txtTime dihapus
    //    }
    //};

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;
            SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
            Date date = new Date(selectedYear, selectedMonth, selectedDay - 1);
            String dayOfWeek = simpledateformat.format(date);
            hari = dayOfWeek;
            String tanggal = year + "-" + LPad("" + (month + 1), "0", 2) + "-" + LPad("" + day, "0", 2);
            txtDate.setText(tanggal);
            Calendar d = Calendar.getInstance();
            int jam = d.get(Calendar.HOUR_OF_DAY);
            int menit = d.get(Calendar.MINUTE);
            int detik = d.get(Calendar.SECOND);
            txtTime.setText(jam + ":" + menit + ":" + detik);
            //Toast.makeText(Daftar.this, txtTime.getText(), Toast.LENGTH_SHORT).show();
            if (hari.equals("Minggu")) {
                Toast.makeText(Daftar.this, getResources().getString(R.string.rs_tutup), Toast.LENGTH_SHORT).show();
            }
            adapter2 = new ArrayAdapter<String>(Daftar.this, android.R.layout.simple_spinner_dropdown_item, items);
            s2.setAdapter(adapter2);
            s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    pol = s2.getSelectedItem().toString();
                    spinner sp = new spinner();
                    sp.execute();
                    adapter = new ArrayAdapter<String>(Daftar.this, android.R.layout.simple_spinner_dropdown_item, listItems);
                    s1.setAdapter(adapter);
                    s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view,
                                                   int position, long id) {
                            // TODO Auto-generated method stub
                            //int index = s1.getSelectedItemPosition();
                            if (s1.equals(null)) {
                                dok = "";
                            } else {
                                dok = s1.getSelectedItem().toString();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                            dok = "";
                        }
                    });
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
    };

    private static String LPad(String schar, String spad, int len) {
        String sret = schar;
        for (int i = sret.length(); i < len; i++) {
            sret = spad + sret;
        }
        return new String(sret);
    }

    //private void scheduleNotification(Notification notification, long delay) {
    //    Intent notificationIntent = new Intent(this, NotificationPublisher.class);
    //    notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
    //    notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
    //    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    //long futureInMillis = SystemClock.elapsedRealtime() + calendar.getTimeInMillis();
    //    AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
    //    alarmManager.set(AlarmManager.RTC_WAKEUP, delay, pendingIntent);
    //}

    //private Notification getNotification(String content) {
    //    Notification.Builder builder = new Notification.Builder(this);
    //    builder.setContentTitle(getResources().getString(R.string.app_name));
    //    builder.setPriority(Notification.PRIORITY_DEFAULT);
    //    builder.setDefaults(Notification.DEFAULT_ALL);
    //    builder.setContentText(content);
    //    builder.setSmallIcon(R.drawable.ic_launcher);
    //   return builder.build();
    //}

    public class spinner extends AsyncTask<String, Void, String> {
        String success;
        ArrayList<String> list;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            list = new ArrayList<>();
            list.clear();
            listItems.clear();
            sDialog = new ProgressDialog(Daftar.this);
            sDialog.setMessage(getResources().getString(R.string.tunggu_msg));
            sDialog.setIndeterminate(false);
            sDialog.setCancelable(false);
            sDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String resultSpinner = getSpinner();
            return resultSpinner;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            sDialog.dismiss();
            if (s.equalsIgnoreCase("0")) {
                Toast.makeText(Daftar.this, R.string.error_timeout, Toast.LENGTH_LONG).show();
            } else {
                listItems.addAll(list);
                adapter.notifyDataSetChanged();
            }
        }

        public String getSpinner() {
            String hasil = "";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Config.SPINNER_DOKTER);
                HttpParams params = httpclient.getParams();
                HttpConnectionParams.setConnectionTimeout(params, 20000);
                HttpConnectionParams.setSoTimeout(params, 20000);
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                Log.e("Pass 1", "connection success ");

                BufferedReader reader = new BufferedReader
                        (new InputStreamReader(is, "iso-8859-1"), 8);

                StringBuilder sb = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                is.close();
                result = sb.toString();
                Log.e("Pass 2", "connection success ");

                JSONArray JA = new JSONArray(result);
                JSONObject json = null;
                final String[] str1 = new String[JA.length()];
                for (int i = 0; i < JA.length(); i++) {
                    json = JA.getJSONObject(i);
                    String keyword = hari;
                    String keyword2 = pol;
                    Boolean found2 = Arrays.asList(json.getString("poli")).contains(keyword2);
                    Boolean found = Arrays.asList(json.getString("hari").split(",")).contains(keyword);
                    if (found2) {
                        if (found) {
                            list.add(json.getString("nama"));
                        }
                    }
                }
            } catch (ConnectionPoolTimeoutException a) {
                a.printStackTrace();
                hasil = "0";
            } catch (Exception e) {
                e.printStackTrace();
                hasil = "0";
            }
            return hasil;
        }
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

    public class InternetConnection extends Activity {
        static final int DIALOG_ERROR_CONNECTION = 1;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}