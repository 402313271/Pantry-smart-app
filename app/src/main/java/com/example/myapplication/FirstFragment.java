package com.example.myapplication;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.adapter.PantryAdapter;
import com.example.myapplication.databinding.DialogAddPantryItemBinding;
import com.example.myapplication.databinding.FragmentFirstBinding;
import com.example.myapplication.model.PantryItem;

import java.util.ArrayList;
import java.util.UUID;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private PantryAdapter adapter;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView();
        setupSearch();

        // Initialize with no items
        adapter.setItems(new ArrayList<>());
        updateItemCount();
    }

    private void setupRecyclerView() {
        adapter = new PantryAdapter(this::updateItemCount);
        binding.recyclerPantry.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerPantry.setAdapter(adapter);
    }

    private void setupSearch() {
        binding.editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s != null ? s.toString() : "";
                adapter.filter(query);
                checkEmptyState();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void checkEmptyState() {
        if (adapter.getDisplayedList().isEmpty()) {
            binding.layoutEmptyState.setVisibility(View.VISIBLE);
            binding.recyclerPantry.setVisibility(View.GONE);
        } else {
            binding.layoutEmptyState.setVisibility(View.GONE);
            binding.recyclerPantry.setVisibility(View.VISIBLE);
        }
    }

    private void updateItemCount() {
        int count = adapter.getMasterList().size();
        binding.textItemCount.setText(count + " items");
        checkEmptyState();
    }

    public void showAddItemDialog() {
        DialogAddPantryItemBinding dialogBinding = DialogAddPantryItemBinding.inflate(getLayoutInflater());

        new AlertDialog.Builder(requireContext())
                .setView(dialogBinding.getRoot())
                .setPositiveButton(R.string.add, (dialog, which) -> {
                    String name = dialogBinding.editItemName.getText() != null ? dialogBinding.editItemName.getText().toString().trim() : "";
                    String qtyStr = dialogBinding.editItemQuantity.getText() != null ? dialogBinding.editItemQuantity.getText().toString().trim() : "1";
                    String category = dialogBinding.editItemCategory.getText() != null ? dialogBinding.editItemCategory.getText().toString().trim() : "General";

                    if (name.isEmpty()) {
                        Toast.makeText(requireContext(), "Please enter an item name", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int quantity = 1;
                    try {
                        quantity = Integer.parseInt(qtyStr);
                    } catch (NumberFormatException ignored) {}

                    PantryItem newItem = new PantryItem(
                            UUID.randomUUID().toString(),
                            name,
                            category.isEmpty() ? "General" : category,
                            quantity,
                            "pcs",
                            7,
                            false
                    );

                    adapter.addItem(newItem);
                    updateItemCount();
                    Toast.makeText(requireContext(), "Added " + name, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
