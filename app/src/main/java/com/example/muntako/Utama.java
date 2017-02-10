package com.example.muntako;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Utama extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    SessionManager session;
    String email, nama, pass, ackey;
    TextView teks1, teks2, teks5;
    ImageView iv, iv2;
    CardView btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8;
    String mapsURL = "-6.361238,106.822724";

    private static final long ANIM_VIEWPAGER_DELAY = 5000;
    private static final long ANIM_VIEWPAGER_DELAY_USER_VIEW = 10000;

    // UI References
    ImageSlideAdapter mAdapter;
    private ViewPager mViewPager;
    TextView imgNameTxt;
    PageIndicator mIndicator;

    List<Info_List> infos = new ArrayList<>();
    ReadInfoTask task;
    boolean stopSliding = false;
    private Runnable animateViewPager;
    private Handler handler;

    // JSON Node names, ini harus sesuai yang di API
    public static final String TAG_SUCCESS = "success";
    public static final String TAG_INFO = "info";
    public static final String TAG_ID_INFO = "id_info";
    public static final String TAG_JUDUL = "judul";
    public static final String TAG_TULISAN = "tulisan";
    public static final String TAG_WAKTU = "waktu";
    public static final String TAG_GAMBAR = "gambar";
    JSONArray daftarInfo = null;
    JSONParser2 jsonParser = new JSONParser2();
    String url_read_info = Config.READ_INFO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.layout.trans1, R.layout.trans2);
        setContentView(R.layout.activity_utama);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        imgNameTxt = (TextView) findViewById(R.id.img_name);

        mAdapter = new ImageSlideAdapter(Utama.this, infos);
//        if (infos == null && infos.size() == 0) {
            ReadInfoTask m = (ReadInfoTask) new ReadInfoTask().execute();
