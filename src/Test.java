import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Test {
    public static void main(String[] args) throws IOException, ParseException {
        Object parser = new JSONParser().parse(new FileReader("src/test.json"));
        JSONArray jsonCourses = (JSONArray) ((JSONObject) parser).get("courses");
        JSONArray jsonExamScores = (JSONArray) ((JSONObject) parser).get("examScores");
        JSONArray jsonPartialScores = (JSONArray) ((JSONObject) parser).get("partialScores");
        int i, j, k;
        for (i = 0; i < jsonCourses.size(); i++) {
            String tempName = (String) ((JSONObject) jsonCourses.get(i)).get("name");
            String tempTeacherFirstName = (String) ((JSONObject) ((JSONObject) jsonCourses.get(i)).get("teacher")).get("firstName");
            String tempTeacherLastName = (String) ((JSONObject) ((JSONObject) jsonCourses.get(i)).get("teacher")).get("lastName");
            Teacher tempTeacher = (Teacher) UserFactory.getUser("Teacher", tempTeacherFirstName, tempTeacherLastName);
            JSONArray jsonAssistants = (JSONArray) ((JSONObject) jsonCourses.get(i)).get("assistants");
            TreeSet<Assistant> tempAssistants = new TreeSet<>();
            JSONArray jsonGroups = (JSONArray) ((JSONObject) jsonCourses.get(i)).get("groups");
            HashMap<String, Group> tempGroupMap = new HashMap<>();
            Strategy tempStrategy;
            Course tempCourse;
            for (j = 0; j < jsonAssistants.size(); j++) {
                String tempAssistantFirstName = (String) ((JSONObject) jsonAssistants.get(j)).get("firstName");
                String tempAssistantLastName = (String) ((JSONObject) jsonAssistants.get(j)).get("lastName");
                tempAssistants.add((Assistant) UserFactory.getUser("Assistant", tempAssistantFirstName, tempAssistantLastName));
            }
            for (j = 0; j < jsonGroups.size(); j++) {
                String tempGroupID = (String) ((JSONObject) jsonGroups.get(j)).get("ID");
                String tempAssistantFirstName = (String) ((JSONObject) ((JSONObject) jsonGroups.get(j)).get("assistant")).get("firstName");
                String tempAssistantLastName = (String) ((JSONObject) ((JSONObject) jsonGroups.get(j)).get("assistant")).get("lastName");
                Assistant tempAssistant = (Assistant) UserFactory.getUser("Assistant", tempAssistantFirstName, tempAssistantLastName);
                JSONArray jsonStudents = (JSONArray) ((JSONObject) jsonGroups.get(j)).get("students");
                Group tempGroup = new Group(tempGroupID, tempAssistant);
                for (k = 0; k < jsonStudents.size(); k++) {
                    String tempStudentFirstName = (String) ((JSONObject) jsonStudents.get(k)).get("firstName");
                    String tempStudentLastName = (String) ((JSONObject) jsonStudents.get(k)).get("lastName");
                    Student tempStudent = (Student) UserFactory.getUser("Student", tempStudentFirstName, tempStudentLastName);
                    if(((JSONObject) jsonStudents.get(k)).get("mother") != null) {
                        String tempMotherFirstName = (String) ((JSONObject) ((JSONObject) jsonStudents.get(k)).get("mother")).get("firstName");
                        String tempMotherLastName = (String) ((JSONObject) ((JSONObject) jsonStudents.get(k)).get("mother")).get("lastName");
                        Parent tempMother = (Parent) UserFactory.getUser("Parent", tempMotherFirstName, tempMotherLastName);
                        tempStudent.setMother(tempMother);
                        Catalog.getInstance().addObserver(tempMother);
                    }
                    if(((JSONObject) jsonStudents.get(k)).get("father") != null) {
                        String tempFatherFirstName = (String) ((JSONObject) ((JSONObject) jsonStudents.get(k)).get("father")).get("firstName");
                        String tempFatherLastName = (String) ((JSONObject) ((JSONObject) jsonStudents.get(k)).get("father")).get("lastName");
                        Parent tempFather = (Parent) UserFactory.getUser("Parent", tempFatherFirstName, tempFatherLastName);
                        tempStudent.setFather(tempFather);
                        Catalog.getInstance().addObserver(tempFather);
                    }
                    tempGroup.add(tempStudent);
                }
                tempGroupMap.put(tempGroupID, tempGroup);
            }
            if ((((JSONObject) jsonCourses.get(i)).get("type")).equals("FullCourse")) {
                Course.CourseBuilder tempCourseBuilder = new FullCourse.FullCourseBuilder();
                tempCourseBuilder = tempCourseBuilder.name(tempName);
                tempCourseBuilder = tempCourseBuilder.assistants(tempAssistants);
                tempCourseBuilder = tempCourseBuilder.teacher(tempTeacher);
                tempCourseBuilder = tempCourseBuilder.grades(new ArrayList<>());
                tempCourseBuilder = tempCourseBuilder.groupMap(tempGroupMap);
                tempCourseBuilder = tempCourseBuilder.credits(5);
                tempCourse = tempCourseBuilder.build();
            } else {
                Course.CourseBuilder tempCourseBuilder = new PartialCourse.PartialCourseBuilder();
                tempCourseBuilder = tempCourseBuilder.name(tempName);
                tempCourseBuilder = tempCourseBuilder.assistants(tempAssistants);
                tempCourseBuilder = tempCourseBuilder.teacher(tempTeacher);
                tempCourseBuilder = tempCourseBuilder.grades(new ArrayList<>());
                tempCourseBuilder = tempCourseBuilder.groupMap(tempGroupMap);
                tempCourseBuilder = tempCourseBuilder.credits(5);
                tempCourse = tempCourseBuilder.build();
            }
            if ((((JSONObject) jsonCourses.get(i)).get("strategy")).equals("BestPartialScore")) {
                tempStrategy = new BestPartialScore();
            }
            else if ((((JSONObject) jsonCourses.get(i)).get("strategy")).equals("BestExamScore")) {
                tempStrategy = new BestExamScore();
            } else {
                tempStrategy = new BestTotalScore();
            }
            tempCourse.setStrategy(tempStrategy);
            Catalog.getInstance().addCourse(tempCourse);
        }
        ScoreVisitor visitor = new ScoreVisitor();
        ArrayList<Teacher> allTeachers = new ArrayList<>();
        for (i = 0; i < jsonExamScores.size(); i++) {
            String tempTeacherFirstName = (String) ((JSONObject) ((JSONObject) jsonExamScores.get(i)).get("teacher")).get("firstName");
            String tempTeacherLastName = (String) ((JSONObject) ((JSONObject) jsonExamScores.get(i)).get("teacher")).get("lastName");
            Teacher tempTeacher = (Teacher) UserFactory.getUser("Teacher", tempTeacherFirstName, tempTeacherLastName);
            boolean hashFlag = false;
            for (Teacher t : visitor.getExamScores().keySet()) {
                if (t.equals(tempTeacher)) {
                    hashFlag = true;
                }
            }
            if (!hashFlag) {
                visitor.getExamScores().put(tempTeacher, new ArrayList<>());
                allTeachers.add(tempTeacher);
            }
            String tempStudentFirstName = (String) ((JSONObject) ((JSONObject) jsonExamScores.get(i)).get("student")).get("firstName");
            String tempStudentLastName = (String) ((JSONObject) ((JSONObject) jsonExamScores.get(i)).get("student")).get("lastName");
            Student tempStudent = (Student) UserFactory.getUser("Student", tempStudentFirstName, tempStudentLastName);
            String tempCourseName = (String) ((JSONObject) jsonExamScores.get(i)).get("course");
            Double tempExamScore = (Double) ((JSONObject) jsonExamScores.get(i)).get("grade");
            visitor.addNewExamScoreEntry(tempTeacher, tempStudent, tempCourseName, tempExamScore);
        }
        for (i = 0; i < allTeachers.size(); i++) {
            allTeachers.get(i).accept(visitor);
        }
        ArrayList<Assistant> allAssistants = new ArrayList<>();
        for (i = 0; i < jsonPartialScores.size(); i++) {
            String tempAssistantFirstName = (String) ((JSONObject) ((JSONObject) jsonPartialScores.get(i)).get("assistant")).get("firstName");
            String tempAssistantLastName = (String) ((JSONObject) ((JSONObject) jsonPartialScores.get(i)).get("assistant")).get("lastName");
            Assistant tempAssistant = (Assistant) UserFactory.getUser("Assistant", tempAssistantFirstName, tempAssistantLastName);
            boolean hashFlag = false;
            for (Assistant a : visitor.getPartialScores().keySet()) {
                if (a.equals(tempAssistant)) {
                    hashFlag = true;
                }
            }
            if (!hashFlag) {
                visitor.getPartialScores().put(tempAssistant, new ArrayList<>());
                allAssistants.add(tempAssistant);
            }
            String tempStudentFirstName = (String) ((JSONObject) ((JSONObject) jsonPartialScores.get(i)).get("student")).get("firstName");
            String tempStudentLastName = (String) ((JSONObject) ((JSONObject) jsonPartialScores.get(i)).get("student")).get("lastName");
            Student tempStudent = (Student) UserFactory.getUser("Student", tempStudentFirstName, tempStudentLastName);
            String tempCourseName = (String) ((JSONObject) jsonPartialScores.get(i)).get("course");
            Double tempPartialScore = (Double) ((JSONObject) jsonPartialScores.get(i)).get("grade");
            visitor.addNewPartialScoreEntry(tempAssistant, tempStudent, tempCourseName, tempPartialScore);
        }
        for (i = 0; i < allAssistants.size(); i++) {
            allAssistants.get(i).accept(visitor);
        }
        System.out.println("Testare addCourse() si removeCourse():");
        System.out.println("\nInainte de adaugare:");
        for (Course c : Catalog.getInstance().getCourses()) {
            System.out.println(c.getName());
        }
        Course cursFals = new FullCourse.FullCourseBuilder().name("Curs fals").build();
        Catalog.getInstance().addCourse(cursFals);
        System.out.println("\nDupa adaugare:");
        for (Course c : Catalog.getInstance().getCourses()) {
            System.out.println(c.getName());
        }
        Catalog.getInstance().removeCourse(cursFals);
        System.out.println("\nDupa stergere:");
        for (Course c : Catalog.getInstance().getCourses()) {
            System.out.println(c.getName());
        }
        System.out.println("\nTestare gettere din Course:");
        for (i = 0; i < Catalog.getInstance().getCourses().size(); i++) {
            System.out.println("\nNume Curs: " + Catalog.getInstance().getCourses().get(i).getName());
            System.out.println("Profesor Titular: " + Catalog.getInstance().getCourses().get(i).getTeacher());
            System.out.println("Asistenti: " + Catalog.getInstance().getCourses().get(i).getAssistants());
            System.out.println("Group Map: " + Catalog.getInstance().getCourses().get(i).getGroupMap());
            System.out.println("Note: " + Catalog.getInstance().getCourses().get(i).getGrades());
            System.out.println("Nr credite: " + Catalog.getInstance().getCourses().get(i).getCredits());
        }
        Course POO = Catalog.getInstance().getCourses().get(0);
        System.out.println("\nTestare addAssistant():");
        System.out.println("\nInainte de interschimbare:");
        for (Group g : POO.getGroupMap().values()) {
            System.out.println(g.getID() + " " + g.getAssistant());
        }
        POO.addAssistant("312CC", (Assistant) UserFactory.getUser("Assistant", "Alexandra", "Maria"));
        POO.addAssistant("314CC", (Assistant) UserFactory.getUser("Assistant", "Andrei", "Georgescu"));
        System.out.println("\nDupa interschimbare: ");
        for (Group g : POO.getGroupMap().values()) {
            System.out.println(g.getID() + " " + g.getAssistant());
        }
        POO.addAssistant("314CC", (Assistant) UserFactory.getUser("Assistant", "Alexandra", "Maria"));
        POO.addAssistant("312CC", (Assistant) UserFactory.getUser("Assistant", "Andrei", "Georgescu"));
        System.out.println("\nTestare addStudent():");
        System.out.println("\nGrupa 314CC inainte de adaugare:");
        System.out.println(POO.getGroupMap().get("314CC"));
        POO.addStudent("314CC", (Student) UserFactory.getUser("Student", "Student", "Adaugat"));
        System.out.println("\nGrupa 314CC dupa adaugare:");
        System.out.println(POO.getGroupMap().get("314CC"));
        System.out.println("\nTestare addGroup() fara comparator: ");
        System.out.println("\nGroupMap inainte de adaugare:");
        System.out.println(POO.getGroupMap());
        POO.addGroup("GrupaAdaugata1", (Assistant) UserFactory.getUser("Assistant", "Asistent", "Grupa 1"));
        POO.getGroupMap().get("GrupaAdaugata1").add((Student) UserFactory.getUser("Student", "AAAA", "ZZZZ"));
        POO.getGroupMap().get("GrupaAdaugata1").add((Student) UserFactory.getUser("Student", "CCCC", "XXXX"));
        POO.getGroupMap().get("GrupaAdaugata1").add((Student) UserFactory.getUser("Student", "BBBB", "YYYY"));
        POO.getGroupMap().get("GrupaAdaugata1").add((Student) UserFactory.getUser("Student", "DDDD", "WWWW"));
        Collections.sort(POO.getGroupMap().get("GrupaAdaugata1"), POO.getGroupMap().get("GrupaAdaugata1").getComparator());
        System.out.println("\nGroupMap dupa adaugare:");
        System.out.println(POO.getGroupMap());
        System.out.println("\nTestare addGroup() cu comparator: ");
        System.out.println("\nGroupMap inainte de adaugare:");
        System.out.println(POO.getGroupMap());
        POO.addGroup("GrupaAdaugata2", (Assistant) UserFactory.getUser("Assistant", "Asistent", "Grupa 2"), new Comparator<Student>() {
            public int compare(Student o1, Student o2) {
                return o1.getLastName().compareTo(o2.getLastName());
            }
        });
        Student s = (Student) UserFactory.getUser("Student", "AAAA", "ZZZZ");
        POO.getGroupMap().get("GrupaAdaugata2").add(s);
        POO.getGroupMap().get("GrupaAdaugata2").add((Student) UserFactory.getUser("Student", "CCCC", "XXXX"));
        POO.getGroupMap().get("GrupaAdaugata2").add((Student) UserFactory.getUser("Student", "BBBB", "YYYY"));
        POO.getGroupMap().get("GrupaAdaugata2").add((Student) UserFactory.getUser("Student", "DDDD", "WWWW"));
        Collections.sort(POO.getGroupMap().get("GrupaAdaugata2"), POO.getGroupMap().get("GrupaAdaugata2").getComparator());
        System.out.println("\nGroupMap dupa adaugare:");
        System.out.println(POO.getGroupMap());
        System.out.println("\nTestare addGrade():");
        System.out.println("\nGrades inainte de adaugare:");
        System.out.println(POO.getGrades());
        POO.addGrade(new Grade(2.3, 3.3, s, "Programare Orientata pe Obiecte"));
        System.out.println("\nGrades dupa adaugare:");
        System.out.println(POO.getGrades());
        System.out.println("\nTestare getAllStudents(), getAllStudentGrades(), getGraduatedStudents(), getBestStudent():");
        for (i = 0; i < Catalog.getInstance().getCourses().size(); i++) {
            System.out.println("\nCurs: " + Catalog.getInstance().getCourses().get(i).getName());
            System.out.println("Toti studentii: " + Catalog.getInstance().getCourses().get(i).getAllStudents());
            System.out.println("Dictionarul student-grades: " + Catalog.getInstance().getCourses().get(i).getAllStudentGrades());
            System.out.println("Studentii promovati: " + Catalog.getInstance().getCourses().get(i).getGraduatedStudents());
            System.out.println("Cel mai bun student: " + Catalog.getInstance().getCourses().get(i).getBestStudent());
        }
        System.out.println("\nTestare sablon Observer:");
        System.out.println("\nObservatorii inainte de adaugare:");
        System.out.println(Catalog.getInstance().getObservers());
        Parent newObserver = (Parent) UserFactory.getUser("Parent", "Observator", "Nou");
        Catalog.getInstance().addObserver(newObserver);
        System.out.println("\nObservatorii dupa adaugare:");
        System.out.println(Catalog.getInstance().getObservers());
        Catalog.getInstance().removeObserver(newObserver);
        System.out.println("\nObservatorii dupa stergere:");
        System.out.println(Catalog.getInstance().getObservers());
        for (Parent p : Catalog.getInstance().getObservers()) {
            System.out.println();
            p.showNotifications();
        }
        System.out.println("\nTestare sablon Memento:");
        System.out.println("\nNota studentului AAAA ZZZZ (Grupa Adaugata 2) la POO inainte de modificare:");
        System.out.println(POO.getGrade(s));
        POO.makeBackup();
        POO.getGrade(s).setPartialScore(1.4);
        System.out.println("\nNota studentului AAAA ZZZZ (Grupa Adaugata 2) la POO dupa modificare:");
        System.out.println(POO.getGrade(s));
        POO.undo();
        System.out.println("\nNota studentului AAAA ZZZZ (Grupa Adaugata 2) la POO dupa restaurarea notei initiale:");
        System.out.println(POO.getGrade(s));
    }
}
