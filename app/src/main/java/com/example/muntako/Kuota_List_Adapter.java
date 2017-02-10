package com.example.muntako;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by EBIITUEVI on 08/05/2016.
 */
public class Kuota_List_Adapter extends BaseAdapter {

    private Activity activity;
    //private ArrayList<HashMap<String, String>> data;
    private ArrayList<Kuota_List> data_kuota=new ArrayList<Kuota_List>();

    private static LayoutInflater inflater = null;

    public Kuota_List_Adapter(Activity a, ArrayList<Kuota_List> d) {
        activity = a; data_kuota = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public int getCount() {
        return data_kuota.size();
    }
    public Object getItem(int position) {
        return data_kuota.get(position);
    }
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.list_item_kuota, null);
        TextView id_kuota = (TextView)vi.findViewById(R.id.id_kuota);
        TextView kelas = (TextView)vi.findViewById(R.id.kelas);
        TextView kuota = (TextView)vi.findViewById(R.id.kuota);

        Kuota_List daftar_kuota = data_kuota.get(position);
        id_kuota.setText(daftar_kuota.getId_kuota());
        kelas.setText(daftar_kuota.getKelas());
        kuota.setText(daftar_kuota.getKuota());
        return vi;
    }

}
