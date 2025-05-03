// Arquivo: app/src/main/java/com/furiagg/fanprofile/adapter/DocumentAdapter.java
package com.furiagg.fanprofile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.furiagg.fanprofile.R;
import com.furiagg.fanprofile.model.Document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.ViewHolder> {

    private List<Document> documentList;
    private Context context;

    public DocumentAdapter(Context context) {
        this.context = context;
        this.documentList = new ArrayList<>();
    }

    public void updateData(List<Document> newList) {
        this.documentList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_document, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Document document = documentList.get(position);

        // Configurar nome do documento
        holder.tvDocumentName.setText(document.getName());

        // Configurar data de adição
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dateStr = "Adicionado em: " + sdf.format(document.getDateAdded());
        holder.tvDocumentDate.setText(dateStr);

        // Carregar thumbnail com Glide
        if (document.getThumbnailUrl() != null && !document.getThumbnailUrl().isEmpty()) {
            Glide.with(context)
                    .load(document.getThumbnailUrl())
                    .placeholder(R.drawable.ic_document)
                    .into(holder.ivDocumentThumbnail);
        } else {
            holder.ivDocumentThumbnail.setImageResource(R.drawable.ic_document);
        }

        // Configurar ícone de verificação
        holder.ivVerifiedDocument.setVisibility(document.isVerified() ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return documentList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivDocumentThumbnail;
        TextView tvDocumentName;
        TextView tvDocumentDate;
        ImageView ivVerifiedDocument;

        ViewHolder(View itemView) {
            super(itemView);
            ivDocumentThumbnail = itemView.findViewById(R.id.ivDocumentThumbnail);
            tvDocumentName = itemView.findViewById(R.id.tvDocumentName);
            tvDocumentDate = itemView.findViewById(R.id.tvDocumentDate);
            ivVerifiedDocument = itemView.findViewById(R.id.ivVerifiedDocument);
        }
    }
}