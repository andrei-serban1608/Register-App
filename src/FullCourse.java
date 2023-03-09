import java.util.ArrayList;
public class FullCourse extends Course {
    public FullCourse(FullCourseBuilder builder) {
        super(builder);
    }
    public static class FullCourseBuilder extends CourseBuilder {
        public Course build() {
            return new FullCourse(this);
        }
    }
    public ArrayList<Student> getGraduatedStudents() {
        ArrayList<Student> graduatedStudents = new ArrayList<>();
        int i;
        for (i = 0; i < getGrades().size(); i++) {
            if (getGrades().get(i).getPartialScore() >= 3 && getGrades().get(i).getExamScore() >= 2) {
                graduatedStudents.add(getGrades().get(i).getStudent());
            }
        }
        return graduatedStudents;
    }
}
