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
 * Created by EBIITUEVI on 13/05/2016.
 */
public class Pasien_List_Adapter extends BaseAdapter {

    private Activity activity;
    //private ArrayList<HashMap<String, String>> data;
    private ArrayList<Pasien_List> data_pasien=new ArrayList<Pasien_List>();

    private static LayoutInflater inflater = null;

    public Pasien_List_Adapter(Activity a, ArrayList<Pasien_List> d) {
        activity = a; data_pasien = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public int getCount() {
        return data_pasien.size();
    }
    public Object getItem(int position) {
        return data_pasien.get(position);
    }
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.list_item_pasien, null);
        TextView id_pasien = (TextView)vi.findViewById(R.id.id_pasien);
        TextView jenis = (TextView)vi.findViewById(R.id.jenis_ruang);
        TextView ruangan = (TextView)vi.findViewById(R.id.ruangan);
        TextView nama = (TextView)vi.findViewById(R.id.nama);
        TextView umur = (TextView)vi.findViewById(R.id.umur);
        TextView jk = (TextView)vi.findViewById(R.id.jekel);
        TextView diag = (TextView)vi.findViewById(R.id.diagnosa);
        TextView obat = (TextView)vi.findViewById(R.id.obat);
        TextView hr = (TextView)vi.findViewById(R.id.hr);
        TextView abp = (TextView)vi.findViewById(R.id.abp);
        TextView spo2 = (TextView)vi.findViewById(R.id.spo2);
        TextView dokter = (TextView)vi.findViewById(R.id.dokter);
        TextView update = (TextView)vi.findViewById(R.id.last_update);
        TextView progress = (TextView)vi.findViewById(R.id.progress);

        Pasien_List daftar_pasien = data_pasien.get(position);
        id_pasien.setText(daftar_pasien.getId_pasien());
        jenis.setText(daftar_pasien.getJenis_ruang());
        ruangan.setText(daftar_pasien.getRuangan());
        nama.setText(daftar_pasien.getNama());
        umur.setText(daftar_pasien.getUmur());
        jk.setText(daftar_pasien.getJekel());
        diag.setText(daftar_pasien.getDiagnosa());
        obat.setText(daftar_pasien.getObat());
        hr.setText(daftar_pasien.getHr());
        abp.setText(daftar_pasien.getAbp());
        spo2.setText(daftar_pasien.getSpo2());
        dokter.setText(daftar_pasien.getDokter());
        update.setText(daftar_pasien.getLast_update());
        progress.setText(daftar_pasien.getProgress());
        return vi;
    }
}
