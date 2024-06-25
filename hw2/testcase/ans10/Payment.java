public class Payment {
    private String paymentId;
    private double amount;
    private String method;
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public void setMethod(String method) {
        this.method = method;
    }
    public double getAmount() {
        return amount;
    }
    public String getMethod() {
        return method;
    }
    public boolean processPayment() {return false;}
}
