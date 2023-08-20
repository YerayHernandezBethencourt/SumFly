package com.example.sumefly.entidades;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;

public class Audio {
    private String id_record;
    @ServerTimestamp
    private Date fecha;
    private String id_user;
    private List<Integer> audio;

    // Agrega aquí los constructores, getters y setters necesarios
    // ...

    public String getId_record() {
        return id_record;
    }

    public void setId_record(String id_record) {
        this.id_record = id_record;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public List<Integer> getAudio() {
        return audio;
    }

    public void setAudio(List<Integer> audio) {
        this.audio = audio;
    }

    // Crea un constructor vacío para Firestore
    public Audio() {
        // Constructor vacío requerido por Firestore
    }
}
