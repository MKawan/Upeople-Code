package com.ecommerce;

import com.ecommerce.orders.Order;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== E-Commerce System Demo ===\n");

        // Create products
        Product p1 = new Product("P001", "Dell Laptop", 1299.99, 10);
        Product p2 = new Product("P002", "Wireless Mouse", 39.90, 50);
        Product p3 = new Product("P003", "Mechanical Keyboard", 129.90, 20);
        Product p4 = new Product("P004", "24\" Monitor", 299.00, 15);

        // Create customer
        Customer customer = new Customer("C001", "Alice Johnson");

        // Add items to cart
        customer.getCart().addItem(p1, 1);
        customer.getCart().addItem(p2, 5);
        customer.getCart().addItem(p4, 2);

        System.out.println("\n" + customer);

        // Place order
        Order order = customer.placeOrder();

        if (order != null) {
            order.printReceipt();
            order.updateStatus("Shipped");
            order.updateStatus("Delivered");
        }

        // Show updated stock
        System.out.println("\nUpdated stock levels:");
        System.out.println(p1);
        System.out.println(p2);
        System.out.println(p4);
    }
}