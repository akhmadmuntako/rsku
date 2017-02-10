package com.example.muntako;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class EditAccount extends AppCompatActivity {
    private TextView txtDate;
    EditText em, ps, nm, nt;
    Button logout, edit;
    JSONParser jsonParser = new JSONParser();
    ProgressDialog pDialog;
    SessionManager session;
    static final int DATE_PICKER_ID = 1;
    RadioGroup jk;
    String type;
    private int year, month, day;
    String email, nama, pass, id, ttl, jenis, notel, ackey, strackey, strttl, strnotel, strnama, stremail, strpassword, strid, strjenis;//edited
    private static final int DIALOG_ERROR_CONNECTION = 0;
    private static String url = Config.EDIT;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.layout.trans1, R.layout.trans2);
        setContentView(R.layout.activity_edit_account);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        session = new SessionManager(getApplicationContext());
        //int angka = session.isLoggedIn() ? 1:0;
        //Toast.makeText(getApplicationContext(),"User Login Status: " + angka, Toast.LENGTH_SHORT).show();

        HashMap<String, String> user = session.getUserDetails();

        nama = user.get(SessionManager.KEY_NAME);
        id = user.get(SessionManager.KEY_ID);
        ackey = user.get(SessionManager.KEY_ACKEY);
        email = user.get(SessionManager.KEY_EMAIL);
        ttl = user.get(SessionManager.KEY_TTL);
        notel = user.get(SessionManager.KEY_NOTEL);
        pass = user.get(SessionManager.KEY_PASS);
        jenis = user.get(SessionManager.KEY_JEKEL);
        if (jenis.equals("Laki-Laki")) {
            RadioButton rb1 = (RadioButton) findViewById(R.id.pria);
            rb1.setChecked(true);
        } else if (jenis.equals("Perempuan")) {
            RadioButton rb2 = (RadioButton) findViewById(R.id.perempuan);
            rb2.setChecked(true);
        }
        txtDate = (TextView) findViewById(R.id.ttl);
        txtDate.setFocusable(false);
        txtDate.setOnClickListener(new View.OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDialog(DATE_PICKER_ID);
            }
        });
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        jk = (RadioGroup) findViewById(R.id.jekel);
        nm = (EditText) findViewById(R.id.nama);
        em = (EditText) findViewById(R.id.email);
        ps = (EditText) findViewById(R.id.password);
        nt = (EditText) findViewById(R.id.notel);
        CheckBox cb = (CheckBox) findViewById(R.id.Cb1);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    ps.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    ps.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
        nm.setText(nama);
        em.setText(email);
        ps.setText(pass);
        nt.setText(notel);
        txtDate.setText(ttl);
        edit = (Button) findViewById(R.id.update);
        final EmailValidator emailValid = new EmailValidator();
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOnline(EditAccount.this)) {
                    showDialog(DIALOG_ERROR_CONNECTION); //displaying the created dialog.
                } else {
                    if (!emailValid.validate(em.getText().toString())) {
                        Toast.makeText(EditAccount.this, R.string.email_valid,
                                Toast.LENGTH_LONG).show();
                    } else if (nm.getText().toString().trim().length() < 1 || ps.getText().toString().trim().length() < 1
                            || nt.getText().toString().trim().length() < 1 || txtDate.getText().toString().trim().length() < 1) {
                        Toast.makeText(EditAccount.this, R.string.p_akun,
                                Toast.LENGTH_LONG).show();
                    } else {
                        new editAku().execute();
                    }
                }
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

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
            case DATE_PICKER_ID:
                return new DatePickerDialog(this, pickerListener, year, month, day);
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

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;
            String tanggal = year + "-" + LPad("" + (month + 1), "0", 2) + "-" + LPad("" + day, "0", 2);
            txtDate.setText(tanggal);

        }
    };

    private static String LPad(String schar, String spad, int len) {
        String sret = schar;
        for (int i = sret.length(); i < len; i++) {
            sret = spad + sret;
        }
        return new String(sret);
    }

    public class editAku extends AsyncTask<String, String, String> {

        String success;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditAccount.this);
            pDialog.setMessage(getResources().getString(R.string.tunggu_msg));
            pDialog.setIndeterminate(false);
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
            strnama = nm.getText().toString();
            stremail = em.getText().toString();
            strpassword = ps.getText().toString();
            strnotel = nt.getText().toString();
            strttl = txtDate.getText().toString();
            strid = id.toString();
            strackey = ackey.toString();
            strjenis = type;

            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("nama", strnama));
            nvp.add(new BasicNameValuePair("email", stremail));
            nvp.add(new BasicNameValuePair("password", strpassword));
            nvp.add(new BasicNameValuePair("ttl", strttl));
            nvp.add(new BasicNameValuePair("notel", strnotel));
            nvp.add(new BasicNameValuePair("jekel", strjenis));
            nvp.add(new BasicNameValuePair("id", strid));
            nvp.add(new BasicNameValuePair("ackey", strackey));

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

            if (success.equals("1")) {
                session.logoutUser();
                session.createLoginSession(stremail, strpassword, strnama, strttl, strnotel, strid, strackey, strjenis);//edited
                Log.e("ok", " ambil data");
                Toast.makeText(getApplicationContext(), R.string.edtsuk,
                        Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), R.string.reggag,
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public class EmailValidator {

        private Pattern pattern;
        private Matcher matcher;

        private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        public EmailValidator() {
            pattern = Pattern.compile(EMAIL_PATTERN);
        }

        public boolean validate(final String hex) {
            matcher = pattern.matcher(hex);
            return matcher.matches();
        }
    }
}