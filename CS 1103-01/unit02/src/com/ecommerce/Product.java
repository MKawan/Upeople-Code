package com.ecommerce;

/**
 * Represents a product available for sale in the online store.
 * Encapsulates all product information and enforces data hiding.
 */
public class Product {
    private String productId;
    private String name;
    private double price;
    private int stockQuantity;

    /**
     * Constructor with full initialization
     */
    public Product(String productId, String name, double price, int stockQuantity) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    // --- Getters ---
    public String getProductId() { return productId; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStockQuantity() { return stockQuantity; }

    // --- Setter only for stock (needed after sales) ---
    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    /**
     * Reduces stock after a successful purchase
     */
    public boolean reduceStock(int quantity) {
        if (quantity > stockQuantity) {
            return false;
        }
        stockQuantity -= quantity;
        return true;
    }

    @Override
    public String toString() {
        return String.format("Product{id='%s', name='%s', price=%.2f, stock=%d}",
                productId, name, price, stockQuantity);
    }
}