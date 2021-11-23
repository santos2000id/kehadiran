package com.example.kehadiran;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.kehadiran.datalayer.DatabaseHelper;
import com.example.kehadiran.datalayer.MahasiswaRepository;
import com.example.kehadiran.model.Mahasiswa;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class MainMenuActivity extends AppCompatActivity {

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view1);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_account:
                        Intent intent = new Intent(getApplicationContext(), RegistrasiActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_absen:
                        Intent intent2 = new Intent(getApplicationContext(), CheckinActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.nav_settings:
                        Intent intent3 = new Intent(getApplicationContext(), ReportCheckinActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.nav_logout:
                        ((GlobalVariable) getApplication()).setNim("");
                        Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent1);
                        MainMenuActivity.this.finish();
                        break;
                }
                return true;
            }
        });

        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView txtSelamat = findViewById(R.id.txtselamat);
        String nim = ((GlobalVariable) getApplication()).getNim();
        txtSelamat.setText("Selamat datang " + nim);


        PostDataMahasiswa();

    }

    private void PostDataMahasiswa() {
        try {
            // Kirim data mahasiswa ke server backend
            DatabaseHelper dbhelper = new DatabaseHelper(this);
            MahasiswaRepository mahasiswaRepository = new MahasiswaRepository(dbhelper);
            ArrayList<Mahasiswa> mahasiswas = mahasiswaRepository.GetAll();
            HashMap<String, String> params = new HashMap<>();
            for (Mahasiswa mahasiswa :
                    mahasiswas) {
                params.put("nim", mahasiswa.getNim());
                params.put("nama", mahasiswa.getNama());
                params.put("alamat", mahasiswa.getAlamat());
                params.put("tglLahir", new SimpleDateFormat("dd-MM-yyyy").format(mahasiswa.getTglLahir()));
                params.put("jurusan", mahasiswa.getJurusan());
                params.put("jenisKelamin", mahasiswa.getJenisKelamin());
                params.put("password", mahasiswa.getPassword());
            }
            new PostTask(this, "https://ekosantoso.xyz/SIP/Home/SaveMahasiswa", params).execute();
        } catch (Exception ex) {

            Toast.makeText(this, ex.getMessage() + ex.getStackTrace(), Toast.LENGTH_LONG)
                    .show();

        }
    }

    // override the onOptionsItemSelected()
    // function to implement
    // the item click listener callback
    // to open and close the navigation
    // drawer when the icon is clicked
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);


    }


}