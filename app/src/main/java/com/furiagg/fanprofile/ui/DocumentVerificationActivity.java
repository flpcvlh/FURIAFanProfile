package com.furiagg.fanprofile.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.furiagg.fanprofile.R;
import com.furiagg.fanprofile.util.DocumentDataExtractor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DocumentVerificationActivity extends AppCompatActivity {

    private ImageView ivBackButton;
    private TextView tvExtractedName;
    private TextView tvExtractedCpf;
    private TextView tvExtractedBirthDate;
    private Button btnConfirmData;

    private String documentUrl;
    private String extractedText;
    private Map<String, String> extractedData;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_verification);

        // Inicializar Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Obter dados da intent
        documentUrl = getIntent().getStringExtra("document_url");
        extractedText = getIntent().getStringExtra("extracted_text");

        // Vincular elementos da UI
        ivBackButton = findViewById(R.id.ivBackButton);
        tvExtractedName = findViewById(R.id.tvExtractedName);
        tvExtractedCpf = findViewById(R.id.tvExtractedCpf);
        tvExtractedBirthDate = findViewById(R.id.tvExtractedBirthDate);
        btnConfirmData = findViewById(R.id.btnConfirmData);

        // Configurar eventos de clique
        ivBackButton.setOnClickListener(v -> onBackPressed());
        btnConfirmData.setOnClickListener(v -> saveVerificationData());

        // Extrair informações do texto
        extractDataFromText();

        // Mostrar dados extraídos na interface
        displayExtractedData();
    }

    private void extractDataFromText() {
        if (extractedText != null && !extractedText.isEmpty()) {
            // Utilizar classe auxiliar para extrair dados
            DocumentDataExtractor extractor = new DocumentDataExtractor(extractedText);
            extractedData = extractor.extractData();
        } else {
            extractedData = new HashMap<>();
        }
    }

    private void displayExtractedData() {
        if (extractedData != null) {
            // Nome
            String name = extractedData.getOrDefault("name", "Não encontrado");
            tvExtractedName.setText(name);

            // CPF
            String cpf = extractedData.getOrDefault("cpf", "Não encontrado");
            tvExtractedCpf.setText(cpf);

            // Data de nascimento
            String birthDate = extractedData.getOrDefault("birthDate", "Não encontrado");
            tvExtractedBirthDate.setText(birthDate);
        }
    }

    private void saveVerificationData() {
        if (extractedData != null && documentUrl != null) {
            String userId = mAuth.getCurrentUser().getUid();

            // Criar mapa com dados a serem atualizados
            Map<String, Object> updates = new HashMap<>();
            updates.put("idDocumentUrl", documentUrl);
            updates.put("verified", true);

            // Atualizar dados do usuário com informações do documento
            if (extractedData.containsKey("name")) {
                updates.put("name", extractedData.get("name"));
            }

            if (extractedData.containsKey("cpf")) {
                updates.put("cpf", extractedData.get("cpf"));
            }

            // Atualizar dados no Firestore
            db.collection("users").document(userId)
                    .update(updates)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(DocumentVerificationActivity.this,
                                "Documento verificado com sucesso!", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(DocumentVerificationActivity.this,
                                "Erro ao salvar dados: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }
}