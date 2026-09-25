package com.example.myapplication.model;

public class PantryItem {
    private String id;
    private String name;
    private String category;
    private int quantity;
    private String unit;
    private int daysToExpiration; // negative = expired, 0 = expires today, >0 = days left
    private boolean isLowStock;

    public PantryItem(String id, String name, String category, int quantity, String unit, int daysToExpiration, boolean isLowStock) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.unit = unit;
        this.daysToExpiration = daysToExpiration;
        this.isLowStock = isLowStock;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getDaysToExpiration() {
        return daysToExpiration;
    }

    public void setDaysToExpiration(int daysToExpiration) {
        this.daysToExpiration = daysToExpiration;
    }

    public boolean isLowStock() {
        return isLowStock;
    }

    public void setLowStock(boolean lowStock) {
        isLowStock = lowStock;
    }
}
