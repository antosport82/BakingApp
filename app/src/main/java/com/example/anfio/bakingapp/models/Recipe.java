package com.example.anfio.bakingapp.models;

public class Recipe {

    private int id;
    private String recipeName;
    private int servings;
    private String image;

    public Recipe(int id, String recipeName, int servings, String image) {
        this.id = id;
        this.recipeName = recipeName;
        this.servings = servings;
        this.image = image;
    }

    public Recipe(int id, String recipeName, String image){
        this.id = id;
        this.recipeName = recipeName;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}