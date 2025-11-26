package com.ecommerce.cart;

import com.ecommerce.Product;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the customer's shopping cart.
 * Handles adding/removing items and calculating the total.
 */
public class ShoppingCart {
    private final List<Product> items;
    private final List<Integer> quantities;

    public ShoppingCart() {
        items = new ArrayList<>();
        quantities = new ArrayList<>();
    }

    /**
     * Adds a product with the desired quantity.
     * Validates stock availability before adding.
     */
    public boolean addItem(Product product, int quantity) {
        if (quantity <= 0) {
            System.out.println("Quantity must be positive.");
            return false;
        }
        if (product.getStockQuantity() < quantity) {
            System.out.println("Not enough stock for " + product.getName());
            return false;
        }
        items.add(product);
        quantities.add(quantity);
        System.out.println(quantity + " x " + product.getName() + " added to cart.");
        return true;
    }

    public void removeItem(Product product) {
        int index = items.indexOf(product);
        if (index != -1) {
            items.remove(index);
            quantities.remove(index);
            System.out.println(product.getName() + " removed from cart.");
        }
    }

    /** Calculates the total value of the cart */
    public double calculateTotal() {
        double total = 0.0;
        for (int i = 0; i < items.size(); i++) {
            total += items.get(i).getPrice() * quantities.get(i);
        }
        return total;
    }

    public List<Product> getItems() { return new ArrayList<>(items); }
    public List<Integer> getQuantities() { return new ArrayList<>(quantities); }

    /** Clears the cart after checkout */
    public void clear() {
        items.clear();
        quantities.clear();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public String toString() {
        if (isEmpty()) return "Cart is empty.";
        StringBuilder sb = new StringBuilder("=== Shopping Cart ===\n");
        for (int i = 0; i < items.size(); i++) {
            Product p = items.get(i);
            int qty = quantities.get(i);
            sb.append(String.format(" %dx %s - $%.2f\n", qty, p.getName(), p.getPrice() * qty));
        }
        sb.append("---------------------\n");
        sb.append(String.format(" Total: $%.2f\n", calculateTotal()));
        return sb.toString();
    }
}