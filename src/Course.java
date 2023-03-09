import java.util.Comparator;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.HashMap;
public abstract class Course {
    private String name;
    private Teacher teacher;
    private TreeSet<Assistant> assistants;
    private ArrayList<Grade> grades;
    private HashMap<String, Group> groupMap;
    private int credits;
    private Strategy strategy;
    private Snapshot snapshot = null;

    protected Course(CourseBuilder builder) {
        name = builder.name;
        teacher = builder.teacher;
        assistants = builder.assistants;
        grades = builder.grades;
        groupMap = builder.groupMap;
        credits = builder.credits;
    }
    public String getName() {
        return name;
    }
    public Teacher getTeacher() {
        return teacher;
    }
    public TreeSet<Assistant> getAssistants() {
        return assistants;
    }
    public ArrayList<Grade> getGrades() {
        return grades;
    }
    public HashMap<String, Group> getGroupMap() {
        return groupMap;
    }
    public int getCredits() {
        return credits;
    }
    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }
    public abstract static class CourseBuilder {
        private String name;
        private Teacher teacher;
        private TreeSet<Assistant> assistants;
        private ArrayList<Grade> grades;
        private HashMap<String, Group> groupMap;
        private int credits;
        public CourseBuilder name(String name) {
            this.name = name;
            return this;
        }
        public CourseBuilder teacher(Teacher teacher) {
            this.teacher = teacher;
            return this;
        }
        public CourseBuilder assistants(TreeSet<Assistant> assistants) {
            this.assistants = assistants;
            return this;
        }
        public CourseBuilder grades(ArrayList<Grade> grades) {
            this.grades = grades;
            return this;
        }
        public CourseBuilder groupMap(HashMap<String, Group> groupMap) {
            this.groupMap = groupMap;
            return this;
        }
        public CourseBuilder credits(int credits) {
            this.credits = credits;
            return this;
        }
        public abstract Course build();
    }
    public void addAssistant(String ID, Assistant assistant) {
        groupMap.get(ID).setAssistant(assistant);
        assistants.add(assistant);
    }
    public void addStudent(String ID, Student student) {
        groupMap.get(ID).add(student);
    }
    public void addGroup(Group group) {
        groupMap.put(group.getID(), group);
        assistants.add(group.getAssistant());
    }
    public void addGroup(String ID, Assistant assistant) {
        groupMap.put(ID, new Group(ID, assistant));
        assistants.add(assistant);
    }
    public void addGroup(String ID, Assistant assistant, Comparator<Student> comp) {
        groupMap.put(ID, new Group(ID, assistant, comp));
        assistants.add(assistant);
    }
    public Grade getGrade(Student student) {
        int i;
        for (i = 0; i < grades.size(); i++) {
            if (grades.get(i).getStudent().equals(student)) {
                return grades.get(i);
            }
        }
        return null;
    }
    public void addGrade(Grade grade) {
        grades.add(grade);
    }
    public ArrayList<Student> getAllStudents() {
        ArrayList<Student> students = new ArrayList<>();
        int i;
        for (Group g : groupMap.values()) {
            for (i = 0; i < g.size(); i++) {
                students.add(g.get(i));
            }
        }
        return students;
    }
    public HashMap<Student, Grade> getAllStudentGrades() {
        HashMap<Student, Grade> studentGrades = new HashMap<>();
        int i;
        for (i = 0; i < grades.size(); i++) {
            studentGrades.put(grades.get(i).getStudent(), grades.get(i));
        }
        return studentGrades;
    }
    public abstract ArrayList<Student> getGraduatedStudents();
    public Student getBestStudent() {
        return strategy.getBestGrade(grades);
    }
    private class Snapshot {
        private ArrayList<Grade> grades = new ArrayList<>(Course.this.grades);
        public void setGrades(ArrayList<Grade> grades) {
            int i;
            for (i = 0; i < this.grades.size(); i++) {
                this.grades.set(i, (Grade) grades.get(i).clone());
            }
        }
    }
    public void makeBackup() {
        snapshot = new Snapshot();
        snapshot.setGrades(grades);
    }
    public void undo() {
        int i;
        if (snapshot == null) {
            throw new NullPointerException("No existing backup!");
        }
        for (i = 0; i < grades.size(); i++) {
            grades.set(i, (Grade) snapshot.grades.get(i).clone());
        }
    }
}
