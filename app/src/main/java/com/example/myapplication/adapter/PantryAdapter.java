package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ItemPantryBinding;
import com.example.myapplication.model.PantryItem;

import java.util.ArrayList;
import java.util.List;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    public interface OnItemChangeListener {
        void onItemChanged();
    }

    private final List<PantryItem> masterList = new ArrayList<>();
    private final List<PantryItem> displayedList = new ArrayList<>();
    private final OnItemChangeListener listener;

    public PantryAdapter(OnItemChangeListener listener) {
        this.listener = listener;
    }

    public void setItems(List<PantryItem> items) {
        masterList.clear();
        masterList.addAll(items);
        displayedList.clear();
        displayedList.addAll(items);
        notifyDataSetChanged();
    }

    public void addItem(PantryItem item) {
        masterList.add(0, item);
        displayedList.add(0, item);
        notifyItemInserted(0);
    }

    public void filter(String query) {
        displayedList.clear();
        String lowerQuery = query.toLowerCase().trim();

        for (PantryItem item : masterList) {
            if (lowerQuery.isEmpty() || item.getName().toLowerCase().contains(lowerQuery) || item.getCategory().toLowerCase().contains(lowerQuery)) {
                displayedList.add(item);
            }
        }
        notifyDataSetChanged();
    }

    public List<PantryItem> getMasterList() {
        return masterList;
    }

    public List<PantryItem> getDisplayedList() {
        return displayedList;
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPantryBinding binding = ItemPantryBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new PantryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        holder.bind(displayedList.get(position));
    }

    @Override
    public int getItemCount() {
        return displayedList.size();
    }

    public class PantryViewHolder extends RecyclerView.ViewHolder {
        private final ItemPantryBinding binding;

        public PantryViewHolder(@NonNull ItemPantryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(PantryItem item) {
            Context context = binding.getRoot().getContext();

            binding.textItemName.setText(item.getName());
            binding.textItemDetails.setText(item.getCategory() + " • Qty: " + item.getQuantity() + " " + item.getUnit());
            binding.textQuantityNum.setText(String.valueOf(item.getQuantity()));

            // Expiration Status formatting
            int days = item.getDaysToExpiration();
            if (days < 0) {
                binding.textExpiration.setText("Expired");
                binding.textExpiration.setBackgroundColor(ContextCompat.getColor(context, R.color.status_expired_bg));
                binding.textExpiration.setTextColor(ContextCompat.getColor(context, R.color.status_expired_text));
            } else if (days <= 3) {
                binding.textExpiration.setText("Exp: " + days + "d");
                binding.textExpiration.setBackgroundColor(ContextCompat.getColor(context, R.color.status_warning_bg));
                binding.textExpiration.setTextColor(ContextCompat.getColor(context, R.color.status_warning_text));
            } else {
                binding.textExpiration.setText("Exp: " + days + "d");
                binding.textExpiration.setBackgroundColor(ContextCompat.getColor(context, R.color.status_good_bg));
                binding.textExpiration.setTextColor(ContextCompat.getColor(context, R.color.status_good_text));
            }

            // Plus & Minus buttons
            binding.buttonPlus.setOnClickListener(v -> {
                item.setQuantity(item.getQuantity() + 1);
                binding.textQuantityNum.setText(String.valueOf(item.getQuantity()));
                binding.textItemDetails.setText(item.getCategory() + " • Qty: " + item.getQuantity() + " " + item.getUnit());
                if (listener != null) listener.onItemChanged();
            });

            binding.buttonMinus.setOnClickListener(v -> {
                int newQty = item.getQuantity() - 1;
                int position = getBindingAdapterPosition();
                if (newQty <= 0) {
                    masterList.remove(item);
                    displayedList.remove(item);
                    if (position != RecyclerView.NO_POSITION) {
                        notifyItemRemoved(position);
                    }
                } else {
                    item.setQuantity(newQty);
                    binding.textQuantityNum.setText(String.valueOf(newQty));
                    binding.textItemDetails.setText(item.getCategory() + " • Qty: " + newQty + " " + item.getUnit());
                }
                if (listener != null) listener.onItemChanged();
            });
        }
    }
}
