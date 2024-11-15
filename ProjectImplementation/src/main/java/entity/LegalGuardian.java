package entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class LegalGuardian extends Person {

    @OneToMany(mappedBy = "guardian", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Client> clients = new ArrayList<>();

    public LegalGuardian() {}

    public LegalGuardian(Long id, String name) {
        super(id, name);
    }

    public void addClient(Client client) {
        clients.add(client);
        client.setGuardian(this);
    }

    public void removeClient(Client client) {
        clients.remove(client);
        client.setGuardian(null);
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }
}