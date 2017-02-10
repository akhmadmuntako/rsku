package com.example.muntako;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

public class Info2 extends AppCompatActivity {

    ListView list;
    private static final int DIALOG_ERROR_CONNECTION = 0;
    JSONParser2 jParser = new JSONParser2();
    SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<Info_List> daftar_info = new ArrayList<Info_List>();
    JSONArray daftarInfo = null;
    String url_read_info = Config.READ_INFO;
    // JSON Node names, ini harus sesuai yang di API
    public static final String TAG_SUCCESS = "success";
    public static final String TAG_INFO = "info";
    public static final String TAG_ID_INFO = "id_info";
    public static final String TAG_JUDUL = "judul";
    public static final String TAG_TULISAN = "tulisan";
    public static final String TAG_WAKTU = "waktu";
    public static final String TAG_GAMBAR = "gambar";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        list = (ListView) findViewById(R.id.listview_info);

            mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.sw);
            mSwipeRefreshLayout.setColorScheme(R.color.orange, R.color.green, R.color.blue);
            //jalankan ReadMhsTask
        if (!isOnline(Info2.this)) {
            showDialog(DIALOG_ERROR_CONNECTION); //displaying the created dialog.
        } else {
            ReadInfoTask m = (ReadInfoTask) new ReadInfoTask().execute();

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int urutan, long id) {
                    //dapatkan data id, nama, nim mahasiswa dan simpan dalam variable string
                    String idinfo = ((TextView) view.findViewById(R.id.id_info)).getText().toString();
                    String idgambar = ((TextView) view.findViewById(R.id.id_gambar)).getText().toString();
                    String judul = ((TextView) view.findViewById(R.id.judul)).getText().toString();
                    String tulisan = ((TextView) view.findViewById(R.id.tulisan)).getText().toString();
                    String waktu = ((TextView) view.findViewById(R.id.waktu)).getText().toString();


                    Intent i = null;
                    i = new Intent(Info2.this, Tampil_info.class);
                    Bundle b = new Bundle();
                    b.putString("id_gambar", idgambar);
                    b.putString("id_info", idinfo);
                    b.putString("judul", judul);
                    b.putString("tulisan", tulisan);
                    b.putString("waktu", waktu);
                    i.putExtras(b);
                    startActivity(i);
                }
            });
        }
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Refresh();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void Refresh(){
        if (!isOnline(Info2.this)) {
            showDialog(DIALOG_ERROR_CONNECTION); //displaying the created dialog.
        } else {
            ReadInfoTask m = (ReadInfoTask) new ReadInfoTask().execute();

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int urutan, long id) {
                    //dapatkan data id, nama, nim mahasiswa dan simpan dalam variable string

                    String idinfo = ((TextView) view.findViewById(R.id.id_info)).getText().toString();
                    String idgambar = ((TextView) view.findViewById(R.id.id_gambar)).getText().toString();
                    String judul = ((TextView) view.findViewById(R.id.judul)).getText().toString();
                    String tulisan = ((TextView) view.findViewById(R.id.tulisan)).getText().toString();
                    String waktu = ((TextView) view.findViewById(R.id.waktu)).getText().toString();

                    Intent i = null;
                    i = new Intent(Info2.this, Tampil_info.class);
                    Bundle b = new Bundle();
                    b.putString("id_gambar", idgambar);
                    b.putString("id_info", idinfo);
                    b.putString("judul", judul);
                    b.putString("tulisan", tulisan);
                    b.putString("waktu", waktu);
                    i.putExtras(b);
                    startActivity(i);
                }
            });
        }
    }

    class ReadInfoTask extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Info2.this);
            pDialog.setMessage(getResources().getString(R.string.tunggu_msg));
            mSwipeRefreshLayout.setRefreshing(true);
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... sText) {
            String returnResult = getInfoList(); //memanggil method getMhsList()
            return returnResult;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            mSwipeRefreshLayout.setRefreshing(false);
            if (result.equalsIgnoreCase("Exception Caught")) {
                Toast.makeText(Info2.this, getResources().getString(R.string.error_timeout), Toast.LENGTH_LONG).show();
            }

            if (result.equalsIgnoreCase("no results")) {
                Toast.makeText(Info2.this, getResources().getString(R.string.no_info), Toast.LENGTH_LONG).show();
            } else {
                list.setAdapter(null);
                list.setAdapter(new Info_List_Adapter(Info2.this, daftar_info)); //Adapter menampilkan data info ke dalam listView
            }
        }


        //method untuk memperoleh daftar info dari JSON
        public String getInfoList() {
            Info_List tempInfo = new Info_List();
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            try {
                JSONObject json = jParser.makeHttpRequest(url_read_info, "POST", parameter);

                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) { //Ada record Data (SUCCESS = 1)
                    //Getting Array of daftar_mhs
                    daftar_info.clear();
                    daftarInfo = json.getJSONArray(TAG_INFO);
                    // looping through All daftar_mhs
                    for (int i = 0; i < daftarInfo.length(); i++) {
                        JSONObject c = daftarInfo.getJSONObject(i);
                        tempInfo = new Info_List();
                        tempInfo.setId_info(c.getString(TAG_ID_INFO));
                        tempInfo.setJudul(c.getString(TAG_JUDUL));
                        tempInfo.setTulisan(c.getString(TAG_TULISAN));
                        tempInfo.setWaktu(c.getString(TAG_WAKTU));
                        tempInfo.setGambar(c.getString(TAG_GAMBAR));
                        tempInfo.setId_gambar(c.getString(TAG_GAMBAR));
                        daftar_info.add(tempInfo);
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
