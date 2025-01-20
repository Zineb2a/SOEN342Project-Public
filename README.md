
# **Gym Management System** - Comprehensive Fitness Organization Management Solution

## 📖 **Project Overview**
The Gym Management System is a software application designed to streamline the operations of a fitness organization. It enables administrators, instructors, and clients to manage schedules, bookings, lessons, and offerings seamlessly. The system also supports legal guardians for minor clients and ensures efficient organization through role-based management.

This project was built using **Java (JPA + Hibernate)**, **MySQL**, and **UML design principles**, showcasing strong proficiency in database integration, object-oriented programming, and system design.

---

## 🚀 **Features**

- **Role-Based Management:**
  - Administrators can manage locations, instructors, clients, lessons, and offerings.
  - Instructors can view and select lessons based on availability and specialization.
  - Clients can book or cancel lessons and view offerings.
- **Booking System:** 
  - Supports bookings for private and group lessons with conflict validation.
  - Ensures legal guardians can register and manage bookings for minors.
- **Schedule Management:**
  - Tracks availability of rooms, instructors, and schedules.
  - Offers conflict-free scheduling of lessons and time slots.
- **System Constraints:** Implements all preconditions and postconditions from the project requirements to ensure data integrity.
- **Scalable and Maintainable:** Built with a modular architecture to support easy maintenance and future expansions.

---

## 🛠️ **Technologies Used**

- **Backend:** Java (JPA + Hibernate)
- **Database:** MySQL
- **Frontend:** Admin Console (CLI-based), Swing (Optional for GUI)
- **Design:** UML diagrams for domain modeling, SSDs, and relational model
- **Hosting:** Localhost or server deployment with MySQL
- **Version Control:** Git

---

## 🧠 **How It Works**

1. **User Roles:**
   - **Administrator:** Manages organization entities, including locations, rooms, schedules, and offerings.
   - **Instructor:** Views available lessons and accepts assignments based on specialization.
   - **Client:** Registers in the system, views offerings, and makes/cancels bookings.
2. **Lesson Management:** Lessons transition to offerings when instructors are assigned.
3. **Booking Process:**
   - Clients or their legal guardians book lessons, with real-time conflict detection for schedules.
4. **Cancellation Process:** Clients or administrators can cancel bookings, with proper validation for offering availability.
5. **Validation and Constraints:** Implements validation for preconditions and ensures postconditions are met as described in the system operations contracts.

---

## 🎯 **Key Use Cases**

1. **Administrator:**
   - Add, update, or delete locations, rooms, and schedules.
   - Manage lessons, offerings, instructors, and clients.
2. **Instructor:**
   - View and accept lessons matching their specialization and availability.
3. **Client:**
   - Book or cancel lessons, with validation for schedule conflicts.
   - Legal guardians can manage bookings for minors.

---

## 🧪 **Testing**

- **Unit Testing:** Coverage for all core operations such as bookings, offerings, and cancellations.
- **Integration Testing:** Validation of database interactions using JPA and Hibernate.

---

## ✨ **Demo**

Check out the live demonstration of the CLI application here:  
**[Watch the Demo on YouTube](https://youtu.be/131FBOKkgnU)**

## 📦 **Installation & Setup**

### **Requirements:**
- JDK 17 or higher
- MySQL 8.x or higher
- IDE (IntelliJ IDEA, Eclipse, etc.)
- Hibernate and JPA dependencies (included in `pom.xml`)

### **Steps:**

1. Clone the repository:
   
   ```bash
   git clone https://github.com/your-username/gym-management-system.git
   ```

2. Navigate to the project directory:
   
   ```bash
   cd gym-management-system
   ```

3. Set up your database:
   - Create a new MySQL database:
     ```sql
     CREATE DATABASE gym_management;
     ```
   - Configure the `application.properties` or `hibernate.cfg.xml` file with your database credentials:
     ```plaintext
     spring.datasource.url=jdbc:mysql://localhost:3306/gym_management
     spring.datasource.username=your-username
     spring.datasource.password=your-password
     ```

4. Run the application:
   - Use your IDE to execute the `ApplicationMain` class or:
     ```bash
     mvn spring-boot:run
     ```

5. Access the application:
   - For CLI-based interface: Follow the menu prompts.
   - For GUI (if implemented): Run the Swing-based admin console.
