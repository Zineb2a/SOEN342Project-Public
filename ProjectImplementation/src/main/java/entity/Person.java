package entity;

import jakarta.persistence.*;

@Entity
@Table(name = "person")
public abstract class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "password", nullable = false)
    private String password; // In a real application, store this hashed and salted

    @Transient
    private boolean loggedIn = false;

    public Person(Long id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public Person() {}

    public Person(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    // Register a new person (Simplified for illustration purposes)
    public void register(String name, String phoneNumber, String password) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
        System.out.println("Registration successful for " + name);
    }

    // Login method (Simplified - assumes password validation is basic)
    public boolean login(String name, String password) {
        if (this.name.equals(name) && this.password.equals(password)) {
            this.loggedIn = true;
            System.out.println(name + " has logged in successfully.");
            return true;
        } else {
            System.out.println("Login failed for " + name);
            return false;
        }
    }

    // Logout method
    public void logout() {
        if (loggedIn) {
            this.loggedIn = false;
            System.out.println(name + " has logged out.");
        } else {
            System.out.println(name + " is not logged in.");
        }
    }
}