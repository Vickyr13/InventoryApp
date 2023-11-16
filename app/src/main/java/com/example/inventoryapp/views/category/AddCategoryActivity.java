package com.example.inventoryapp.views.category;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.inventoryapp.adapters.CategoriaAdapter;
import com.example.inventoryapp.models.CategoriaModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import com.example.inventoryapp.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class AddCategoryActivity extends AppCompatActivity {

        private EditText notesTitleText;
        private ImageView categoryImage;
        private Button selectImageBtn;
        private ImageButton saveCategoryBtn;

        private FirebaseFirestore firestore;
        private FirebaseStorage storage;
        private StorageReference storageRef;
        private TextView pageTitle;


    private static final int PICK_IMAGE_REQUEST = 1;
        private Uri imageUri;
        private boolean isEditMode = false;


    // Utilizando ActivityResultLauncher para manejar la selección de imágenes
        private final ActivityResultLauncher<String> imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result != null) {
                            imageUri = result;
                            categoryImage.setImageURI(imageUri);
                        }
                    }
                }
        );

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_category);

            firestore = FirebaseFirestore.getInstance();
            storage = FirebaseStorage.getInstance();
            storageRef = storage.getReference();

            notesTitleText = findViewById(R.id.category_title_text);
            categoryImage = findViewById(R.id.category_image);
            selectImageBtn = findViewById(R.id.select_image_btn);
            saveCategoryBtn = findViewById(R.id.save_category_btn);
            pageTitle = findViewById(R.id.page_title);

            selectImageBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openImagePicker();
                }
            });

            saveCategoryBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveToFirestore();
                }
            });

            //mode edit
            Intent intent = getIntent();
            if(intent.hasExtra("EDIT_MODE")){
                isEditMode = intent.getBooleanExtra("EDIT_MODE", false);
                if(isEditMode){
                    setupEditMode();
                    String categoryId =intent.getStringExtra("id");
                    String categoryTitle =intent.getStringExtra("title");
                    String categoryImageUrl =intent.getStringExtra("imageUrl");
                    notesTitleText.setText(categoryTitle);
                    if (categoryImageUrl != null && !categoryImageUrl.isEmpty()) {
                        // Cargar la imagen existente utilizando Glide
                        Glide.with(this)
                                .load(categoryImageUrl)
                                .into(categoryImage);
                    }
                }
            }
        }

        private void openImagePicker() {
            // Lanzar el selector de imágenes utilizando ActivityResultLauncher
            imagePickerLauncher.launch("image/*");
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                imageUri = data.getData();
                categoryImage.setImageURI(imageUri);
            }
        }

        private void saveToFirestore() {
            String title = notesTitleText.getText().toString();

            if (title.isEmpty()) {
                Toast.makeText(this, "Por favor introduce un título", Toast.LENGTH_SHORT).show();
                return;
            }

            if (imageUri != null) {
                // Subir imagen a Firebase Storage
                StorageReference imageRef = storageRef.child("images/" + System.currentTimeMillis() + ".jpg");
                imageRef.putFile(imageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Obtener la URL de la imagen y guardar en Firestore
                                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl = uri.toString();
                                        saveCategoryData(title, imageUrl);
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddCategoryActivity.this, "Error al subir la imagen", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                // No hay imagen, solo guardar los datos de texto
                saveCategoryData(title, null);
            }
        }

        private void saveCategoryData(String title, String imageUrl) {

            Map<String, Object> data = new HashMap<>();
            data.put("title", title);
            if (imageUrl != null) {
                data.put("imageUrl", imageUrl);
            }

            if(isEditMode){
                String categoryId = getIntent().getStringExtra("id");
                firestore.collection("categoria")
                        .document(categoryId)
                        .update(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toasty.success(AddCategoryActivity.this, "Actualizada correctamenta!", Toasty.LENGTH_SHORT, true).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toasty.error(AddCategoryActivity.this, "Error al momento de actualizar", Toasty.LENGTH_SHORT, true).show();
                            }
                        });
            }else {
                firestore.collection("categoria")
                        .add(data)
                        .addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Toast.makeText(AddCategoryActivity.this, "Se guardo con exito!!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddCategoryActivity.this, "Error no se guardo", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }

        //edit
        private void setupEditMode(){
            // Cambiar el texto del botón de guardar
            //saveCategoryBtn.setText("Actualizar");
            pageTitle.setText("Editar categoria");

            // Obtener datos existentes y cargarlos en la interfaz
            String existingTitle = getIntent().getStringExtra("title");
            String existingImageUrl = getIntent().getStringExtra("imageUrl");

            notesTitleText.setText(existingTitle);

            if (existingImageUrl != null && !existingImageUrl.isEmpty()) {
                // Cargar la imagen existente utilizando alguna biblioteca de carga de imágenes
                // (por ejemplo, Glide o Picasso)
                // imageView.load(existingImageUrl);
                // Cargar la imagen existente utilizando Glide
                Glide.with(this)
                        .load(existingImageUrl)
                        .into(categoryImage);
            }
        }

}