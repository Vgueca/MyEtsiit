package com.example.myetsiit.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myetsiit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText editTextUsername = findViewById(R.id.editTextUsername);
        EditText editTextPassword = findViewById(R.id.editTextPassword);
        Button buttonLogin = findViewById(R.id.buttonLogin);
        TextView textViewRegister = findViewById(R.id.textViewRegister);

        // En tu método onCreate() o donde configures tus vistas, configura el onClickListener del botón
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los valores introducidos por el usuario
                String nombreUsuario = editTextUsername.getText().toString();
                String contraseña = editTextPassword.getText().toString();

                // Llamar a la función de inicio de sesión
                iniciarSesionConFirebase(nombreUsuario, contraseña);
            }
        });

        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ir a la RegisterActivity
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    // Agrega esta función en tu clase
    private void iniciarSesionConFirebase(String email, String contraseña) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, contraseña)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Inicio de sesión exitoso, realiza acciones adicionales si es necesario
                            Toast.makeText(getApplicationContext(), "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

                            // Ejemplo de redirección a MainActivity
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);

                            // Asegúrate de cerrar la actividad de inicio de sesión si ya no es necesaria
                            finish();
                        } else {
                            // Si el inicio de sesión falla, muestra un mensaje de error
                            Toast.makeText(getApplicationContext(), "Inicio de sesión fallido. Verifica tus credenciales.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }




}

