package com.example.inventoryapp.views.category;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.inventoryapp.R;
import com.example.inventoryapp.views.main.MainActivity;
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