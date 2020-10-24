package cronlike;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Correspondence between scheduled times and commands
 *
 */
public class TimeTable {

    private final HashMap<Integer, ArrayList<String>> map = new HashMap<>();
    private final int step;
    private final int unit;

    TimeTable(int step, int unit) {
        this.step = step;
        this.unit = unit;
    }

    public void add(int time, String command) {
        int key = truncateToStep(time);
        ArrayList<String> commands = map.get(key);
        if (commands == null) {
            map.put(key, new ArrayList<>(Arrays.asList(command)));
        } else {
            commands.add(command);
        }
    }

    public String toString() {
        return map.toString();
    }

    public ArrayList<String> getCommands(Calendar calendar) {
        int time = calendar.get(unit);
        ArrayList<String> commands = map.get(truncateToStep(time));
        return commands == null ? new ArrayList<>() : commands;
    }

    int truncateToStep(int time) {
        return time - time % step;
    }

    int getStep() {
        return step;
    }

}
