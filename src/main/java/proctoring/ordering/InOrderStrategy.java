package proctoring.ordering;

import java.util.List;

public class InOrderStrategy implements OrderStrategy {
    @Override
    public List<Question> order(List<Question> questions) {
        return List.copyOf(questions);
    }
}
