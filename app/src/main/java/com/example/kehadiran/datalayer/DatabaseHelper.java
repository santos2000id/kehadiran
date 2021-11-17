package com.example.kehadiran.datalayer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String NamaDatabase = "hadir.db";
    private static final int VersiDatabase = 1;


    public DatabaseHelper(Context context) {
        super(context,NamaDatabase,null,VersiDatabase);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MahasiswaRepository.SQL_CREATE_ENTRIES);
        db.execSQL(CheckinRepository.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(MahasiswaRepository.SQL_DELETE_ENTRIES);
        db.execSQL(CheckinRepository.SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
