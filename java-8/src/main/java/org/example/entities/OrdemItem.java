package org.example.entities;

import java.math.BigDecimal;
import java.util.Objects;

public class OrdemItem {
    private String id;
    private String name;
    private BigDecimal unitPrice;
    private int quantity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OrdemItem ordemItem = (OrdemItem) o;
        return quantity == ordemItem.quantity && Objects.equals(id, ordemItem.id) && Objects.equals(name, ordemItem.name) && Objects.equals(unitPrice, ordemItem.unitPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, unitPrice, quantity);
    }

    @Override
    public String toString() {
        return "OrdemItem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", unitPrice=" + unitPrice +
                ", quantity=" + quantity +
                '}';
    }
}
