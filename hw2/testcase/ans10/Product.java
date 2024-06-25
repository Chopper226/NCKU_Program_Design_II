public class Product {
    private String productId;
    private String name;
    private double price;
    public void setName(String name) {
        this.name = name;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public String getName() {
        return name;
    }
    public double getPrice() {
        return price;
    }
    public void applyDiscount(double discountPercentage) {;}
}
