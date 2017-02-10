package com.example.muntako;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Daftar_RR extends AppCompatActivity {
    private static final int DIALOG_ERROR_CONNECTION = 3;
    private TextView txtDate, txtTime;
    private int year,year2, month, day, hour, minute, sec, tl;
    private static String url = Config.DAFTAR_RUANG;
    static final int DATE_PICKER_ID = 1;
    static final int TIME_DIALOG_ID = 0;
    JSONParser jsonParser = new JSONParser();
    String hasil="gagal",dok,jm,gab,email, nama, pass, id ,ackey, ttl, notel,jenis;
    EditText nm,not,peny,um,kelas;
    RadioGroup jk;
    Button klik;
    private Handler handler;
    TextView selection;
    ProgressDialog pDialog,sDialog;
    SessionManager session;
    EditText EditTxtID, EditTxtJenis, EditTxtKuota;
    TextView TxtViewId;
    String type;
    InputStream is=null;
    String result=null;
    String line=null;
    String strnama,strnotel,strpeny,strumur,strjenis,strdok,strkelas;
    ArrayAdapter<String> adapter;
    ArrayList<String> listItems=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar__rr);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        overridePendingTransition(R.layout.trans1, R.layout.trans2);
        EditTxtID = (EditText) findViewById(R.id.editTextID);
        EditTxtKuota = (EditText) findViewById(R.id.editTextKuota);
        kelas = (EditText) findViewById(R.id.editTextJenisRR);
        TxtViewId = (TextView) findViewById(R.id.id_ruang);
        handler = new Handler();
        nm=(EditText)findViewById(R.id.nama_rr);
        not=(EditText)findViewById(R.id.notel_rr);
        peny=(EditText)findViewById(R.id.penyakit_rr);
        um=(EditText)findViewById(R.id.umur_rr);
        jk=(RadioGroup) findViewById(R.id.jekel_rr);
        klik = (Button)findViewById(R.id.daftar_rr);//button
        txtDate = (TextView) findViewById(R.id.tanggal_rr);
        txtDate.setFocusable(false);
        txtTime = (TextView) findViewById(R.id.waktu_rr);
        txtTime.setFocusable(false);
        session = new SessionManager(getApplicationContext());
        Bundle f = getIntent().getExtras();
        String isi_id_ruang = f.getString("id_kuota");
        String isi_jenis_ruang= f.getString("kelas");
        String isi_kuota= f.getString("kuota");
        //meng-set bundle tersebut
        EditTxtID.setText(isi_id_ruang);
        kelas.setText(isi_jenis_ruang);
        EditTxtKuota.setText(isi_kuota);
        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);
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
        klik.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                try{
                    if(!isOnline(Daftar_RR.this)) {
                        showDialog(DIALOG_ERROR_CONNECTION);
                    }else{
                        if (nm.getText().toString().length() == 0 || not.getText().toString().length() == 0 || peny.getText().toString().length() == 0
                                || um.getText().toString().length() == 0 || txtDate.getText().toString().length() == 0 || txtTime.getText().toString().length() == 0
                                ||kelas.getText().toString().length() == 0) {
                            Toast.makeText(Daftar_RR.this, getResources().getString(R.string.p_akun), Toast.LENGTH_SHORT).show();
                        } else {
                            new daftarRR().execute();
                        }
                    }

                }
                catch(NullPointerException e){
                    Toast.makeText(Daftar_RR.this, getResources().getString(R.string.p_akun), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        if(session.isLoggedIn()==true){
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
            tl = Integer.parseInt(ttl.substring(0,4));
            year2=d.get(Calendar.YEAR);
            um.setText(""+(year2-tl));
            jenis = user.get(SessionManager.KEY_JEKEL);
            if(jenis.equals("Laki-Laki")){
                RadioButton rb1 = (RadioButton)findViewById(R.id.pria_rr);
                rb1.setChecked(true);
            }else if(jenis.equals("Perempuan")){
                RadioButton rb2 = (RadioButton)findViewById(R.id.perempuan_rr);
                rb2.setChecked(true);
            }
        }
    }

    public class daftarRR extends AsyncTask<String, String, String>{
        String success;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Daftar_RR.this);
            pDialog.setMessage(getResources().getString(R.string.tunggu_msg));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            switch (jk.getCheckedRadioButtonId()) {
                case R.id.pria_rr:
                    type="Laki-Laki";
                    break;
                case R.id.perempuan_rr:
                    type="Perempuan";
                    break;
            }
            strnama = nm.getText().toString();
            strnotel = not.getText().toString();
            strpeny = peny.getText().toString();
            strumur = um.getText().toString();
            strkelas = kelas.getText().toString();
            strjenis = type;
            gab=txtDate.getText()+" "+txtTime.getText();
            String strjam = gab.toString();


            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("kelas", strkelas));
            nvp.add(new BasicNameValuePair("nama", strnama));
            nvp.add(new BasicNameValuePair("notel", strnotel));
            nvp.add(new BasicNameValuePair("umur", strumur));
            nvp.add(new BasicNameValuePair("jekel", strjenis));
            nvp.add(new BasicNameValuePair("penyakit", strpeny));
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

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (success.equals("1")) {
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), R.string.regsuk,
                        Toast.LENGTH_LONG).show();
                finish();
            } else {
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), R.string.reggag,
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:
                Date today = new Date();
                Calendar c = Calendar.getInstance();
                c.setTime(today);
                c.set(Calendar.HOUR_OF_DAY, 23);
                c.set(Calendar.MINUTE, 59);
                c.set(Calendar.SECOND, 59);
                c.add( Calendar.DAY_OF_MONTH, 7 );// Subtract 6 months
                long maxDate = c.getTime().getTime(); // Twice!
                DatePickerDialog dialog = new DatePickerDialog(this, pickerListener, year, month, day);
                dialog.getDatePicker().setMinDate(today.getTime());
                dialog.getDatePicker().setMaxDate(maxDate);
                return dialog;
            case TIME_DIALOG_ID:
                //return new TimePickerDialog(this, timepicker, hour, minute, true);
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
            // TODO Auto-generated method stub
    //        hour = hourOfDay;
    //        minute = minuteOfHour;
            // Show selected date
    //        String jam = LPad(""+hour, "0", 2)+":"+LPad(""+minute,"0", 2)+":"+LPad(""+sec,"0",2);
    //        if(hour>=21||hour<=8&&minute<=59){
    //            Toast.makeText(getApplicationContext(),"Rumah Sakit sedang tutup",Toast.LENGTH_SHORT).show();
    //            txtTime.setText("");
    //        }else if(hour>=9&&minute>=0){
    //            txtTime.setText(jam);
    //        }else{
    //            txtTime.setText(jam);
    //        }
            //buat if untuk jika jam 9 pagi dan 9 malam dipilih maka keluar toast dan txtTime dihapus
    //    }
    //};

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year  = selectedYear;
            month = selectedMonth;
            day   = selectedDay;
            String tanggal = year+"-"+LPad(""+(month+1), "0", 2)+"-"+LPad(""+day, "0", 2);
            txtDate.setText(tanggal);
            Calendar d = Calendar.getInstance();
            int jam = d.get(Calendar.HOUR_OF_DAY);
            int menit = d.get(Calendar.MINUTE);
            int detik = d.get(Calendar.SECOND);
            txtTime.setText(jam+":"+menit+":"+detik);
        }
    };

    private static String LPad(String schar, String spad, int len) {
        String sret = schar;
        for (int i = sret.length(); i < len; i++) {
            sret = spad + sret;
        }
        return new String(sret);
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

}
