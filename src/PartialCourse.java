import java.util.ArrayList;
public class PartialCourse extends Course {
    private PartialCourse(PartialCourseBuilder builder) {
        super(builder);
    }
    public static class PartialCourseBuilder extends CourseBuilder {
        public Course build() {
            return new PartialCourse(this);
        }
    }
    public ArrayList<Student> getGraduatedStudents() {
        ArrayList<Student> graduatedStudents = new ArrayList<>();
        int i;
        for (i = 0; i < getGrades().size(); i++) {
            if (getGrades().get(i).getTotal() >= 5) {
                graduatedStudents.add(getGrades().get(i).getStudent());
            }
        }
        return graduatedStudents;
    }
}
