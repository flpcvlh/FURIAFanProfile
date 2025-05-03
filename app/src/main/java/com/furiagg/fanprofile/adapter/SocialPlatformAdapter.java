package com.furiagg.fanprofile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.furiagg.fanprofile.R;
import com.furiagg.fanprofile.ui.SocialMediaIntegrationActivity;

import java.util.ArrayList;
import java.util.List;

public class SocialPlatformAdapter extends RecyclerView.Adapter<SocialPlatformAdapter.ViewHolder> {

    private List<SocialMediaIntegrationActivity.SocialPlatformItem> platformList;
    private Context context;
    private SocialPlatformListener listener;

    public SocialPlatformAdapter(Context context, SocialPlatformListener listener) {
        this.context = context;
        this.listener = listener;
        this.platformList = new ArrayList<>();
    }

    public void updateData(List<SocialMediaIntegrationActivity.SocialPlatformItem> newList) {
        this.platformList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_social_platform, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SocialMediaIntegrationActivity.SocialPlatformItem platform = platformList.get(position);

        // Configurar ícone da plataforma
        holder.ivPlatformIcon.setImageResource(platform.getIconResource());

        // Configurar nome da plataforma
        holder.tvPlatformName.setText(platform.getName());

        // Configurar descrição
        holder.tvPlatformDescription.setText(platform.getDescription());

        // Configurar visibilidade dos layouts com base no status de conexão
        holder.btnConnect.setVisibility(platform.isConnected() ? View.GONE : View.VISIBLE);
        holder.layoutConnected.setVisibility(platform.isConnected() ? View.VISIBLE : View.GONE);

        if (platform.isConnected() && platform.getUsername() != null) {
            holder.tvConnectedUsername.setText("@" + platform.getUsername());
        } else if (platform.isConnected()) {
            holder.tvConnectedUsername.setText("@usuario_" + platform.getName().toLowerCase());
        }

        // Configurar eventos de clique
        holder.btnConnect.setOnClickListener(v -> {
            if (listener != null) {
                listener.onConnectClicked(platform);
            }
        });

        holder.btnDisconnect.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDisconnectClicked(platform);
            }
        });
    }

    @Override
    public int getItemCount() {
        return platformList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPlatformIcon;
        TextView tvPlatformName;
        TextView tvPlatformDescription;
        Button btnConnect;
        LinearLayout layoutConnected;
        TextView tvConnectedUsername;
        Button btnDisconnect;

        ViewHolder(View itemView) {
            super(itemView);
            ivPlatformIcon = itemView.findViewById(R.id.ivPlatformIcon);
            tvPlatformName = itemView.findViewById(R.id.tvPlatformName);
            tvPlatformDescription = itemView.findViewById(R.id.tvPlatformDescription);
            btnConnect = itemView.findViewById(R.id.btnConnect);
            layoutConnected = itemView.findViewById(R.id.layoutConnected);
            tvConnectedUsername = itemView.findViewById(R.id.tvConnectedUsername);
            btnDisconnect = itemView.findViewById(R.id.btnDisconnect);
        }
    }

    public interface SocialPlatformListener {
        void onConnectClicked(SocialMediaIntegrationActivity.SocialPlatformItem platform);
        void onDisconnectClicked(SocialMediaIntegrationActivity.SocialPlatformItem platform);
    }
}