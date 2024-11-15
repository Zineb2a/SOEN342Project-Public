import entity.*;
import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ApplicationMain {

    private static EntityManager em;
    private static Client loggedInClient;
    private static Instructor loggedInInstructor;
    private static Administrator loggedInAdmin;



    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("lesson_management");
        em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
//            initializeData();
            em.getTransaction().commit();

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("Select an option: 1. Register\n2. Login \n3. View Offerings\n4.Logout \n5. Exit");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1 -> registerUser(scanner);
                    case 2 -> loginUser(scanner);
                    case 3 -> viewOfferings(scanner);
                    case 4 -> logoutUser();
                    case 5 -> {
                        System.out.println("Exiting the application.");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            }
        } finally {
            em.close();
            emf.close();
        }
    }

    private static void initializeData(){
        Organization organization = new Organization();
        organization.setName("EconoFitness");

        em.persist(organization);

        Location location1 = new Location();
        location1.setName("Toronto Gym");
        location1.setCity("Toronto");
        location1.setOrganization(organization);
        organization.addLocation(location1);

        em.persist(location1);
        em.persist(organization);

        Administrator admin = new Administrator(null, "Jack Ryan",organization,location1);
        admin.setOrganization(organization);
        admin.setPassword("krasinski");
        em.persist(admin);

        Room room1 = new Room();
        room1.setName("Room 1");
        room1.setLocation(location1);

        Room room2 = new Room();
        room2.setName("Room 2");
        room2.setLocation(location1);

        organization.addLocation(location1);
        location1.addRoom(room1);
        location1.addRoom(room2);

        em.persist(room1);
        em.persist(room2);

        em.persist(location1);
        em.persist(organization);



        Schedule schedule = new Schedule(
                LocalDateTime.of(2024, Month.JANUARY, 5, 13, 0),
                LocalDateTime.of(2024, Month.JANUARY, 5, 15, 0),
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

        Lesson yogaLesson = admin.createLesson("Yoga Class","Private",true,location1);

        Lesson pilatesLesson = admin.createLesson("Pilates Class","Group",false,location1);

        Lesson MMALesson = admin.createLesson("MMA Class","Group",false,location1);

        Lesson swimmingLesson = admin.createLesson("Swimming Class","Group",false,location1);

        Lesson MuayThaiLesson = admin.createLesson("Muay Thai Class","Group",false,location1);



        em.persist(yogaLesson);
        em.persist(pilatesLesson);
        em.persist(MMALesson);
        em.persist(swimmingLesson);
        em.persist(MuayThaiLesson);


        Instructor instructor1 = new Instructor(null, "Stipe Miocic", "MMA");
        instructor1.setPassword("Legend");
        em.persist(instructor1);

        Offering offered = instructor1.createOffering(MMALesson, room1, schedule,
                LocalDateTime.of(2024, Month.JANUARY, 5, 10, 0), 21, "Learn MMA from the baddest man on the planet");
        em.persist(offered);

        Offering offered2 = instructor1.createOffering(pilatesLesson, room1, schedule,
                LocalDateTime.of(2024, Month.JANUARY, 13, 10, 0), 10,"Come explore your body to the fullest extent");

        em.persist(offered2);


    }

    private static void registerUser(Scanner scanner) {
        System.out.println("Select user type for registration: 1. Client 2. Instructor");
        int userType = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.println("Enter name:");
        String name = scanner.nextLine();

        System.out.println("Enter password:");
        String password = scanner.nextLine();

        // Check for unique password
        if (findPersonByPassword(password) != null) {
            System.out.println("This password is already taken. Please choose a unique password.");
            return;
        }

        try {
            // Start the transaction
            em.getTransaction().begin();

            if (!em.getTransaction().isActive()) {
                System.out.println("Failed to start transaction.");
                return;
            }

            if (userType == 1) {
                System.out.print("Enter age: ");
                int age = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                Client client = new Client(null, name, age);
                client.setPassword(password);

                // Check if client is underage and assign guardian
                if (age < 18) {
                    System.out.println("Client is underage. Checking for assigned legal guardian...");
                    LegalGuardian guardian = findOrRegisterLegalGuardian(scanner);

                    if (guardian != null) {
                        client.setGuardian(guardian);
                        guardian.addClient(client);  // Ensure guardian is not null
                    } else {
                        System.out.println("Failed to register guardian.");
                        return;
                    }
                }

                em.persist(client);
                System.out.println("Client registered successfully.");

            } else if (userType == 2) {
                System.out.print("Enter specialization (e.g., Yoga, Swimming): ");
                String specialization = scanner.nextLine();

                Instructor instructor = new Instructor(null, name, specialization);
                instructor.setPassword(password);

                List<String> availableCities = new ArrayList<>();
                System.out.println("Enter the cities you are available to work in. Type each city name and press Enter. Type '1' when done:");

                while (true) {
                    String city = scanner.nextLine();
                    if ("1".equals(city)) {
                        break;
                    }
                    availableCities.add(city);
                    System.out.println("City added: " + city + ". Enter another city or '1' to finish.");
                }

                instructor.setAvailableCities(availableCities);
                em.persist(instructor);
                System.out.println("Instructor registered successfully.");

            } else {
                System.out.println("Invalid user type selection.");
            }

            // Commit the transaction only if it's active
            if (em.getTransaction().isActive()) {
                em.getTransaction().commit();
                System.out.println("Transaction committed successfully.");
            } else {
                System.out.println("Transaction not active, cannot commit.");
            }

        } catch (Exception e) {
            // If any error occurs, rollback the transaction
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
                System.out.println("Transaction rolled back due to an error.");
            }
            System.out.println("Registration failed. Please try again.");
            e.printStackTrace();
        }
    }

    private static Person findPersonByPassword(String password) {
        try {
            return em.createQuery("SELECT p FROM Person p WHERE p.password = :password", Person.class)
                    .setParameter("password", password)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    private static void loginUser(Scanner scanner) {
        if (loggedInClient != null) {
            System.out.println("Already logged in as " + loggedInClient.getName());
            return;
        }

        System.out.println("Enter name:");
        String name = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();

        try {
            Person user = em.createQuery(
                            "SELECT p FROM Person p WHERE p.name = :name AND p.password = :password", Person.class)
                    .setParameter("name", name)
                    .setParameter("password", password)
                    .getSingleResult();

            if (user instanceof Client) {
                loggedInClient = (Client) user;
                System.out.println("Login successful. Welcome, " + loggedInClient.getName() + "!");
                displayClientOptions(scanner); // Show client-specific options after login
            } else if (user instanceof Instructor){
                loggedInInstructor = (Instructor) user;
                System.out.println("Login successful. Welcome, " + loggedInInstructor.getName() + "!");
                displayInstructorOptions(scanner);

            }
            else if (user instanceof Administrator){
                loggedInAdmin = (Administrator) user;
                System.out.println("Login successful. Welcome, " + loggedInAdmin.getName() + "!");
                handleAdminOperations(loggedInAdmin, scanner);
            }
            {
                System.out.println("Only clients can book offerings. Instructors can only view offerings.");
            }
        } catch (NoResultException e) {
            System.out.println("Invalid name or password. Please try again.");
        }
    }
    private static void handleAdminOperations(Administrator admin, Scanner scanner) {
        while (loggedInAdmin != null) {
            System.out.println("""
                    Choose an operation:
                    1. Delete Client Account
                    2. Delete Instructor Account
                    3. View All Bookings
                    4. Delete Booking
                    5. Create Lesson
                    6. Logout
                    
                    """);

            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1 -> deleteClientAccount(admin, scanner);
                case 2 -> deleteInstructorAccount(admin, scanner);
                case 3 -> viewAllBookings(admin);
                case 4 -> deleteBooking(admin, scanner);
                case 5 -> createLesson(admin, scanner);
                case 6 -> {
                    loggedInAdmin = null;
                    System.out.println("Logged out successfully.");
                }

                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    private static void deleteClientAccount(Administrator admin, Scanner scanner) {
        System.out.println("Enter Client ID to delete:");
        Long clientId = Long.parseLong(scanner.nextLine());

        em.getTransaction().begin();
        try {
            Client client = em.find(Client.class, clientId);
            if (client != null) {
                em.remove(client);
                System.out.println("Client account deleted: " + client.getName());
            } else {
                System.out.println("Client not found.");
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("Failed to delete client account.");
        }
    }
    private static void deleteInstructorAccount(Administrator admin, Scanner scanner) {
        System.out.println("Enter Instructor ID to delete:");
        Long instructorId = Long.parseLong(scanner.nextLine());

        em.getTransaction().begin();
        try {
            Instructor instructor = em.find(Instructor.class, instructorId);
            if (instructor != null) {
                em.remove(instructor);
                System.out.println("Instructor account deleted: " + instructor.getName());
            } else {
                System.out.println("Instructor not found.");
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("Failed to delete instructor account.");
        }
    }
    private static void deleteBooking(Administrator admin, Scanner scanner) {
        System.out.println("Enter Booking ID to delete:");
        Long bookingId = Long.parseLong(scanner.nextLine());

        em.getTransaction().begin();
        try {


            Booking booking = em.find(Booking.class, bookingId);


            if (booking != null &&  booking.getOffering().getRoom().getLocation().equals(admin.getLocation())) {
                em.remove(booking);
                System.out.println("Booking deleted: " + bookingId);
            } else {
                System.out.println("Booking not found or not within administrator's location.");
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("Failed to delete booking.");
        }
    }
    private static void createLesson(Administrator admin, Scanner scanner) {
        System.out.println("Enter Lesson Name:");
        String name = scanner.nextLine();
        System.out.println("Enter Lesson Type:");
        String type = scanner.nextLine();
        System.out.println("Is this lesson private? (true/false):");
        Boolean isPrivate = Boolean.parseBoolean(scanner.nextLine());

        em.getTransaction().begin();
        try {
            Lesson lesson = new Lesson();
            lesson.setName(name);
            lesson.setType(type);
            lesson.setIsPrivate(isPrivate);
            lesson.setLocation(admin.getLocation());

            em.persist(lesson);
            em.getTransaction().commit();
            System.out.println("Lesson created: " + name);
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("Failed to create lesson.");
        }
    }
    private static void viewAllBookings(Administrator admin) {
        em.getTransaction().begin();
        try {
            List<Object[]> results = em.createQuery(
                            "SELECT b.client.name, b.offering.lesson.name, b.id " +
                                    "FROM Booking b " +
                                    "JOIN b.client c " +
                                    "JOIN b.offering o " +
                                    "WHERE b.offering.room.location = :location", Object[].class)
                    .setParameter("location", admin.getLocation())
                    .getResultList();

            em.getTransaction().commit();

            results.forEach(row -> {
                String clientName = (String) row[0];
                String offeringName = (String) row[1];
                Long bookingId = (Long) row[2];
                System.out.println("Booking ID: " + bookingId + ", Client: " + clientName + ", Offering: " + offeringName);
            });
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Failed to fetch bookings.");
        }
    }
    private static void displayClientOptions(Scanner scanner) {
        while (loggedInClient != null) {
            System.out.println("Select an option: \n1. View Offerings \n2. View Bookings \n3. Logout");
            int clientChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (clientChoice) {
                case 1 -> viewOfferings(scanner);
                case 2 -> viewBookings();
                case 3 -> logoutUser();
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void viewLessons() {
        TypedQuery<Lesson> query = em.createQuery("SELECT l FROM Lesson l", Lesson.class);
        List<Lesson> lessons = query.getResultList();

        if (lessons.isEmpty()) {
            System.out.println("No lessons available.");
        } else {
            System.out.println("Available Lessons:");
            for (Lesson lesson : lessons) {
                System.out.println("- "+ " lessonID "+ lesson.getId() + " - " + lesson.getName() + " - " + lesson.getType() +" Company Name: "+ lesson.getLocation().getOrganization().getName() + " Location: " +lesson.getLocation().getName() +","+ lesson.getLocation().getCity() + " Lesson Time: "+ lesson.getOfferings());
            }
        }
    }
    private static void createOffering(Scanner scanner) {
        viewLessons();
        System.out.println("Enter the LessonID of the lesson you'd like to create an offering for:");
        String lessonName = scanner.nextLine();

        Lesson selectedLesson = em.find(Lesson.class, lessonName);
        if (selectedLesson == null) {
            System.out.println("Lesson not found. Please check the ID and try again.");
            return;
        }

        System.out.println("Enter the room ID:");
        String roomName = scanner.nextLine();
        Room selectedRoom = em.find(Room.class, roomName);
        if (selectedRoom == null) {
            System.out.println("Room not found. Please check the ID and try again.");
            return;
        }

        System.out.println("Enter the schedule ID:");
        int scheduleId = scanner.nextInt();
        scanner.nextLine(); // consume newline
        Schedule selectedSchedule = em.find(Schedule.class, scheduleId);
        if (selectedSchedule == null) {
            System.out.println("Schedule not found. Please check the ID and try again.");
            return;
        }

        Offering existingOffering = em.createQuery(
                        "SELECT o FROM Offering o WHERE o.schedule = :schedule", Offering.class)
                .setParameter("schedule", selectedSchedule)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);

        if (existingOffering != null) {
            System.out.println("This schedule is already assigned to an offering. Please choose another schedule.");
            return;
        }

        System.out.println("Enter the date and time for this offering (YYYY-MM-DD HH:MM):");
        String dateTimeInput = scanner.nextLine();
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeInput, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        System.out.println("Enter the maximum capacity for this offering:");
        int maxCapacity = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Please provide a description for this offering:");
        String description = scanner.nextLine();

        em.getTransaction().begin();
        try {

            Offering newOffering = loggedInInstructor.createOffering(selectedLesson, selectedRoom, selectedSchedule, dateTime, maxCapacity, description);

            em.persist(newOffering);
            em.getTransaction().commit();
            System.out.println("Offering created successfully for " + selectedLesson.getName());
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
            System.out.println("Failed to create offering. Please try again.");
        }
    }

    private static void viewMyOfferings() {
        TypedQuery<Offering> query = em.createQuery("SELECT o FROM Offering o WHERE o.instructor = :instructor", Offering.class);
        query.setParameter("instructor", loggedInInstructor);
        List<Offering> offerings = query.getResultList();

        if (offerings.isEmpty()) {
            System.out.println("You have no offerings.");
        } else {
            System.out.println("Your Offerings:");
            for (Offering offering : offerings) {
                System.out.println("- " + offering.getLesson().getName() + " on " + offering.getDateTime());
            }
        }
    }

    private static void displayInstructorOptions(Scanner scanner) {
        while (loggedInInstructor != null) {
            System.out.println("Select an option: \n1. View Lessons \n2. Create Offering \n3. View My Offerings \n4. Logout");
            int instructorChoice = scanner.nextInt();
            scanner.nextLine();

            switch (instructorChoice) {
                case 1 -> viewLessons();
                case 2 -> createOffering(scanner);
                case 3 -> viewMyOfferings();
                case 4 -> {
                    loggedInInstructor = null;
                    System.out.println("Logged out successfully.");
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void viewOfferings(Scanner scanner) {
        List<Offering> offerings = em.createQuery("SELECT o FROM Offering o", Offering.class).getResultList();
        for (int i = 0; i < offerings.size(); i++) {
            Offering offering = offerings.get(i);
            System.out.println((i + 1) + ". Offering: " + offering.getLesson().getName() +
                    " on " + offering.getDateTime() +
                    " | Room: " + offering.getRoom().getName() +
                    " | Location: " + offering.getRoom().getLocation().getName()  +
                    " | City: " + offering.getRoom().getLocation().getCity() +
                    " | start Time:" + offering.getSchedule().getStartTime() +
                    " | end Time:" + offering.getSchedule().getEndTime() +
                    " | Max Capacity: " + offering.getMaxCapacity() +
                    " | Available: " + offering.isAvailable());
        }

        if (loggedInClient != null) {
            System.out.println("Enter the number of the offering you want to reserve, or 0 to go back:");
            int selection = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (selection > 0 && selection <= offerings.size()) {
                Offering selectedOffering = offerings.get(selection - 1);
                if (selectedOffering.isAvailable()) {
                    makeBooking(selectedOffering);
                } else {
                    System.out.println("Selected offering is not available.");
                }
            } else if (selection != 0) {
                System.out.println("Invalid selection.");
            }
        } else {
            System.out.println("Please log in as a client to reserve an offering.");
        }
    }

    private static void makeBooking(Offering offering) {
        em.getTransaction().begin();
        try {
            // Check if the client has an existing booking for this offering
            TypedQuery<Booking> query = em.createQuery(
                    "SELECT b FROM Booking b WHERE b.client = :client AND b.offering = :offering",
                    Booking.class);
            query.setParameter("client", loggedInClient);
            query.setParameter("offering", offering);
            List<Booking> existingBookings = query.getResultList();

            if (!existingBookings.isEmpty()) {
                System.out.println("You are already booked for this offering. Cannot book twice.");
                em.getTransaction().rollback();
                return;
            }

            Booking booking = new Booking(loggedInClient, offering, Instant.now());
            booking.setIsAvailable(false);
            em.persist(booking);
            em.merge(offering); // Save the updated offering status if needed

            em.getTransaction().commit();
            System.out.println("Booking successful for " + offering.getLesson().getName() + " on " + offering.getDateTime());
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
            System.out.println("Booking failed. Please try again.");
        }
    }

    private static void viewBookings() {
        if (loggedInClient != null) {
            List<Booking> bookings = em.createQuery(
                            "SELECT b FROM Booking b WHERE b.client = :client", Booking.class)
                    .setParameter("client", loggedInClient)
                    .getResultList();

            if (bookings.isEmpty()) {
                System.out.println("You have no bookings.");
            } else {
                System.out.println("Your bookings:");
                for (Booking booking : bookings) {
                    System.out.println("Booking for: " + booking.getOffering().getLesson().getName() +
                            " on " + booking.getOffering().getDateTime() +
                            " | Room: " + booking.getOffering().getRoom().getName());
                }
            }
        } else {
            System.out.println("You need to be logged in as a client to view bookings.");
        }
    }

    private static void logoutUser() {
        if (loggedInClient == null && loggedInInstructor == null && loggedInAdmin == null) {
            System.out.println("No user is currently logged in.");
        } else {
            if (loggedInClient != null) {
                System.out.println("Logging out Client: " + loggedInClient.getName() + "...");
                loggedInClient = null;
            } else if (loggedInInstructor != null) {
                System.out.println("Logging out Instructor: " + loggedInInstructor.getName() + "...");
                loggedInInstructor = null;
            } else if (loggedInAdmin != null) {
                System.out.println("Logging out Administrator: " + loggedInAdmin.getName() + "...");
                loggedInAdmin = null;
            }
            System.out.println("Logout successful.");
        }
    }
    private static LegalGuardian findOrRegisterLegalGuardian(Scanner scanner) {
        System.out.println("Do you have a legal guardian already registered? (yes/no)");
        String answer = scanner.nextLine();

        if ("yes".equalsIgnoreCase(answer)) {
            System.out.print("Enter guardian's name: ");
            String guardianName = scanner.nextLine();

            LegalGuardian guardian = em.find(LegalGuardian.class, guardianName);
            if (guardian != null) {
                return guardian;
            } else {
                System.out.println("Guardian not found.");
            }
        }

        System.out.println("Registering new legal guardian...");
        System.out.print("Enter guardian's name: ");
        String guardianName = scanner.nextLine();

        System.out.print("Enter password for the legal guardian: ");
        String password = scanner.nextLine();

        LegalGuardian newGuardian = new LegalGuardian(null, guardianName);
        newGuardian.setPassword(password);


        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }

        try {
            em.persist(newGuardian);
            em.getTransaction().commit();
            System.out.println("Legal guardian registered successfully.");

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Failed to register legal guardian.");
            e.printStackTrace();
        }

        return newGuardian;
    }

}