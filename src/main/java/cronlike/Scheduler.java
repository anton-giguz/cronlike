package cronlike;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Scheduling application
 *
 */
public class Scheduler {

    private static final boolean DEBUG = true;

    private static final TimeTable table = DEBUG ? new TimeTable(getPeriod(), Calendar.SECOND) :
                                                   new TimeTable(getPeriod() / 60, Calendar.MINUTE);

    public static void main(String[] args) {
        configure();
        run();
    }

    private static void configure() {
        table.add(10, "false");
        table.add( 0, "sleep 15");
        table.add(20, "pwd");
        table.add(10, "true");
        table.add(55, "ls > `date`.txt");
        System.out.println("[INFO] Time table: " + table);
        System.out.println("[INFO] Scheduler configured at: " + new Date());
    }

    private static void run() {
        ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
        exec.scheduleAtFixedRate(new Iteration(table), getInitialDelay(), getPeriod(), TimeUnit.SECONDS);
    }

    static int getInitialDelay() {
        int current = Calendar.getInstance().get(Calendar.SECOND);
        int remainder = current % getPeriod();
        return (remainder > 0) ? (getPeriod() - remainder) : 0;
    }

    static int getPeriod() {
        return DEBUG ? 10 : 60;
    }

}