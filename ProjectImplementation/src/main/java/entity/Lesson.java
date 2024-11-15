package entity;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "lesson")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    @Column(name = "type", nullable = false, length = 50)
    private String type;

    @Column(name = "is_private", nullable = false)
    private Boolean isPrivate = false;

    @OneToMany
    private List<Offering> offerings;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    public Lesson() {
        this.offerings = new ArrayList<>();
    }

    public Lesson(Long id) {
        this.id = id;
        this.offerings = new ArrayList<>();
    }

    public void addOffering(Offering offering) {
        offerings.add(offering);
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Offering> getOfferings() {
        return offerings;
    }

    public void setOfferings(List<Offering> offerings) {
        this.offerings = offerings;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}