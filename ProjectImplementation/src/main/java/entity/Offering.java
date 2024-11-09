package entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "offering")
public class Offering {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    // Many-to-one relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id", nullable = false)
    private Instructor instructor;

    // Additional fields
    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "max_capacity", nullable = false)
    private int maxCapacity;

    @Column(name = "description", length = 255)
    private String description;

    // One-to-many relationship with Booking
    @OneToMany(mappedBy = "offering", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings = new ArrayList<>();

    // Calculated attributes
    @Transient
    private int currentCapacity;

    @Transient
    private boolean isAvailable;

    // Constructors
    public Offering() {}

    public Offering(Long id) {
        this.id = id;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
        updateCurrentCapacity();
    }

    // Methods to add and remove bookings
    public void addBooking(Booking booking) {
        bookings.add(booking);
        booking.setOffering(this);
        updateCurrentCapacity();
    }

    public void removeBooking(Booking booking) {
        bookings.remove(booking);
        booking.setOffering(null);
        updateCurrentCapacity();
    }

    // Helper method to retrieve clients associated with this offering
    public List<Client> getClients() {
        List<Client> clients = new ArrayList<>();
        for (Booking booking : bookings) {
            clients.add(booking.getClient());
        }
        return clients;
    }

    // Get the current capacity based on bookings list
    public int getCurrentCapacity() {
        return currentCapacity;
    }

    // Check if offering is available
    public boolean isAvailable() {
        return currentCapacity < maxCapacity;
    }

    // Private helper method to update current capacity and availability
    private void updateCurrentCapacity() {
        this.currentCapacity = bookings.size();
        this.isAvailable = this.currentCapacity < this.maxCapacity;
    }
}