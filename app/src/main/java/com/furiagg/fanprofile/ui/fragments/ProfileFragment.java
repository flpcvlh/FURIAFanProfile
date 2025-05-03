package com.furiagg.fanprofile.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.furiagg.fanprofile.R;
import com.furiagg.fanprofile.adapter.DocumentAdapter;
import com.furiagg.fanprofile.adapter.EsportsProfileAdapter;
import com.furiagg.fanprofile.adapter.SocialMediaAdapter;
import com.furiagg.fanprofile.model.User;
import com.furiagg.fanprofile.ui.DocumentUploadActivity;
import com.furiagg.fanprofile.ui.EsportsProfileIntegrationActivity;
import com.furiagg.fanprofile.ui.SocialMediaIntegrationActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private CircleImageView ivProfilePhoto;
    private TextView tvProfileName, tvProfileEmail, tvProfileCpf, tvProfileAddress, tvProfileBirthDate, tvVerificationStatus;
    private Button btnEditProfile, btnUploadDocument, btnAddSocialMedia, btnAddEsportsProfile;
    private RecyclerView rvDocuments, rvSocialMedia, rvEsportsProfiles;
    private TextView tvNoDocuments;

    private DocumentAdapter documentAdapter;
    private SocialMediaAdapter socialMediaAdapter;
    private EsportsProfileAdapter esportsProfileAdapter;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private User currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Inicializar Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Vincular elementos da UI
        ivProfilePhoto = view.findViewById(R.id.ivProfilePhoto);
        tvProfileName = view.findViewById(R.id.tvProfileName);
        tvProfileEmail = view.findViewById(R.id.tvProfileEmail);
        tvProfileCpf = view.findViewById(R.id.tvProfileCpf);
        tvProfileAddress = view.findViewById(R.id.tvProfileAddress);
        tvProfileBirthDate = view.findViewById(R.id.tvProfileBirthDate);
        tvVerificationStatus = view.findViewById(R.id.tvVerificationStatus);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        btnUploadDocument = view.findViewById(R.id.btnUploadDocument);
        btnAddSocialMedia = view.findViewById(R.id.btnAddSocialMedia);
        btnAddEsportsProfile = view.findViewById(R.id.btnAddEsportsProfile);
        rvDocuments = view.findViewById(R.id.rvDocuments);
        rvSocialMedia = view.findViewById(R.id.rvSocialMedia);
        rvEsportsProfiles = view.findViewById(R.id.rvEsportsProfiles);
        tvNoDocuments = view.findViewById(R.id.tvNoDocuments);

        // Configurar RecyclerViews
        setupRecyclerViews();

        // Configurar eventos de clique
        setupClickListeners();

        // Carregar dados do usuário
        loadUserData();

        return view;
    }

    private void setupRecyclerViews() {
        // Configurar RecyclerView para documentos
        rvDocuments.setLayoutManager(new LinearLayoutManager(getContext()));
        documentAdapter = new DocumentAdapter(getContext());
        rvDocuments.setAdapter(documentAdapter);

        // Configurar RecyclerView para redes sociais
        rvSocialMedia.setLayoutManager(new LinearLayoutManager(getContext()));
        socialMediaAdapter = new SocialMediaAdapter(getContext());
        rvSocialMedia.setAdapter(socialMediaAdapter);

        // Configurar RecyclerView para perfis de esports
        rvEsportsProfiles.setLayoutManager(new LinearLayoutManager(getContext()));
        esportsProfileAdapter = new EsportsProfileAdapter(getContext());
        rvEsportsProfiles.setAdapter(esportsProfileAdapter);
    }

    private void setupClickListeners() {
        btnUploadDocument.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DocumentUploadActivity.class);
            startActivity(intent);
        });

        btnAddSocialMedia.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SocialMediaIntegrationActivity.class);
            startActivity(intent);
        });

        btnAddEsportsProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EsportsProfileIntegrationActivity.class);
            startActivity(intent);
        });
    }

    private void loadUserData() {
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        currentUser = documentSnapshot.toObject(User.class);
                        updateUI();
                    }
                })
                .addOnFailureListener(e -> {
                    // Tratar erros
                });
    }

    private void updateUI() {
        if (currentUser != null) {
            tvProfileName.setText(currentUser.getName());
            tvProfileEmail.setText(currentUser.getEmail());

            // Formatar CPF
            String cpf = currentUser.getCpf();
            if (cpf != null && cpf.length() == 11) {
                cpf = cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." +
                        cpf.substring(6, 9) + "-" + cpf.substring(9);
                tvProfileCpf.setText(cpf);
            }

            tvProfileAddress.setText(currentUser.getAddress());

            // Data de nascimento
            if (currentUser.getBirthDate() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                tvProfileBirthDate.setText(sdf.format(currentUser.getBirthDate()));
            } else {
                tvProfileBirthDate.setText("Não informada");
            }

            // Status de verificação
            if (currentUser.isVerified()) {
                tvVerificationStatus.setVisibility(View.VISIBLE);
            } else {
                tvVerificationStatus.setVisibility(View.GONE);
            }

            // Foto de perfil
            if (currentUser.getPhotoUrl() != null && !currentUser.getPhotoUrl().isEmpty()) {
                Glide.with(this)
                        .load(currentUser.getPhotoUrl())
                        .placeholder(R.drawable.default_profile)
                        .into(ivProfilePhoto);
            }

            // Atualizar adapters
            if (currentUser.getSocialMediaAccounts() != null && !currentUser.getSocialMediaAccounts().isEmpty()) {
                socialMediaAdapter.updateData(currentUser.getSocialMediaAccounts());
                rvSocialMedia.setVisibility(View.VISIBLE);
            } else {
                rvSocialMedia.setVisibility(View.GONE);
            }

            if (currentUser.getEsportsProfiles() != null && !currentUser.getEsportsProfiles().isEmpty()) {
                esportsProfileAdapter.updateData(currentUser.getEsportsProfiles());
                rvEsportsProfiles.setVisibility(View.VISIBLE);
            } else {
                rvEsportsProfiles.setVisibility(View.GONE);
            }

            // Verificar documentos
            boolean hasDocuments = currentUser.getIdDocumentUrl() != null && !currentUser.getIdDocumentUrl().isEmpty();
            tvNoDocuments.setVisibility(hasDocuments ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Recarregar dados do usuário ao retornar para o fragment
        loadUserData();
    }
}