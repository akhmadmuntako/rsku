package com.example.muntako;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by EBIITUEVI on 19/04/2016.
 */
public class Parser_antrian extends AsyncTask<Void,Integer,Integer>{
    private static final int NOTIFICATION_ID = 0;
    Context c;
    ListView lv;
    String data;
    SessionManager session;
    String nama,notifnama,a,status,jam;
    String ai="";


    ArrayList<String> players = new ArrayList<>();
    ProgressDialog pd ;

    public Parser_antrian(Context c, String data, ListView lv) {
        this.c = c;
        this.data = data;
        this.lv = lv;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd= new ProgressDialog(c);
        pd.setMessage(c.getString(R.string.tunggu_msg));
        pd.show();
    }

    @Override
    protected Integer doInBackground(Void... params) {
        return this.parse();
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        if(integer==1){
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(c,android.R.layout.simple_list_item_1,players);
            lv.setAdapter(adapter);
            if(ai=="") {

            }else{
                //Toast.makeText(c, ai , Toast.LENGTH_SHORT).show();
                notif();
            }
        }else{
            players.add(c.getResources().getString(R.string.no_antrian));
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(c,android.R.layout.simple_list_item_1,players);
            lv.setAdapter(adapter);
        }
        pd.dismiss();
    }

    private int parse()
    {
        try{

            JSONArray ja = new JSONArray(data);

            JSONObject jo = null;

            players.clear();
            session = new SessionManager(c);
            HashMap<String, String> user = session.getUserDetails();
            notifnama = user.get(SessionManager.KEY_NAME);
            for(int i=0;i<ja.length();i++){
                jo=ja.getJSONObject(i);
                a = ""+(i+1);
                nama = jo.getString("nama").toUpperCase();
                status = jo.getString("status");
                jam = jo.getString("jam");
                players.add("\nNo. Antrian : "+a+"\n\n"+nama+"\n\n== "+ status +" ==\n\n"+ jam +"\n");
                if(jo.getString("nama").equals(notifnama)){
                    ai=String.valueOf(i);
                }
            }
            return 1;
        }catch(JSONException e){
            e.printStackTrace();
        }
        return 0 ;
    }

    public void notif(){
        NotificationManager mNotificationManager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        //Build the notification using Notification.Builder
        String longText="";
        if(Integer.valueOf(ai).equals(0)){
            longText="Silahkan langsung masuk ke ruang pemeriksaan";
        }else if(Integer.valueOf(ai).equals(1)){
            longText="Sesaat lagi giliran anda masuk ke ruang pemeriksaan";
        }else if(Integer.valueOf(ai)>1){
            longText="Saat ini ada "+ai+" orang menunggu antrian termasuk anda";
        }
        Intent intent = new Intent(c, Antrian.class);
        PendingIntent pIntent = PendingIntent.getActivity(c, (int) System.currentTimeMillis(), intent, 0);
        int notifyID = 1;
        Notification.Builder builder = new Notification.Builder(c)
                .setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setContentTitle("Status Antrian "+notifnama)
                .addAction(0,"Klik untuk lihat antrian", pIntent)
                .setStyle(new Notification.BigTextStyle().bigText(longText))
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setDefaults(Notification.DEFAULT_ALL)
                .setNumber(++notifyID);
        //Show the notification
        mNotificationManager.notify(notifyID, builder.build());
    }
}
