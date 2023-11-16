package com.example.inventoryapp.views.suppliers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.inventoryapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SuppliersActivity extends AppCompatActivity {

    private FloatingActionButton addSuppliersBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suppliers);
        addSuppliersBtn = findViewById(R.id.add_suppliers_btn);

        addSuppliersBtn.setOnClickListener(v->startActivity(new Intent(SuppliersActivity.this, AddSuppliersActivity.class)));
    }
}