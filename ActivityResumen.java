package com.example.sumefly;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
public class ActivityResumen extends AppCompatActivity {
    private static final String TAG = "ActivityResumen";
    private EditText resumen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen);
        resumen = findViewById(R.id.etResumen);

        // Obtiene el nombre del archivo del Intent
        Intent intent = getIntent();
        String fileName2 = intent.getStringExtra("fileName2");

        // Inicia la tarea asincrónica para obtener el contenido del archivo
        new GetSummaryClass().execute(fileName2);
    }

    public class GetSummaryClass extends AsyncTask<String, Void, String> {
        private static final String TAG = "GetSummaryClass";
        private String fileName2;

        @Override
        protected String doInBackground(String... params) {
            fileName2 = params[0];
            Log.d(TAG, "El archivo se llama: " + fileName2);

            try {
                FirebaseStorage storage = FirebaseStorage.getInstance("gs://sumefly.appspot.com");
                StorageReference storageRef = storage.getReference();
                StorageReference fileRef = storageRef.child(fileName2 + "-transcription_tokenizado_resumen.txt");

                int maxRetries = 5;
                int retryDelayMs = 15000;
                int retryCount = 0;

                while (retryCount < maxRetries) {
                    try {
                        // Obtener la URL pública del archivo
                        Uri downloadUrl = Tasks.await(fileRef.getDownloadUrl());

                        // Abrir una conexión URL y crear un lector para el archivo de texto
                        URL fileUrl = new URL(downloadUrl.toString());
                        BufferedReader reader = new BufferedReader(new InputStreamReader(fileUrl.openStream(), StandardCharsets.UTF_8));

                        StringBuilder stringBuilder = new StringBuilder();
                        String line;

                        // Leer línea por línea y construir el contenido del archivo
                        while ((line = reader.readLine()) != null) {
                            stringBuilder.append(line);
                        }

                        // Cerrar el lector
                        reader.close();

                        Log.d(TAG, "Resumen terminado con éxito!");
                        // Devolver el contenido del archivo como una cadena de texto
                        return stringBuilder.toString();
                    } catch (Exception e) {
                        Log.e(TAG, "Error al obtener el contenido del archivo", e);
                        e.printStackTrace();

                        // Incrementar el contador de intentos de lectura
                        retryCount++;

                        // Esperar antes de realizar el siguiente intento de lectura
                        try {
                            Thread.sleep(retryDelayMs);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                Log.e(TAG, "No se pudo obtener el contenido del archivo después de " + maxRetries + " intentos");
            } catch (Exception e) {
                Log.e(TAG, "Error al obtener la referencia al archivo", e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                // Muestra el contenido del archivo en el EditText
                resumen.setText(result);
            } else {
                // Maneja el caso de error
                resumen.setText("Error al obtener el contenido del archivo.");
            }
        }
    }
}