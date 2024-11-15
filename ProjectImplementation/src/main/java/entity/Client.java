package entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Client extends Person {

    @ManyToOne
    private Administrator administrator;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "guardian_id")
    private LegalGuardian guardian;

    @Column(name = "age", nullable = false)
    private int age;

    public Client() {}

    public Client(Long id, String name, int age) {
        super(id, name);
        this.age = age;
    }

    // Getters and Setters
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public LegalGuardian getGuardian() {
        return guardian;
    }

    public void setGuardian(LegalGuardian guardian) {
        this.guardian = guardian;
    }

    // Methods to add and remove bookings
    public void addBooking(Booking booking) {
        bookings.add(booking);
        booking.setClient(this);
    }

    public void removeBooking(Booking booking) {
        bookings.remove(booking);
        booking.setClient(null);
    }
}