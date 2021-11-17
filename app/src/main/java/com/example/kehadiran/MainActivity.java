package com.example.kehadiran;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kehadiran.datalayer.DatabaseHelper;
import com.example.kehadiran.datalayer.MahasiswaRepository;
import com.example.kehadiran.model.Mahasiswa;

public class MainActivity extends AppCompatActivity {

    EditText username,password ;
    Button btnlogin ;
    MahasiswaRepository mahasiswaRepository;
    TextView register ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         username = findViewById(R.id.username);
         password = findViewById(R.id.password);
         btnlogin = findViewById(R.id.btnlogin);
         register = findViewById(R.id.registrasi);
         DatabaseHelper dbhelper = new DatabaseHelper(this);
         mahasiswaRepository = new MahasiswaRepository(dbhelper);

         btnlogin.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                    if (ValidasiLogin(username.getText().toString().trim(),password.getText().toString().trim())){
                        Toast.makeText(v.getContext(),"Login Berhasil",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(v.getContext(), MainMenuActivity.class);
                        v.getContext().startActivity(intent);
                        ((Activity)v.getContext()).finish();
                    }else{
                        Toast.makeText(v.getContext(),"Login gagal!",Toast.LENGTH_LONG).show();
                    }
             }
         });

         register.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(v.getContext(), RegistrasiActivity.class);
                 startActivity(intent);
             }
         });
    }


    private boolean ValidasiLogin(String username,String password){
        boolean result = false;
        Mahasiswa mahasiswa = mahasiswaRepository.GetOne(username);
        if (mahasiswa != null){
            if (mahasiswa.getPassword().equals(password))
            {
                ((GlobalVariable) this.getApplication()).setNim(username);
                result = true ;
            }
        }

        return result;
    }
}