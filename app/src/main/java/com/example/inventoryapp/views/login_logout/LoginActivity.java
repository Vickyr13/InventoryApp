package com.example.inventoryapp.views.login_logout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.inventoryapp.R;

public class LoginActivity extends AppCompatActivity {
    TextView createAccountBtn;
    ProgressBar progress_Bar;
    EditText emailEditText, passwordEditText;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        createAccountBtn = (TextView) findViewById(R.id.txtCreateAccountBtn);
        progress_Bar = (ProgressBar) findViewById(R.id.progressBar);
        emailEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);
        loginBtn = (Button) findViewById(R.id.loginButton);
        createAccountBtn.setOnClickListener(v->startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    void changeInProgress(boolean inProgress) {//Esta línea define la función, que toma un parámetro booleano llamado inprogress parámetro indica si la aplicación está en un estado de progreso o no

        if (inProgress) {
            progress_Bar.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
        } else {
            progress_Bar.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String email, String password) {
// La función validateData probablemente contiene lógica para verificar si los datos son válidos según ciertos criterios, como si el correo electrónico tiene un formato válido o si la contraseña y su confirmación coinciden.
        //validacion de los dattos de cada input del usuario

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Email invalido");
            return false;
        }
        if (password.length() < 6) {
            passwordEditText.setError("Tamaño de contraseña incorrecto");
            return false;
        }
        return true;
    }
}