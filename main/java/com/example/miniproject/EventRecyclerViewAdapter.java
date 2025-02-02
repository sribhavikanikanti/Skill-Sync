package com.example.miniproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.MyViewHolder> {

    private ArrayList<EventModel> eventModels;
    private Context context;

    public EventRecyclerViewAdapter(Context context, ArrayList<EventModel> eventModels) {
        this.context = context;
        this.eventModels = eventModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.event_recycler_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        EventModel event = eventModels.get(position);

        holder.event_name.setText(event.getEventName() != null ? event.getEventName() : "No Name");
        holder.description.setText(event.getDescription() != null ? event.getDescription() : "No Description");

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EventDetailActivity.class);
            intent.putExtra("event_id", event.getEventId()); // Pass event ID
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return eventModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView event_name, description;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            event_name = itemView.findViewById(R.id.event_name);
            description = itemView.findViewById(R.id.description);
        }
    }
}
