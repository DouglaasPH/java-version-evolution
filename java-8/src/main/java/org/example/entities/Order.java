package org.example.entities;

import org.example.entities.enums.CustomerType;
import org.example.entities.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

public class Order {
    private String id;
    private String customerName;
    private CustomerType customerType;
    private List<OrdemItem> items;
    private BigDecimal originalTotal;
    private BigDecimal discountAmount;
    private BigDecimal finalTotal;
    private OrderStatus orderStatus;
    private Instant createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public CustomerType getCustomerType() {
        return customerType;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }

    public List<OrdemItem> getItems() {
        return items;
    }

    public void setItems(List<OrdemItem> items) {
        this.items = items;
    }

    public BigDecimal getOriginalTotal() {
        return originalTotal;
    }

    public void setOriginalTotal(BigDecimal originalTotal) {
        this.originalTotal = originalTotal;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getFinalTotal() {
        return finalTotal;
    }

    public void setFinalTotal(BigDecimal finalTotal) {
        this.finalTotal = finalTotal;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(customerName, order.customerName) && customerType == order.customerType && Objects.equals(items, order.items) && Objects.equals(originalTotal, order.originalTotal) && Objects.equals(discountAmount, order.discountAmount) && Objects.equals(finalTotal, order.finalTotal) && orderStatus == order.orderStatus && Objects.equals(createdAt, order.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerName, customerType, items, originalTotal, discountAmount, finalTotal, orderStatus, createdAt);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", customerName='" + customerName + '\'' +
                ", customerType=" + customerType +
                ", items=" + items +
                ", originalTotal=" + originalTotal +
                ", discountAmount=" + discountAmount +
                ", finalTotal=" + finalTotal +
                ", orderStatus=" + orderStatus +
                ", createdAt=" + createdAt +
                '}';
    }
}
