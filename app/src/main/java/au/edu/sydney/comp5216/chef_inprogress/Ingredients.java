package au.edu.sydney.comp5216.chef_inprogress;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredients implements Parcelable {
    private String ingredientsName;

    // ex. 200g, 1/4 cup, 2 tbspn
    private String description;

    public Ingredients(){}

    public Ingredients(String name, String description){
        this.ingredientsName = name;
        this.description = description;
    }

    protected Ingredients(Parcel in) {
        ingredientsName = in.readString();
        description = in.readString();
    }

    public static final Creator<Ingredients> CREATOR = new Creator<Ingredients>() {
        @Override
        public Ingredients createFromParcel(Parcel in) {
            return new Ingredients(in);
        }

        @Override
        public Ingredients[] newArray(int size) {
            return new Ingredients[size];
        }
    };

    public String getDescription() {
        return description;
    }

    public String getIngredientsName() {
        return ingredientsName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ingredientsName);
        parcel.writeString(description);
    }
}
