package au.edu.sydney.comp5216.chef_inprogress.ui.favorite;

/**
 * Favorite class for favorite item
 */
public class Favorite {
    private String title;
    private String time;
    private String pic;

    /**
     * Constructor
     * @param title
     * @param time
     * @param pic
     */
    public Favorite(String title, String time, String pic){
        this.title = title;
        this.time = time;
        this.pic = pic;
    }

    /** GETTERS **/

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public String getPic() {
        return pic;
    }
}
