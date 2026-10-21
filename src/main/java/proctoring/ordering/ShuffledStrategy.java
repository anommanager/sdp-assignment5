package proctoring.ordering;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ShuffledStrategy implements OrderStrategy {
    private final Random random;

    public ShuffledStrategy(Random random) {
        this.random = random;
    }

    @Override
    public List<Question> order(List<Question> questions) {
        List<Question> result = new ArrayList<>(questions);
        Collections.shuffle(result, random);
        return result;
    }
}
