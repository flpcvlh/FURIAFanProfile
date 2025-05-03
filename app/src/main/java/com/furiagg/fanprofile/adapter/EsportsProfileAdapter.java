package com.furiagg.fanprofile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.furiagg.fanprofile.R;
import com.furiagg.fanprofile.model.EsportsProfile;

import java.util.ArrayList;
import java.util.List;

public class EsportsProfileAdapter extends RecyclerView.Adapter<EsportsProfileAdapter.ViewHolder> {

    private List<EsportsProfile> profileList;
    private Context context;

    public EsportsProfileAdapter(Context context) {
        this.context = context;
        this.profileList = new ArrayList<>();
    }

    public void updateData(List<EsportsProfile> newList) {
        this.profileList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_esports_profile, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EsportsProfile profile = profileList.get(position);

        // Configurar ícone da plataforma
        setIconForPlatform(holder.ivPlatformIcon, profile.getPlatform());

        // Configurar nome da plataforma
        holder.tvPlatformName.setText(getPlatformName(profile.getPlatform()));

        // Configurar nome de usuário
        holder.tvUsername.setText(profile.getUsername());

        // Configurar foco em jogos
        holder.tvGameFocus.setText(profile.getGamesFocus());

        // Configurar ícone de verificação
        holder.ivVerified.setVisibility(profile.isVerified() ? View.VISIBLE : View.GONE);
    }

    private void setIconForPlatform(ImageView imageView, EsportsProfile.Platform platform) {
        switch (platform) {
            case STEAM:
                imageView.setImageResource(R.drawable.ic_steam);
                break;
            case BATTLENET:
                imageView.setImageResource(R.drawable.ic_battlenet);
                break;
            case RIOT:
                imageView.setImageResource(R.drawable.ic_riot);
                break;
            case EPIC:
                imageView.setImageResource(R.drawable.ic_epic);
                break;
            case FACEIT:
                imageView.setImageResource(R.drawable.ic_faceit);
                break;
            case ESEA:
                imageView.setImageResource(R.drawable.ic_esea);
                break;
            case GAMERSCLUB:
                imageView.setImageResource(R.drawable.ic_gamersclub);
                break;
            default:
                imageView.setImageResource(R.drawable.ic_games);
                break;
        }
    }

    private String getPlatformName(EsportsProfile.Platform platform) {
        switch (platform) {
            case STEAM:
                return "Steam";
            case BATTLENET:
                return "Battle.net";
            case RIOT:
                return "Riot Games";
            case EPIC:
                return "Epic Games";
            case FACEIT:
                return "FACEIT";
            case ESEA:
                return "ESEA";
            case GAMERSCLUB:
                return "GamersClub";
            default:
                return "Outra";
        }
    }

    @Override
    public int getItemCount() {
        return profileList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPlatformIcon;
        TextView tvPlatformName;
        TextView tvUsername;
        TextView tvGameFocus;
        ImageView ivVerified;

        ViewHolder(View itemView) {
            super(itemView);
            ivPlatformIcon = itemView.findViewById(R.id.ivPlatformIcon);
            tvPlatformName = itemView.findViewById(R.id.tvPlatformName);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvGameFocus = itemView.findViewById(R.id.tvGameFocus);
            ivVerified = itemView.findViewById(R.id.ivVerified);
        }
    }
}
