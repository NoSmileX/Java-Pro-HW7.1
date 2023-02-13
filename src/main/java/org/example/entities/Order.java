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
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    private boolean isComplete;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "order_products",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> products = new ArrayList<>();

    public Order(boolean isComplete) {
        this.isComplete = isComplete;
    }

    public boolean addProduct(Product product, int count){
        if(product.getQuantity() >= count){
            products.add(product);
            product.getOrders().add(this);
            product.setQuantity(product.getQuantity() - count);
            return true;
        }else {
            System.out.println("Not enough product in store.");
            return false;
        }
    }

    @Override
    public String toString() {
        return "Order Id = " + orderId +
                ", isComplete = " + isComplete +
                ' ';
    }
}
