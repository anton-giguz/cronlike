package cronlike;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Date;

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
            System.out.println("[INFO] Task [" + command + "] started at: " + new Date());
            try (BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
                 BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                readOutput(stdout, "[INFO] [" + command + "] [stdout] ");
                readOutput(stderr, "[INFO] [" + command + "] [stderr] ");
                try {
                    int exitCode = process.waitFor();
                    if (exitCode == 0) {
                        System.out.println("[INFO] Task [" + command + "] finished successfully at: " + new Date());
                    } else {
                        System.err.println("[ERROR] Task [" + command + "] finished with exit code " + exitCode + " at: " + new Date());
                    }
                } catch(InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } catch(IOException e) {
                System.err.println("[ERROR] Output of [" + command + "] cannot be read: " + e);
            }
        } catch(IOException e) {
            System.err.println("[ERROR] Task [" + command + "] cannot start: " + e);
        }
    }

    private void readOutput(BufferedReader reader, String prefix) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(prefix + line);
        }
    }

}
