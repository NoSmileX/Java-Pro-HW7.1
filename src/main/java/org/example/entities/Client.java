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
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;
    @Column(unique = true, length = 12, nullable = false)
    private String phone;

    @OneToMany(mappedBy = "client", cascade = CascadeType.PERSIST)
    private List<Order> orders = new ArrayList<>();

    public Client(String firstName, String lastName, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    public void addOrder(Order order) {
        if (!orders.contains(order) && !order.isComplete()) {
            orders.add(order);
            order.setClient(this);
            order.setComplete(false);
        }
    }

    @Override
    public String toString() {
        return "Client id = " + id +
                ", First name = '" + firstName + '\'' +
                ", Last name = '" + lastName + '\'' +
                ", Phone number = '" + phone + '\'' +
                ", Orders = " + orders;
    }
}
