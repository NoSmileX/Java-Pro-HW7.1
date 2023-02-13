package org.example;

import lombok.NoArgsConstructor;
import org.example.entities.Client;
import org.example.entities.Order;
import org.example.entities.Product;

import javax.persistence.*;
import java.util.List;
import java.util.Scanner;

@NoArgsConstructor
public class Application {
    private final String NUMBER_VALIDATOR = "380\\d{9}";
    private Scanner sc = new Scanner(System.in);
    private EntityManager em;

    public void init() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Shop");
        em = emf.createEntityManager();
        try {
            while (true) {
                showMenu();
                var choice = sc.nextLine();
                switch (choice) {
                    case "1" -> addClient();
                    case "2" -> addProduct();
                    case "3" -> addOrder();
                    case "4" -> {
                        return;
                    }
                    default -> System.out.println("Incorrect command. Try again");
                }
            }
        } finally {
            sc.close();
            em.close();
            emf.close();
        }
    }

    private void addClient() {
        System.out.println("Enter first name: ");
        String fName = sc.nextLine();
        System.out.println("Enter last name: ");
        String lName = sc.nextLine();
        System.out.println("Enter phone number. Format 380********* (12 digits): ");
        String phone = sc.nextLine();

        if (phone.matches(NUMBER_VALIDATOR)) {
            Client client = new Client(fName, lName, phone);
            if (commit(client)) {
                System.out.println("Client added to DB");
            } else {
                System.out.println("Error. Incorrect data or phone number already exist in DB.");
            }
        } else {
            System.out.println("Number format must be like 380********* (12 digits total)");
        }
    }

    private void addProduct() {
        System.out.println("Enter product name: ");
        String name = sc.nextLine();
        System.out.println("Enter product quantity: ");
        Integer quantity = Integer.parseInt(sc.nextLine());
        Product product = new Product(name, quantity);

        if (commit(product)) {
            System.out.println("Product \"" + name + "\" added to shop. Quantity: " + quantity);
        } else {
            System.out.println("Incorrect data. Try again.");
        }
    }

    private void addOrder() {
        showClients();
        showProducts();
        System.out.println("Enter client ID: ");
        Long clientId = Long.parseLong(sc.nextLine());
        System.out.println("Enter product ID: ");
        Long productId = Long.parseLong(sc.nextLine());
        System.out.println("Enter quantity: ");
        int quantity = Integer.parseInt(sc.nextLine());

        Client client = em.getReference(Client.class, clientId);
        Product product = em.getReference(Product.class, productId);
        Order order = new Order(false);
        client.addOrder(order);
        order.setClient(client);
        product.addOrder(order);
        if (order.addProduct(product, quantity)) {
            commit(client);
            commit(order);
            commit(product);
        }

    }

    private void showClients() {
        TypedQuery<Client> query = em.createQuery("from Client", Client.class);
        List<Client> list = query.getResultList();
        System.out.println("*".repeat(25) + " CLIENTS " + "*".repeat(25));
        for (Client client : list) {
            System.out.println(client);
        }
        System.out.println("*".repeat(57));
    }

    private void showProducts() {
        TypedQuery<Product> query = em.createQuery("from Product", Product.class);
        List<Product> list = query.getResultList();
        System.out.println("*".repeat(25) + " PRODUCTS " + "*".repeat(25));
        for (Product product : list) {
            System.out.println(product);
        }
        System.out.println("*".repeat(58));
    }

    private void showMenu() {
        System.out.println("""
                Enter your choice:
                1. Add new client
                2. Add new product
                3. Add new order
                4. Exit
                ->\s""");
    }

    private boolean commit(Object o) {
        em.getTransaction().begin();
        try {
            em.persist(o);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
            return false;
        }
        return true;
    }
}
