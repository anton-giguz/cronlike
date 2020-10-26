package cronlike;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Date;

import static cronlike.Scheduler.DEBUG;

/**
 * Task to run at scheduled times
 *
 */
public class Task implements Runnable {

    private final String command;

    Task(String command) {
        this.command = command;
    }

    public void run() {
        //ProcessBuilder builder = new ProcessBuilder(command.split("\\s+"));
        try {
            //Process process = builder.start();
            //Process process = Runtime.getRuntime().exec(command);
            Process process = Runtime.getRuntime().exec(new String[]{"sh", "-c", command});
            Recorder.info(command, "START-OK");
            try (BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
                 BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                readOutput(stdout, "[INFO] [" + command + "] [stdout] ");
                readOutput(stderr, "[INFO] [" + command + "] [stderr] ");
                try {
                    int exitCode = process.waitFor();
                    if (exitCode == 0) {
                        Recorder.info(command, "FINISH-OK");
                    } else {
                        Recorder.error(command, "FINISH-ERROR", String.valueOf(exitCode));
                    }
                } catch(InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } catch(IOException e) {
                System.err.println("[WARNING] Output of [" + command + "] cannot be read: " + e);
            }
        } catch(IOException e) {
            Recorder.error(command, "START-ERROR", e.toString());
        }
    }

    private void readOutput(BufferedReader reader, String prefix) throws IOException {
        if (DEBUG) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(prefix + line);
            }
        } else {
            while (reader.readLine() != null) {
                // ignore
            }
        }
    }

}
