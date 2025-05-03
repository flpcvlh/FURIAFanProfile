package com.furiagg.fanprofile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.furiagg.fanprofile.R;
import com.furiagg.fanprofile.model.FanInterest;

import java.util.ArrayList;
import java.util.List;

public class InterestAdapter extends RecyclerView.Adapter<InterestAdapter.ViewHolder> {

    private List<FanInterest> interestList;
    private Context context;

    public InterestAdapter(Context context) {
        this.context = context;
        this.interestList = new ArrayList<>();
    }

    public void updateData(List<FanInterest> newList) {
        this.interestList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_interest, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FanInterest interest = interestList.get(position);

        // Configurar ícone da categoria
        setIconForCategory(holder.ivInterestIcon, interest.getCategory());

        // Configurar categoria
        holder.tvInterestCategory.setText(getCategoryName(interest.getCategory()));

        // Configurar nome do interesse
        holder.tvInterestName.setText(interest.getName());

        // Configurar detalhes
        if (interest.getDetails() != null && !interest.getDetails().isEmpty()) {
            holder.tvInterestDetails.setText(interest.getDetails());
            holder.tvInterestDetails.setVisibility(View.VISIBLE);
        } else {
            holder.tvInterestDetails.setVisibility(View.GONE);
        }

        // Configurar avaliação
        holder.ratingBar.setRating(interest.getInterestLevel());
    }

    private void setIconForCategory(ImageView imageView, FanInterest.Category category) {
        switch (category) {
            case GAME:
                imageView.setImageResource(R.drawable.ic_game);
                break;
            case TEAM:
                imageView.setImageResource(R.drawable.ic_team);
                break;
            case PLAYER:
                imageView.setImageResource(R.drawable.ic_player);
                break;
            case TOURNAMENT:
                imageView.setImageResource(R.drawable.ic_tournament);
                break;
            case MERCHANDISE:
                imageView.setImageResource(R.drawable.ic_merchandise);
                break;
            case CONTENT_CREATOR:
                imageView.setImageResource(R.drawable.ic_content_creator);
                break;
            default:
                imageView.setImageResource(R.drawable.ic_add_interest);
                break;
        }
    }

    private String getCategoryName(FanInterest.Category category) {
        switch (category) {
            case GAME:
                return "JOGO";
            case TEAM:
                return "TIME";
            case PLAYER:
                return "JOGADOR";
            case TOURNAMENT:
                return "TORNEIO";
            case MERCHANDISE:
                return "PRODUTO";
            case CONTENT_CREATOR:
                return "CRIADOR DE CONTEÚDO";
            default:
                return "OUTRO";
        }
    }

    @Override
    public int getItemCount() {
        return interestList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivInterestIcon;
        TextView tvInterestCategory;
        TextView tvInterestName;
        TextView tvInterestDetails;
        RatingBar ratingBar;

        ViewHolder(View itemView) {
            super(itemView);
            ivInterestIcon = itemView.findViewById(R.id.ivInterestIcon);
            tvInterestCategory = itemView.findViewById(R.id.tvInterestCategory);
            tvInterestName = itemView.findViewById(R.id.tvInterestName);
            tvInterestDetails = itemView.findViewById(R.id.tvInterestDetails);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }
}