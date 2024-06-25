public class Song {
    private String title;
    private String artist;
    private int duration;
    public void setTitle(String title) {
        this.title = title;
    }
    public void setArtist(String artist) {
        this.artist = artist;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }
    public String getTitle() {
        return title;
    }
    public String getArtist() {
        return artist;
    }
    public int getDuration() {
        return duration;
    }
    public void play() {;}
    public void pause() {;}
}
