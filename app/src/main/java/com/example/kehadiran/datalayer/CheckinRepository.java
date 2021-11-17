package com.example.kehadiran.datalayer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.example.kehadiran.model.Kehadiran;
import com.example.kehadiran.model.Mahasiswa;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CheckinRepository {

    private DatabaseHelper dbHelper ;

    static abstract class MyColumns implements BaseColumns{
        static final String NamaTabel = "Kehadiran";
        static final String NIM ="NIM";
        static final String Masuk = "Masuk";
        static final String Jarak = "Jarak";

    }

    public  CheckinRepository(DatabaseHelper helper){
        dbHelper = helper;
    }
    //Query yang digunakan untuk membuat Tabel
    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + MyColumns.NamaTabel +
        "("+  MyColumns.NIM + " TEXT NOT NULL , " +  MyColumns.Masuk + " TEXT NOT NULL, " + MyColumns.Jarak +
            " TEXT NOT NULL , PRIMARY KEY( " + MyColumns.NIM + "," + MyColumns.Masuk + "))";

    //Query yang digunakan untuk mengupgrade Tabel
    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + MyColumns.NamaTabel;



    public boolean Insert(Kehadiran kehadiran){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        MapKehadiranToCV(kehadiran, cv);
        long res = 0 ;
        res = db.insert(MyColumns.NamaTabel,null,cv);
        return res > 0 ;
    }

    private void MapKehadiranToCV(Kehadiran kehadiran, ContentValues cv) {
        cv.put(MyColumns.NIM, kehadiran.getNim());
        cv.put(MyColumns.Jarak, kehadiran.getJarak());
        cv.put(MyColumns.Masuk, new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(kehadiran.getMasuk()));
    }


    public boolean Delete(Kehadiran kehadiran){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int res = db.delete(MyColumns.NamaTabel, MyColumns.NIM + " LIKE ?", new String[] { kehadiran.getNim()});
        return res > 0 ;
    }

    public ArrayList<Kehadiran> GetAll(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null ;
        ArrayList<Kehadiran> results = new ArrayList<Kehadiran>();

        try
        {
            cursor = db.rawQuery("Select * from Kehadiran", null);
            while (cursor.moveToNext()){
                Kehadiran kehadiran = new Kehadiran();
                MapCursorToKehadiran(cursor, kehadiran);
                results.add(kehadiran);
            }

        }catch (Exception ex){

        }finally {
            cursor.close();
            return results;
        }

    }

    private void MapCursorToKehadiran(Cursor cursor, Kehadiran kehadiran) throws ParseException {
        kehadiran.setNim(cursor.getString(cursor.getColumnIndexOrThrow(MyColumns.NIM)));
        kehadiran.setJarak(cursor.getString(cursor.getColumnIndexOrThrow(MyColumns.Jarak)));
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        kehadiran.setMasuk(format.parse(cursor.getString(cursor.getColumnIndexOrThrow(MyColumns.Masuk))));
    }


}
