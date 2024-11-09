package entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class LegalGuardian extends Person {

    // Association with clients
    @OneToMany(mappedBy = "guardian", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Client> clients = new ArrayList<>();

    public LegalGuardian() {}

    public LegalGuardian(Long id, String name) {
        super(id, name);
    }

    // Method to add a client to the guardian's list of clients
    public void addClient(Client client) {
        clients.add(client);
        client.setGuardian(this); // Set the back-reference in Client
    }

    // Method to remove a client from the guardian's list
    public void removeClient(Client client) {
        clients.remove(client);
        client.setGuardian(null); // Remove the back-reference in Client
    }

    // Getters and Setters for clients
    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }
}