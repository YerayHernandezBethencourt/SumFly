package com.example.sumefly.database;

import android.content.Context;

import com.google.firebase.firestore.FirebaseFirestore;

public class DBAudio {

    Context context;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public DBAudio(Context context) {
        this.context = context;
    }

    private void postAudio(String id_user, byte[] audio) {

    }

    //public long insertarAudio(int id_user,  byte[] audio, String fecha){
    //    long id = 0;
    //    try{
    //        DbHelper dbHelper = new DbHelper(context);
    //        SQLiteDatabase db = dbHelper.getWritableDatabase(); //SE QUEDA AQUI
    //        ContentValues values = new ContentValues();
    //        values.put("id_user", id_user);
    //        values.put("audio", audio);
    //        db.insert(TABLE_RECORDS, null, values);
    //        id = db.insert(TABLE_RECORDS, null, values);
    //        System.out.println("ID: " + id);
    //    }catch(Exception ex){
    //        ex.toString();
    //    }
    //    return id;
    //}

    //public ArrayList<Audios> mostrarAudios(){
    //    DbHelper dbHelper = new DbHelper(context);
    //    SQLiteDatabase db = dbHelper.getWritableDatabase();
    //    ArrayList<Audios> listaAudios = new ArrayList<>();
    //    Audios audios = null;
    //    Cursor cursorAudio = null;
    //    cursorAudio = db.rawQuery("SELECT title, fehcha FROM " + TABLE_RECORDS, null);
    //    if(cursorAudio.moveToFirst()){
    //        do{
    //            audios = new Audios();
    //            audios.setId(cursorAudio.getInt(0));
    //            audios.setId_record(cursorAudio.getString(1));
    //            audios.setId_user(cursorAudio.getInt(2));
    //            audios.setTitle(cursorAudio.getString(3));
    //            audios.setAudio(cursorAudio.getString(4));
    //            audios.setFecha(Timestamp.valueOf(cursorAudio.getString(5)));
    //            listaAudios.add(audios);
    //        }while(cursorAudio.moveToNext());
    //    }
    //    cursorAudio.close();
    //    return listaAudios;
    //}
}
