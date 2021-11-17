package com.example.kehadiran;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.kehadiran.adapter.CheckinAdapter;
import com.example.kehadiran.datalayer.CheckinRepository;
import com.example.kehadiran.datalayer.DatabaseHelper;
import com.example.kehadiran.model.Kehadiran;

import java.util.ArrayList;

public class ReportCheckinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_checkin);
        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        RecyclerView listKehadiran = findViewById(R.id.rcKehadiran);
        DatabaseHelper dbhelper = new DatabaseHelper(this);
        CheckinRepository checkinRepository = new CheckinRepository(dbhelper);
        try {
            ArrayList<Kehadiran> kehadirans = checkinRepository.GetAll();
            CheckinAdapter checkinAdapter = new CheckinAdapter(kehadirans);
            listKehadiran.setAdapter(checkinAdapter);
        }catch (Exception ex){
            Toast.makeText(this,ex.getMessage() + ex.getStackTrace(),Toast.LENGTH_LONG).show();
        }


        listKehadiran.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}