package proctoring.ordering;

import java.util.List;

public interface OrderStrategy {
    List<Question> order(List<Question> questions);
}
