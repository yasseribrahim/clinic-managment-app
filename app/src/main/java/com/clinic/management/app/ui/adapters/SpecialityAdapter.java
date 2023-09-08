package com.clinic.management.app.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.clinic.management.app.databinding.ItemSpecialityBinding;
import com.clinic.management.app.models.Speciality;

import java.util.List;

public class SpecialityAdapter extends RecyclerView.Adapter<SpecialityAdapter.ViewHolder> {
    private List<Speciality> specialities;
    private OnSpecialityActionListener listener;

    public SpecialityAdapter(List<Speciality> specialities, OnSpecialityActionListener listener) {
        this.specialities = specialities;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSpecialityBinding binding = ItemSpecialityBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new ViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Speciality speciality = specialities.get(position);
        holder.binding.name.setText(speciality.getName());

        holder.binding.containerEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(speciality);
            }
        });
        holder.binding.containerRemove.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(speciality);
            }
        });
    }

    @Override
    public int getItemCount() {
        return specialities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemSpecialityBinding binding;

        public ViewHolder(@NonNull View view, ItemSpecialityBinding binding) {
            super(view);
            this.binding = binding;
        }
    }

    public interface OnSpecialityActionListener {
        void onEditClick(Speciality speciality);

        void onDeleteClick(Speciality speciality);
    }
}