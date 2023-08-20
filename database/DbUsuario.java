package com.example.sumefly.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.sumefly.entidades.Usuarios;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class DbUsuario extends DbHelper{

    Context context;

    public DbUsuario(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    public long insertarContacto(String email, String password){
        long id = 0;
        try{
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.beginTransaction();
            ContentValues values = new ContentValues();
            values.put("email", email);
            values.put("password", password);
            db.insert(TABLE_USER, null, values);
            id = db.insert(TABLE_USER, null, values);

        }catch(Exception ex){
            ex.toString();
        }
        return id;
    }
    public ArrayList<Usuarios> mostrarUsuarios(){
        ArrayList<Usuarios> listaUsuarios = new ArrayList<>();
        try{
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String[] columnas = {COLUMN_ID, COLUMN_EMAIL, COLUMN_PASSWORD};
            Cursor cursor = db.query(TABLE_USER, columnas, null, null, null, null, null);

            while(cursor.moveToNext()){
                Usuarios usuario = new Usuarios();
                usuario.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                usuario.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
                usuario.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)));
                listaUsuarios.add(usuario);
            }
            cursor.close();
            db.close();

        }catch(Exception ex){
            ex.toString();
        }
        return listaUsuarios;
    }
    //Metodo para comprobar si los datos introducidos en el login son correctos
    @SuppressLint("Range")
    public int comprobarContacto(String email, String password){
        int idContacto =-1;
        try{
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String[] columnas = {COLUMN_ID};
            String selection = COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?";
            String[] selectionArgs = {email, password};

            Cursor cursor = db.query(TABLE_USER, columnas, selection, selectionArgs, null, null, null);

            if(cursor.moveToFirst()){
                idContacto = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            }
            cursor.close();
            db.close();

        }catch(Exception ex){
            ex.toString();
        }
        return idContacto;
    }
    public int obtenerIdUsuario(){
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int id = 0;
        Cursor cursor = null;
        cursor = db.rawQuery("SELECT id FROM " + TABLE_USER, null);
        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(0);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return id;
    }
}
