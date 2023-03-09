import java.util.ArrayList;
import java.util.TreeSet;

public class Catalog implements Subject {
    private ArrayList<Course> courses;
    private TreeSet<Parent> observers;
    private static Catalog instance = null;
    private Catalog() {
        courses = new ArrayList<>();
        observers = new TreeSet<>();
    }
    public static Catalog getInstance() {
        if (instance == null) {
            instance = new Catalog();
        }
        return instance;
    }
    public ArrayList<Course> getCourses() {
        return courses;
    }
    public TreeSet<Parent> getObservers() {
        return observers;
    }
    public void addCourse(Course course) {
        if (!courses.contains(course)) {
            courses.add(course);
        }
    }
    public void removeCourse(Course course) {
        courses.remove(course);
    }
    public void addObserver(Observer observer) {
        observers.add((Parent) observer);
    }
    public void removeObserver(Observer observer) {
        observers.remove((Parent) observer);
    }
    public void notifyObservers(Grade grade) {
        for (Parent obs : observers) {
            obs.update(new Notification(grade));
        }
    }
}
