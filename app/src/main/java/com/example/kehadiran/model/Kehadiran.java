package com.example.kehadiran.model;

import java.util.Date;

public class Kehadiran {

    private String nim ;
    private Date masuk ;
    private String jarak;

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public Date getMasuk() {
        return masuk;
    }

    public void setMasuk(Date masuk) {
        this.masuk = masuk;
    }

    public String getJarak() {
        return jarak;
    }

    public void setJarak(String jarak) {
        this.jarak = jarak;
    }
}
