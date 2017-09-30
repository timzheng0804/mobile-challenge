package github.timzheng0804.a500px.Modle;

import java.util.ArrayList;

/**
 * Created by Tim on 28/09/2017.
 */

public class Photograph {
    private static ArrayList<Photograph> photoList = new ArrayList<>();;

    private int width;
    private int height;
    private String numLikes;
    private String author;
    private String authorProfilePhoto;
    private String photographUrl;
    private String photographName;

    public Photograph(int width, int height, String numLikes, String author, String authorProfilePhoto, String photographUrl, String photographName) {
        this.width = width;
        this.height = height;
        this.numLikes = numLikes;
        this.author = author;
        this.authorProfilePhoto = authorProfilePhoto;
        this.photographUrl = photographUrl;
        this.photographName = photographName;
    }

    public static void addToPhotoList(Photograph photo) {
        photoList.add(photo);
    }

    public static Photograph getItemFromList(int index) {
        if (index >= photoList.size()) return null;
        return photoList.get(index);
    }

    public static int getListSize() {
        return photoList.size();
    }

    public String getNumLikes() {
        return numLikes;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getAuthor() {
        return author;
    }

    public String getAuthorProfilePhoto() {
        return authorProfilePhoto;
    }

    public String getPhotographUrl() {
        return photographUrl;
    }

    public String getPhotographName() {
        return photographName;
    }


}

