package com.example.inventoryapp.views.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.inventoryapp.R;
import com.example.inventoryapp.views.category.CategoryActivity;
import com.example.inventoryapp.views.login_logout.LoginActivity;
import com.example.inventoryapp.views.login_logout.RegisterActivity;
import com.example.inventoryapp.views.products.ProductActivity;
import com.example.inventoryapp.views.sales.SalesActivity;
import com.example.inventoryapp.views.suppliers.SuppliersActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    TextView email;
    FloatingActionButton floatingActionButton;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    ImageView product_Image, suppliers_Image, sales_Image, category_Image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floatingActionButton = findViewById(R.id.logoutIcon);
        email = findViewById(R.id.desc);

        product_Image = findViewById(R.id.productImage);
        suppliers_Image = findViewById(R.id.suppliersImage);
        sales_Image = findViewById(R.id.salesImage);
        category_Image = findViewById(R.id.categoryImage);

        category_Image.setOnClickListener(v->startActivity(new Intent(MainActivity.this, CategoryActivity.class)));
        product_Image.setOnClickListener(v->startActivity(new Intent(MainActivity.this, ProductActivity.class)));
        sales_Image.setOnClickListener(v->startActivity(new Intent(MainActivity.this, SalesActivity.class)));
        suppliers_Image.setOnClickListener(v->startActivity(new Intent(MainActivity.this, SuppliersActivity.class)));

    }
}