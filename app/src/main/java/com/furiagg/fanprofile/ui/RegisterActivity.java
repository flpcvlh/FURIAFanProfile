package com.furiagg.fanprofile.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.furiagg.fanprofile.R;
import com.furiagg.fanprofile.model.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private TextInputEditText etName, etEmail, etPassword, etConfirmPassword, etCpf, etAddress;
    private Button btnRegister;
    private ImageView ivBackButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inicializa Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Vincula elementos da UI
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etCpf = findViewById(R.id.etCpf);
        etAddress = findViewById(R.id.etAddress);
        btnRegister = findViewById(R.id.btnRegister);
        ivBackButton = findViewById(R.id.ivBackButton);

        // Mudar o hint do campo de endereço para CEP
        etAddress.setHint("CEP (somente números)");

        // Configurar o tipo de entrada para apenas números
        etAddress.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);

        // Limitar o tamanho máximo para 8 dígitos (CEP brasileiro sem hífen)
        etAddress.setFilters(new android.text.InputFilter[] {
                new android.text.InputFilter.LengthFilter(8)
        });

        // Configurar evento de clique no botão de voltar
        ivBackButton.setOnClickListener(view -> onBackPressed());

        // Configurar evento de clique no botão de registro
        btnRegister.setOnClickListener(view -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();
            String cpf = etCpf.getText().toString().trim();
            String cep = etAddress.getText().toString().trim(); // Agora é CEP

            if (validateForm(name, email, password, confirmPassword, cpf, cep)) {
                registerUser(name, email, password, cpf, cep);
            }
        });
    }

    private boolean validateForm(String name, String email, String password,
                                 String confirmPassword, String cpf, String cep) {
        boolean valid = true;

        // Validação do nome
        if (TextUtils.isEmpty(name)) {
            etName.setError("Nome é obrigatório");
            valid = false;
        } else {
            etName.setError(null);
        }

        // Validação do email
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("E-mail é obrigatório");
            valid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("E-mail inválido");
            valid = false;
        } else {
            etEmail.setError(null);
        }

        // Validação da senha
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Senha é obrigatória");
            valid = false;
        } else if (password.length() < 6) {
            etPassword.setError("A senha deve ter pelo menos 6 caracteres");
            valid = false;
        } else {
            etPassword.setError(null);
        }

        // Validação da confirmação de senha
        if (TextUtils.isEmpty(confirmPassword)) {
            etConfirmPassword.setError("Confirmação de senha é obrigatória");
            valid = false;
        } else if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("As senhas não coincidem");
            valid = false;
        } else {
            etConfirmPassword.setError(null);
        }

        // Validação do CPF
        if (TextUtils.isEmpty(cpf)) {
            etCpf.setError("CPF é obrigatório");
            valid = false;
        } else if (cpf.length() != 11) {
            etCpf.setError("CPF deve ter 11 dígitos");
            valid = false;
        } else {
            etCpf.setError(null);
        }

        // Validação do CEP
        if (TextUtils.isEmpty(cep)) {
            etAddress.setError("CEP é obrigatório");
            valid = false;
        } else if (cep.length() != 8) {
            etAddress.setError("CEP deve ter 8 dígitos");
            valid = false;
        } else {
            etAddress.setError(null);
        }

        return valid;
    }

    private void registerUser(String name, String email, String password, String cpf, String cep) {
        btnRegister.setEnabled(false);

        // Adicionar logs para depuração
        Log.d(TAG, "Tentando registrar usuário: " + email);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Criação de usuário bem-sucedida
                        Log.d(TAG, "Criação de usuário Firebase bem-sucedida");
                        String userId = mAuth.getCurrentUser().getUid();
                        saveUserToFirestore(userId, name, email, cpf, cep);
                    } else {
                        // Se o cadastro falhar, exibir uma mensagem para o usuário
                        Log.e(TAG, "Falha no cadastro de usuário Firebase", task.getException());
                        Toast.makeText(RegisterActivity.this, "Falha no cadastro: " +
                                task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        btnRegister.setEnabled(true);
                    }
                });
    }

    private void saveUserToFirestore(String userId, String name, String email, String cpf, String cep) {
        try {
            // Criar objeto de usuário
            User user = new User();
            user.setId(userId);
            user.setName(name);
            user.setEmail(email);
            user.setCpf(cpf);
            user.setAddress(cep); // Agora estamos salvando o CEP como endereço
            user.setVerified(false);

            // Inicializar listas para evitar NullPointerException
            user.setSocialMediaAccounts(new ArrayList<>());
            user.setEsportsProfiles(new ArrayList<>());
            user.setInterests(new ArrayList<>());
            user.setEvents(new ArrayList<>());
            user.setPurchases(new ArrayList<>());

            // Log para depuração
            Log.d(TAG, "Salvando dados do usuário no Firestore: " + userId);

            // Salvar no Firestore
            db.collection("users").document(userId)
                    .set(user)
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "Dados do usuário salvos com sucesso no Firestore");
                        Toast.makeText(RegisterActivity.this, "Cadastro realizado com sucesso!",
                                Toast.LENGTH_SHORT).show();

                        // Ir para MainActivity
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Erro ao salvar dados no Firestore", e);
                        Toast.makeText(RegisterActivity.this, "Erro ao salvar dados: " +
                                e.getMessage(), Toast.LENGTH_LONG).show();
                        btnRegister.setEnabled(true);
                    });
        } catch (Exception e) {
            Log.e(TAG, "Exceção ao tentar salvar dados do usuário", e);
            Toast.makeText(RegisterActivity.this, "Erro inesperado: " +
                    e.getMessage(), Toast.LENGTH_LONG).show();
            btnRegister.setEnabled(true);
        }
    }
}