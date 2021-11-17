package com.example.kehadiran.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kehadiran.R;
import com.example.kehadiran.datalayer.CheckinRepository;
import com.example.kehadiran.model.Kehadiran;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CheckinAdapter extends RecyclerView.Adapter<CheckinAdapter.ViewHolder> {

    private ArrayList<Kehadiran> kehadirans;
    private Context context ;


    public CheckinAdapter(ArrayList<Kehadiran> kehadiranList){
        kehadirans = kehadiranList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView Nim,Masuk,Jarak;
        private ImageButton Overflow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Mendapatkan Context dari itemView yang terhubung dengan Activity ViewData
            context = itemView.getContext();
            Nim = itemView.findViewById(R.id.NIM);
            Masuk = itemView.findViewById(R.id.masuk);
            Jarak = itemView.findViewById(R.id.jarak);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_checkin,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CheckinAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Kehadiran Kehadiran = kehadirans.get(position);
        holder.Nim.setText("NIM : " + Kehadiran.getNim());
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        String jamMasuk = df.format( Kehadiran.getMasuk());
        holder.Masuk.setText("Absen Masuk : " + jamMasuk);
        holder.Jarak.setText("Jarak : " + Kehadiran.getJarak());

    }

    @Override
    public int getItemCount() {
        return kehadirans.size();
    }
}

