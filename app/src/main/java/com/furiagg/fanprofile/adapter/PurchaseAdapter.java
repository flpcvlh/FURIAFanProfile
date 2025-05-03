package com.furiagg.fanprofile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.furiagg.fanprofile.R;
import com.furiagg.fanprofile.model.FanPurchase;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.ViewHolder> {

    private List<FanPurchase> purchaseList;
    private Context context;

    public PurchaseAdapter(Context context) {
        this.context = context;
        this.purchaseList = new ArrayList<>();
    }

    public void updateData(List<FanPurchase> newList) {
        this.purchaseList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_purchase, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FanPurchase purchase = purchaseList.get(position);

        // Configurar nome do produto
        holder.tvProductName.setText(purchase.getProductName());

        // Configurar data de compra
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dateStr = "Comprado em: " + sdf.format(purchase.getPurchaseDate());
        holder.tvPurchaseDate.setText(dateStr);

        // Configurar tipo de compra
        String purchaseType = "Categoria: " + getPurchaseTypeName(purchase.getType());
        holder.tvPurchaseType.setText(purchaseType);

        // Configurar preço
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        holder.tvPrice.setText(currencyFormat.format(purchase.getPrice()));
    }

    private String getPurchaseTypeName(FanPurchase.PurchaseType type) {
        switch (type) {
            case MERCHANDISE:
                return "Merchandise";
            case TICKET:
                return "Ingresso";
            case SUBSCRIPTION:
                return "Assinatura";
            case GAME:
                return "Jogo";
            case IN_GAME_ITEM:
                return "Item in-game";
            default:
                return "Outro";
        }
    }

    @Override
    public int getItemCount() {
        return purchaseList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName;
        TextView tvPurchaseDate;
        TextView tvPurchaseType;
        TextView tvPrice;

        ViewHolder(View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvPurchaseDate = itemView.findViewById(R.id.tvPurchaseDate);
            tvPurchaseType = itemView.findViewById(R.id.tvPurchaseType);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}