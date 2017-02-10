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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Tampil_Pasien extends AppCompatActivity {
    EditText jenis,ruangan,nama,umur,jk,diag,obat,hr,abp,spo2,progress;
    private static final int DIALOG_ERROR_CONNECTION = 0;
    TextView id_pasien,dokter,update;
    JSONParser2 jParser = new JSONParser2();
    String url_update_pas= Config.UPDATE_PASIEN;
    String url_delete_mhs= Config.DELETE_PASIEN;
    String strid,strdiag,strobat,strhr,strabp,strspo2,strprogress;
    Button btn_update,btn_delete;
    public static final String TAG_SUCCESS = "success";
    public static final String TAG_ID_PASIEN = "id_pasien";
    public static final String TAG_DIAGNOSA = "diagnosa";
    public static final String TAG_OBAT = "obat";
    public static final String TAG_HR = "hr";
    public static final String TAG_ABP = "abp";
    public static final String TAG_SPO2 = "spo2";
    public static final String TAG_PROGRESS = "progress";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.layout.trans1, R.layout.trans2);
        setContentView(R.layout.activity_tampil_pasien);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        id_pasien = (TextView)findViewById(R.id.id_pasien);
        jenis = (EditText)findViewById(R.id.jenis_ruang);
        ruangan = (EditText)findViewById(R.id.ruangan);
        nama = (EditText)findViewById(R.id.nama);
        umur = (EditText)findViewById(R.id.umur);
        jk = (EditText)findViewById(R.id.jekel);
        diag = (EditText)findViewById(R.id.diagnosa);
        obat = (EditText)findViewById(R.id.obat);
        hr = (EditText)findViewById(R.id.hr);
        abp = (EditText)findViewById(R.id.abp);
        spo2 = (EditText)findViewById(R.id.spo2);
        dokter = (TextView)findViewById(R.id.dokter);
        update = (TextView)findViewById(R.id.update);
        progress = (EditText)findViewById(R.id.progress);
        btn_update = (Button)findViewById(R.id.btn_update);
        btn_delete = (Button)findViewById(R.id.btn_delete);


        Bundle f = getIntent().getExtras();
        id_pasien.setText(f.getString("id_pasien"));
        jenis.setText(f.getString("jenis"));
        ruangan.setText(f.getString("ruangan"));
        nama.setText(f.getString("nama"));
        umur.setText(f.getString("umur"));
        jk.setText(f.getString("jk"));
        diag.setText(f.getString("diag"));
        obat.setText(f.getString("obat"));
        hr.setText(f.getString("hr"));
        abp.setText(f.getString("abp"));
        spo2.setText(f.getString("spo2"));
        dokter.setText(f.getString("dokter"));
        update.setText(f.getString("update"));
        progress.setText(f.getString("progress"));
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strid = id_pasien.getText().toString();
                strdiag = diag.getText().toString();
                strobat = obat.getText().toString();
                strhr = hr.getText().toString();
                strabp = abp.getText().toString();
                strspo2 = spo2.getText().toString();
                strprogress = progress.getText().toString();
                if (!isOnline(Tampil_Pasien.this)) {
                    showDialog(DIALOG_ERROR_CONNECTION); //displaying the created dialog.
                } else {
                    if (strdiag.trim().length() == 0 || strhr.trim().length() == 0 || strabp.trim().length() == 0 ||
                            strspo2.trim().length() == 0 || strprogress.trim().length() == 0) {
                        Toast.makeText(Tampil_Pasien.this, getResources().getString(R.string.p_akun), Toast.LENGTH_SHORT).show();
                    } else {
                        UpdatePasienTask u = (UpdatePasienTask) new UpdatePasienTask().execute();
                    }
                }
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strid = id_pasien.getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(Tampil_Pasien.this);
                builder.setTitle(R.string.hapus_msg);
                builder.setCancelable(true);
                builder.setPositiveButton(R.string.ya, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!isOnline(Tampil_Pasien.this)) {
                            showDialog(DIALOG_ERROR_CONNECTION); //displaying the created dialog.
                        } else {
                            DeletePasienTask u = (DeletePasienTask) new DeletePasienTask().execute();
                        }
                    }
                });
                builder.setNegativeButton(R.string.tidak, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();
            }
        });

    }

    class UpdatePasienTask extends AsyncTask<String, Void, String>
    {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Tampil_Pasien.this);
            pDialog.setMessage(getResources().getString(R.string.tunggu_msg));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... sText) {

            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            parameter.add(new BasicNameValuePair(TAG_ID_PASIEN, strid));
            parameter.add(new BasicNameValuePair(TAG_DIAGNOSA, strdiag));
            parameter.add(new BasicNameValuePair(TAG_OBAT, strobat));
            parameter.add(new BasicNameValuePair(TAG_HR, strhr));
            parameter.add(new BasicNameValuePair(TAG_ABP, strabp));
            parameter.add(new BasicNameValuePair(TAG_SPO2, strspo2));
            parameter.add(new BasicNameValuePair(TAG_PROGRESS, strprogress));

            try {
                JSONObject json = jParser.makeHttpRequest(url_update_pas,"POST", parameter);

                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {

                    return "OK";
                }
                else {

                    return "FAIL";

                }

            } catch (Exception e) {
                e.printStackTrace();
                return "Exception Caught";
            }

        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            pDialog.dismiss();

            if(result.equalsIgnoreCase("Exception Caught"))
            {

                Toast.makeText(Tampil_Pasien.this, getResources().getString(R.string.error_timeout), Toast.LENGTH_LONG).show();
            }

            if(result.equalsIgnoreCase("FAIL"))
            {
                Toast.makeText(Tampil_Pasien.this, getResources().getString(R.string.upgag), Toast.LENGTH_LONG).show();
            }

            else {
                Toast.makeText(Tampil_Pasien.this, R.string.upsuk, Toast.LENGTH_LONG).show();
                finish();
            }

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

    class DeletePasienTask extends AsyncTask<String, Void, String>
    {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Tampil_Pasien.this);
            pDialog.setMessage(getResources().getString(R.string.tunggu_msg));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... sText) {

            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            parameter.add(new BasicNameValuePair(TAG_ID_PASIEN, strid));

            try {
                JSONObject json = jParser.makeHttpRequest(url_delete_mhs,"POST", parameter);

                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    return "OK";
                }
                else {
                    return "FAIL";
                }

            } catch (Exception e) {
                e.printStackTrace();
                return "Exception Caught";
            }

        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            pDialog.dismiss();
            if(result.equalsIgnoreCase("Exception Caught"))
            {

                Toast.makeText(Tampil_Pasien.this, getResources().getString(R.string.error_timeout), Toast.LENGTH_LONG).show();
            }

            if(result.equalsIgnoreCase("FAIL"))
            {
                Toast.makeText(Tampil_Pasien.this, getResources().getString(R.string.upgag), Toast.LENGTH_LONG).show();
            }

            else {
                Toast.makeText(Tampil_Pasien.this, R.string.hapsuk, Toast.LENGTH_LONG).show();
                Intent i = new Intent(Tampil_Pasien.this,Data_Pasien.class);
                startActivity(i);
            }

        }
    }
}
