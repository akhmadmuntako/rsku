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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Data_Pasien extends AppCompatActivity {
    ListView list;
    private static final int DIALOG_ERROR_CONNECTION = 0;
    JSONParser2 jParser = new JSONParser2();
    SwipeRefreshLayout mSwipeRefreshLayout;
    String nama_dokter;
    ArrayList<Pasien_List> daftar_pasien = new ArrayList<Pasien_List>();
    JSONArray daftarPasien = null;
    String url_read_pasien;
    // JSON Node names, ini harus sesuai yang di API
    public static final String TAG_SUCCESS = "success";
    public static final String TAG_PASIEN = "pasien";
    public static final String TAG_ID_PASIEN = "id_pasien_dokter";
    public static final String TAG_JENIS = "jenis_ruang";
    public static final String TAG_RUANG = "ruangan";
    public static final String TAG_NAMA = "nama";
    public static final String TAG_UMUR = "umur";
    public static final String TAG_JK = "jk";
    public static final String TAG_DIAG = "diagnosa";
    public static final String TAG_OBAT = "obat";
    public static final String TAG_HR = "hr";
    public static final String TAG_ABP = "abp";
    public static final String TAG_SPO2 = "spo2";
    public static final String TAG_DOKTER = "dokter";
    public static final String TAG_UPDATE = "update_terakhir";
    public static final String TAG_PROGRESS = "progress";
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_pasien);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        overridePendingTransition(R.layout.trans1, R.layout.trans2);
        session = new SessionManager(getApplicationContext());
        if(session.isLoggedIn()==true) {
            HashMap<String, String> user = session.getUserDetails();
            nama_dokter = user.get(SessionManager.KEY_NAME);
            //Toast.makeText(Data_Pasien.this, nama_dokter, Toast.LENGTH_SHORT).show();
            url_read_pasien = Config.READ_PASIEN+"?dokter="+nama_dokter.replace(" ","%20");
        }
        list = (ListView) findViewById(R.id.listview_data_pasien);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.sw);
        mSwipeRefreshLayout.setColorScheme(R.color.orange, R.color.green, R.color.blue);
        if (!isOnline(Data_Pasien.this)) {
            showDialog(DIALOG_ERROR_CONNECTION); //displaying the created dialog.
        } else {
            ReadPasien m = (ReadPasien) new ReadPasien().execute();
        }
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Refresh();
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String id_pasien = ((TextView)view.findViewById(R.id.id_pasien)).getText().toString();
                String jenis = ((TextView)view.findViewById(R.id.jenis_ruang)).getText().toString();
                String ruangan = ((TextView)view.findViewById(R.id.ruangan)).getText().toString();
                String nama = ((TextView)view.findViewById(R.id.nama)).getText().toString();
                String umur = ((TextView)view.findViewById(R.id.umur)).getText().toString();
                String jk = ((TextView)view.findViewById(R.id.jekel)).getText().toString();
                String diag = ((TextView)view.findViewById(R.id.diagnosa)).getText().toString();
                String obat = ((TextView)view.findViewById(R.id.obat)).getText().toString();
                String hr = ((TextView)view.findViewById(R.id.hr)).getText().toString();
                String abp = ((TextView)view.findViewById(R.id.abp)).getText().toString();
                String spo2 = ((TextView)view.findViewById(R.id.spo2)).getText().toString();
                String dokter = ((TextView)view.findViewById(R.id.dokter)).getText().toString();
                String update = ((TextView)view.findViewById(R.id.last_update)).getText().toString();
                String progress = ((TextView)view.findViewById(R.id.progress)).getText().toString();

                Intent z = null;
                z = new Intent(Data_Pasien.this, Tampil_Pasien.class);
                Bundle f = new Bundle();
                f.putString("id_pasien", id_pasien);
                f.putString("jenis", jenis);
                f.putString("ruangan", ruangan);
                f.putString("nama", nama);
                f.putString("umur", umur);
                f.putString("jk", jk);
                f.putString("diag", diag);
                f.putString("obat", obat);
                f.putString("hr", hr);
                f.putString("abp", abp);
                f.putString("spo2", spo2);
                f.putString("dokter", dokter);
                f.putString("update", update);
                f.putString("progress", progress);
                z.putExtras(f);
                startActivity(z);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Refresh();
    }

    public void Refresh() {
        if (!isOnline(Data_Pasien.this)) {
            showDialog(DIALOG_ERROR_CONNECTION); //displaying the created dialog.
        } else {
             ReadPasien m = (ReadPasien) new ReadPasien().execute();
        }
    }
    class ReadPasien extends AsyncTask<String, Void, String>{
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Data_Pasien.this);
            pDialog.setMessage(getResources().getString(R.string.tunggu_msg));
            mSwipeRefreshLayout.setRefreshing(true);
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String returnResult = getPasienList(); //memanggil method getMhsList()
            return returnResult;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            mSwipeRefreshLayout.setRefreshing(false);
            if (result.equalsIgnoreCase("Exception Caught")) {
                Toast.makeText(Data_Pasien.this, getResources().getString(R.string.error_timeout), Toast.LENGTH_LONG).show();
            }

            if (result.equalsIgnoreCase("no results")) {
                Toast.makeText(Data_Pasien.this, getResources().getString(R.string.no_pasien), Toast.LENGTH_LONG).show();
            } else {
                list.setAdapter(null);
                list.setAdapter(new Pasien_List_Adapter(Data_Pasien.this, daftar_pasien)); //Adapter menampilkan data info ke dalam listView
            }
        }

        public String getPasienList() {
            Pasien_List tempPasien = new Pasien_List();
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            try {
                JSONObject json = jParser.makeHttpRequest(url_read_pasien, "POST", parameter);
                //JSONObject json = jParser.getJSONFromUrl(url_read_pasien);

                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) { //Ada record Data (SUCCESS = 1)
                    //Getting Array of daftar_mhs
                    daftar_pasien.clear();
                    daftarPasien = json.getJSONArray(TAG_PASIEN);
                    // looping through All daftar_mhs
                    for (int i = 0; i < daftarPasien.length(); i++) {
                        JSONObject c = daftarPasien.getJSONObject(i);
                        tempPasien = new Pasien_List();
                        tempPasien.setId_pasien(c.getString(TAG_ID_PASIEN));
                        tempPasien.setJenis_ruang(c.getString(TAG_JENIS));
                        tempPasien.setRuangan(c.getString(TAG_RUANG));
                        tempPasien.setNama(c.getString(TAG_NAMA));
                        tempPasien.setUmur(c.getString(TAG_UMUR));
                        tempPasien.setJekel(c.getString(TAG_JK));
                        tempPasien.setDiagnosa(c.getString(TAG_DIAG));
                        tempPasien.setObat(c.getString(TAG_OBAT));
                        tempPasien.setHr(c.getString(TAG_HR));
                        tempPasien.setAbp(c.getString(TAG_ABP));
                        tempPasien.setSpo2(c.getString(TAG_SPO2));
                        tempPasien.setDokter(c.getString(TAG_DOKTER));
                        tempPasien.setLast_update(c.getString(TAG_UPDATE));
                        tempPasien.setProgress(c.getString(TAG_PROGRESS));
                        daftar_pasien.add(tempPasien);
                    }
                    return "OK";
                } else {
                    //Tidak Ada Record Data (SUCCESS = 0)
                    return "no results";
                }

            } catch (Exception e) {
                e.printStackTrace();
                return "Exception Caught";
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
}
