package com.example.muntako;

/**
 * Created by Afifatul on 6/18/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Info_List_Adapter extends BaseAdapter {
    private Activity activity;
    //private ArrayList<HashMap<String, String>> data;
    private ArrayList<Info_List> data_info=new ArrayList<Info_List>();

    private static LayoutInflater inflater = null;

    public Info_List_Adapter(Activity a, ArrayList<Info_List> d) {
        activity = a; data_info = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public int getCount() {
        return data_info.size();
    }
    public Object getItem(int position) {
        return data_info.get(position);
    }
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.list_item_info, null);
        ImageView image = (ImageView) vi.findViewById(R.id.imageJSON);
        TextView id_info = (TextView) vi.findViewById(R.id.id_info);
        TextView id_gambar = (TextView) vi.findViewById(R.id.id_gambar);
        TextView judul = (TextView) vi.findViewById(R.id.judul);
        TextView tulisan = (TextView) vi.findViewById(R.id.tulisan);
        TextView waktu = (TextView) vi.findViewById(R.id.waktu);

        Info_List daftar_info = data_info.get(position);
        id_gambar.setText(daftar_info.getId_gambar());
        Picasso.with(vi.getContext())
                .load(daftar_info.getGambar())
                .placeholder(R.drawable.placeholder)   // optional
                .error(R.drawable.error)      // optional
                .resize(400,400)                        // optional
                .into(image);
        id_info.setText(daftar_info.getId_info());
        judul.setText(daftar_info.getJudul());
        tulisan.setText(daftar_info.getTulisan());
        waktu.setText(daftar_info.getWaktu());

        return vi;
    }
}

