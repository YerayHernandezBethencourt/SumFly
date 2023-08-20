package com.example.sumefly.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.sumefly.entidades.Transcripcion;

import java.util.ArrayList;

public class DbTranscripcion extends DbHelper {
    Context context;

    public DbTranscripcion(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    public long insertarTranscripcion(int id_record, String transcripcion) {
        long id = 0;
        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put("id_record", id_record);
            values.put("transcription", transcripcion);

            id = db.insert(TABLE_TRANSCRIPTIONS, null, values);
            System.out.println("ID: " + id);
        } catch (Exception ex) {
            ex.toString();
        }
        return id;
    }

    public ArrayList<Transcripcion> mostrarTranscripciones() {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ArrayList<Transcripcion> listaTranscripciones = new ArrayList<>();
        Transcripcion transcripcion = null;
        Cursor cursorTranscripcion = null;
        cursorTranscripcion = db.rawQuery("SELECT id_transcription, id_record, transcription FROM " + TABLE_TRANSCRIPTIONS, null);
        if (cursorTranscripcion.moveToFirst()) {
            do {
                transcripcion = new Transcripcion();
                transcripcion.setId(cursorTranscripcion.getInt(0));
                transcripcion.setIdRecord(cursorTranscripcion.getInt(1));
                transcripcion.setTranscripcion(cursorTranscripcion.getString(2));
                listaTranscripciones.add(transcripcion);
            } while (cursorTranscripcion.moveToNext());
        }
        cursorTranscripcion.close();
        return listaTranscripciones;
    }
}


