package au.edu.sydney.comp5216.chef_inprogress;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Ingredients class
 */
public class Ingredients implements Parcelable {
    private String ingredientsName;

    // ex. 200g, 1/4 cup, 2 tbspn
    private String description;

    /**
     * Constructor void
     */
    public Ingredients(){}

    /**
     * Constructor accepting name and description
     * @param name
     * @param description
     */
    public Ingredients(String name, String description){
        this.ingredientsName = name;
        this.description = description;
    }

    /**
     * Constructor for passing intent as parcel
     * @param in
     */
    protected Ingredients(Parcel in) {
        ingredientsName = in.readString();
        description = in.readString();
    }

    /**
     * Methods from Parcelable
     */
    public static final Creator<Ingredients> CREATOR = new Creator<Ingredients>() {
        /**
         * Create from parcel
         * @param in
         * @return
         */
        @Override
        public Ingredients createFromParcel(Parcel in) {
            return new Ingredients(in);
        }

        /**
         * Get new array
         * @param size
         * @return
         */
        @Override
        public Ingredients[] newArray(int size) {
            return new Ingredients[size];
        }
    };

    /**
     * Get description
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get ingredients name
     * @return
     */
    public String getIngredientsName() {
        return ingredientsName;
    }

    /**
     * Parcelable method
     * @return
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Write to parcel
     * @param parcel
     * @param i
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ingredientsName);
        parcel.writeString(description);
    }
}
