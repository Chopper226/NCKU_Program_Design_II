public class Reader {
    private String readerId;
    private String name;
    private int borrowedBooks;
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void borrowBook(String bookId) {;}
    public boolean returnBook(String bookId) {return false;}
}
