package au.edu.sydney.comp5216.chef_inprogress.ui.favorite;

import java.net.URL;

public class Favorite {
    private String title;
    private String time;
    private String pic;

    public Favorite(String title, String time, String pic){
        this.title = title;
        this.time = time;
        this.pic = pic;
    }

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
