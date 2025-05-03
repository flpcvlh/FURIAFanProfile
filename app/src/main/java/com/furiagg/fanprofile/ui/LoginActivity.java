package com.furiagg.fanprofile.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.furiagg.fanprofile.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private Button btnLogin, btnRegister;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializa o Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Vincula elementos da UI
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        // Configurar evento de clique no botão de login
        btnLogin.setOnClickListener(view -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (validateForm(email, password)) {
                signIn(email, password);
            }
        });

        // Configurar evento de clique no botão de registro
        btnRegister.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private boolean validateForm(String email, String password) {
        boolean valid = true;

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("E-mail é obrigatório");
            valid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("E-mail inválido");
            valid = false;
        } else {
            etEmail.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Senha é obrigatória");
            valid = false;
        } else if (password.length() < 6) {
            etPassword.setError("A senha deve ter pelo menos 6 caracteres");
            valid = false;
        } else {
            etPassword.setError(null);
        }

        return valid;
    }

    private void signIn(String email, String password) {
        btnLogin.setEnabled(false);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    btnLogin.setEnabled(true);
                    if (task.isSuccessful()) {
                        // Login bem-sucedido, ir para MainActivity
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        // Se o login falhar, exibir uma mensagem para o usuário
                        Toast.makeText(LoginActivity.this, "Falha na autenticação: " +
                                task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}