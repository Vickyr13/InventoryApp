package com.example.inventoryapp.views.login_logout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventoryapp.R;
import com.example.inventoryapp.utils.Utility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import es.dmoral.toasty.Toasty;

public class RegisterActivity extends AppCompatActivity {
    EditText editTextUserName, editTextPassword, editTextConfirmPassword;
    Button registerbtn;
    TextView textViewLoginBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextUserName = findViewById(R.id.username);
        editTextPassword = findViewById(R.id.password);
        editTextConfirmPassword = findViewById(R.id.repeatPassword);
        registerbtn = findViewById(R.id.registerButton);
        textViewLoginBtn = findViewById(R.id.txtLoginBtn);

        registerbtn.setOnClickListener(v->createAccount());
        textViewLoginBtn.setOnClickListener(v->startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));
    }

    void createAccount() {
        String username = editTextUserName.getText().toString();
        String password = editTextPassword.getText().toString();
        String confirmpassword = editTextConfirmPassword.getText().toString();

        if (TextUtils.isEmpty(username)||TextUtils.isEmpty(password)||TextUtils.isEmpty(confirmpassword)){
            Toasty.warning(RegisterActivity.this, "Los campos estan vacios.", Toasty.LENGTH_LONG, true).show();
        }else{
            boolean isValidated = validateData(username, password, confirmpassword);
            if(!isValidated){
                return;
            }

            createAccountInFirebase(username, password);
        }
    }

    void createAccountInFirebase(String username, String password){

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    Toasty.success(RegisterActivity.this, "Su cuenta se creo exitosamente!", Toasty.LENGTH_LONG, true).show();
                    firebaseAuth.getCurrentUser().sendEmailVerification();
                    firebaseAuth.signOut();
                    finish();
                }else{
                    Toasty.error(RegisterActivity.this, "Error al crear la cuenta: " + task.getException().getMessage(), Toasty.LENGTH_SHORT, true).show();
                }
            }
        });
    }


    boolean validateData(String username, String password, String confirmPassword){
        if(!Patterns.EMAIL_ADDRESS.matcher(username).matches()){
            editTextUserName.setError("El correo ingresado es invalido");
        }
        if(password.length() <6){
            editTextPassword.setError("Debe ingresar mas de 6 caracteres");
            return false;
        }
        if(!password.equals(confirmPassword)){
            editTextConfirmPassword.setError("Las contraseñas no son iguales");
            return false;
        }
        return true;
    }
}