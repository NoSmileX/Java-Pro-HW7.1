package org.example.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String productName;
    private Integer quantity;

    @ManyToMany(mappedBy = "products")
    private List<Order> orders = new ArrayList<>();

    public Product(String productName, Integer quantity) {
        this.productName = productName;
        this.quantity = quantity;
    }

    public void addOrder(Order order) {
        orders.add(order);
        order.getProducts().add(this);
    }

    @Override
    public String toString() {
        return "Product Id = " + productId +
                ", productName = '" + productName + '\'' +
                ", quantity = " + quantity;
    }
}
