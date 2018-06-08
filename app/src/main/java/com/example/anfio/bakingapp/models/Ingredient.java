package com.example.anfio.bakingapp.models;

public class Ingredient {

    private double quantity;
    private String measure;
    private String ingredientName;
    private int idRecipe;

    public Ingredient(double quantity, String measure, String ingredientName, int idRecipe) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredientName = ingredientName;
        this.idRecipe = idRecipe;
    }

    public Ingredient(double quantity, String measure, String ingredientName) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredientName = ingredientName;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public int getIdRecipe() {
        return idRecipe;
    }

    public void setIdRecipe(int idRecipe) {
        this.idRecipe = idRecipe;
    }
}
