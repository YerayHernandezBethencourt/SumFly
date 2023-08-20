package com.example.sumefly;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sumefly.adapadores.AudioAdapter;
import com.example.sumefly.entidades.Audio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListaAudiosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AudioAdapter audioAdapter;
    private List<Audio> audioList;
    private boolean estado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_audios);

        // Inicializar RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar lista de audios
        audioList = new ArrayList<>();

        // Inicializar adaptador
        audioAdapter = new AudioAdapter(audioList);
        audioAdapter.setOnItemClickListener(this::onItemClick);

        // Asignar adaptador al RecyclerView
        recyclerView.setAdapter(audioAdapter);

        // Obtener la lista de audios grabados (puedes obtenerla desde Firebase o desde cualquier otra fuente de datos)
        // y agregarlos a la lista audioList

        // Ejemplo de cómo agregar un audio a la lista
        Audio audio = new Audio();
        audio.setId_record("audio1");
        // Agregar más propiedades del audio si es necesario
        audioList.add(audio);

        // Notificar al adaptador que los datos han cambiado
        audioAdapter.notifyDataSetChanged();
    }

    public void onItemClick(Audio audio) {
        // Reproducir el audio cuando se hace clic en el elemento del RecyclerView

        // Aquí debes implementar la lógica para reproducir el audio utilizando MediaPlayer o cualquier otra biblioteca de reproducción de audio
        // Puedes utilizar la ruta del archivo de audio en el objeto Audio para obtener el archivo y reproducirlo
        String rutaAudio = getExternalFilesDir(null).getAbsolutePath() + "/" + ".mp3";

        // Ejemplo de cómo reproducir el audio utilizando MediaPlayer
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            if(estado) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = new MediaPlayer();
            }
            mediaPlayer.setDataSource(rutaAudio);
            mediaPlayer.prepare();
            mediaPlayer.start();
            estado = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}