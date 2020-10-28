package cronlike;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.util.Arrays;
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

    /**
     * EXAMPLE
     *
     * command: date +\%T; sort%Eve%Al\%ce%Bob%
     *
     * head:    date +%T; sort
     *
     * input:   Eve
     *          Al%ce
     *          Bob
     *
     */
    public void run() {
        String[] parts = Arrays.stream(command.split("(?<!\\\\)%", -1)).map(s -> s.replaceAll("\\\\%", "%")).toArray(String[]::new);
        String   head  = parts[0];
        String   input = parts.length > 1 ? String.join("\n", Arrays.copyOfRange(parts, 1, parts.length)) : null;
        try {
            Process process = startProcess(head);
            Recorder.info(command, "START-OK");
            tryInput(process, input);
            tryOutput(process);
            wait(process);
        } catch(IOException e) {
            Recorder.error(command, "START-ERROR", e.toString());
        }
    }

    private Process startProcess(String head) throws IOException {
        if (DEBUG) {
            System.out.println("[INFO] [" + command + "] head: " + head);
        }
        return Runtime.getRuntime().exec(new String[]{"sh", "-c", head});
    }

    private void tryInput(Process process, String input) {
        if (input == null) {
            return;
        }
        try (BufferedWriter stdin = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()))) {
            if (DEBUG) {
                System.out.println("[INFO] [" + command + "] input: " + input);
            }
            stdin.write(input);
            stdin.close();
        } catch(IOException e) {
            System.err.println("[WARNING] Input of [" + command + "] cannot be written: " + e);
        }
    }

    private void tryOutput(Process process) {
        try (BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
             BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            readOutput(stdout, "[INFO] [" + command + "] [stdout] ");
            readOutput(stderr, "[INFO] [" + command + "] [stderr] ");
        } catch(IOException e) {
            System.err.println("[WARNING] Output of [" + command + "] cannot be read: " + e);
        }
    }

    private void wait(Process process) {
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
