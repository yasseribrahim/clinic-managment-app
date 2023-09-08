package com.clinic.management.app.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.clinic.management.app.R;
import com.clinic.management.app.databinding.ItemDoctorBinding;
import com.clinic.management.app.models.User;

import java.util.List;

public class DoctorsAdapter extends RecyclerView.Adapter<DoctorsAdapter.ViewHolder> {
    private List<User> users;
    private SelectedItemsListener selectedItemsListener;

    public DoctorsAdapter(List<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDoctorBinding binding = ItemDoctorBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new ViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        holder.binding.name.setText(user.getFullName());
        holder.binding.speciality.setText(user.getSpeciality());
        holder.binding.price.setText(user.getPrice() + " $");
        holder.binding.username.setText(user.getUsername());
        holder.binding.phone.setText(user.getPhone());
        holder.binding.address.setText(user.getAddress());
        Glide.with(holder.binding.image.getContext()).load(user.getImageProfile()).placeholder(R.drawable.ic_account_circle).into(holder.binding.image);

        holder.binding.getRoot().setOnClickListener(v -> {

        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemDoctorBinding binding;

        public ViewHolder(@NonNull View view, ItemDoctorBinding binding) {
            super(view);
            this.binding = binding;
        }
    }

    public interface SelectedItemsListener {
        void onSelectedItems(Integer count);
    }
}