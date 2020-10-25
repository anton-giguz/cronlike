package cronlike;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for TimeTable
 */
public class TimeTableTest {

    private final int unit = Calendar.MINUTE;

    /**
     * Test getCommands
     */
    @Test
    public void newlyCreatedTableShouldBeEmpty() {
        TimeTable table = new TimeTable(1, unit);
        Calendar calendar = Calendar.getInstance();
        for (int time = calendar.getMinimum(unit); time <= calendar.getMaximum(unit); time++) {
            calendar.set(unit, time);
            assertEquals(Collections.EMPTY_LIST, table.getCommands(calendar));
        }
    }

    /**
     * Test add
     */
    @Test
    public void addedCommandShouldBeReturned() {
        int time = 42;
        String command = "test";
        TimeTable table = new TimeTable(1, unit);
        table.add(time, command);
        Calendar calendar = Calendar.getInstance();
        calendar.set(unit, time);
        assertEquals(Arrays.asList(command), table.getCommands(calendar));
    }

    /**
     * Test truncateToStep
     */
    @Test
    public void originalMinusTruncatedShouldEqualOriginalModStep() {
        TimeTable table = new TimeTable(11, unit);
        for (int time : Arrays.asList(0, 6, 11, 12, 33)) {
            assertEquals(time % table.getStep(), time - table.truncateToStep(time));
        }
    }

}
