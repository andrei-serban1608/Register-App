import java.util.TreeSet;

public class Parent extends User implements Observer {
    private TreeSet<String> notifications;
    public Parent(String firstName, String lastName) {
        super(firstName, lastName);
        notifications = new TreeSet<>();
    }
    public void update(Notification notification) {
        Parent tempFather = notification.getGrade().getStudent().getFather();
        Parent tempMother = notification.getGrade().getStudent().getMother();
        if (tempFather != null && tempFather.equals(this)) {
            notifications.add(notification.toString());
            return;
        }
        if (tempMother != null && tempMother.equals(this)) {
            notifications.add(notification.toString());
        }
    }
    public void showNotifications() {
        System.out.println("Parintele " + this + " are notificarile:\n");
        for (String s : notifications) {
            System.out.println(s);
        }
    }
}
