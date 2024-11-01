import java.time.LocalDate;
import java.util.UUID;

public class Booking {

    private String bookingID;
    private Client client;
    private Offering offering;
    private LocalDate bookingDate;
    private BookingStatus status;

    public Booking(Client client, Offering offering) {
        this.bookingID = UUID.randomUUID().toString();
        this.client = client;
        this.offering = offering;
        this.bookingDate = LocalDate.now();
        this.status = BookingStatus.Active;
    }

    public void cancel() throws Exception {
        if (status == BookingStatus.Cancelled) {
            throw new Exception("Booking is already cancelled");
        }
        status = BookingStatus.Cancelled;
    }

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Offering getOffering() {
        return offering;
    }

    public void setOffering(Offering offering) {
        this.offering = offering;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }
}


//In process