//        }

        mIndicator.setOnPageChangeListener(new PageChangeListener());
        mViewPager.setOnPageChangeListener(new PageChangeListener());
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction()) {

                    case MotionEvent.ACTION_CANCEL:
                        break;

                    case MotionEvent.ACTION_UP:
                        // calls when touch release on ViewPager
                        if (infos != null && infos.size() != 0) {
                            stopSliding = false;
                            runnable(infos.size());
                            handler.postDelayed(animateViewPager,
                                    ANIM_VIEWPAGER_DELAY_USER_VIEW);
                        }
                        break;

                    case MotionEvent.ACTION_MOVE:
                        // calls when ViewPager touch
                        if (handler != null && stopSliding == false) {
                            stopSliding = true;
                            handler.removeCallbacks(animateViewPager);
                        }
                        break;
                }
                return false;
            }
        });

        session = new SessionManager(getApplicationContext());
        //Toast.makeText(getApplicationContext(),"User Login Status: " + session.isLoggedIn(), Toast.LENGTH_SHORT).show();
        if (session.isLoggedIn() == false) {
            login();
        }

        teks5 = (TextView) findViewById(R.id.teks5);
        iv = (ImageView) findViewById(R.id.logo5);
        iv2 = (ImageView) findViewById(R.id.logo8);
        btn1 = (CardView) findViewById(R.id.button1);
        btn2 = (CardView) findViewById(R.id.button2);
        btn3 = (CardView) findViewById(R.id.button3);
        btn4 = (CardView) findViewById(R.id.button4);
        btn5 = (CardView) findViewById(R.id.button5);
        btn6 = (CardView) findViewById(R.id.button6);
        btn7 = (CardView) findViewById(R.id.button7);
        btn8 = (CardView) findViewById(R.id.button8);

        Picasso.with(getApplicationContext())
                .load("http://maps.google.com/maps/api/staticmap?center=" + mapsURL + "&zoom=16&size=400x300&sensor=false")
                .placeholder(R.drawable.lokasi)   // optional
                .into(iv);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(Utama.this, Info2.class);
                startActivity(a);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent z = new Intent(Utama.this, Daftar.class);
                startActivity(z);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent y = new Intent(Utama.this, Kuota_RR.class);
                startActivity(y);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent f = new Intent(Utama.this, Jadwal_Dokter.class);
                startActivity(f);
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent f = new Intent(Utama.this, Antrian.class);
                startActivity(f);
            }
        });
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent f = new Intent(Utama.this, Bantuan.class);
                startActivity(f);
            }
        });
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent f = null;
                if (session.isLoggedIn() == true) {
                    f = new Intent(Utama.this, MyAccount.class);
                } else {
                    f = new Intent(Utama.this, Login.class);
                }
                startActivity(f);
            }
        });
        if (session.isLoggedIn() == true) {
            HashMap<String, String> user = session.getUserDetails();
            String jk = user.get(SessionManager.KEY_JEKEL);
            if (jk.equalsIgnoreCase("perempuan")) {
                iv2.setImageResource(R.drawable.account_cewe);
            }

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mViewPager.setAdapter(mAdapter);
        mIndicator.setViewPager(mViewPager);

        runnable(infos.size());
        //Re-run callback
        handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (session.isLoggedIn() == true) {
            HashMap<String, String> user = session.getUserDetails();
            nama = user.get(SessionManager.KEY_NAME);
            //id = user.get(SessionManager.KEY_ID);
            email = user.get(SessionManager.KEY_EMAIL);
            pass = user.get(SessionManager.KEY_PASS);
            ackey = user.get(SessionManager.KEY_ACKEY);

            //TODO ubah dokter jadi pasien
            if (ackey.equalsIgnoreCase("Dokter")) {
//                if(ackey.equals("Pasien")) {
                //Toast.makeText(getApplicationContext(), "You Login As Doctor", Toast.LENGTH_SHORT).show();
                btn5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(getApplicationContext(), "Kepencet dokter", Toast.LENGTH_SHORT).show();
                        Intent a = new Intent(Utama.this, Data_Pasien.class);
                        startActivity(a);
                    }
                });
                iv.setImageResource(R.drawable.data_patient);
                teks5.setText(R.string.tombol51);
            } else {
                //Toast.makeText(getApplicationContext(), "You Login As Patient", Toast.LENGTH_SHORT).show();
                btn5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO EDIT LOKASI GPS
                        //Toast.makeText(getApplicationContext(), "Kepencet pasien", Toast.LENGTH_SHORT).show();
                        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + mapsURL);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    }
                });
                iv.setImageResource(R.drawable.lokasi);
                teks5.setText(R.string.tombol5);
            }
        }
        mAdapter = new ImageSlideAdapter(Utama.this, infos);
        if (infos == null && infos.size() == 0) {
            ReadInfoTask m = (ReadInfoTask) new ReadInfoTask().execute();
        } else {
            mViewPager.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            mIndicator.setViewPager(mViewPager);

            runnable(infos.size());
            //Re-run callback
            handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
        }
        if(infos == null){

        }else {
            mViewPager.setAdapter(mAdapter);
            mIndicator.setViewPager(mViewPager);

            runnable(infos.size());
            //Re-run callback
            handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            exit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.utama, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_user) {
            if (session.isLoggedIn() == true) {
                Intent a = new Intent(Utama.this, EditAccount.class);
                startActivity(a);
            } else {
                Intent b = new Intent(Utama.this, Login.class);
                startActivity(b);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void exit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.logout_msg);
        builder.setIcon(R.drawable.logo2);
        builder
                .setCancelable(false)//tidak bisa tekan tombol back
                        //jika pilih yes
                .setPositiveButton(R.string.ya, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                        //jika pilih no
                .setNegativeButton(R.string.tidak, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();
    }

    private void login() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.login_msg);
        builder.setIcon(R.drawable.logo1);
        builder.setCancelable(false)//tidak bisa tekan tombol back
                        //jika pilih yes
                .setPositiveButton(R.string.ya, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent l = new Intent(Utama.this, Login.class);
                        startActivity(l);
                    }
                })
                        //jika pilih no
                .setNegativeButton(R.string.lewati, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do nothing
                    }
                }).show();
    }

    public void runnable(final int size) {
        handler = new Handler();
        animateViewPager = new Runnable() {
            public void run() {
                if (!stopSliding) {
                    if (mViewPager.getCurrentItem() == size - 1) {
                        mViewPager.setCurrentItem(0);
                    } else {
                        mViewPager.setCurrentItem(
                                mViewPager.getCurrentItem() + 1, true);
                    }
                    handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
                }
            }
        };
    }

    @Override
    public void onPause() {
        if (task != null)
            task.cancel(true);
        if (handler != null) {
            //Remove callback
            handler.removeCallbacks(animateViewPager);
        }
        super.onPause();
    }

    private class PageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                if (infos != null) {
                    imgNameTxt.setText(""
                            + ((Info_List) infos.get(mViewPager
                            .getCurrentItem())).getJudul());
                }
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
        }
    }

    class ReadInfoTask extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... sText) {
            String returnResult = getInfoList(); //memanggil method getInfoList()
            return returnResult;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            mAdapter.notifyDataSetChanged();

        }


        //method untuk memperoleh daftar info dari JSON
        public String getInfoList() {
            Info_List tempInfo = new Info_List();
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            try {
                JSONObject json = jsonParser.makeHttpRequest(url_read_info, "POST", parameter);

                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) { //Ada record Data (SUCCESS = 1)
                    //Getting Array of daftar_mhs
                    infos.clear();
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
                        infos.add(tempInfo);
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

}
