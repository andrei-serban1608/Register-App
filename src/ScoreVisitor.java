import org.json.simple.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScoreVisitor implements Visitor {
    private HashMap<Teacher, ArrayList<Tuple<Student, String, Double>>> examScores;
    private HashMap<Assistant, ArrayList<Tuple<Student, String, Double>>> partialScores;
    public ScoreVisitor() {
        examScores = new HashMap<>();
        partialScores = new HashMap<>();
    }
    public HashMap<Teacher, ArrayList<Tuple<Student, String, Double>>> getExamScores() {
        return examScores;
    }
    public HashMap<Assistant, ArrayList<Tuple<Student, String, Double>>> getPartialScores() {
        return partialScores;
    }
    private class Tuple<S, C, D> {
        private S student;
        private C courseName;
        private D score;
        public Tuple(S student, C courseName, D score) {
            this.student = student;
            this.courseName = courseName;
            this.score = score;
        }
        public S getStudent() {
            return student;
        }
        public C getCourseName() {
            return courseName;
        }
        public D getScore() {
            return score;
        }
    }
    public void addNewExamScoreEntry(Teacher teacher, Student student, String courseName, Double score) {
        for(Map.Entry<Teacher, ArrayList<Tuple<Student, String, Double>>> e : examScores.entrySet()) {
            if(e.getKey().equals(teacher)) {
                e.getValue().add(new Tuple<>(student, courseName, score));
                break;
            }
        }
    }
    public void addNewPartialScoreEntry(Assistant assistant, Student student, String courseName, Double score) {
        for(Map.Entry<Assistant, ArrayList<Tuple<Student, String, Double>>> e : partialScores.entrySet()) {
            if(e.getKey().equals(assistant)) {
                e.getValue().add(new Tuple<>(student, courseName, score));
                break;
            }
        }
    }
    public void visit(Teacher teacher) {
        ArrayList<Course> tempCourses = Catalog.getInstance().getCourses();
        int i, j;
        for (Map.Entry<Teacher, ArrayList<Tuple<Student, String, Double>>> e : examScores.entrySet()) {
            if (e.getKey().equals(teacher)) {
                for (i = 0; i < e.getValue().size(); i++) {
                    Grade tempGrade;
                    Tuple<Student, String, Double> tempTuple = e.getValue().get(i);
                    Student tempStudent = tempTuple.getStudent();
                    String tempCourseName = tempTuple.getCourseName();
                    Double tempScore = tempTuple.getScore();
                    for (j = 0; j < tempCourses.size(); j++) {
                        Course tempCourse = tempCourses.get(j);
                        for (Group g : tempCourse.getGroupMap().values()) {
                            for (Student s : g) {
                                if (s.equals(tempStudent)) {
                                    tempStudent = s;
                                    break;
                                }
                            }
                        }
                        if (tempCourse.getName().equals(tempCourseName)) {
                            ArrayList<Grade> tempGrades = tempCourse.getGrades();
                            if (tempCourse.getGrade(tempStudent) == null) {
                                tempGrades.add(new Grade(0.0, tempScore, tempStudent, tempCourseName));
                            } else {
                                tempCourse.getGrade(tempStudent).setExamScore(tempScore);
                            }
                            tempGrade = tempCourse.getGrade(tempStudent);
                            Catalog.getInstance().notifyObservers(tempGrade);
                        }
                    }
                }
            }
        }
    }
    public void visit(Assistant assistant) {
        ArrayList<Course> tempCourses = Catalog.getInstance().getCourses();
        int i, j;
        for (Map.Entry<Assistant, ArrayList<Tuple<Student, String, Double>>> e : partialScores.entrySet()) {
            if (e.getKey().equals(assistant)) {
                for (i = 0; i < e.getValue().size(); i++) {
                    Grade tempGrade;
                    Tuple<Student, String, Double> tempTuple = e.getValue().get(i);
                    Student tempStudent = tempTuple.getStudent();
                    String tempCourseName = tempTuple.getCourseName();
                    Double tempScore = tempTuple.getScore();
                    for (j = 0; j < tempCourses.size(); j++) {
                        Course tempCourse = tempCourses.get(j);
                        for (Group g : tempCourse.getGroupMap().values()) {
                            for (Student s : g) {
                                if (s.equals(tempStudent)) {
                                    tempStudent = s;
                                    break;
                                }
                            }
                        }
                        if (tempCourse.getName().equals(tempCourseName)) {
                            ArrayList<Grade> tempGrades = tempCourse.getGrades();
                            if (tempCourse.getGrade(tempStudent) == null) {
                                tempGrades.add(new Grade(tempScore, 0.0, tempStudent, tempCourseName));
                            } else {
                                tempCourse.getGrade(tempStudent).setPartialScore(tempScore);
                            }
                            tempGrade = tempCourse.getGrade(tempStudent);
                            Catalog.getInstance().notifyObservers(tempGrade);
                        }
                    }
                }
            }
        }
    }
}
