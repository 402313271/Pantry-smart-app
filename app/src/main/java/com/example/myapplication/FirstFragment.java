package com.example.myapplication;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.adapter.RecipeAdapter;
import com.example.myapplication.databinding.DialogRecipeDetailBinding;
import com.example.myapplication.databinding.FragmentFirstBinding;
import com.example.myapplication.model.Recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private RecipeAdapter adapter;

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
        load12BreakfastRecipes();
    }

    private void setupRecyclerView() {
        adapter = new RecipeAdapter(this::showRecipeDetailDialog);
        binding.recyclerRecipes.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerRecipes.setAdapter(adapter);
    }

    private void setupSearch() {
        binding.editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilter();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void applyFilter() {
        String query = binding.editSearch.getText() != null ? binding.editSearch.getText().toString() : "";
        adapter.filter(query);
        checkEmptyState();
        updateCount();
    }

    private void checkEmptyState() {
        if (adapter.getDisplayedList().isEmpty()) {
            binding.layoutEmptyState.setVisibility(View.VISIBLE);
            binding.recyclerRecipes.setVisibility(View.GONE);
        } else {
            binding.layoutEmptyState.setVisibility(View.GONE);
            binding.recyclerRecipes.setVisibility(View.VISIBLE);
        }
    }

    private void updateCount() {
        int count = adapter.getDisplayedList().size();
        binding.textRecipeCount.setText(count + " recipes");
    }

    private void showRecipeDetailDialog(Recipe recipe) {
        DialogRecipeDetailBinding detailBinding = DialogRecipeDetailBinding.inflate(getLayoutInflater());

        detailBinding.textDetailTitle.setText(recipe.getTitle());
        detailBinding.textDetailMeta.setText("⏱ Prep: " + recipe.getPrepTime() + "  •  👥 Servings: " + recipe.getServings());

        StringBuilder instBuilder = new StringBuilder();
        int step = 1;
        for (String inst : recipe.getInstructions()) {
            instBuilder.append(step).append(". ").append(inst).append("\n\n");
            step++;
        }
        detailBinding.textDetailInstructions.setText(instBuilder.toString().trim());

        new AlertDialog.Builder(requireContext())
                .setView(detailBinding.getRoot())
                .setPositiveButton(R.string.close, null)
                .show();
    }

    private void load12BreakfastRecipes() {
        List<Recipe> recipes = new ArrayList<>();

        // 1. Classic Scrambled Eggs
        recipes.add(new Recipe(
                UUID.randomUUID().toString(),
                "Classic Scrambled Eggs",
                "Eggs",
                "5 mins",
                "2 servings",
                Arrays.asList("4 Large Eggs", "2 tbsp Whole Milk", "1 tbsp Butter", "Salt & Black Pepper to taste", "Fresh Chives for garnish"),
                Arrays.asList("Whisk eggs, milk, salt, and pepper in a bowl until smooth.", "Melt butter in a non-stick skillet over medium-low heat.", "Pour in egg mixture and let it set slightly for 20 seconds.", "Gently pull eggs across the pan with a spatula to form soft curds.", "Remove from heat while slightly soft, garnish with fresh chives and serve immediately.")
        ));

        // 2. Fluffy Buttermilk Pancakes
        recipes.add(new Recipe(
                UUID.randomUUID().toString(),
                "Fluffy Pancakes",
                "Sweet",
                "15 mins",
                "4 servings",
                Arrays.asList("1.5 cups Flour", "2 tbsp Sugar", "1 tbsp Baking Powder", "1/2 tsp Salt", "1.25 cups Milk", "1 Large Egg", "2 tbsp Melted Butter"),
                Arrays.asList("Whisk flour, sugar, baking powder, and salt together in a bowl.", "In a separate bowl, whisk milk, egg, and melted butter.", "Pour wet ingredients into dry ingredients and stir until just combined (small lumps are okay).", "Heat a lightly greased griddle over medium heat.", "Pour 1/4 cup batter for each pancake. Flip when bubbles form and edges set. Cook until golden brown.")
        ));

        // 3. Avocado Toast with Poached Egg
        recipes.add(new Recipe(
                UUID.randomUUID().toString(),
                "Avocado Toast with Egg",
                "Healthy",
                "10 mins",
                "1 serving",
                Arrays.asList("2 Slices Sourdough Bread", "1 Ripe Avocado", "1 Egg", "1 tsp Lemon Juice", "Red Pepper Flakes", "Sea Salt & Black Pepper"),
                Arrays.asList("Toast sourdough slices until crispy.", "Mash ripe avocado with lemon juice, sea salt, and black pepper in a small bowl.", "Poach or fry egg in a pan to desired doneness.", "Spread mashed avocado evenly onto toasted sourdough.", "Top with egg and sprinkle red pepper flakes over top.")
        ));

        // 4. Oatmeal with Berries & Honey
        recipes.add(new Recipe(
                UUID.randomUUID().toString(),
                "Overnight Oats",
                "Healthy",
                "8 mins",
                "1 serving",
                Arrays.asList("1/2 cup Rolled Oats", "1 cup Milk or Water", "1/4 cup Mixed Fruit(optional) ", "1 tbsp Honey"),
                Arrays.asList("Combine oats, milk and honey in a jar", "stir, and refrigerate overnighty.", "Warm in the morning into a bowl.", "Add fruit of your choice.", "Drizzle with honey and serve warm.")
        ));


        adapter.setRecipes(recipes);
        updateCount();
        checkEmptyState();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
