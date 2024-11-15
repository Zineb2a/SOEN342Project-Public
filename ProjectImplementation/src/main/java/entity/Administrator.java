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

    @OneToOne
    @JoinColumn(name = "location_id", nullable = true)
    private Location location;

    public Administrator() {}

    public Administrator(Long id, String name, Organization organization, Location location) {
        super(id, name);
        this.organization = organization;
        this.location = location;
    }

    public void addClient(Client client) {
        managedClients.add(client);
    }

    public void removeClient(Client client) {
        managedClients.remove(client);
    }

    public Lesson createLesson(String name, String type, Boolean isPrivate, Location location) {
        Lesson lesson = new Lesson();
        lesson.setName(name);
        lesson.setType(type);
        lesson.setIsPrivate(isPrivate);
        lesson.setLocation(location);  // Assign location to lesson
        System.out.println("Lesson created by Administrator: " + getName());
        return lesson;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Set<Client> getManagedClients() {
        return managedClients;
    }

    public void setManagedClients(Set<Client> managedClients) {
        this.managedClients = managedClients;
    }
}