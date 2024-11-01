import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Client {

    private String clientID;
    private String name;
    private int age;
    private String phoneNumber;
    private Guardian guardian;
    private List<Booking> bookings;

    public Client(String name, int age, String phoneNumber, Guardian guardian) {
        this.clientID = UUID.randomUUID().toString();
        this.name = name;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.guardian = guardian;
        this.bookings = new ArrayList<>();
    }

    public Booking makeBooking(Offering offering) throws Exception {
        if (!offering.isAvailable()) {
            throw new Exception("Offering is not available");
        }

        for (Booking b : bookings) {
            if (b.getStatus() == BookingStatus.Active &&
                    b.getOffering().getSchedule().overlapsWith(offering.getSchedule())) {
                throw new Exception("Time conflict with existing booking");
            }
        }

        if (age < 18 && guardian == null) {
            throw new Exception("Underage clients must have a guardian");
        }

        offering.markAsBooked();
        Booking booking = new Booking(this, offering);
        bookings.add(booking);
        return booking;
    }

    public void cancelBooking(String bookingID) throws Exception {
        Booking booking = null;
        for (Booking b : bookings) {
            if (b.getBookingID().equals(bookingID)) {
                booking = b;
                break;
            }
        }

        if (booking == null || booking.getStatus() == BookingStatus.Cancelled) {
            throw new Exception("Booking not found or already cancelled");
        }

        booking.cancel();
        booking.getOffering().markAsAvailable();
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Guardian getGuardian() {
        return guardian;
    }

    public void setGuardian(Guardian guardian) {
        this.guardian = guardian;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
//In process