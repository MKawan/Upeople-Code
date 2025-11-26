package com.ecommerce;

import com.ecommerce.cart.ShoppingCart;
import com.ecommerce.orders.Order;

/**
 * Represents a registered customer.
 * Owns one shopping cart and can place orders.
 */
public class Customer {
    private final String customerId;
    private final String name;
    private final ShoppingCart cart;

    public Customer(String customerId, String name) {
        this.customerId = customerId;
        this.name = name;
        this.cart = new ShoppingCart();
    }

    public String getCustomerId() { return customerId; }
    public String getName() { return name; }
    public ShoppingCart getCart() { return cart; }

    /**
     * Places an order if cart is not empty.
     * Reduces product stock atomically.
     * Returns the created Order or null if failed.
     */
    public Order placeOrder() {
        if (cart.isEmpty()) {
            System.out.println("Error: Cannot place order - cart is empty!");
            return null;
        }

        // Reduce stock for all items
        for (int i = 0; i < cart.getItems().size(); i++) {
            Product p = cart.getItems().get(i);
            int qty = cart.getQuantities().get(i);
            if (!p.reduceStock(qty)) {
                System.out.println("Order failed: Insufficient stock for " + p.getName());
                return null;
            }
        }

        Order order = new Order(this, cart.getItems(), cart.getQuantities(), cart.calculateTotal());
        System.out.println("Order placed successfully by " + name + "!");
        cart.clear();
        return order;
    }

    @Override
    public String toString() {
        return "Customer{id='" + customerId + "', name='" + name + "'}\n" + cart;
    }
}