package entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
public class Instructor extends Person {
    @Column(name = "specialization", nullable = false, length = 50)
    private String specialization; // e.g., "Swimming", "Yoga"

    @ElementCollection
    @CollectionTable(name = "available_cities", joinColumns = @JoinColumn(name = "instructor_id"))
    @Column(name = "available_cities")
    private List<String> availableCities = new ArrayList<>(); // Now a List of Strings

    // One-to-many relationship with Offering
    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Offering> offerings = new ArrayList<>();

    // Constructors
    public Instructor() {}

    public Instructor(Long id, String name, String specialization) {
        super(id, name);
        this.specialization = specialization;
    }

    // Create an offering for a specific lesson
    public Offering createOffering( Lesson lesson, Room room, Schedule schedule, LocalDateTime dateTime, int maxCapacity) {
        Offering offering = new Offering(null);
        offering.setLesson(lesson);
        offering.setRoom(room);
        offering.setSchedule(schedule);
        offering.setInstructor(this);
        offering.setDateTime(dateTime);
        offering.setMaxCapacity(maxCapacity);

        offerings.add(offering); // Add offering to instructor's list
        System.out.println("Offering created by Instructor: " + getName());

        return offering;
    }

    // Get a specific offering by ID
    public Offering getOfferingById(Long offeringId) {
        Optional<Offering> offering = offerings.stream()
                .filter(o -> o.getId().equals(offeringId))
                .findFirst();
        return offering.orElse(null);
    }

    // Getters and Setters
    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public List<String> getAvailableCities() {
        return availableCities;
    }

    public void setAvailableCities(List<String> availableCities) {
        this.availableCities = availableCities;
    }

    public List<Offering> getOfferings() {
        return offerings;
    }

    public void setOfferings(List<Offering> offerings) {
        this.offerings = offerings;
    }
}