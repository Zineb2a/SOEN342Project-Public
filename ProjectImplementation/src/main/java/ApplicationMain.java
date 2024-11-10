import entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Scanner;

public class ApplicationMain {

    private static EntityManager em;
    private static Person loggedInUser;

    public static void main(String[] args) {
        // Set up EntityManager and EntityManagerFactory
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("lesson_management");
         em = emf.createEntityManager();

        try {

            em.getTransaction().begin();

            Organization organization = new Organization();
            organization.setName("Fitness Academy");

            Administrator admin = new Administrator(null, "Admin John");
            admin.setOrganization(organization);
            admin.setPassword("double007");
            em.persist(admin);

            Location location1 = new Location();
            location1.setName("Downtown Gym");
            location1.setCity("Montreal");
            location1.setOrganization(organization);

            Room room1 = new Room();
            room1.setName("Room A");
            room1.setLocation(location1);

            Room room2 = new Room();
            room2.setName("Room B");
            room2.setLocation(location1);

            organization.addLocation(location1);
            location1.addRoom(room1);
            location1.addRoom(room2);

            em.persist(organization);

            Schedule schedule = new Schedule(
                    LocalDateTime.of(2024, Month.JANUARY, 5, 9, 0),
                    LocalDateTime.of(2024, Month.JANUARY, 5, 17, 0),
                    room1
            );

            em.persist(schedule);

            TimeSlot timeslot1 = new TimeSlot(
                    LocalDateTime.of(2024, Month.JANUARY, 5, 10, 0),
                    LocalDateTime.of(2024, Month.JANUARY, 5, 11, 0),
                    schedule
            );
            if (schedule.addTimeSlotWithValidation(timeslot1)) {
                em.persist(timeslot1);
            }

            // Attempt to add overlapping TimeSlot (should print error)
            TimeSlot timeslot2 = new TimeSlot(
                    LocalDateTime.of(2024, Month.JANUARY, 5, 10, 30),
                    LocalDateTime.of(2024, Month.JANUARY, 5, 11, 30),
                    schedule
            );
            if (schedule.addTimeSlotWithValidation(timeslot2)) {
                em.persist(timeslot2);
            }

            // Adding a non-overlapping TimeSlot (should succeed)
            TimeSlot timeslot3 = new TimeSlot(
                    LocalDateTime.of(2024, Month.JANUARY, 5, 11, 30),
                    LocalDateTime.of(2024, Month.JANUARY, 5, 12, 30),
                    schedule
            );
            if (schedule.addTimeSlotWithValidation(timeslot3)) {
                em.persist(timeslot3);
            }

            Lesson yogaLesson = new Lesson();
            yogaLesson.setName("Yoga Class");
            yogaLesson.setType("Private");
            em.persist(yogaLesson);

            Lesson pilatesLesson = new Lesson();
            pilatesLesson.setName("Pilates Class");
            pilatesLesson.setType("Group");
            em.persist(pilatesLesson);

            Instructor instructor1 = new Instructor(null, "Instructor 1", "Yoga");
            instructor1.setPassword("double008");
            em.persist(instructor1);

            Offering offered = instructor1.createOffering(yogaLesson, room1, schedule,
                    LocalDateTime.of(2024, Month.JANUARY, 5, 10, 0), 10);
            offered.setMaxCapacity(1);
            em.persist(offered);

            LegalGuardian guardian = new LegalGuardian(null, "Jane Doe");
            guardian.setPassword("double009");
            em.persist(guardian);

            Client client1 = new Client(null, "Alice Johnson");
            client1.setPassword("double008");
            em.persist(client1);

            Client client2 = new Client(null, "Tommy Doe");
            client2.setPassword("double009");
            client2.setGuardian(guardian);
            em.persist(client2);

            Booking booking1 = new Booking(client1, offered, Instant.now());
            booking1.setIsAvailable(true);
            em.persist(booking1);

            em.getTransaction().commit();

            displayOrganizationStructure(em);
            displayOfferings(em);

        } finally {
            em.close();
            emf.close();
        }
    }

    // Registration Functionality
    private static void registerUser(Scanner scanner) {
        System.out.println("Select user type for registration: 1. Client 2. Instructor");
        int userType = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.println("Enter name:");
        String name = scanner.next();
        System.out.println("Enter password:");
        String password = scanner.next();

        em.getTransaction().begin();
        try {
            if (userType == 1) {

                Client client = new Client(null, name);
                client.setPassword(password);
                em.persist(client);
                System.out.println("Client registered successfully.");
            } else if (userType == 2) {

                System.out.print("Enter specialization (e.g., Yoga, Swimming): ");
                String specialization = scanner.nextLine();
                Instructor instructor = new Instructor(null, name, specialization);
                instructor.setPassword(password);
                em.persist(instructor);
                System.out.println("Instructor registered successfully.");
            } else {
                System.out.println("Invalid user type selection.");
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
            System.out.println("Registration failed. Please try again.");
        }
    }

    // Login Functionality
    private static void loginUser(Scanner scanner) {
        if (loggedInUser != null) {
            System.out.println("Already logged in as " + loggedInUser.getName());
            return;
        }

        System.out.println("Enter name:");
        String name = scanner.next();
        System.out.println("Enter password:");
        String password = scanner.next();

        try {
            Person user = em.createQuery(
                            "SELECT p FROM Person p WHERE p.name = :name AND p.password = :password", Person.class)
                    .setParameter("name", name)
                    .setParameter("password", password)
                    .getSingleResult();

            loggedInUser = user;
            System.out.println("Login successful. Welcome, " + loggedInUser.getName() + "!");
        } catch (NoResultException e) {
            System.out.println("Invalid name or password. Please try again.");
        }
    }

    // Logout Functionality
    private static void logoutUser() {
        if (loggedInUser == null) {
            System.out.println("No user is currently logged in.");
        } else {
            System.out.println("Logging out " + loggedInUser.getName() + "...");
            loggedInUser = null;
            System.out.println("Logout successful.");
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
    private static void displayOfferings(EntityManager em) {
        List<Offering> offerings = em.createQuery("SELECT o FROM Offering o", Offering.class).getResultList();
        for (Offering offering : offerings) {
            System.out.println("Offering: " + offering.getLesson().getName() + " on " + offering.getDateTime() + " " + offering.getDescription() + " "+ offering.isAvailable());

        }
    }
}