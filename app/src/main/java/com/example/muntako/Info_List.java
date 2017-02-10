package com.example.muntako;

/**
 * Created by Afifatul on 6/18/2015.
 */
public class Info_List {

    private String id_info;
    private String judul;
    private String tulisan;
    private String waktu;
    private String gambar;
    private String id_gambar;

    public String getId_gambar() {
        return id_gambar;
    }

    public void setId_gambar(String id_gambar) {
        this.id_gambar = id_gambar;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getId_info() {
        return id_info;
    }

    public void setId_info(String id_info) {
        this.id_info = id_info;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getTulisan() {
        return tulisan;
    }

    public void setTulisan(String tulisan) {
        this.tulisan = tulisan;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }
}
