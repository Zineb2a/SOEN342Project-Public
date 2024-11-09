import entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

public class ApplicationMain {

    public static void main(String[] args) {
        // Set up EntityManager and EntityManagerFactory
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("lesson_management");
        EntityManager em = emf.createEntityManager();

        try {
            // Start a transaction
            em.getTransaction().begin();

            // 1. Create Organization
            Organization organization = new Organization();
            organization.setName("Fitness Academy");

            //  Create Administrator
            Administrator admin = new Administrator(null, "Admin John");
            admin.setOrganization(organization);
            admin.setPassword("double007");
            em.persist(admin);
//
//            // 2. Create Locations and Rooms within Organization
            Location location1 = new Location();
            location1.setName("Downtown Gym");
            location1.setCity("San Francisco");
            location1.setOrganization(organization);
//
            Room room1 = new Room();
            room1.setName("Room A");
            room1.setLocation(location1);
//
            Room room2 = new Room();
            room2.setName("Room B");
            room2.setLocation(location1);

            organization.addLocation(location1);
            location1.addRoom(room1);
            location1.addRoom(room2);

            // Persist Organization and its structure
            em.persist(organization);

            // 3. Create Schedule and Timeslots
            Schedule schedule = new Schedule();
            TimeSlot timeslot1 = new TimeSlot(LocalDateTime.of(2024, Month.JANUARY, 5, 10, 0));
            TimeSlot timeslot2 = new TimeSlot(LocalDateTime.of(2024, Month.JANUARY, 5, 14, 0));
            schedule.addTimeSlot(timeslot1);
            schedule.addTimeSlot(timeslot2);
            schedule.setStartTime(Instant.now());
            schedule.setEndTime(Instant.now());
            schedule.setRoom(room1);
//
            em.persist(schedule);

            // 4. Create Lessons and Offerings
            Lesson yogaLesson = new Lesson();
            yogaLesson.setName("Yoga Class");
            yogaLesson.setType("Private");


            admin.createLesson("Taekwando","Group",false);


            Lesson pilatesLesson = new Lesson();
            pilatesLesson.setName("Pilates Class");
            pilatesLesson.setType("Group");

            em.persist(yogaLesson);
            em.persist(pilatesLesson);

            // 5. Create Instructors
            Instructor instructor1 = new Instructor(null, "Instructor 1","Yoga");
            instructor1.setPassword("double008");
            em.persist(instructor1);

            Offering offered = instructor1.createOffering(yogaLesson,room1,schedule,LocalDateTime.of(2024, Month.JANUARY, 5, 10, 0),10);


            offered.setMaxCapacity(1);
            System.out.println();
            em.persist(offered);



            // 6. Create Clients and LegalGuardian (for minor clients)
            LegalGuardian guardian = new LegalGuardian(null, "Jane Doe");
//
            guardian.setPassword("double009");
            Client client1 = new Client(null, "Alice Johnson");
            Client client2 = new Client(null, "Tommy Doe");

            client1.setPassword("double008");
            client2.setPassword("double009");

            client2.setGuardian(guardian);

            em.persist(guardian);
            em.persist(client1);
            em.persist(client2);
//
//
            // 8. Create Bookings
            Booking booking1 = new Booking(client1, offered, Instant.now());
            booking1.setIsAvailable(true);



            em.persist(booking1);

            System.out.println( offered.getBookings()+"iefjeifjef"+offered.getCurrentCapacity()+"YEYEYEYEYEYEYEYEYEYEEEEEYEYYE"+offered.isAvailable());

            // Commit the transaction
            em.getTransaction().commit();

            // Display information
            displayOrganizationStructure(em);
            displayOfferingsAndClients(em);

        } finally {
            // Close EntityManager and EntityManagerFactory
            em.close();
            emf.close();
        }
    }

    // Display organization structure
    private static void displayOrganizationStructure(EntityManager em) {
        List<Organization> organizations = em.createQuery("SELECT o FROM Organization o", Organization.class).getResultList();
        for (Organization org : organizations) {
            System.out.println("Organization: " + org.getName());
            for (Location location : org.getLocations()) {
                System.out.println("  Location: " + location.getName());
                for (Room room : location.getRooms()) {
                    System.out.println("    Room: " + room.getName());
                }
            }
        }
    }

    // Display offerings and clients
    private static void displayOfferingsAndClients(EntityManager em) {
        List<Offering> offerings = em.createQuery("SELECT o FROM Offering o", Offering.class).getResultList();
        for (Offering offering : offerings) {
            System.out.println("Offering: " + offering.getLesson().getName() + " on " + offering.getDateTime());
            for (Booking booking : offering.getBookings()) {
                System.out.println("  - Client: " + booking.getClient().getName() + " (Booking Available: " + booking.getIsAvailable() + ")");
            }
        }
    }
}