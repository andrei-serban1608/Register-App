import java.util.ArrayList;
public class BestTotalScore implements Strategy {
    public Student getBestGrade(ArrayList<Grade> grades) {
        int i;
        Grade bestGrade = new Grade(0.0, 0.0, null, "");
        Double bestTotalScore = 0.0;
        for (i = 0; i < grades.size(); i++) {
            if (grades.get(i).getTotal() > bestTotalScore) {
                bestTotalScore = grades.get(i).getTotal();
                bestGrade = new Grade(grades.get(i));
            }
        }
        return bestGrade.getStudent();
    }
}
