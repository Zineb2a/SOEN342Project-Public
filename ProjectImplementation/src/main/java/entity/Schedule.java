package entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "schedule")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    // Many-to-one relationship with Room
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    // One-to-many relationship with TimeSlot
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TimeSlot> timeSlots = new ArrayList<>();

    // Many-to-one relationship with Lesson
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    public Schedule() {}

    public Schedule(LocalDateTime startTime, LocalDateTime endTime, Room room) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
    }

    // Method to add a time slot with overlap validation
    public boolean addTimeSlotWithValidation(TimeSlot newTimeSlot) {
        if (isTimeSlotOverlapping(newTimeSlot)) {
            System.out.println("Error: The new time slot overlaps with an existing one.");
            return false;
        } else {
            addTimeSlot(newTimeSlot);
            return true;
        }
    }

    private boolean isTimeSlotOverlapping(TimeSlot newTimeSlot) {
        for (TimeSlot existingTimeSlot : timeSlots) {
            if (existingTimeSlot.overlapsWith(newTimeSlot)) {
                return true; // Conflict found
            }
        }
        return false; // No conflicts
    }

    public void addTimeSlot(TimeSlot timeSlot) {
        timeSlots.add(timeSlot);
        timeSlot.setSchedule(this);
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public List<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(List<TimeSlot> timeSlots) {
        this.timeSlots = timeSlots;
    }
}
