public class Notification {
    private Grade grade;
    public Notification(Grade grade) {
        this.grade = grade;
    }
    public Grade getGrade() {
        return grade;
    }
    public String toString() {
        return "Studentului " + grade.getStudent() + " i-a fost modificata nota la " + grade.getCourse() + ":\n" + grade;
    }
}
