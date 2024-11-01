public class Offering {

    private boolean available;
    private Lesson lesson;
    private Mode mode;
    private Schedule schedule;
    private Space space;
    private Instructor instructor;

    public Offering(Lesson lesson, Schedule schedule, Space space, Mode mode) {
        this.lesson = lesson;
        this.schedule = schedule;
        this.space = space;
        this.mode = mode;
        this.available = true;
        this.instructor = null;
    }

    public void assignInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public void updateAvailability(boolean available) {
        this.available = available;
    }

    public boolean isAvailable() {
        return available;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public Mode getMode() {
        return mode;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Space getSpace() {
        return space;
    }

    public Instructor getInstructor() {
        return instructor;
    }
}
//In process