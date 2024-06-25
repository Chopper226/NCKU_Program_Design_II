public class LibraryBook {
    private String bookId;
    private String title;
    private String author;
    public void setTitle(String title) {
        this.title = title;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getTitle() {
        return title;
    }
    public String getAuthor() {
        return author;
    }
    public boolean checkAvailability() {return false;}
    public void lend() {;}
    public void returnBook() {;}
}
