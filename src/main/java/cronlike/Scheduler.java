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

    private static String confFile;
    private static String logFile;
    private static String database;

    private static final TimeTable table = DEBUG ? new TimeTable(getPeriod(), Calendar.SECOND) :
                                                   new TimeTable(getPeriod() / 60, Calendar.MINUTE);

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java -jar cronlike.jar <config> <log> [<database>]");
            System.exit(1);
        }

        confFile = args[0];
        logFile  = args[1];
        if (args.length > 2) {
            database = args[2];
        }

        configure();
        run();
    }

    private static void configure() {
        Configurer.readConfFile(table, confFile);
        System.out.println("[INFO] Time table: " + table);
        Recorder.init(logFile, database);
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
