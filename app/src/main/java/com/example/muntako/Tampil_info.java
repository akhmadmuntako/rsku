package com.example.muntako;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Tampil_info extends AppCompatActivity {

    EditText EditTxtJudul, EditTxtTulisan, EditTxtWaktu;
    TextView judul, konten, waktu;
    ImageView ImageJSON;
    TextView TxtViewId;
    String namaStr, nimStr, idStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.layout.trans1, R.layout.trans2);
        setContentView(R.layout.activity_tampil_info);
        judul = (TextView)findViewById(R.id.textViewJudul);
        konten = (TextView)findViewById(R.id.textViewKonten);
        waktu = (TextView)findViewById(R.id.textViewWaktu);
        ImageJSON = (ImageView) findViewById(R.id.imageJSON);
        TxtViewId = (TextView) findViewById(R.id.id_info);

        Bundle b = getIntent().getExtras();
        String isi_id_gambar = b.getString("id_gambar");
        String isi_id_info = b.getString("id_info");
        String isi_judul= b.getString("judul");
        String isi_tulisan= b.getString("tulisan");
        String isi_waktu= b.getString("waktu");
        //meng-set bundle tersebut
        Picasso.with(Tampil_info.this)
                .load(isi_id_gambar)
                .placeholder(R.drawable.placeholder)   // optional
                .error(R.drawable.error)      // optional
                .into(ImageJSON);
        judul.setText(isi_judul);
        konten.setText(isi_tulisan);
        waktu.setText(isi_waktu);
        TxtViewId.setText(isi_id_info);
    }
}
