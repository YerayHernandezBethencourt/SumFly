package com.example.sumefly.adapadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sumefly.R;
import com.example.sumefly.entidades.Audio;

import java.util.ArrayList;

public class ListaAudiosAdapter extends RecyclerView.Adapter<ListaAudiosAdapter.AudioViewHolder> implements View.OnClickListener{

    ArrayList<Audio> listaArrayAudios;
    LayoutInflater inflater;

    //listener
    private View.OnClickListener listener;

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_item, parent, false);
        view.setOnClickListener(this);
        return new AudioViewHolder(view);
    }

    public ListaAudiosAdapter(Context context, ArrayList<Audio> listaArrayAudios) {
        this.inflater = LayoutInflater.from(context);
        this.listaArrayAudios = listaArrayAudios;
    }

    public static class AudioViewHolder extends RecyclerView.ViewHolder {

        public EditText etNombreAudio;
        public TextView fechaAudio;
        public AudioViewHolder(@NonNull View itemView) {
            super(itemView);
            etNombreAudio = itemView.findViewById(R.id.etTitleElement);
            fechaAudio = itemView.findViewById(R.id.txtFecha);
        }
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    public void onClick(View view) {
        if(listener != null){
            listener.onClick(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder holder, int position) {
        holder.etNombreAudio.setText(listaArrayAudios.get(position).getId_user());
        holder.fechaAudio.setText(listaArrayAudios.get(position).getFecha().toString());
    }

    @Override
    public int getItemCount() {
        return listaArrayAudios.size();
    }
}
