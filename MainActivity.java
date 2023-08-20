package com.example.sumefly;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Handler;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_PERMISSIONS_CODE = 100;

    private MediaRecorder grabadora = null;
    private File file = null;
    private int estado = 0;     //0: no grabando, 1: grabando, 2: pausado
    private String ruta = null;

    private ImageView imgGrabar = null;
    private ImageView imgDetener = null;
    private ImageView imgPausar = null;

    private Dialog dialogEspera;
    private static final int TIEMPO_ESPERA = 10000; // 10 segundos de espera
    private Handler handler;

    private boolean isRecording = false;

    private Chronometer crono;
    private long tiempo = 0;
    private int ordenLogico = 0;

    //Prueba Firebase
    //private  FirebaseFirestore mFirestore;
    private String email;
    private String idUsuario;
    private String fileName;
    private String fileName2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //mFirestore = FirebaseFirestore.getInstance();
        email = getIntent().getStringExtra("email");

        //Estas lineas son para que se ejecute antes de usar cualquier otro SDK de Firebase
        FirebaseApp.initializeApp(this);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
                PlayIntegrityAppCheckProviderFactory.getInstance());

        crono = findViewById(R.id.crono);
        imgGrabar = findViewById(R.id.imgGrabar);
        imgDetener = findViewById(R.id.imgDetener);
        imgPausar = findViewById(R.id.imgPausar);

        // Verifica si los permisos están concedidos
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Solicita los permisos
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            Log.d(TAG, "Solicitud de permisos");
        } else {
            // Los permisos ya están concedidos, puedes realizar las acciones que requieren los permisos
            System.out.println("Los permisos ya están concedidos, puedes realizar las acciones que requieren los permisos");
        }

        imgDetener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detenerGrabacion(v);
            }
        });
        imgGrabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grabar(v);
            }
        });
        imgPausar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pausar( v);
            }
        });
    }
    //------------- METODOS PRINCIPALES ------------------//
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            // Verifica si los permisos fueron concedidos por el usuario
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Los permisos fueron concedidos
                System.out.println("Permisos concedidos");
            } else {
                // Los permisos fueron denegados
                System.out.println("Permisos denegados");
            }
        }
    }

    //Método para comenzar la grabacion
    private void grabar(View view) {
        //Si no hay una grabacion en curso
        if (grabadora == null) {
            if (estado == 0) {
                //Se cambia el estado a grabando
                estado = 1;
                //Se crea una nueva grabadora
                grabadora = new MediaRecorder();
                //Se le asigna la ruta en firebase donde se guardará el archivo
                //File rutaTemporal = new File(getCacheDir(), "temporal.mp4");
                ruta = getExternalFilesDir(null).getAbsolutePath() + "/" + ".mp4";
                //Se le asigna el microfono como fuente de audio
                grabadora.setAudioSource(MediaRecorder.AudioSource.MIC);
                //Se le asigna el formato de salida
                grabadora.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                //Se le asigna la ruta donde se guardara el archivo
                grabadora.setOutputFile(ruta);                            //prueba de las 18:43. Cambiando la ruta.
                //Se le asigna otro lugar donde se guardara el archivo(DIRECTORY_MUSIC)
                //grabadora.setOutputFile(getRecordingFilePath());          //prueba de las 18:43. Cambiando la ruta.
                //Se le asigna el codificador de audio
                grabadora.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                //Se prepara la grabadora
                try {
                    grabadora.prepare();
                    grabadora.start();  //Comienza la grabacion
                    isRecording = true;
                    Log.d(TAG, "La grabación de audio ha comenzado. Guardando en: " + ruta);
                } catch (IOException e) {
                    Log.e(TAG, "Fallo en grabación al comenzar");
                    e.printStackTrace();
                }
                isRecording = false;
                //Se reinicia el cronometro
                crono.setBase(SystemClock.elapsedRealtime() - tiempo);
                //Comienza el cronometro
                crono.start();
            } else if (estado == 2) {
                //Se reanuda la grabacion
                grabadora.resume();
                //Se reanuda el cronometro
                crono.start();
                //Se cambia el estado a grabando
                estado = 1;
            }

            //Se oscurece la imagen del boton de grabar
            imgGrabar.setImageResource(R.drawable.image_play2);
            //Se acalara la imagen del boton de pausar
            imgPausar.setImageResource(R.drawable.pause_image);
            //Se aclara el boton de detener
            imgDetener.setImageResource(R.drawable.stop_image);

            //Se deshabilita el boton de pausar
            imgGrabar.setEnabled(false);
            //Se habilita el boton de detener
            imgDetener.setEnabled(true);
            //Se habilita el botón de pausar
            imgPausar.setEnabled(true);
            //Se muestra un mensaje
            Toast.makeText(this, "Grabando...", Toast.LENGTH_SHORT).show();
        } else if (grabadora != null && estado == 2) {
            //Se cambia el estado a grabando
            estado = 1;
            //Se continua la grabacion
            grabadora.resume();
            //Comienza el cronometro
            crono.start();
            //Continua el cronómetro
            crono.setBase(SystemClock.elapsedRealtime() - tiempo);
            //Se oscurece la imagen del boton de grabar
            imgGrabar.setImageResource(R.drawable.image_play2);
            //Se acalara la imagen del boton de pausar
            imgPausar.setImageResource(R.drawable.pause_image);
            //Se aclara el boton de detener
            imgDetener.setImageResource(R.drawable.stop_image);

            //Se deshabilita el boton de grabar
            imgGrabar.setEnabled(false);
            //Se habilita el boton de detener
            imgDetener.setEnabled(true);
            //Se habilita el botón de pausar
            imgPausar.setEnabled(true);
            //Se muestra un mensaje
            Toast.makeText(this, "Continuando la grabacion", Toast.LENGTH_SHORT).show();
        }
    }

    //Metodo para pausar la grabacsetImageResourceion
    private void pausar(View view) {
        //Si hay una grabacion en curso
        if (grabadora != null) {
            //Se cambia el estado a pausado
            estado = 2;
            //Se pausa la grabacion
            grabadora.pause();
            //Se oscurece la imagen del boton de pausar
            imgPausar.setImageResource(R.drawable.image_pause2);
            //Se aclara la imagen del boton play
            imgGrabar.setImageResource(R.drawable.play_image);
            //Se habilita el boton de grabar
            imgGrabar.setEnabled(true);
            //Se deshabilita el boton de pausar
            imgPausar.setEnabled(false);
            //Se pausa el cronómetro
            crono.stop();
            //Se guarda el tiempo actual
            tiempo = SystemClock.elapsedRealtime() - crono.getBase();
            //Se muestra un mensaje
            Toast.makeText(this, "Pausado...", Toast.LENGTH_SHORT).show();
        }
    }

    //Metodo para detener la grabacion
    private void detenerGrabacion(View view) {
        if (grabadora != null) {
            detenerGrabacionAudio();
            liberarGrabadora();
            guardarArchivo();
            enviarArchivoAudio(ruta);
            byte[] audioEnBytes = new byte[0];
            try {
                audioEnBytes = convertirAudio(ruta);
            } catch (IOException e) {
                e.printStackTrace();
            }
            configurarBotonesDetener();
            detenerCronometro();
            reiniciarCronometro();
            establecerEstadoDetenido();
            mostrarMensajeGrabacionFinalizada();
            //Ya no vamos a la lista de audios
            //Intent intent = new Intent(this, ListaAudiosActivity.class);
            //startActivity(intent);
            showLoadingDialog();
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideLoadingDialog();

                    Intent intent = new Intent(MainActivity.this, ActivityResumen.class);
                    intent.putExtra("fileName2", fileName2);
                    System.out.println("El nombre del archivo es: " + fileName2);
                    startActivity(intent);
                }
            },TIEMPO_ESPERA);
        }
    }
