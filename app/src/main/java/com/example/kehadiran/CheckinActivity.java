package com.example.kehadiran;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.example.kehadiran.datalayer.CheckinRepository;
import com.example.kehadiran.datalayer.DatabaseHelper;
import com.example.kehadiran.datalayer.MahasiswaRepository;
import com.example.kehadiran.model.Kehadiran;
import com.example.kehadiran.model.Mahasiswa;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class CheckinActivity extends AppCompatActivity {
    private int PERMISSION_ID = 44;
    private FusedLocationProviderClient fusedLocationClient;
    private final double  Latitude = -6.1696902 ;
    private  final double longitude = 106.8020888;
    private CheckinRepository checkinRepository ;
    private double jarak;
    WebView web_view;
    private Location location;
    private  ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        DatabaseHelper dbhelper = new DatabaseHelper(this);
        checkinRepository = new CheckinRepository(dbhelper);

        getLastLocation();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data...");
        progressDialog.setCancelable(false);
        web_view = findViewById(R.id.webview1);
        web_view.requestFocus();
        web_view.getSettings().setJavaScriptEnabled(true);
        web_view.getSettings().setGeolocationEnabled(true);
        web_view.loadUrl("https://ekosantoso.xyz/SIP/Home/DisplayMap");
        web_view.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress < 100) {
                    progressDialog.show();
                }
                if (progress == 100) {
                    progressDialog.dismiss();
                }
            }
        });

        Button btnCheckIn = findViewById(R.id.btnCheckin);
        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                   boolean res = SaveCheckin(jarak);
                }catch (Exception ex){
                    Toast.makeText(v.getContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
                }

            }
        });
    }



    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                fusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            jarak = meterDistanceBetweenPoints((float)location.getLatitude(),(float)location.getLongitude(),(float)Latitude,(float)longitude);
                            Toast.makeText(getApplicationContext(),"Jarak Lokasi Anda dengan kampus : " + jarak + " meter",Toast.LENGTH_LONG ).show();

                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            location = locationResult.getLastLocation();
            double jarak = meterDistanceBetweenPoints((float)location.getLatitude(),(float)location.getLongitude(),(float)Latitude,(float)longitude);
            Toast.makeText(getApplicationContext(),"Jarak Lokasi Anda dengan kampus : " + jarak  + " meter",Toast.LENGTH_LONG ).show();

        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // If everything is alright then
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    private double meterDistanceBetweenPoints(float lat_a, float lng_a, float lat_b, float lng_b) {
        float pk = (float) (180.f/Math.PI);

        float a1 = lat_a / pk;
        float a2 = lng_a / pk;
        float b1 = lat_b / pk;
        float b2 = lng_b / pk;

        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
        double t3 = Math.sin(a1) * Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);

        return  Math.floor(6366000 * tt) ;
    }

    //Simpan data Checkin
    private boolean SaveCheckin(double jarak){
        boolean res = false;
        try {
            Kehadiran kehadiran = new Kehadiran();
            kehadiran.setJarak(Double.toString(jarak));
            kehadiran.setMasuk(new Date());
            kehadiran.setNim(((GlobalVariable) this.getApplication()).getNim());
            res = checkinRepository.Insert(kehadiran);
            if (res){
                PostDataCheckin(kehadiran);
                Toast.makeText(this,"Checkin berhasil",Toast.LENGTH_LONG).show();
            }else
            {
                PostDataCheckin(kehadiran);
                Toast.makeText(this,"Checkin gagal!",Toast.LENGTH_LONG).show();
            }
        }catch (Exception ex){
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG).show();
        }

        web_view.loadUrl("https://ekosantoso.xyz/SIP/Home/DisplayMap?lat=" + location.getLatitude() + "&lon=" + location.getLongitude());
        web_view.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress < 100) {
                    progressDialog.show();
                }
                if (progress == 100) {
                    progressDialog.dismiss();
                }
            }
        });

        return res ;
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

    private void PostDataCheckin(Kehadiran kehadiran) {
        try {
            // Kirim data Checkin ke server backend
            HashMap<String,String> params = new HashMap<>();
            params.put("nim", kehadiran.getNim());
            params.put("jarak", kehadiran.getJarak());
            params.put("masuk", new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(kehadiran.getMasuk()));

            new PostTask(this,"https://ekosantoso.xyz/SIP/Home/SaveKehadiran",params).execute();
        }catch (Exception ex){

            Toast.makeText(this,ex.getMessage()+ex.getStackTrace(),Toast.LENGTH_LONG)
                    .show();

        }
    }
}