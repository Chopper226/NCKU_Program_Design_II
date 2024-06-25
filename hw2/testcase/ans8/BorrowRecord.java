public class BorrowRecord {
    private String recordId;
    private String bookId;
    private String borrowerId;
    private Date borrowDate;
    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }
    public Date getBorrowDate() {
        return borrowDate;
    }
    public void markAsReturned() {;}
}
