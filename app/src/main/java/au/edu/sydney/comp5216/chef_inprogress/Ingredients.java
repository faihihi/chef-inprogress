package au.edu.sydney.comp5216.chef_inprogress;

public class Ingredients {
    private String ingredientsName;

    // ex. 200g, 1/4 cup, 2 tbspn
    private String description;

    public Ingredients(String name, String description){
        this.ingredientsName = name;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getIngredientsName() {
        return ingredientsName;
    }
}
