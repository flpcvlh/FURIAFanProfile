package com.furiagg.fanprofile.ui.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.furiagg.fanprofile.R;
import com.furiagg.fanprofile.adapter.EventAdapter;
import com.furiagg.fanprofile.adapter.InterestAdapter;
import com.furiagg.fanprofile.adapter.PurchaseAdapter;
import com.furiagg.fanprofile.model.FanEvent;
import com.furiagg.fanprofile.model.FanInterest;
import com.furiagg.fanprofile.model.FanPurchase;
import com.furiagg.fanprofile.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class InterestsFragment extends Fragment {

    private Button btnAddInterest, btnAddEvent, btnAddPurchase;
    private RecyclerView rvInterests, rvEvents, rvPurchases;

    private InterestAdapter interestAdapter;
    private EventAdapter eventAdapter;
    private PurchaseAdapter purchaseAdapter;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private User currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interests, container, false);

        // Inicializar Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Vincular elementos da UI
        btnAddInterest = view.findViewById(R.id.btnAddInterest);
        btnAddEvent = view.findViewById(R.id.btnAddEvent);
        btnAddPurchase = view.findViewById(R.id.btnAddPurchase);
        rvInterests = view.findViewById(R.id.rvInterests);
        rvEvents = view.findViewById(R.id.rvEvents);
        rvPurchases = view.findViewById(R.id.rvPurchases);

        // Configurar RecyclerViews
        setupRecyclerViews();

        // Configurar eventos de clique
        setupClickListeners();

        // Carregar dados do usuário
        loadUserData();

        return view;
    }

    private void setupRecyclerViews() {
        // Configurar RecyclerView para interesses
        rvInterests.setLayoutManager(new LinearLayoutManager(getContext()));
        interestAdapter = new InterestAdapter(getContext());
        rvInterests.setAdapter(interestAdapter);

        // Configurar RecyclerView para eventos
        rvEvents.setLayoutManager(new LinearLayoutManager(getContext()));
        eventAdapter = new EventAdapter(getContext());
        rvEvents.setAdapter(eventAdapter);

        // Configurar RecyclerView para compras
        rvPurchases.setLayoutManager(new LinearLayoutManager(getContext()));
        purchaseAdapter = new PurchaseAdapter(getContext());
        rvPurchases.setAdapter(purchaseAdapter);
    }

    private void setupClickListeners() {
        btnAddInterest.setOnClickListener(v -> showAddInterestDialog());
        btnAddEvent.setOnClickListener(v -> showAddEventDialog());
        btnAddPurchase.setOnClickListener(v -> showAddPurchaseDialog());
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
            // Atualizar adapter de interesses
            if (currentUser.getInterests() != null && !currentUser.getInterests().isEmpty()) {
                interestAdapter.updateData(currentUser.getInterests());
            } else {
                // Lista vazia
                interestAdapter.updateData(new ArrayList<>());
            }

            // Atualizar adapter de eventos
            if (currentUser.getEvents() != null && !currentUser.getEvents().isEmpty()) {
                eventAdapter.updateData(currentUser.getEvents());
            } else {
                // Lista vazia
                eventAdapter.updateData(new ArrayList<>());
            }

            // Atualizar adapter de compras
            if (currentUser.getPurchases() != null && !currentUser.getPurchases().isEmpty()) {
                purchaseAdapter.updateData(currentUser.getPurchases());
            } else {
                // Lista vazia
                purchaseAdapter.updateData(new ArrayList<>());
            }
        }
    }

    private void showAddInterestDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_interest, null);
        builder.setView(dialogView);

        final Spinner spinnerCategory = dialogView.findViewById(R.id.spinnerCategory);
        final EditText etInterestName = dialogView.findViewById(R.id.etInterestName);
        final RatingBar ratingBarInterest = dialogView.findViewById(R.id.ratingBarInterest);
        final EditText etInterestDetails = dialogView.findViewById(R.id.etInterestDetails);

        builder.setTitle("Adicionar Interesse");
        builder.setPositiveButton("Adicionar", (dialog, which) -> {
            // Obter valores do formulário
            String name = etInterestName.getText().toString().trim();
            int interestLevel = (int) ratingBarInterest.getRating();
            String details = etInterestDetails.getText().toString().trim();

            if (!name.isEmpty()) {
                // Obter categoria selecionada
                FanInterest.Category category;
                switch (spinnerCategory.getSelectedItemPosition()) {
                    case 0:
                        category = FanInterest.Category.GAME;
                        break;
                    case 1:
                        category = FanInterest.Category.TEAM;
                        break;
                    case 2:
                        category = FanInterest.Category.PLAYER;
                        break;
                    case 3:
                        category = FanInterest.Category.TOURNAMENT;
                        break;
                    case 4:
                        category = FanInterest.Category.MERCHANDISE;
                        break;
                    case 5:
                        category = FanInterest.Category.CONTENT_CREATOR;
                        break;
                    default:
                        category = FanInterest.Category.GAME;
                        break;
                }

                // Criar e adicionar interesse
                FanInterest interest = new FanInterest(category, name, interestLevel, details);
                saveInterestToFirestore(interest);
            } else {
                Toast.makeText(getContext(), "Nome do interesse é obrigatório", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showAddEventDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_event, null);
        builder.setView(dialogView);

        final EditText etEventName = dialogView.findViewById(R.id.etEventName);
        final EditText etEventDate = dialogView.findViewById(R.id.etEventDate);
        final EditText etEventLocation = dialogView.findViewById(R.id.etEventLocation);
        final EditText etEventType = dialogView.findViewById(R.id.etEventType);
        final EditText etEventDetails = dialogView.findViewById(R.id.etEventDetails);

        builder.setTitle("Adicionar Evento");
        builder.setPositiveButton("Adicionar", (dialog, which) -> {
            // Obter valores do formulário
            String name = etEventName.getText().toString().trim();
            String dateStr = etEventDate.getText().toString().trim();
            String location = etEventLocation.getText().toString().trim();
            String eventType = etEventType.getText().toString().trim();
            String details = etEventDetails.getText().toString().trim();

            if (!name.isEmpty() && !dateStr.isEmpty() && !location.isEmpty()) {
                try {
                    // Converter string para data
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    Date date = sdf.parse(dateStr);

                    // Criar e adicionar evento
                    FanEvent event = new FanEvent(name, date, location, eventType, true);
                    event.setDetails(details);
                    saveEventToFirestore(event);

                } catch (ParseException e) {
                    Toast.makeText(getContext(), "Formato de data inválido. Use dd/mm/aaaa", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Nome, data e local são obrigatórios", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showAddPurchaseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_purchase, null);
        builder.setView(dialogView);

        final EditText etProductName = dialogView.findViewById(R.id.etProductName);
        final EditText etPurchaseDate = dialogView.findViewById(R.id.etPurchaseDate);
        final EditText etPrice = dialogView.findViewById(R.id.etPrice);
        final Spinner spinnerPurchaseType = dialogView.findViewById(R.id.spinnerPurchaseType);
        final EditText etPurchaseDetails = dialogView.findViewById(R.id.etPurchaseDetails);

        builder.setTitle("Adicionar Compra");
        builder.setPositiveButton("Adicionar", (dialog, which) -> {
            // Obter valores do formulário
            String productName = etProductName.getText().toString().trim();
            String dateStr = etPurchaseDate.getText().toString().trim();
            String priceStr = etPrice.getText().toString().trim();
            String details = etPurchaseDetails.getText().toString().trim();

            if (!productName.isEmpty() && !dateStr.isEmpty() && !priceStr.isEmpty()) {
                try {
                    // Converter string para data
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    Date date = sdf.parse(dateStr);

                    // Converter preço para double
                    double price = Double.parseDouble(priceStr.replace(",", "."));

                    // Obter tipo selecionado
                    FanPurchase.PurchaseType purchaseType;
                    switch (spinnerPurchaseType.getSelectedItemPosition()) {
                        case 0:
                            purchaseType = FanPurchase.PurchaseType.MERCHANDISE;
                            break;
                        case 1:
                            purchaseType = FanPurchase.PurchaseType.TICKET;
                            break;
                        case 2:
                            purchaseType = FanPurchase.PurchaseType.SUBSCRIPTION;
                            break;
                        case 3:
                            purchaseType = FanPurchase.PurchaseType.GAME;
                            break;
                        case 4:
                            purchaseType = FanPurchase.PurchaseType.IN_GAME_ITEM;
                            break;
                        default:
                            purchaseType = FanPurchase.PurchaseType.OTHER;
                            break;
                    }

                    // Criar e adicionar compra
                    FanPurchase purchase = new FanPurchase(productName, date, price, purchaseType);
                    purchase.setDetails(details);
                    savePurchaseToFirestore(purchase);

                } catch (ParseException e) {
                    Toast.makeText(getContext(), "Formato de data inválido. Use dd/mm/aaaa", Toast.LENGTH_SHORT).show();
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Formato de preço inválido", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Nome do produto, data e preço são obrigatórios", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void saveInterestToFirestore(FanInterest interest) {
        if (currentUser != null) {
            String userId = mAuth.getCurrentUser().getUid();

            // Adicionar interesse ao usuário atual
            if (currentUser.getInterests() == null) {
                currentUser.setInterests(new ArrayList<>());
            }
            currentUser.addInterest(interest);

            // Atualizar no Firestore
            db.collection("users").document(userId)
                    .set(currentUser)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Interesse adicionado com sucesso!", Toast.LENGTH_SHORT).show();
                        updateUI();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Erro ao salvar interesse: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void saveEventToFirestore(FanEvent event) {
        if (currentUser != null) {
            String userId = mAuth.getCurrentUser().getUid();

            // Adicionar evento ao usuário atual
            if (currentUser.getEvents() == null) {
                currentUser.setEvents(new ArrayList<>());
            }
            currentUser.addEvent(event);

            // Atualizar no Firestore
            db.collection("users").document(userId)
                    .set(currentUser)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Evento adicionado com sucesso!", Toast.LENGTH_SHORT).show();
                        updateUI();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Erro ao salvar evento: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void savePurchaseToFirestore(FanPurchase purchase) {
        if (currentUser != null) {
            String userId = mAuth.getCurrentUser().getUid();

            // Adicionar compra ao usuário atual
            if (currentUser.getPurchases() == null) {
                currentUser.setPurchases(new ArrayList<>());
            }
            currentUser.addPurchase(purchase);

            // Atualizar no Firestore
            db.collection("users").document(userId)
                    .set(currentUser)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Compra adicionada com sucesso!", Toast.LENGTH_SHORT).show();
                        updateUI();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Erro ao salvar compra: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Recarregar dados do usuário ao retornar para o fragment
        loadUserData();
    }
}