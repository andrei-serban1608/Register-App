import java.lang.Comparable;
import java.lang.Cloneable;
public class Grade implements Comparable, Cloneable{
    private Double partialScore, examScore;
    private Student student;
    private String course;
    public Grade(Double partialScore, Double examScore, Student student, String course) {
        this.partialScore = partialScore;
        this.examScore = examScore;
        this.student = student;
        this.course = course;
    }
    public Grade(Grade grade) {
        this.partialScore = grade.partialScore;
        this.examScore = grade.examScore;
        this.student = grade.student;
        this.course = grade.course;
    }
    public Double getPartialScore() {
        return partialScore;
    }
    public Double getExamScore() {
        return examScore;
    }
    public Student getStudent() {
        return student;
    }
    public String getCourse() {
        return course;
    }
    public void setPartialScore(Double partialScore) {
        this.partialScore = partialScore;
    }
    public void setExamScore(Double examScore) {
        this.examScore = examScore;
    }
    public void setStudent(Student student) {
        this.student = student;
    }
    public void setCourse(String course) {
        this.course = course;
    }
    public int compareTo(Object o) {
        if (o == null) {
            throw new NullPointerException("Null Argument!");
        }
        if (!(o instanceof Grade)) {
            throw new ClassCastException("Parameter given is not a Grade!");
        }
        return getTotal().compareTo(((Grade) o).getTotal());
    }
    public boolean equals(Object obj) {
        if (obj == null) {
            throw new NullPointerException("Null Argument!");
        }
        if (!(obj instanceof Grade)) {
            throw new ClassCastException("Parameter given is not a Grade!");
        }
        return getPartialScore().equals(((Grade) obj).getPartialScore())
                && getExamScore().equals(((Grade) obj).getExamScore());
    }
    public Double getTotal() {
        return partialScore + examScore;
    }
    public Object clone() {
        return new Grade(this);
    }
    public String toString() {
        return "Partial: " + getPartialScore() + "\nExamen: " + getExamScore() + "\nTotal: " + getTotal() + "\n";
    }
}
