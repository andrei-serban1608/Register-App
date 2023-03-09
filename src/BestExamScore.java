import java.util.ArrayList;
public class BestExamScore implements Strategy {
    public Student getBestGrade(ArrayList<Grade> grades) {
        int i;
        Grade bestGrade = new Grade(0.0, 0.0, null, "");
        Double bestExamScore = 0.0;
        for (i = 0; i < grades.size(); i++) {
            if (grades.get(i).getExamScore() > bestExamScore) {
                bestExamScore = grades.get(i).getExamScore();
                bestGrade = new Grade(grades.get(i));
            }
        }
        return bestGrade.getStudent();
    }
}
