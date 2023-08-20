package com.example.sumefly.login;

import static com.example.sumefly.database.DbHelper.COLUMN_EMAIL;
import static com.example.sumefly.database.DbHelper.COLUMN_PASSWORD;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sumefly.MainActivity;
import com.example.sumefly.R;
import com.example.sumefly.database.DbHelper;
import com.example.sumefly.database.DbUsuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class LoginActivity extends AppCompatActivity {

    //Prueba con Firebase
    private static final int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.
    private boolean showOneTapUI = true;
    private FirebaseAuth mAuth;
    //Prueba con Firebase 2. Accediendo a la base de datos
    private DatabaseReference mDatabase;
    private FirebaseAnalytics mFirebaseAnalytics;


    private static final String TAG = "LoginActivity";
    String gmail, gPass;
    EditText email;
    EditText password;
    Button btnLogin;
    Button btnRegistrarse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //este código solo verifica si el usuario está autenticado actualmente en la aplicación
        //checkCurrentUser();


        //Prueba con Firebase 3.
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Context context = getApplicationContext();
        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPass);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegistrarse = findViewById(R.id.btnRegistrar);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailText = email.getText().toString();
                String passwordText = password.getText().toString();

                if(emailText.isEmpty() || passwordText.isEmpty()){
                    Toast.makeText(context, "Por favor, rellene todos los campos", Toast.LENGTH_SHORT).show();
                }else{
                    loginUser(emailText, passwordText);
                }
            }
        });
        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrarseActivity.class);
                startActivity(intent);
                Log.d(TAG, "Registrarse");
            }
        });
    }
  //@Override
  //public void onStart() {
  //    super.onStart();
  //    // Check if user is signed in (non-null) and update UI accordingly.
  //    FirebaseUser currentUser = mAuth.getCurrentUser();
  //    currentUser.reload();

  //}
    //@Override
    //protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    //    super.onActivityResult(requestCode, resultCode, data);
//
    //    switch (requestCode) {
    //        case REQ_ONE_TAP:
    //            try {
    //                SignInClient oneTapClient = com.google.android.gms.auth.api.identity.Identity.getSignInClient(this) ;
    //                SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
    //                String idToken = credential.getGoogleIdToken();
    //                if (idToken !=  null) {
    //                    // Got an ID token from Google. Use it to authenticate
    //                    System.out.println("ID TOKEN: " + idToken);
    //                    // with Firebase.
    //                    Log.d(TAG, "Got ID token.");
    //                }
    //            } catch (ApiException e) {
    //                // ...
    //            }
    //            break;
    //    }
    //}

    //Comprobar los datos con la base de datos de Firebase
    private boolean checkUser(String email, String password) {
        return true;
    }

    //Comprobar los datos del login
    private boolean comprobarInicioSesion() {
        gmail = email.getText().toString();
        gPass = password.getText().toString();

        //Se instancia nuestra base de datos
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                COLUMN_EMAIL,
                COLUMN_PASSWORD
        };

        String selection = "email = ? AND password = ?";

        String[] selectionArgs = {gmail, gPass};

        Cursor cursor = db.query(
                DbUsuario.TABLE_USER,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if(cursor != null && cursor.moveToFirst()){
            do{
                String valorEmail = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
                String valorPass = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD));

                System.out.println("Email: " + valorEmail + " Password: " + valorPass);
                if(valorEmail.equals(gmail) && valorPass.equals(gPass)){
                    cursor.close();
                    db.close();
                    return true;
                }
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return false;
    }
    private void checkCurrentUser() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // El usuario existe en la base de datos
            Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "onCreate: " + currentUser.getEmail());
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            // El usuario no existe en la base de datos
            Toast.makeText(this, "No existe el usuario", Toast.LENGTH_SHORT).show();
        }
    }
    private void loginUser(String email, String password) {

        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    finish();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    Toast.makeText(LoginActivity.this,"Bienvenido", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "signInWithEmail:success");
                }else{
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this,"Error al iniciar sesión", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "signInWithEmail:failure", e);
            }
        });


    }


}