//------------- FIN METODOS PRINCIPALES ------------------//

    private void detenerGrabacionAudio() {
        try {
            isRecording = true;
            Log.d(TAG, "La grabación de audio se ha detenido correctamente");
        } catch (Exception e) {
            Log.e(TAG, "Fallo en grabación" + e.getMessage());
        }
        grabadora.stop();
        isRecording = false;
    }

    private void guardarArchivo() {
        File sourceFile = new File(ruta);
        File destinationFile = new File(getCacheDir(), "grabacion.mp4"); // Ruta de destino para guardar el archivo

        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;

        try {
            inputStream = new FileInputStream(sourceFile);
            outputStream = new FileOutputStream(destinationFile);

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            Log.d(TAG, "Archivo guardado en: " + destinationFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void liberarGrabadora() {
        grabadora.reset();
        grabadora.release();
        grabadora = null;
        Log.d(TAG, "Grabadora liberada");
    }

    private void detenerCronometro() {
        crono.stop();
    }

    private void reiniciarCronometro() {
        crono.setBase(SystemClock.elapsedRealtime());
        tiempo = 0;
    }

    private void establecerEstadoDetenido() {
        estado = 0;
    }

    private void mostrarMensajeGrabacionFinalizada() {
        Toast.makeText(this, "Grabacion finalizada", Toast.LENGTH_SHORT).show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu:
                losRegistros();
                return true;
            case R.id.menu2:
                nuevaGrabacion();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void losRegistros() {
        Intent intent = new Intent(this, DirectorioGrabaciones.class);
        startActivity(intent);
    }

    public void nuevaGrabacion() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void configurarBotonesDetener() {
        imgGrabar.setImageResource(R.drawable.play_image);
        imgPausar.setImageResource(R.drawable.pause_image);
        imgDetener.setImageResource(R.drawable.stop_image2);
        imgGrabar.setEnabled(true);
        imgPausar.setEnabled(false);
        imgDetener.setEnabled(false);
    }

    private byte[] convertirAudio(String filePath) throws IOException {
        FileInputStream fileInputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;

        try {
            File outputFile = new File(filePath);
            fileInputStream = new FileInputStream(filePath);
            byteArrayOutputStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
                //System.out.println("------Bytes: " + bytesRead);
            }
            return byteArrayOutputStream.toByteArray();
        } finally {
            // Cerrar los flujos de manera segura en el bloque finally.
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error al cerrar el flujo: " + e.getMessage());
                }
            }

            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error al cerrar el flujo: " + e.getMessage());
                }
            }
        }
    }

    private void enviarArchivoAudio(String ruta){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        Log.w(TAG, "Ruta: "+ruta);
        Log.w(TAG, "storageRef: "+storageRef.toString());
        fileName2 = UUID.randomUUID().toString();
        fileName = fileName2+".mp4";
        Log.w(TAG, "Nombre: "+fileName);
        StorageReference audioRef = storageRef.child(fileName);
        Log.w(TAG, "AudioRef: "+audioRef.toString());

        Uri archivoUri = Uri.fromFile(new File(ruta));

        //Asignamos los metadatos del archivo
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("audio/mpeg")
                .build();

        UploadTask uploadTask = audioRef.putFile(archivoUri, metadata);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot){
                audioRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Guardar el URI en la base de datos
                        String archivoURL = uri.toString();
                        Log.d(TAG, "URI: " + uri.toString());
                        Log.d(TAG, "Audio enviado correctamente");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Ocurrió un error al obtener el URI del archivo
                        Log.e(TAG, "Error al obtener el URI del archivo", e);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Ocurrió un error al cargar (upload) el archivo de audio
                Log.e(TAG, "Error al cargar el archivo de audio", e);
            }
        });
    }

    private void showLoadingDialog() {
        if(dialogEspera != null && dialogEspera.isShowing()) {
            dialogEspera.dismiss();
        }
    }
    private void hideLoadingDialog(){
        if(dialogEspera != null && !dialogEspera.isShowing()) {
            dialogEspera.show();
        }
    }
}
