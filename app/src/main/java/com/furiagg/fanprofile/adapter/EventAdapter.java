package com.furiagg.fanprofile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.furiagg.fanprofile.R;
import com.furiagg.fanprofile.model.FanEvent;
import com.google.android.material.chip.Chip;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private List<FanEvent> eventList;
    private Context context;

    public EventAdapter(Context context) {
        this.context = context;
        this.eventList = new ArrayList<>();
    }

    public void updateData(List<FanEvent> newList) {
        this.eventList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FanEvent event = eventList.get(position);

        // Configurar nome do evento
        holder.tvEventName.setText(event.getName());

        // Configurar data
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        holder.tvEventDate.setText(sdf.format(event.getDate()));

        // Configurar local
        holder.tvEventLocation.setText(event.getLocation());

        // Configurar tipo
        String eventType = "Tipo: " + event.getEventType();
        holder.tvEventType.setText(eventType);

        // Configurar detalhes
        if (event.getDetails() != null && !event.getDetails().isEmpty()) {
            holder.tvEventDetails.setText(event.getDetails());
            holder.tvEventDetails.setVisibility(View.VISIBLE);
        } else {
            holder.tvEventDetails.setVisibility(View.GONE);
        }

        // Configurar chip de participação
        holder.chipAttended.setVisibility(event.isAttended() ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvEventName;
        TextView tvEventDate;
        TextView tvEventLocation;
        TextView tvEventType;
        TextView tvEventDetails;
        Chip chipAttended;

        ViewHolder(View itemView) {
            super(itemView);
            tvEventName = itemView.findViewById(R.id.tvEventName);
            tvEventDate = itemView.findViewById(R.id.tvEventDate);
            tvEventLocation = itemView.findViewById(R.id.tvEventLocation);
            tvEventType = itemView.findViewById(R.id.tvEventType);
            tvEventDetails = itemView.findViewById(R.id.tvEventDetails);
            chipAttended = itemView.findViewById(R.id.chipAttended);
        }
    }
}