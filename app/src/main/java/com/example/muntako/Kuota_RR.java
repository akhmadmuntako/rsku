package com.example.muntako;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
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
import java.util.List;

public class Kuota_RR extends AppCompatActivity {
    ListView list;
    private static final int DIALOG_ERROR_CONNECTION = 0;
    JSONParser2 jParser = new JSONParser2();
    SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<Kuota_List> daftar_kuota = new ArrayList<>();
    JSONArray daftarKuota = null;
    String url_read_kuota = Config.READ_KUOTA_RR;
    String id_ruang,jenis_ruang,kuota;
    // JSON Node names, ini harus sesuai yang di API
    public static final String TAG_SUCCESS = "success";
    public static final String TAG_KUOTA_RR = "kuota_rr";
    public static final String TAG_ID_RUANG = "id_ruang";
    public static final String TAG_JENIS_RUANG = "jenis_ruang";
    public static final String TAG_KUOTA = "kuota";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.layout.trans1, R.layout.trans2);
        setContentView(R.layout.activity_kuota__rr);
        list = (ListView) findViewById(R.id.listview_rr);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.sw);
        mSwipeRefreshLayout.setColorScheme(R.color.orange, R.color.green, R.color.blue);
        //jalankan ReadMhsTask
        if (!isOnline(Kuota_RR.this)) {
            showDialog(DIALOG_ERROR_CONNECTION); //displaying the created dialog.
        } else {
            ReadKuotaTask m = (ReadKuotaTask) new ReadKuotaTask().execute();
        }
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Refresh();
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int urutan, long id) {
                String id_kuota = ((TextView) view.findViewById(R.id.id_kuota)).getText().toString();
                String kelas = ((TextView) view.findViewById(R.id.kelas)).getText().toString();
                String kuota = ((TextView) view.findViewById(R.id.kuota)).getText().toString();

                if(Integer.parseInt(kuota)<=0){
                    Toast.makeText(Kuota_RR.this, getResources().getString(R.string.h_kuota), Toast.LENGTH_SHORT).show();
                }else {
                    Intent z = null;
                    z = new Intent(Kuota_RR.this, Daftar_RR.class);
                    Bundle f = new Bundle();
                    f.putString("id_kuota", id_kuota);
                    f.putString("kelas", kelas);
                    f.putString("kuota", kuota);
                    z.putExtras(f);
                    startActivity(z);
                }
            }
        });

    }

    public void Refresh(){
        if (!isOnline(Kuota_RR.this)) {
            showDialog(DIALOG_ERROR_CONNECTION); //displaying the created dialog.
        } else {
            ReadKuotaTask m = (ReadKuotaTask) new ReadKuotaTask().execute();
        }
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int urutan, long id) {
                String id_kuota = ((TextView) view.findViewById(R.id.id_kuota)).getText().toString();
                String kelas = ((TextView) view.findViewById(R.id.kelas)).getText().toString();
                String kuota = ((TextView) view.findViewById(R.id.kuota)).getText().toString();

                Intent z = null;
                z = new Intent(Kuota_RR.this, Daftar_RR.class);
                Bundle f = new Bundle();
                f.putString("id_kuota", id_kuota);
                f.putString("kelas", kelas);
                f.putString("kuota", kuota);
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

    class ReadKuotaTask extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Kuota_RR.this);
            pDialog.setMessage(getResources().getString(R.string.tunggu_msg));
            mSwipeRefreshLayout.setRefreshing(true);
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... sText) {
            String returnResult = getKuotaList(); //memanggil method getMhsList()
            return returnResult;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            mSwipeRefreshLayout.setRefreshing(false);
            if (result.equalsIgnoreCase("Exception Caught")) {
                Toast.makeText(Kuota_RR.this, getResources().getString(R.string.error_timeout), Toast.LENGTH_LONG).show();
            }else if (result.equalsIgnoreCase("no results")) {
                Toast.makeText(Kuota_RR.this, getResources().getString(R.string.no_info), Toast.LENGTH_LONG).show();
            }else{
                list.setAdapter(null);
                list.setAdapter(new Kuota_List_Adapter(Kuota_RR.this, daftar_kuota)); //Adapter menampilkan data info ke dalam listView
            }
        }


        //method untuk memperoleh daftar info dari JSON
        public String getKuotaList() {
            Kuota_List tempKuota = new Kuota_List();
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            try {
                JSONObject json = jParser.makeHttpRequest(url_read_kuota, "POST", parameter);
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) { //Ada record Data (SUCCESS = 1)
                    //Getting Array of daftar_kuota
                    daftar_kuota.clear();
                    daftarKuota = json.getJSONArray(TAG_KUOTA_RR);
                    // looping through All daftar_mhs
                    for (int i = 0; i < daftarKuota.length(); i++) {
                        JSONObject c = daftarKuota.getJSONObject(i);
                        tempKuota = new Kuota_List();
                        tempKuota.setId_kuota(c.getString(TAG_ID_RUANG));
                        tempKuota.setKelas(c.getString(TAG_JENIS_RUANG));
                        tempKuota.setKuota(c.getString(TAG_KUOTA));
                        daftar_kuota.add(tempKuota);
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
