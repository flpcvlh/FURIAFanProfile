package com.furiagg.fanprofile.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.furiagg.fanprofile.R;
import com.furiagg.fanprofile.model.EsportsProfile;
import com.furiagg.fanprofile.model.User;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EsportsProfileIntegrationActivity extends AppCompatActivity {

    private ImageView ivBackButton;
    private Spinner spinnerPlatform;
    private TextInputEditText etUsername, etProfileUrl, etGamesFocus;
    private Button btnAddProfile, btnConfirmProfile;
    private MaterialCardView cardVerificationResult;
    private ProgressBar progressBarVerification;
    private ImageView ivProfileVerified, ivContentRelevant;
    private TextView tvRelevantContent;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EsportsProfile currentProfile;
    private EsportsProfile.Platform selectedPlatform;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esports_profile_integration);

        // Inicializar Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Vincular elementos da UI
        ivBackButton = findViewById(R.id.ivBackButton);
        spinnerPlatform = findViewById(R.id.spinnerPlatform);
        etUsername = findViewById(R.id.etUsername);
        etProfileUrl = findViewById(R.id.etProfileUrl);
        etGamesFocus = findViewById(R.id.etGamesFocus);
        btnAddProfile = findViewById(R.id.btnAddProfile);
        btnConfirmProfile = findViewById(R.id.btnConfirmProfile);
        cardVerificationResult = findViewById(R.id.cardVerificationResult);
        progressBarVerification = findViewById(R.id.progressBarVerification);
        ivProfileVerified = findViewById(R.id.ivProfileVerified);
        ivContentRelevant = findViewById(R.id.ivContentRelevant);
        tvRelevantContent = findViewById(R.id.tvRelevantContent);

        // Configurar eventos de clique
        ivBackButton.setOnClickListener(v -> onBackPressed());
        btnAddProfile.setOnClickListener(v -> verifyProfile());
        btnConfirmProfile.setOnClickListener(v -> saveProfileToFirestore());

        // Configurar Spinner com plataformas
        setupPlatformSpinner();
    }

    private void setupPlatformSpinner() {
        List<String> platforms = Arrays.asList(
                "Steam",
                "Battle.net",
                "Riot Games",
                "Epic Games",
                "FACEIT",
                "ESEA",
                "GamersClub",
                "Outra"
        );

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, R.layout.spinner_item, platforms);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerPlatform.setAdapter(adapter);

        spinnerPlatform.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setPlatformFromPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedPlatform = EsportsProfile.Platform.OTHER;
            }
        });
    }

    private void setPlatformFromPosition(int position) {
        switch (position) {
            case 0:
                selectedPlatform = EsportsProfile.Platform.STEAM;
                break;
            case 1:
                selectedPlatform = EsportsProfile.Platform.BATTLENET;
                break;
            case 2:
                selectedPlatform = EsportsProfile.Platform.RIOT;
                break;
            case 3:
                selectedPlatform = EsportsProfile.Platform.EPIC;
                break;
            case 4:
                selectedPlatform = EsportsProfile.Platform.FACEIT;
                break;
            case 5:
                selectedPlatform = EsportsProfile.Platform.ESEA;
                break;
            case 6:
                selectedPlatform = EsportsProfile.Platform.GAMERSCLUB;
                break;
            default:
                selectedPlatform = EsportsProfile.Platform.OTHER;
                break;
        }
    }

    private void verifyProfile() {
        String username = etUsername.getText().toString().trim();
        String profileUrl = etProfileUrl.getText().toString().trim();
        String gamesFocus = etGamesFocus.getText().toString().trim();

        if (validateForm(username, profileUrl, gamesFocus)) {
            // Mostrar progresso
            progressBarVerification.setVisibility(View.VISIBLE);
            btnAddProfile.setEnabled(false);

            // Criar objeto de perfil
            currentProfile = new EsportsProfile(selectedPlatform, username, profileUrl, gamesFocus);

            // Simular verificação do perfil
            // Em uma implementação real, aqui seria feita uma requisição para uma API
            simulateProfileVerification();
        }
    }

    private boolean validateForm(String username, String profileUrl, String gamesFocus) {
        boolean valid = true;

        if (username.isEmpty()) {
            etUsername.setError("Nome de usuário é obrigatório");
            valid = false;
        } else {
            etUsername.setError(null);
        }

        if (profileUrl.isEmpty()) {
            etProfileUrl.setError("URL do perfil é obrigatória");
            valid = false;
        } else if (!android.util.Patterns.WEB_URL.matcher(profileUrl).matches()) {
            etProfileUrl.setError("URL inválida");
            valid = false;
        } else {
            etProfileUrl.setError(null);
        }

        if (gamesFocus.isEmpty()) {
            etGamesFocus.setError("Informe pelo menos um jogo");
            valid = false;
        } else {
            etGamesFocus.setError(null);
        }

        return valid;
    }

    private void simulateProfileVerification() {
        // Simular um atraso de verificação
        new android.os.Handler().postDelayed(() -> {
            // Esconder progresso
            progressBarVerification.setVisibility(View.GONE);

            // Marcar perfil como verificado
            currentProfile.setVerified(true);

            // Mostrar resultados da verificação
            cardVerificationResult.setVisibility(View.VISIBLE);

            // Gerar texto relevante com base na plataforma e jogos
            String relevantContent = generateRelevantContent();
            tvRelevantContent.setText(relevantContent);

        }, 2000); // 2 segundos de atraso
    }

    private String generateRelevantContent() {
        String platformName = getPlatformName(currentProfile.getPlatform());
        String gamesFocus = currentProfile.getGamesFocus();

        return "Encontramos interações com conteúdo relacionado à FURIA e outros times de eSports na sua conta " +
                platformName + ". Seus jogos (" + gamesFocus + ") incluem títulos que a FURIA participa competitivamente, o que" +
                " indica uma boa relevância para o seu perfil de fã.";
    }

    private String getPlatformName(EsportsProfile.Platform platform) {
        switch (platform) {
            case STEAM:
                return "Steam";
            case BATTLENET:
                return "Battle.net";
            case RIOT:
                return "Riot Games";
            case EPIC:
                return "Epic Games";
            case FACEIT:
                return "FACEIT";
            case ESEA:
                return "ESEA";
            case GAMERSCLUB:
                return "GamersClub";
            default:
                return "de eSports";
        }
    }

    private void saveProfileToFirestore() {
        if (currentProfile != null) {
            String userId = mAuth.getCurrentUser().getUid();

            // Buscar usuário atual
            db.collection("users").document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            User user = documentSnapshot.toObject(User.class);

                            // Adicionar perfil ao usuário
                            if (user.getEsportsProfiles() == null) {
                                user.setEsportsProfiles(new ArrayList<>());
                            }
                            user.addEsportsProfile(currentProfile);

                            // Atualizar usuário no Firestore
                            db.collection("users").document(userId)
                                    .set(user)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(EsportsProfileIntegrationActivity.this,
                                                "Perfil adicionado com sucesso!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(EsportsProfileIntegrationActivity.this,
                                                "Erro ao salvar perfil: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(EsportsProfileIntegrationActivity.this,
                                "Erro ao buscar usuário: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }
}