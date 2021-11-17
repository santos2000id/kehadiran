package com.example.kehadiran.model;

import java.util.Date;

public class Mahasiswa {

    private String nim ;
    private String nama;
    private String Alamat;
    private Date tglLahir ;
    private String jurusan;
    private String jenisKelamin;



    private String password;


    public Date getTglLahir() {
        return tglLahir;
    }

    public String getAlamat() {
        return Alamat;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public String getJurusan() {
        return jurusan;
    }

    public String getNama() {
        return nama;
    }

    public String getNim() {
        return nim;
    }

    public void setAlamat(String alamat) {
        Alamat = alamat;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public void setJurusan(String jurusan) {
        this.jurusan = jurusan;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public void setTglLahir(Date tglLahir) {
        this.tglLahir = tglLahir;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

