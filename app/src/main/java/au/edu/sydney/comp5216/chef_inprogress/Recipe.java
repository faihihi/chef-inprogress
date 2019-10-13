package au.edu.sydney.comp5216.chef_inprogress;

import android.net.Uri;

import java.util.ArrayList;

public class Recipe {
    private int id;
    private String title;
    private Uri image;

    // Tags ex. vegetarian, gluten-free
    private ArrayList<String> tags;

    // Nutrition intake in gram
    private int protein;
    private int fat;
    private int carbs;

    // Details
    private double timeTaken;
    private int servings;

    // Ingredients
    private ArrayList<Ingredients> ingredientsList;

    // Instruction
    

}
