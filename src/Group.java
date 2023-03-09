import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
public class Group extends ArrayList<Student> {
    private String ID;
    private Assistant assistant;
    private Comparator<Student> comparator;
    public Group(String ID, Assistant assistant, Comparator<Student> comparator) {
        super();
        this.ID = ID;
        this.assistant = assistant;
        this.comparator = comparator;
    }
    public Group(String ID, Assistant assistant) {
        super();
        this.ID = ID;
        this.assistant = assistant;
        this.comparator = new Comparator<Student>() {
            public int compare(Student o1, Student o2) {
                return o1.compareTo(o2);
            }
        };
    }
    public Assistant getAssistant() {
        return assistant;
    }
    public String getID() {
        return ID;
    }
    public Comparator<Student> getComparator() {
        return comparator;
    }
    public void setAssistant(Assistant assistant) {
        this.assistant = assistant;
    }
    public String toString() {
        return "Nume Grupa: " + ID + "\nAsistent: " + assistant + "\n" + super.toString();
    }
}
