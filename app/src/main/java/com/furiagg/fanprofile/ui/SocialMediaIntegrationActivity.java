package com.furiagg.fanprofile.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.furiagg.fanprofile.R;
import com.furiagg.fanprofile.adapter.SocialPlatformAdapter;
import com.furiagg.fanprofile.model.SocialMedia;
import com.furiagg.fanprofile.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SocialMediaIntegrationActivity extends AppCompatActivity implements SocialPlatformAdapter.SocialPlatformListener {

    private ImageView ivBackButton;
    private RecyclerView rvSocialMediaPlatforms;
    private SocialPlatformAdapter adapter;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media_integration);

        // Inicializar Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Vincular elementos da UI
        ivBackButton = findViewById(R.id.ivBackButton);
        rvSocialMediaPlatforms = findViewById(R.id.rvSocialMediaPlatforms);

        // Configurar eventos de clique
        ivBackButton.setOnClickListener(v -> onBackPressed());

        // Configurar RecyclerView
        rvSocialMediaPlatforms.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SocialPlatformAdapter(this, this);
        rvSocialMediaPlatforms.setAdapter(adapter);

        // Carregar dados do usuário
        loadUserData();
    }

    private void loadUserData() {
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        currentUser = documentSnapshot.toObject(User.class);
                        loadSocialPlatforms();
                    }
                })
                .addOnFailureListener(e -> {
                    // Tratar erro
                });
    }

    private void loadSocialPlatforms() {
        List<SocialPlatformItem> platforms = new ArrayList<>();

        // Adicionar plataformas disponíveis
        platforms.add(new SocialPlatformItem(
                SocialMedia.Platform.TWITTER,
                "Twitter",
                "Conecte sua conta do Twitter para compartilhar suas interações com a FURIA",
                R.drawable.ic_twitter,
                isAlreadyConnected(SocialMedia.Platform.TWITTER)
        ));

        platforms.add(new SocialPlatformItem(
                SocialMedia.Platform.INSTAGRAM,
                "Instagram",
                "Conecte sua conta do Instagram para compartilhar suas interações com a FURIA",
                R.drawable.ic_instagram,
                isAlreadyConnected(SocialMedia.Platform.INSTAGRAM)
        ));

        platforms.add(new SocialPlatformItem(
                SocialMedia.Platform.FACEBOOK,
                "Facebook",
                "Conecte sua conta do Facebook para compartilhar suas interações com a FURIA",
                R.drawable.ic_facebook,
                isAlreadyConnected(SocialMedia.Platform.FACEBOOK)
        ));

        platforms.add(new SocialPlatformItem(
                SocialMedia.Platform.TWITCH,
                "Twitch",
                "Conecte sua conta da Twitch para compartilhar seus hábitos de assistir a FURIA",
                R.drawable.ic_twitch,
                isAlreadyConnected(SocialMedia.Platform.TWITCH)
        ));

        platforms.add(new SocialPlatformItem(
                SocialMedia.Platform.YOUTUBE,
                "YouTube",
                "Conecte sua conta do YouTube para compartilhar vídeos e canais relacionados à FURIA",
                R.drawable.ic_youtube,
                isAlreadyConnected(SocialMedia.Platform.YOUTUBE)
        ));

        platforms.add(new SocialPlatformItem(
                SocialMedia.Platform.TIKTOK,
                "TikTok",
                "Conecte sua conta do TikTok para compartilhar vídeos relacionados à FURIA",
                R.drawable.ic_tiktok,
                isAlreadyConnected(SocialMedia.Platform.TIKTOK)
        ));

        platforms.add(new SocialPlatformItem(
                SocialMedia.Platform.DISCORD,
                "Discord",
                "Conecte sua conta do Discord para compartilhar interações nos servidores da FURIA",
                R.drawable.ic_discord,
                isAlreadyConnected(SocialMedia.Platform.DISCORD)
        ));

        // Atualizar o adapter
        adapter.updateData(platforms);
    }

    private boolean isAlreadyConnected(SocialMedia.Platform platform) {
        if (currentUser != null && currentUser.getSocialMediaAccounts() != null) {
            for (SocialMedia socialMedia : currentUser.getSocialMediaAccounts()) {
                if (socialMedia.getPlatform() == platform && socialMedia.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    private SocialMedia getConnectedAccount(SocialMedia.Platform platform) {
        if (currentUser != null && currentUser.getSocialMediaAccounts() != null) {
            for (SocialMedia socialMedia : currentUser.getSocialMediaAccounts()) {
                if (socialMedia.getPlatform() == platform) {
                    return socialMedia;
                }
            }
        }
        return null;
    }

    @Override
    public void onConnectClicked(SocialPlatformItem platform) {
        // Em uma aplicação real, aqui seria aberta a autenticação com a rede social
        // Para este protótipo, vamos simular a autenticação
        simulateAuthentication(platform);
    }

    @Override
    public void onDisconnectClicked(SocialPlatformItem platform) {
        // Remover a conexão
        disconnectPlatform(platform.getPlatform());
    }

    private void simulateAuthentication(SocialPlatformItem platformItem) {
        // Em um app real, aqui seria implementado OAuth ou outro método de autenticação
        // Para o protótipo, vamos simular um login bem-sucedido

        // Criar um novo objeto de mídia social
        SocialMedia socialMedia = new SocialMedia(
                platformItem.getPlatform(),
                "usuario_" + platformItem.getName().toLowerCase(),
                "https://" + platformItem.getName().toLowerCase() + ".com/usuario",
                true
        );

        // Salvar a conexão
        saveSocialMedia(socialMedia);

        // Atualizar a lista
        platformItem.setConnected(true);
        adapter.notifyDataSetChanged();
    }

    private void disconnectPlatform(SocialMedia.Platform platform) {
        if (currentUser != null && currentUser.getSocialMediaAccounts() != null) {
            List<SocialMedia> updatedAccounts = new ArrayList<>();

            for (SocialMedia account : currentUser.getSocialMediaAccounts()) {
                if (account.getPlatform() != platform) {
                    updatedAccounts.add(account);
                }
            }

            currentUser.setSocialMediaAccounts(updatedAccounts);

            // Atualizar no Firestore
            String userId = mAuth.getCurrentUser().getUid();
            db.collection("users").document(userId)
                    .set(currentUser)
                    .addOnSuccessListener(aVoid -> {
                        // Atualizar a interface
                        loadSocialPlatforms();
                    })
                    .addOnFailureListener(e -> {
                        // Tratar erro
                    });
        }
    }

    private void saveSocialMedia(SocialMedia socialMedia) {
        if (currentUser != null) {
            String userId = mAuth.getCurrentUser().getUid();

            // Verificar se já existe uma conta com esta plataforma
            boolean exists = false;
            if (currentUser.getSocialMediaAccounts() != null) {
                for (int i = 0; i < currentUser.getSocialMediaAccounts().size(); i++) {
                    if (currentUser.getSocialMediaAccounts().get(i).getPlatform() == socialMedia.getPlatform()) {
                        // Atualizar a existente
                        currentUser.getSocialMediaAccounts().set(i, socialMedia);
                        exists = true;
                        break;
                    }
                }
            } else {
                currentUser.setSocialMediaAccounts(new ArrayList<>());
            }

            // Adicionar nova se não existir
            if (!exists) {
                currentUser.addSocialMedia(socialMedia);
            }

            // Atualizar no Firestore
            db.collection("users").document(userId)
                    .set(currentUser)
                    .addOnSuccessListener(aVoid -> {
                        // Atualizado com sucesso
                    })
                    .addOnFailureListener(e -> {
                        // Tratar erro
                    });
        }
    }

    // Classe modelo para os itens da lista de plataformas
    public static class SocialPlatformItem {
        private SocialMedia.Platform platform;
        private String name;
        private String description;
        private int iconResource;
        private boolean connected;
        private String username;

        public SocialPlatformItem(SocialMedia.Platform platform, String name, String description,
                                  int iconResource, boolean connected) {
            this.platform = platform;
            this.name = name;
            this.description = description;
            this.iconResource = iconResource;
            this.connected = connected;
        }

        public SocialMedia.Platform getPlatform() {
            return platform;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public int getIconResource() {
            return iconResource;
        }

        public boolean isConnected() {
            return connected;
        }

        public void setConnected(boolean connected) {
            this.connected = connected;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}