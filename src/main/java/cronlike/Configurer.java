package cronlike;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Reading configuration
 *
 */
public class Configurer {

    public static void readConfFile(TimeTable table, String fileName) {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(fileName))) {
            Pattern pattern = Pattern.compile("^\\s*(\\d+)\\s+(.*)$");
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    table.add(Integer.parseInt(matcher.group(1)), matcher.group(2));
                } else {
                    System.err.println("[WARNING] Wrong format of configuration line: " + line);
                }
            }
        } catch(IOException e) {
            System.err.println("[ERROR] Cannot read configuration file: " + e);
            System.exit(1);
        }
    }

}
