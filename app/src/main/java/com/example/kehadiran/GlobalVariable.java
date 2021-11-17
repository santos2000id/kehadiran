package com.example.kehadiran;

import android.app.Application;

public class GlobalVariable extends Application {

    public String getNim() {
        return Nim;
    }

    public void setNim(String nim) {
        Nim = nim;
    }

    private String Nim ;
}
