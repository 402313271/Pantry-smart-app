package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.myapplication.databinding.FragmentSettingsBinding;
import com.example.myapplication.db.PantryDatabaseHelper;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private PantryDatabaseHelper dbHelper;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new PantryDatabaseHelper(requireContext());

        binding.buttonResetDb.setOnClickListener(v -> new AlertDialog.Builder(requireContext())
                .setTitle("Reset Pantry Database?")
                .setMessage("This will restore default sample ingredients into your local database.")
                .setPositiveButton("Reset", (dialog, which) -> {
                    dbHelper.resetDatabase();
                    Toast.makeText(requireContext(), "Pantry database reset successfully!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
