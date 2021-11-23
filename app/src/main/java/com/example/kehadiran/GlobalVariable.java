package com.example.kehadiran;

import android.app.Application;

public class GlobalVariable extends Application {

    private String Nim;

    public String getNim() {
        return Nim;
    }

    public void setNim(String nim) {
        Nim = nim;
    }
}
