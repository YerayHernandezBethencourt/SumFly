package com.example.sumefly.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sumefly.R;
import com.example.sumefly.entidades.Audio;

public class FragmentDetalleAudio extends Fragment {
    EditText editTextTranscripcionAudio;
    TextView editTextTituloAudio;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detalle_audio_fragment, container, false);
        editTextTituloAudio = view.findViewById(R.id.tvTituloDetalleAudio);
        editTextTranscripcionAudio = view.findViewById(R.id.editTextTranscripcion);
        //Crear objeto bundle para recibir el objeto recibido por argumentos
        Bundle objetoAudio = getArguments();
        //validacion para verificar si existen argumentos enviados para mostrar
        if (objetoAudio != null) {
            Audio audio = (Audio) objetoAudio.getSerializable("objeto");
            //establecer los datos en las vistas
            editTextTituloAudio.setText(audio.getId_user());
            //cambiar el getId por un getTranscription cuando toque
            editTextTranscripcionAudio.setText((CharSequence) audio.getFecha());
        }
        return view;
    }
}
