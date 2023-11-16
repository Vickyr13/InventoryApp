package com.example.inventoryapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.inventoryapp.R;
import com.example.inventoryapp.models.CategoriaModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.ViewHolder> {

    private final List<CategoriaModel> categoriaList;
    private final Context context;
    private OnItemClickListener itemClickListener;


    public CategoriaAdapter(Context context, List<CategoriaModel> categoriaList) {
        this.context = context;
        this.categoriaList = categoriaList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoriaModel categoria = categoriaList.get(position);

        holder.titleTextView.setText(categoria.getTitle());

        // Cargar la imagen utilizando Glide
        Glide.with(context)
                .load(categoria.getImageUrl())
                .placeholder(R.drawable.img_productos)
                .error(R.drawable.img_productos)
                .into(holder.categoryImageView);

        //edit
        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(categoria);
            }
        });

        // Configurar el clic de eliminación
        holder.deleteCategoryImageView.setOnClickListener(v -> {
            // Llamar al método para eliminar la categoría
            showDeleteConfirmationDialog(categoria);
        });
    }


    @Override
    public int getItemCount() {
        return categoriaList.size();
    }
    //edit
    public interface OnItemClickListener {
        void onItemClick(CategoriaModel categoria);
    }
    //edit
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public ImageView categoryImageView;
        public ImageView deleteCategoryImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.item_category_title);
            categoryImageView = itemView.findViewById(R.id.item_category_image);
            deleteCategoryImageView = itemView.findViewById(R.id.delete_category);
        }
    }
    private void showDeleteConfirmationDialog(CategoriaModel categoria) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirmar eliminación");
        builder.setMessage("¿Estás seguro de que quieres eliminar esta categoría?");
        builder.setPositiveButton("Eliminar", (dialog, which) -> {
            // Llamar al método para eliminar la categoría de Firestore
            deleteCategoriaFromFirestore(categoria);
        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void deleteCategoriaFromFirestore(CategoriaModel categoria) {
        FirebaseFirestore.getInstance().collection("categoria")
                .whereEqualTo("imageUrl", categoria.getImageUrl())
                .whereEqualTo("title", categoria.getTitle())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                document.getReference().delete()
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(context, "Categoría eliminada correctamente", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(context, "Error al eliminar la categoría", Toast.LENGTH_SHORT).show();
                                        });
                                break; // Solo necesitas eliminar un documento si hay coincidencias múltiples (lo cual no debería ocurrir en este caso)
                            }
                        }
                    }
                });
    }

}


