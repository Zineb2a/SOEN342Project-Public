package entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Client extends Person {

    @ManyToOne
    private Administrator administrator;
    // One-to-many relationship with Booking
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings = new ArrayList<>();

    // Many-to-one relationship with LegalGuardian (optional)
    @ManyToOne
    @JoinColumn(name = "guardian_id")
    private LegalGuardian guardian;

    // Constructors
    public Client() {}

    public Client(Long id, String name) {
        super(id, name);
    }

    // Getters and Setters
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