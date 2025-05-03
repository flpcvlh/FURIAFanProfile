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
import com.furiagg.fanprofile.model.SocialMedia;

import java.util.ArrayList;
import java.util.List;

public class SocialMediaAdapter extends RecyclerView.Adapter<SocialMediaAdapter.ViewHolder> {

    private List<SocialMedia> socialMediaList;
    private Context context;

    public SocialMediaAdapter(Context context) {
        this.context = context;
        this.socialMediaList = new ArrayList<>();
    }

    public void updateData(List<SocialMedia> newList) {
        this.socialMediaList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_social_media, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SocialMedia socialMedia = socialMediaList.get(position);

        // Configurar ícone da plataforma
        setIconForPlatform(holder.ivSocialMediaIcon, socialMedia.getPlatform());

        // Configurar texto da plataforma
        holder.tvSocialMediaPlatform.setText(getPlatformName(socialMedia.getPlatform()));

        // Configurar nome de usuário
        holder.tvSocialMediaUsername.setText(socialMedia.getUsername());

        // Configurar ícone de status de conexão
        holder.ivConnectedStatus.setVisibility(socialMedia.isConnected() ? View.VISIBLE : View.GONE);
    }

    private void setIconForPlatform(ImageView imageView, SocialMedia.Platform platform) {
        switch (platform) {
            case TWITTER:
                imageView.setImageResource(R.drawable.ic_twitter);
                break;
            case INSTAGRAM:
                imageView.setImageResource(R.drawable.ic_instagram);
                break;
            case FACEBOOK:
                imageView.setImageResource(R.drawable.ic_facebook);
                break;
            case TWITCH:
                imageView.setImageResource(R.drawable.ic_twitch);
                break;
            case YOUTUBE:
                imageView.setImageResource(R.drawable.ic_youtube);
                break;
            case TIKTOK:
                imageView.setImageResource(R.drawable.ic_tiktok);
                break;
            case DISCORD:
                imageView.setImageResource(R.drawable.ic_discord);
                break;
            default:
                imageView.setImageResource(R.drawable.ic_social_media);
                break;
        }
    }

    private String getPlatformName(SocialMedia.Platform platform) {
        switch (platform) {
            case TWITTER:
                return "Twitter";
            case INSTAGRAM:
                return "Instagram";
            case FACEBOOK:
                return "Facebook";
            case TWITCH:
                return "Twitch";
            case YOUTUBE:
                return "YouTube";
            case TIKTOK:
                return "TikTok";
            case DISCORD:
                return "Discord";
            default:
                return "Outra";
        }
    }

    @Override
    public int getItemCount() {
        return socialMediaList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivSocialMediaIcon;
        TextView tvSocialMediaPlatform;
        TextView tvSocialMediaUsername;
        ImageView ivConnectedStatus;

        ViewHolder(View itemView) {
            super(itemView);
            ivSocialMediaIcon = itemView.findViewById(R.id.ivSocialMediaIcon);
            tvSocialMediaPlatform = itemView.findViewById(R.id.tvSocialMediaPlatform);
            tvSocialMediaUsername = itemView.findViewById(R.id.tvSocialMediaUsername);
            ivConnectedStatus = itemView.findViewById(R.id.ivConnectedStatus);
        }
    }
}