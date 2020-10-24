package cronlike;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Unit tests for Scheduler
 */
public class SchedulerTest {

    /**
     * Test getInitialDelay
     */
    @Test
    public void initialDelayShouldBeLessThanPeriod() {
        assertTrue(Scheduler.getInitialDelay() < Scheduler.getPeriod());
    }

}
