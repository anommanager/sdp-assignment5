package proctoring.reaction;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ReactStrategyTest {
    @Test
    void warnWritesToLog() {
        assertTrue(new WarnStrategy().react("r1").startsWith("LOG:"));
    }

    @Test
    void pauseMentionsPause() {
        assertTrue(new PauseStrategy().react("r1").contains("paused"));
    }

    @Test
    void terminateNotifiesTeacher() {
        assertTrue(new TerminateStrategy().react("r1").contains("teacher notified"));
    }

    @Test
    void setStrategyChangesResult() {
        ViolationHandler handler = new ViolationHandler(new WarnStrategy());
        String before = handler.handle("r1");
        handler.setStrategy(new TerminateStrategy());
        assertNotEquals(before, handler.handle("r1"));
    }
}
