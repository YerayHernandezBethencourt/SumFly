package com.example.sumefly.fragments;

import static com.example.sumefly.R.id.recyclerViewListaAudios;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sumefly.R;
import com.example.sumefly.adapadores.ListaAudiosAdapter;
import com.example.sumefly.entidades.Audio;
import com.example.sumefly.interfaces.iComunicarFragments;

import java.util.ArrayList;

public class FragmentListaAudios extends Fragment {
    ArrayList<Audio> listaArrayAudios;
    ListaAudiosAdapter listaAudiosAdapter;
    RecyclerView recyclerViewAudios;

    //para el detalle del audio
    Activity activity;
    iComunicarFragments interfaceComunicaFragments;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vistaAudios = inflater.inflate(R.layout.fragment_lista_audios, container, false);
        recyclerViewAudios = vistaAudios.findViewById(recyclerViewListaAudios);
        listaArrayAudios = new ArrayList<com.example.sumefly.entidades.Audio>();
        //cargar la lista de audios
        //cargarListaAudios();
        //mostrar la lista de audios
        mostrarListaAudios();

        return vistaAudios;
    }

    /*
    private void cargarListaAudios() {
        listaArrayAudios.add(new Audios("Audio 1", "2020-12-20 12:00:00"));
        listaArrayAudios.add(new Audios("Audio 2", "2020-12-20 12:10:00"));
        listaArrayAudios.add(new Audios("Audio 3", "2020-12-20 12:20:00"));
        listaArrayAudios.add(new Audios("Audio 1", "2020-12-20 12:00:00"));
        listaArrayAudios.add(new Audios("Audio 2", "2020-12-20 12:10:00"));
        listaArrayAudios.add(new Audios("Audio 3", "2020-12-20 12:20:00"));
        listaArrayAudios.add(new Audios("Audio 1", "2020-12-20 12:00:00"));
        listaArrayAudios.add(new Audios("Audio 2", "2020-12-20 12:10:00"));
        listaArrayAudios.add(new Audios("Audio 3", "2020-12-20 12:20:00"));
        listaArrayAudios.add(new Audios("Audio 1", "2020-12-20 12:00:00"));
        listaArrayAudios.add(new Audios("Audio 2", "2020-12-20 12:10:00"));
        listaArrayAudios.add(new Audios("Audio 3", "2020-12-20 12:20:00"));
        listaArrayAudios.add(new Audios("Audio 1", "2020-12-20 12:00:00"));
        listaArrayAudios.add(new Audios("Audio 2", "2020-12-20 12:10:00"));
        listaArrayAudios.add(new Audios("Audio 3", "2020-12-20 12:20:00"));

    }

     */

    private void mostrarListaAudios() {
        recyclerViewAudios.setLayoutManager(new LinearLayoutManager(getContext()));
        listaAudiosAdapter = new ListaAudiosAdapter(getContext(), listaArrayAudios);
        recyclerViewAudios.setAdapter(listaAudiosAdapter);

        listaAudiosAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombreAudio = listaArrayAudios.get(recyclerViewAudios.getChildAdapterPosition(view)).getId_user();
                String fechaAudio = String.valueOf(listaArrayAudios.get(recyclerViewAudios.getChildAdapterPosition(view)).getFecha());
                Toast.makeText(getContext(), "Seleccionó: " + nombreAudio + " " + fechaAudio, Toast.LENGTH_SHORT).show();
                //Enviamos el objeto
                interfaceComunicaFragments.enviarAudio(listaArrayAudios.get(recyclerViewAudios.getChildAdapterPosition(view)));
            }
        });
    }

    //Para comunicación con el fragment de reproducción de audio
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.activity = (Activity) context;
            interfaceComunicaFragments = (iComunicarFragments) this.activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
