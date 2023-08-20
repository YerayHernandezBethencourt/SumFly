package com.example.sumefly;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.sumefly.entidades.Audio;
import com.example.sumefly.fragments.FragmentDetalleAudio;
import com.example.sumefly.fragments.FragmentListaAudios;
import com.example.sumefly.interfaces.iComunicarFragments;

import java.io.IOException;
import java.io.Serializable;

public abstract class DirectorioGrabaciones extends AppCompatActivity implements iComunicarFragments {
    LinearLayout linearLayout;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    FragmentDetalleAudio detalleAudioFragment;
    Button btnPlay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_lista_audios);

        linearLayout = findViewById(R.id.linearLayoutFragment);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        FragmentListaAudios fragmentListaAudios = new FragmentListaAudios();
        fragmentTransaction.add(linearLayout.getId(), fragmentListaAudios);
        fragmentTransaction.commit();

    }


    public void enviarAudio(Audio audio) {
        detalleAudioFragment = new FragmentDetalleAudio();
        Bundle bundleEnvio = new Bundle();
        bundleEnvio.putSerializable("objeto", (Serializable) audio);
        detalleAudioFragment.setArguments(bundleEnvio);
        //abrimos el fragment
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.linearLayoutFragment, detalleAudioFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void play_song(View view){
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource("dataSource");
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}