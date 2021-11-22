package com.example.kehadiran;

import com.example.kehadiran.model.Kehadiran;

import java.util.ArrayList;

public interface AsyncResponse {
    void processFinish(ArrayList<Kehadiran> output);
}