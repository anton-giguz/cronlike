package cronlike;

import java.util.Arrays;
import java.util.Calendar;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for TimeTable
 */
public class TimeTableTest {

    /**
     * Test truncateToStep
     */
    @Test
    public void originalMinusTruncatedShouldEqualOriginalModStep() {
        TimeTable table = new TimeTable(11, Calendar.MINUTE);
        for (int time : Arrays.asList(0, 6, 11, 12, 33)) {
            assertEquals(time % table.getStep(), time - table.truncateToStep(time));
        }
    }

}
