package proctoring.reaction;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.Test;
import proctoring.ExamType;

class ReactSelectorTest {
    private final ReactSelector selector = new ReactSelector();

    @Test
    void practiceEscalatesWithViolationNumber() {
        assertInstanceOf(WarnStrategy.class, selector.select(1, ExamType.PRACTICE));
        assertInstanceOf(PauseStrategy.class, selector.select(2, ExamType.PRACTICE));
        assertInstanceOf(TerminateStrategy.class, selector.select(3, ExamType.PRACTICE));
        assertInstanceOf(TerminateStrategy.class, selector.select(10, ExamType.PRACTICE));
    }

    @Test
    void finalIsOneStepStricter() {
        assertInstanceOf(PauseStrategy.class, selector.select(1, ExamType.FINAL));
        assertInstanceOf(TerminateStrategy.class, selector.select(2, ExamType.FINAL));
    }
}
