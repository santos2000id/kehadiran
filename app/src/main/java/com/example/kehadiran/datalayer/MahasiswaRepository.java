package com.example.kehadiran.datalayer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.example.kehadiran.model.Mahasiswa;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MahasiswaRepository  {

    private DatabaseHelper dbHelper ;

    static abstract class MyColumns implements BaseColumns{
        static final String NamaTabel = "Mahasiswa";
        static final String NIM ="NIM";
        static final String Nama = "Nama_Mahasiswa";
        static final String Jurusan = "Jurusan";
        static final String JenisKelamin = "Jenis_Kelamin";
        static final String TanggalLahir = "Tanggal_Lahir";
        static final String Alamat = "Alamat";
        static final String Password = "Password";
    }

    public MahasiswaRepository(DatabaseHelper helper){
        dbHelper = helper;
    }

    //Query yang digunakan untuk membuat Tabel
    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + MyColumns.NamaTabel +
        "("+  MyColumns.NIM + " TEXT PRIMARY KEY, " +  MyColumns.Nama + " TEXT NOT NULL, " + MyColumns.Jurusan +
            " TEXT NOT NULL, " + MyColumns.JenisKelamin + " TEXT NOT NULL," + MyColumns.TanggalLahir +
            " TEXT NOT NULL, " + MyColumns.Alamat + " TEXT NOT NULL, " + MyColumns.Password +  " TEXT NOT NULL)";

    //Query yang digunakan untuk mengupgrade Tabel
    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + MyColumns.NamaTabel;


    public boolean Insert(Mahasiswa mahasiswa){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        MapMahasiswaToCV(mahasiswa, cv);
        long res = 0 ;
        res = db.insert(MyColumns.NamaTabel,null,cv);
        return res > 0 ;
    }

    private void MapMahasiswaToCV(Mahasiswa mahasiswa, ContentValues cv) {
        cv.put(MyColumns.NIM, mahasiswa.getNim());
        cv.put(MyColumns.Nama, mahasiswa.getNama());
        cv.put(MyColumns.Alamat, mahasiswa.getAlamat());
        cv.put(MyColumns.JenisKelamin, mahasiswa.getJenisKelamin());
        cv.put(MyColumns.Jurusan, mahasiswa.getJurusan());
        cv.put(MyColumns.Password, mahasiswa.getPassword());
        cv.put(MyColumns.TanggalLahir, new SimpleDateFormat("dd-MM-yyyy").format(mahasiswa.getTglLahir()));
    }

    public boolean Update(Mahasiswa mahasiswa){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        MapMahasiswaToCV(mahasiswa, cv);
        int res = 0 ;
        res = db.update(MyColumns.NamaTabel,cv,"NIM=?",new String[]{mahasiswa.getNim()});
        return res > 0 ;
    }

    public boolean Delete(Mahasiswa mahasiswa){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int res = db.delete(MyColumns.NamaTabel,MyColumns.NIM + " LIKE ?", new String[] {mahasiswa.getNim()});
        return res > 0 ;
    }

    public ArrayList<Mahasiswa> GetAll(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null ;
        ArrayList<Mahasiswa> results = new ArrayList<Mahasiswa>();

        try
        {
            cursor = db.rawQuery("Select * from Mahasiswa", null);
            while (cursor.moveToNext()){
                Mahasiswa mahasiswa = new Mahasiswa();
                MapCursorToMahasiswa(cursor, mahasiswa);
                results.add(mahasiswa);
            }



        }catch (Exception ex){

        }finally {
            cursor.close();
            return results;
        }

    }

    private void MapCursorToMahasiswa(Cursor cursor, Mahasiswa mahasiswa) throws ParseException {
        mahasiswa.setNim(cursor.getString(cursor.getColumnIndexOrThrow(MyColumns.NIM)));
        mahasiswa.setNama(cursor.getString(cursor.getColumnIndexOrThrow(MyColumns.Nama)));
        mahasiswa.setAlamat(cursor.getString(cursor.getColumnIndexOrThrow(MyColumns.Alamat)));
        mahasiswa.setJurusan(cursor.getString(cursor.getColumnIndexOrThrow(MyColumns.Jurusan)));
        mahasiswa.setJenisKelamin(cursor.getString(cursor.getColumnIndexOrThrow(MyColumns.JenisKelamin)));
        mahasiswa.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(MyColumns.Password)));
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        mahasiswa.setTglLahir(format.parse(cursor.getString(cursor.getColumnIndexOrThrow(MyColumns.TanggalLahir))));
    }

    public Mahasiswa GetOne(String nim){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null ;
        Mahasiswa mahasiswa = null;

        try
        {
            cursor = db.rawQuery("Select * from Mahasiswa Where NIM='"+nim+"'", null);
            while (cursor.moveToNext()){
                mahasiswa = new Mahasiswa();
                MapCursorToMahasiswa(cursor,mahasiswa);
            }



        }catch (Exception ex){

        }finally {
            cursor.close();
            return mahasiswa;
        }

    }
}
