package com.example.sumefly.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.format.DateTimeFormatter;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1; //incrementar al reailzar cambios en la base de datos
    private static final String DATABASE_NAME = "sumefly.db";
    public static final String TABLE_USER = "t_usuarios";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOMBRE = "nombre";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";

    public static final String TABLE_RECORDS = "t_records";
    public static final String COLUMN_ID_RECORD = "id_record";
    public static final String COLUMN_ID_USER = "id_user";
    public static final String AUDIO_TITLE = "title";
    public static final String AUDIO = "audio";
    public static final String FECHA = "fecha";

    public static final String TABLE_TRANSCRIPTIONS = "t_transcriptions";
    public static final String COLUMN_ID_TRANSCRIPTION = "id_transcription";
    public static final String COLUMN_ID_RECORD_TRANSCRIPTION = "id_record";
    public static final String TRANSCRIPTION = "transcription";

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_USER + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_EMAIL + " TEXT NOT NULL," +
                COLUMN_PASSWORD + " TEXT NOT NULL" +
                ");"
        );
        db.execSQL("CREATE TABLE " + TABLE_RECORDS + "(" +
                COLUMN_ID_RECORD + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_ID_USER + " INTEGER NOT NULL," +
                AUDIO_TITLE + " TEXT NOT NULL," +
                AUDIO + " BLOB NOT NULL," +
                FECHA + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (" + COLUMN_ID_USER + ") REFERENCES " +
                TABLE_USER + "(" + COLUMN_ID + ")" +
                ");"
        );
        db.execSQL("CREATE TABLE " + TABLE_TRANSCRIPTIONS + "(" +
                COLUMN_ID_TRANSCRIPTION + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_ID_RECORD_TRANSCRIPTION + " INTEGER NOT NULL," +
                TRANSCRIPTION + " TEXT NOT NULL," +
                "FOREIGN KEY (" + COLUMN_ID_RECORD_TRANSCRIPTION + ") REFERENCES " +
                TABLE_RECORDS + "(" + COLUMN_ID_RECORD + ")" +
                ");"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSCRIPTIONS);
        onCreate(db);

    }
}
