package com.example.kehadiran;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kehadiran.adapter.CheckinAdapter;
import com.example.kehadiran.model.Kehadiran;

import java.util.ArrayList;

public class ReportCheckinActivity extends AppCompatActivity {

    RecyclerView listKehadiran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_checkin);
        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        listKehadiran = findViewById(R.id.rcKehadiran);
        /*
        Menampilkan data checkin dari local db
        DatabaseHelper dbhelper = new DatabaseHelper(this);
        CheckinRepository checkinRepository = new CheckinRepository(dbhelper);
        try {
            ArrayList<Kehadiran> kehadirans = checkinRepository.GetAll();
            CheckinAdapter checkinAdapter = new CheckinAdapter(kehadirans);
            listKehadiran.setAdapter(checkinAdapter);
            listKehadiran.setLayoutManager(new LinearLayoutManager(this));
        }catch (Exception ex){
            Toast.makeText(this,ex.getMessage() + ex.getStackTrace(),Toast.LENGTH_LONG).show();
        }
        */


        GetDataCheckinAllMahasiswa();
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

    private void GetDataCheckinAllMahasiswa() {
        try {
            // Mendapatkan data checkin dari server backend via API
            GetTask task = new GetTask(this, "https://ekosantoso.xyz/SIP/Home/GetAllKehadiran",
                    new AsyncResponse() {
                        @Override
                        public void processFinish(ArrayList<Kehadiran> output) {
                            CheckinAdapter checkinAdapter = new CheckinAdapter(output);
                            listKehadiran.setAdapter(checkinAdapter);
                            listKehadiran.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        }
                    });
            task.execute();


        } catch (Exception ex) {

            Toast.makeText(this, ex.getMessage() + ex.getStackTrace(), Toast.LENGTH_LONG)
                    .show();

        }
    }


}