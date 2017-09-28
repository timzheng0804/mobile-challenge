package github.timzheng0804.a500px;

/**
 * Created by Tim on 28/09/2017.
 */

public class Photograph {
    private String numLikes;
    private String author;
    private String authorProfilePhoto;
    private String photographUrl;
    private String photographName;

    public Photograph(String numLikes, String author, String authorProfilePhoto, String photographUrl, String photographName) {
        this.numLikes = numLikes;
        this.author = author;
        this.authorProfilePhoto = authorProfilePhoto;
        this.photographUrl = photographUrl;
        this.photographName = photographName;
    }

    public String getNumLikes() {
        return numLikes;
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

