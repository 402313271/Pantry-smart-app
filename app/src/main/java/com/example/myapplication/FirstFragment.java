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
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private RecipeAdapter adapter;
    private String selectedCategory = "All";

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
        setupCategoryFilters();
        setupSearch();
        load12BreakfastRecipes();
    }

    private void setupRecyclerView() {
        adapter = new RecipeAdapter(this::showRecipeDetailDialog);
        binding.recyclerRecipes.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerRecipes.setAdapter(adapter);
    }

    private void setupCategoryFilters() {
        binding.chipGroupCategories.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (!checkedIds.isEmpty()) {
                int chipId = checkedIds.get(0);
                Chip chip = group.findViewById(chipId);
                if (chip != null) {
                    selectedCategory = chip.getText().toString();
                    applyFilter();
                }
            } else {
                selectedCategory = "All";
                applyFilter();
            }
        });
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
        adapter.filter(query, selectedCategory);
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
        detailBinding.textDetailCategory.setText(recipe.getCategory());
        detailBinding.textDetailMeta.setText("⏱ Prep: " + recipe.getPrepTime() + "  •  👥 Servings: " + recipe.getServings());

        StringBuilder ingBuilder = new StringBuilder();
        for (String ing : recipe.getIngredients()) {
            ingBuilder.append("• ").append(ing).append("\n");
        }
        detailBinding.textDetailIngredients.setText(ingBuilder.toString().trim());

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

        // 4. Overnight Oats
        recipes.add(new Recipe(
                UUID.randomUUID().toString(),
                "Overnight Oats",
                "Healthy",
                "8 mins",
                "1 serving",
                Arrays.asList("1/2 cup Rolled Oats", "1 cup Milk or Water", "1/4 cup Mixed Fruit(optional) ", "1 tbsp Honey"),
                Arrays.asList("Combine oats, milk and honey in a jar", "stir, and refrigerate overnighty.", "Warm in the morning into a bowl.", "Add fruit of your choice.", "Drizzle with honey and serve warm.")
        ));

        // 5. Classic French Toast
        recipes.add(new Recipe(
                UUID.randomUUID().toString(),
                "Classic French Toast",
                "Sweet",
                "10 mins",
                "2 servings",
                Arrays.asList("4 Slices Thick Brioche or Challah Bread", "2 Large Eggs", "1/4 cup Whole Milk", "1/2 tsp Ground Cinnamon", "1 tsp Vanilla Extract", "1 tbsp Butter"),
                Arrays.asList("Whisk eggs, milk, ground cinnamon, and vanilla extract in a shallow bowl.", "Melt butter in a large skillet over medium heat.", "Dip bread slices into egg mixture, coating both sides thoroughly.", "Place bread on hot skillet and cook 2–3 minutes per side until golden brown.", "Serve warm topped with butter and maple syrup.")
        ));

        // 6. Smoothie Bowl
        recipes.add(new Recipe(
                UUID.randomUUID().toString(),
                "Smoothie Bowl",
                "Quick",
                "4 mins",
                "2 servings",
                Arrays.asList("Frozen fruit", "milk or yogurt", "and toppings like granola and seeds."),
                Arrays.asList("Blend fruit and milk until thick", "pour into a bowl", "and add toppings.")
        ));

        // 7. Greek Yogurt & Granola Parfait
        recipes.add(new Recipe(
                UUID.randomUUID().toString(),
                "Greek Yogurt Parfait",
                "Quick",
                "5 mins",
                "1 serving",
                Arrays.asList("1 cup Plain Greek Yogurt", "1/2 cup Honey Oat Granola", "1/2 cup Mixed Berries (Blueberries, Strawberries)", "1 tbsp Maple Syrup or Honey"),
                Arrays.asList("Spoon half of the Greek yogurt into the bottom of a glass or parfait bowl.", "Add a layer of half the granola and berries.", "Repeat with remaining yogurt, granola, and berries.", "Drizzle honey or maple syrup over the top and enjoy immediately.")
        ));

        // 8. Classic Cheese Omelette
        recipes.add(new Recipe(
                UUID.randomUUID().toString(),
                "Classic Cheese Omelette",
                "Eggs",
                "8 mins",
                "1 serving",
                Arrays.asList("3 Large Eggs", "1 tbsp Butter", "1/4 cup Shredded Sharp Cheddar Cheese", "Salt & Pepper to taste", "Fresh Herbs"),
                Arrays.asList("Whisk eggs with salt and pepper until light and airy.", "Melt butter in an 8-inch non-stick skillet over medium-low heat.", "Pour in eggs and swirl pan so eggs coat the bottom evenly.", "When eggs are mostly set but top is slightly moist, sprinkle cheese over one half.", "Fold omelette over cheese, cook for 30 more seconds until cheese melts, and slide onto plate.")
        ));

        // 9. Bacon, Egg & Cheese Sandwich
        recipes.add(new Recipe(
                UUID.randomUUID().toString(),
                "Bacon, Egg & Cheese Muffin",
                "Savory",
                "10 mins",
                "1 serving",
                Arrays.asList("1 English Muffin", "1 Large Egg", "2 Slices Crispy Cooked Bacon", "1 Slice American or Cheddar Cheese", "1/2 tbsp Butter"),
                Arrays.asList("Split and toast English muffin until golden.", "Melt butter in skillet and cook egg sunny-side up or over-easy.", "Place cheese slice on bottom half of warm toasted muffin.", "Top with hot fried egg and crispy bacon slices.", "Cover with top muffin half and press down gently before serving.")
        ));

        // 10. Banana Berry Smoothie Bowl
        recipes.add(new Recipe(
                UUID.randomUUID().toString(),
                "Banana Berry Smoothie Bowl",
                "Smoothies",
                "5 mins",
                "1 serving",
                Arrays.asList("1 Frozen Banana", "1/2 cup Frozen Strawberries", "1/4 cup Almond Milk", "1 tbsp Chia Seeds", "2 tbsp Granola", "Fresh Sliced Fruit"),
                Arrays.asList("Add frozen banana, frozen strawberries, and almond milk into a high-speed blender.", "Blend on high until thick, creamy, and spoonable.", "Pour smoothie thick mixture into a bowl.", "Arrange chia seeds, crunchy granola, and fresh banana or berry slices neatly on top.")
        ));

        // 11. Peanut Butter and Banana Toast
        recipes.add(new Recipe(
                UUID.randomUUID().toString(),
                "Peanut Butter and Banana Toast",
                "Sweet",
                "5 mins",
                "1 servings",
                Arrays.asList( "Whole-grain bread", "peanut butter", "banana slices", "and a drizzle of honey."),
                Arrays.asList("Spread peanut butter on toast", "top with banana", "and finish with honey.")
        ));

        // 12. Overnight Chia Seed Pudding
        recipes.add(new Recipe(
                UUID.randomUUID().toString(),
                "Overnight Chia Seed Pudding",
                "Quick",
                "5 mins",
                "2 servings",
                Arrays.asList("1/4 cup Chia Seeds", "1 cup Unsweetened Almond Milk", "1 tbsp Pure Maple Syrup", "1/2 tsp Vanilla Extract", "Sliced Mango or Berries for topping"),
                Arrays.asList("Whisk chia seeds, almond milk, maple syrup, and vanilla extract together in a mason jar.", "Let sit for 10 minutes, then whisk again to prevent chia seeds from settling.", "Cover jar and chill in refrigerator for at least 4 hours (preferably overnight).", "Top with fresh sliced mango or berries before serving cold.")
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
