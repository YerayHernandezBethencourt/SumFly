package com.example.sumefly.entidades;

public class Transcripcion {
    private int id;
    private int idRecord;
    private String transcripcion;

    public Transcripcion(){}
    public Transcripcion(int id, int idRecord, String transcripcion) {
        this.id = id;
        this.idRecord = idRecord;
        this.transcripcion = transcripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdRecord() {
        return idRecord;
    }

    public void setIdRecord(int idRecord) {
        this.idRecord = idRecord;
    }

    public String getTranscripcion() {
        return transcripcion;
    }

    public void setTranscripcion(String transcripcion) {
        this.transcripcion = transcripcion;
    }
}
