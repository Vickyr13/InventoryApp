package com.example.inventoryapp.views.login_logout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventoryapp.R;
import com.example.inventoryapp.views.main.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {
    TextView createAccountBtn;
    ProgressBar progress_Bar;
    EditText userNameEditText, passwordEditText;
    Button loginBtn;

    SignInButton signInButton;
    GoogleSignInClient gsc;
    GoogleSignInOptions gso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        createAccountBtn = (TextView) findViewById(R.id.txtCreateAccountBtn);
        userNameEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);
        loginBtn = (Button) findViewById(R.id.loginButton);
        createAccountBtn.setOnClickListener(v->startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
        loginBtn.setOnClickListener(v->loginUser());
    }

    void loginUser() {
        String username = userNameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        boolean isValidated = validateData(username, password);
        if(!isValidated){
            return;
        }
        loginAccountInFirebase(username, password);
    }

    void loginAccountInFirebase(String username, String password){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }else {
                        Toasty.warning(LoginActivity.this, "Su correo no ha sido verificado.", Toasty.LENGTH_SHORT, true).show();
                    }
                }else{
                    Toasty.info(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();

                }
            }
        });
    }
    boolean validateData(String username, String password) {
// La función validateData probablemente contiene lógica para verificar si los datos son válidos según ciertos criterios, como si el correo electrónico tiene un formato válido o si la contraseña y su confirmación coinciden.
        //validacion de los dattos de cada input del usuario

        if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
            userNameEditText.setError("Email invalido");
            return false;
        }
        if (password.length() < 6) {
            passwordEditText.setError("Tamaño de contraseña incorrecto");
            return false;
        }
        return true;
    }
}