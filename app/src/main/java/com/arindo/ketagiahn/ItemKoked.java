package com.arindo.ketagiahn;

public class ItemKoked {

    String bulan, unitup, idpel, nama, alamat, tarif, daya, koked, langkah, lembar, tagihan, biller, pbm;

    public ItemKoked(String bulan, String unitup, String idpel, String nama,
                     String alamat, String tarif, String daya, String koked, String langkah, String lembar, String tagihan, String biller, String pbm ) {
        this.bulan = bulan;
        this.unitup = unitup;
        this.idpel = idpel;
        this.nama = nama;
        this.alamat = alamat;
        this.tarif = tarif;
        this.daya = daya;
        this.koked = koked;
        this.langkah = langkah;
        this.lembar = lembar;
        this.tagihan = tagihan;
        this.biller = biller;
        this.pbm = pbm;
    }

    public String getBulan() {
        return bulan;
    }
    public String getUnitup() {
        return unitup;
    }
    public String getIdpel(){
        return idpel;
    }
    public String getNama(){
        return nama;
    }
    public String getAlamat(){
        return alamat;
    }
    public String getTarif(){
        return tarif;
    }
    public String getDaya(){
        return daya;
    }
    public String getKoked(){
        return koked;
    }
    public String getLangkah(){
        return langkah;
    }
    public String getLembar(){
        return lembar;
    }
    public String getTagihan(){
        return tagihan;
    }
    public String getBiller(){
        return biller;
    }
    public String getPbm(){
        return pbm;
    }

}

