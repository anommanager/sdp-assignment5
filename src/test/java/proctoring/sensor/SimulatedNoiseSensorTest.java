package proctoring.sensor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class SimulatedNoiseSensorTest {
  @Test
  void returnsSameLevelForAnyRoom() {
    NoiseSensor sensor = new SimulatedNoiseSensor();
    assertEquals(sensor.levelDb("a"), sensor.levelDb("b"));
  }
}
