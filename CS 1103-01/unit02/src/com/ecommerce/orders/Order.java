package com.ecommerce.orders;

import com.ecommerce.Customer;
import com.ecommerce.Product;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Represents a confirmed order.
 * Immutable regarding items and quantities.
 */
public class Order {
    private static int nextId = 1001;
    private final String orderId;
    private final Customer customer;
    private final List<Product> items;
    private final List<Integer> quantities;
    private final double totalAmount;
    private String status;
    private final LocalDateTime orderDate;

    public Order(Customer customer, List<Product> items, List<Integer> quantities, double totalAmount) {
        this.orderId = "ORD" + nextId++;
        this.customer = customer;
        this.items = List.copyOf(items);           // Immutable copy
        this.quantities = List.copyOf(quantities); // Immutable copy
        this.totalAmount = totalAmount;
        this.status = "Confirmed";
        this.orderDate = LocalDateTime.now();
    }

    public String getOrderId() { return orderId; }
    public double getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
    public LocalDateTime getOrderDate() { return orderDate; }

    /** Updates the fulfillment status */
    public void updateStatus(String newStatus) {
        this.status = newStatus;
        System.out.println("Order " + orderId + " status updated to: " + status);
    }

    /** Prints a beautiful receipt */
    public void printReceipt() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        System.out.println("========================================");
        System.out.println("           ORDER RECEIPT                ");
        System.out.println("========================================");
        System.out.println("Order ID : " + orderId);
        System.out.println("Customer : " + customer.getName());
        System.out.println("Date     : " + fmt.format(orderDate));
        System.out.println("Status   : " + status);
        System.out.println("----------------------------------------");
        for (int i = 0; i < items.size(); i++) {
            Product p = items.get(i);
            int qty = quantities.get(i);
            System.out.printf(" %dx %-20s $%.2f\n", qty, p.getName(), p.getPrice() * qty);
        }
        System.out.println("----------------------------------------");
        System.out.printf(" TOTAL AMOUNT:          $%.2f\n", totalAmount);
        System.out.println("========================================");
    }
}