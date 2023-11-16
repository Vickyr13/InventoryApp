package com.example.inventoryapp.views.category;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.inventoryapp.R;
import com.example.inventoryapp.adapters.CategoriaAdapter;
import com.example.inventoryapp.models.CategoriaModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private FloatingActionButton addCategoryBtn;
    private RecyclerView recyclerView;
    private CategoriaAdapter categoriaAdapter;
    private List<CategoriaModel> categoriaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        addCategoryBtn = findViewById(R.id.add_category_btn);
        recyclerView = findViewById(R.id.recycler_view_Category);
        categoriaList = new ArrayList<>();
        categoriaAdapter = new CategoriaAdapter(this, categoriaList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(categoriaAdapter);

        addCategoryBtn.setOnClickListener((v) -> startActivity(new Intent(CategoryActivity.this, AddCategoryActivity.class)));


        // Cargar datos de Firestore
        loadCategoriaData();
        //editar datos de Firestore
        categoriaAdapter.setOnItemClickListener(new CategoriaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CategoriaModel categoria) {
                // Abrir la actividad AddCategoryActivity y pasar la información del elemento seleccionado
                Intent intent = new Intent(CategoryActivity.this, AddCategoryActivity.class);
                intent.putExtra("EDIT_MODE", true);
                intent.putExtra("id", categoria.getId());
                intent.putExtra("title", categoria.getTitle());
                intent.putExtra("imageUrl", categoria.getImageUrl());
                startActivity(intent);
            }
        });
    }


    private void loadCategoriaData() {
        FirebaseFirestore.getInstance().collection("categoria")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        // Manejar el error
                        return;
                    }

                    if (value != null) {
                        categoriaList.clear();
                        for (QueryDocumentSnapshot document : value) {
                            CategoriaModel categoria = document.toObject(CategoriaModel.class);
                            categoriaList.add(categoria);
                        }
                        categoriaAdapter.notifyDataSetChanged();
                    }
                });
    }
}