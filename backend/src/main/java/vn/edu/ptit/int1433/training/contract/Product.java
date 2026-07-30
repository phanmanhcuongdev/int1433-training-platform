package vn.edu.ptit.int1433.training.contract;

import java.io.Serializable;

public class Product implements Serializable {
    public static final long serialVersionUID = 1433001L;

    private String id;
    private String name;
    private double price;
    private int quantity;
    private boolean normalized;

    public Product() {
    }

    public Product(String id, String name, double price, int quantity, boolean normalized) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.normalized = normalized;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public boolean isNormalized() { return normalized; }
    public void setNormalized(boolean normalized) { this.normalized = normalized; }
}
