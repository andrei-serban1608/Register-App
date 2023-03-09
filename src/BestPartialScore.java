import java.util.ArrayList;
public class BestPartialScore implements Strategy {
    public Student getBestGrade(ArrayList<Grade> grades) {
        int i;
        Grade bestGrade = new Grade(0.0, 0.0, null, "");
        Double bestPartialScore = 0.0;
        for (i = 0; i < grades.size(); i++) {
            if (grades.get(i).getPartialScore() > bestPartialScore) {
                bestPartialScore = grades.get(i).getPartialScore();
                bestGrade = new Grade(grades.get(i));
            }
        }
        return bestGrade.getStudent();
    }
}
