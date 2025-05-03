// Arquivo: app/src/main/java/com/furiagg/fanprofile/ui/DocumentUploadActivity.java
package com.furiagg.fanprofile.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.furiagg.fanprofile.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DocumentUploadActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PICK_IMAGE = 2;
    private static final int REQUEST_CAMERA_PERMISSION = 101;

    private ImageView ivBackButton;
    private ImageView ivDocumentPreview;
    private View ivDocumentPlaceholder;
    private TextView tvNoDocumentText;
    private Button btnTakePhoto;
    private Button btnChooseGallery;
    private Button btnVerifyDocument;
    private ProgressBar progressBarVerification;

    private Uri documentImageUri;
    private String currentPhotoPath;

    private FirebaseAuth mAuth;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_upload);

        // Inicializar Firebase
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        // Vincular elementos da UI
        ivBackButton = findViewById(R.id.ivBackButton);
        ivDocumentPreview = findViewById(R.id.ivDocumentPreview);
        ivDocumentPlaceholder = findViewById(R.id.ivDocumentPlaceholder);
        tvNoDocumentText = findViewById(R.id.tvNoDocumentText);
        btnTakePhoto = findViewById(R.id.btnTakePhoto);
        btnChooseGallery = findViewById(R.id.btnChooseGallery);
        btnVerifyDocument = findViewById(R.id.btnVerifyDocument);
        progressBarVerification = findViewById(R.id.progressBarVerification);

        // Configurar eventos de clique
        ivBackButton.setOnClickListener(v -> onBackPressed());

        btnTakePhoto.setOnClickListener(v -> {
            if (checkCameraPermission()) {
                dispatchTakePictureIntent();
            } else {
                requestCameraPermission();
            }
        });

        btnChooseGallery.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_PICK_IMAGE);
        });

        btnVerifyDocument.setOnClickListener(v -> {
            if (documentImageUri != null) {
                verifyDocument();
            } else {
                Toast.makeText(this, "Selecione uma imagem do documento primeiro", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.CAMERA},
                REQUEST_CAMERA_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Permissão de câmera negada", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, "Erro ao criar arquivo de imagem", Toast.LENGTH_SHORT).show();
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.furiagg.fanprofile.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                // Foto tirada pela câmera
                File file = new File(currentPhotoPath);
                documentImageUri = Uri.fromFile(file);
                displayImage();
            } else if (requestCode == REQUEST_PICK_IMAGE && data != null) {
                // Imagem selecionada da galeria
                documentImageUri = data.getData();
                displayImage();
            }
        }
    }

    private void displayImage() {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), documentImageUri);
            ivDocumentPreview.setImageBitmap(bitmap);
            ivDocumentPreview.setVisibility(View.VISIBLE);
            ivDocumentPlaceholder.setVisibility(View.GONE);
            tvNoDocumentText.setVisibility(View.GONE);
            btnVerifyDocument.setEnabled(true);
        } catch (IOException e) {
            Toast.makeText(this, "Erro ao exibir imagem", Toast.LENGTH_SHORT).show();
        }
    }

    private void verifyDocument() {
        progressBarVerification.setVisibility(View.VISIBLE);
        btnVerifyDocument.setEnabled(false);

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), documentImageUri);

            // Criar objeto InputImage
            InputImage image = InputImage.fromBitmap(bitmap, 0);

            // Detectar texto e face em paralelo
            detectTextInDocument(image);
            detectFaceInDocument(image);

        } catch (IOException e) {
            progressBarVerification.setVisibility(View.GONE);
            btnVerifyDocument.setEnabled(true);
            Toast.makeText(this, "Erro ao processar imagem", Toast.LENGTH_SHORT).show();
        }
    }

    private void detectTextInDocument(InputImage image) {
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        recognizer.process(image)
                .addOnSuccessListener(text -> {
                    // Verificar se há texto suficiente para ser um documento válido
                    if (text.getText().length() > 50) {
                        // Extrair informações relevantes como nome, CPF, etc.
                        String extractedText = text.getText();
                        uploadDocumentToStorage(extractedText);
                    } else {
                        progressBarVerification.setVisibility(View.GONE);
                        btnVerifyDocument.setEnabled(true);
                        Toast.makeText(DocumentUploadActivity.this,
                                "Documento inválido. Texto insuficiente.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    progressBarVerification.setVisibility(View.GONE);
                    btnVerifyDocument.setEnabled(true);
                    Toast.makeText(DocumentUploadActivity.this,
                            "Falha ao reconhecer texto: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void detectFaceInDocument(InputImage image) {
        FaceDetectorOptions options = new FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                .build();

        FaceDetector detector = FaceDetection.getClient(options);

        detector.process(image)
                .addOnSuccessListener(faces -> {
                    // Verifica se detectou alguma face no documento
                    if (faces.size() > 0) {
                        // Documento contém uma face, provavelmente válido
                    } else {
                        // Sem face detectada, pode não ser um documento de identidade
                        Toast.makeText(DocumentUploadActivity.this,
                                "Atenção: Nenhuma face detectada no documento", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(DocumentUploadActivity.this,
                            "Falha ao detectar face: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void uploadDocumentToStorage(String extractedText) {
        // Criar referência para o Storage
        String userId = mAuth.getCurrentUser().getUid();
        StorageReference storageRef = storage.getReference();
        StorageReference documentRef = storageRef.child("documents/" + userId + "/id_document.jpg");

        // Fazer upload da imagem
        documentRef.putFile(documentImageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Obter URL de download
                    documentRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Salvar URL no Firestore e navegar para a tela de verificação
                        navigateToVerification(uri.toString(), extractedText);
                    });
                })
                .addOnFailureListener(e -> {
                    progressBarVerification.setVisibility(View.GONE);
                    btnVerifyDocument.setEnabled(true);
                    Toast.makeText(DocumentUploadActivity.this,
                            "Falha ao fazer upload: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void navigateToVerification(String documentUrl, String extractedText) {
        Intent intent = new Intent(DocumentUploadActivity.this, DocumentVerificationActivity.class);
        intent.putExtra("document_url", documentUrl);
        intent.putExtra("extracted_text", extractedText);
        startActivity(intent);
        finish();
    }
}