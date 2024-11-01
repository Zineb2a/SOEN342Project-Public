public class Lesson {

    private String type;

    public Lesson(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static class Specialization {

        private String type;
        private Lesson lesson;

        public Specialization(String type, Lesson lesson) {
            this.type = type;
            this.lesson = lesson;
        }

        public String getType() {
            return type;
        }

        public Lesson getLesson() {
            return lesson;
        }
    }
}

//In process