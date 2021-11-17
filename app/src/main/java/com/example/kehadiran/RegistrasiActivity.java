package com.example.kehadiran;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.kehadiran.datalayer.DatabaseHelper;
import com.example.kehadiran.datalayer.MahasiswaRepository;
import com.example.kehadiran.model.Mahasiswa;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class RegistrasiActivity extends AppCompatActivity {
    EditText nim ;
    EditText nama ;
    EditText alamat;
    EditText tgllahir;
    EditText password;
    Spinner jurusan;
    RadioButton male ;
    RadioButton female;
    private MahasiswaRepository mahasiswaRepository ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);
        getSupportActionBar().setTitle("Registrasi Awal");


        nim = findViewById(R.id.nim);
        nama = findViewById(R.id.nama);
        alamat = findViewById(R.id.alamat);
        tgllahir = findViewById(R.id.tgllahir);
        jurusan = findViewById(R.id.jurusan);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        password = findViewById(R.id.passwd);
        Button btnSimpan = findViewById(R.id.tombol1);
        DatabaseHelper dbhelper = new DatabaseHelper(this);
        mahasiswaRepository = new MahasiswaRepository(dbhelper);
        Toast.makeText(getApplication(),dbhelper.getDatabaseName(), Toast.LENGTH_SHORT)
                .show();

        tgllahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment fragment = new DatePickerFragment();
                fragment.show(getSupportFragmentManager(),"datePicker");
                fragment.setEditText(tgllahir);
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mahasiswa mahasiswa = mahasiswaRepository.GetOne(nim.getText().toString().trim());
                boolean insert = false;
                if (mahasiswa == null)
                {
                    mahasiswa = new Mahasiswa();
                    insert = true;
                }
                setData(mahasiswa);

                try {

                    boolean res;
                    if (insert)
                        res = mahasiswaRepository.Insert(mahasiswa);
                    else
                        res = mahasiswaRepository.Update(mahasiswa);
                    if (res)
                    {
                        Toast.makeText(v.getContext(),"registrasi berhasil",Toast.LENGTH_SHORT).show();
                       // Reset Form
                        nim.setText("");
                        nama.setText("");
                        alamat.setText("");
                        jurusan.setSelected(false);
                        male.setChecked(false);
                        female.setChecked(false);
                        tgllahir.setText("");
                        password.setText("");
                        startActivity(new Intent(RegistrasiActivity.this, MainActivity.class));
                        finish();


                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(v.getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }

            }
        });

        //Load Data dari Menu Profile
        String nim = ((GlobalVariable) this.getApplication()).getNim();
        if (nim != null && ! nim.isEmpty()){
            getSupportActionBar().setTitle("User Profile");
            btnSimpan.setText("Update");
            Mahasiswa mahasiswa = mahasiswaRepository.GetOne(nim);
            LoadMahasiswaToForm(mahasiswa);
        }


    }




    private void setData(Mahasiswa mahasiswa) {
        mahasiswa.setNim(nim.getText().toString().trim());
        mahasiswa.setNama(nama.getText().toString().trim());
        mahasiswa.setPassword(password.getText().toString().trim());
        mahasiswa.setAlamat(alamat.getText().toString().trim());
        mahasiswa.setJurusan(jurusan.getSelectedItem().toString());
        if (male.isChecked())
            mahasiswa.setJenisKelamin(male.getText().toString());
        else if (female.isChecked())
            mahasiswa.setJenisKelamin(female.getText().toString());
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {
            mahasiswa.setTglLahir(format.parse(tgllahir.getText().toString().trim()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //Load Form from Object Mahasiswa
    private void LoadMahasiswaToForm(Mahasiswa mahasiswa){
        nim.setText(mahasiswa.getNim());
        nama.setText(mahasiswa.getNama());
        alamat.setText(mahasiswa.getAlamat());
        String[] jurusans = getResources().getStringArray(R.array.daftar_jurusan);
        int pos = 0;
        for(int i=0;i<=jurusans.length-1;i++){
            if (jurusans[i].equals(mahasiswa.getJurusan()))
            {
                pos = i ;
                break;
            }
        }
        jurusan.setSelection(pos);
        if (mahasiswa.getJenisKelamin().equals("Pria"))
            male.setChecked(true);
        else  if (mahasiswa.getJenisKelamin().equals("Wanita"))
            female.setChecked(true);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        tgllahir.setText(format.format(mahasiswa.getTglLahir()));
    }
}