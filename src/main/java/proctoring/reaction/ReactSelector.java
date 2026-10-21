package proctoring.reaction;

import proctoring.ExamType;

public class ReactSelector {
    public ReactStrategy select(int violationNumber, ExamType type) {
        int level = type == ExamType.FINAL ? violationNumber + 1 : violationNumber;
        if (level <= 1) {
            return new WarnStrategy();
        }
        if (level == 2) {
            return new PauseStrategy();
        }
        return new TerminateStrategy();
    }
}
