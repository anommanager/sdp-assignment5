package proctoring.ordering;

import java.util.Comparator;
import java.util.List;

public class EasyFirstStrategy implements OrderStrategy {
    @Override
    public List<Question> order(List<Question> questions) {
        return questions.stream().sorted(Comparator.comparingInt(Question::difficulty)).toList();
    }
}
