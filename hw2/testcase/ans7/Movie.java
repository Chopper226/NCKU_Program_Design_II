public class Movie {
    private String movieId;
    private String title;
    private int releaseYear;
    public void setTitle(String title) {
        this.title = title;
    }
    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }
    public String getTitle() {
        return title;
    }
    public int getReleaseYear() {
        return releaseYear;
    }
    public void addActor(Actor actor) {;}
    public boolean removeActor(Actor actor) {return false;}
}
