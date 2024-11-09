package entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Administrator extends Person {

    // Association with clients
    @OneToMany(mappedBy = "administrator", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Client> managedClients = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    public Organization getOrganization() {
        return organization;
    }

    public Administrator() {}

    public Administrator(Long id, String name) {
        super(id, name);
    }

    // Methods specific to Administrator

    // Add a client under this administrator's management
    public void addClient(Client client) {
        managedClients.add(client);
    }

    // Remove a client from this administrator's management
    public void removeClient(Client client) {
        managedClients.remove(client);
    }

    // Create a new lesson managed by the administrator
    public Lesson createLesson( String name, String type, Boolean isPrivate) {
        Lesson lesson = new Lesson();
        lesson.setName(name);
        lesson.setType(type);
        lesson.setIsPrivate(isPrivate);
        System.out.println("Lesson created by Administrator: " + getName());
        return lesson;
    }



    // Getters and Setters for managedClients
    public Set<Client> getManagedClients() {
        return managedClients;
    }

    public void setManagedClients(Set<Client> managedClients) {
        this.managedClients = managedClients;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
}