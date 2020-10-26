package cronlike;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

import static cronlike.Scheduler.DEBUG;

/**
 * Iteration occurring every minute
 *
 */
public class Iteration implements Runnable {

    private final TimeTable table;
    private final ExecutorService exec = Executors.newCachedThreadPool();

    Iteration(TimeTable table) {
        this.table = table;
    }

    public void run() {
        List<String> commands = table.getCommands(Calendar.getInstance());
        if (DEBUG) {
            System.out.println("[INFO] Iteration at " + new Date() + " found commands: " + commands);
        }
        for (String command : commands) {
            exec.execute(new Task(command));
        }
    }

}
